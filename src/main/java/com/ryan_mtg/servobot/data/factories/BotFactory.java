package com.ryan_mtg.servobot.data.factories;

import com.ryan_mtg.servobot.data.models.BotHomeRow;
import com.ryan_mtg.servobot.data.models.BotRow;
import com.ryan_mtg.servobot.data.models.GameQueueEntryRow;
import com.ryan_mtg.servobot.data.models.GameQueueRow;
import com.ryan_mtg.servobot.data.models.ServiceHomeRow;
import com.ryan_mtg.servobot.data.repositories.GameQueueEntryRepository;
import com.ryan_mtg.servobot.data.repositories.ServiceHomeRepository;
import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.model.Book;
import com.ryan_mtg.servobot.model.Bot;
import com.ryan_mtg.servobot.model.BotHome;
import com.ryan_mtg.servobot.commands.CommandTable;
import com.ryan_mtg.servobot.model.GameQueue;
import com.ryan_mtg.servobot.model.giveaway.Giveaway;
import com.ryan_mtg.servobot.model.scope.Scope;
import com.ryan_mtg.servobot.model.Service;
import com.ryan_mtg.servobot.model.ServiceHome;
import com.ryan_mtg.servobot.model.reaction.ReactionTable;
import com.ryan_mtg.servobot.model.storage.StorageTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotFactory {
    private static Logger LOGGER = LoggerFactory.getLogger(BotFactory.class);

    @Autowired
    private ServiceHomeRepository serviceHomeRepository;

    @Autowired
    private SerializerContainer serializers;

    @PostConstruct
    public void setFactoryProperty() {
        serializers.setBotFactory(this);
    }

    public Bot createBot(final BotRow botRow, final Scope globalScope) throws BotErrorException {
        LOGGER.info(">>>>>>>>>>>>>>>> Starting bot creation ");
        ServiceSerializer serviceSerializer = serializers.getServiceSerializer();
        Map<Integer, Service> services = serviceSerializer.getServiceMap();

        Bot bot = new Bot(botRow.getName(), globalScope, services, serializers);
        for (BotHomeRow botHomeRow : serializers.getBotHomeRepository().findAll()) {
            bot.addHome(createBotHome(botHomeRow));
        }

        LOGGER.info("<<<<<<<<<<<<<<<< Ending bot creation ");
        return bot;
    }

    public BotHome createBotHome(int botHomeId) throws BotErrorException {
        return createBotHome(serializers.getBotHomeRepository().findById(botHomeId));
    }

    @Transactional(rollbackOn = BotErrorException.class)
    protected BotHome createBotHome(final BotHomeRow botHomeRow) throws BotErrorException {
        LOGGER.info(">>>>>> Starting bot home creation: {} ", botHomeRow.getHomeName());
        ServiceSerializer serviceSerializer = serializers.getServiceSerializer();
        Map<Integer, Service> services = serviceSerializer.getServiceMap();
        String homeName = botHomeRow.getHomeName();
        String botName = botHomeRow.getBotName();
        String timeZone = botHomeRow.getTimeZone();
        int botHomeId = botHomeRow.getId();

        LOGGER.info("------ Creating Books: {} ", botHomeRow.getHomeName());
        List<Book> books = serializers.getBookSerializer().createBooks(botHomeId);
        Map<Integer, Book> bookMap = new HashMap<>();
        for (Book book : books) {
            bookMap.put(book.getId(), book);
        }

        LOGGER.info("------ Creating CommandTable: {} ", botHomeRow.getHomeName());
        CommandTable commandTable = serializers.getCommandTableSerializer().createCommandTable(botHomeId, bookMap);
        LOGGER.info("------ Creating ReactionTable: {} ", botHomeRow.getHomeName());
        ReactionTable reactionTable =
                serializers.getReactionTableSerializer().createReactionTable(botHomeId, commandTable);
        LOGGER.info("------ Creating StorageTable: {} ", botHomeRow.getHomeName());
        StorageTable storageTable = serializers.getStorageTableSerializer().createStorageTable(botHomeId);

        LOGGER.info("------ Creating service homes: {} ", botHomeRow.getHomeName());
        Map<Integer, ServiceHome> serviceHomes = new HashMap<>();
        for (ServiceHomeRow serviceHomeRow : serviceHomeRepository.findAllByBotHomeId(botHomeId)) {
            int serviceType = serviceHomeRow.getServiceType();
            Service service = services.get(serviceType);
            ServiceHome serviceHome = serviceSerializer.createServiceHome(serviceHomeRow, service);
            serviceHomes.put(serviceType, serviceHome);
        }

        LOGGER.info("------ Creating Game Queues: {} ", botHomeRow.getHomeName());
        List<GameQueue> gameQueues = new ArrayList<>();
        for (GameQueueRow gameQueueRow : serializers.getGameQueueRepository().findAllByBotHomeId(botHomeId)) {
            GameQueue gameQueue = new GameQueue(gameQueueRow.getId(), gameQueueRow.getName(),
                    gameQueueRow.getState(), gameQueueRow.getNext(), gameQueueRow.getCurrentPlayerId());

            GameQueueEntryRepository gameQueueEntryRepository = serializers.getGameQueueEntryRepository();
            for (GameQueueEntryRow gameQueueEntryRow :
                    gameQueueEntryRepository.findByGameQueueIdOrderBySpotAsc(gameQueue.getId())) {
                gameQueue.enqueue(gameQueueEntryRow.getUserId(), gameQueueEntryRow.getSpot());
            }

            gameQueues.add(gameQueue);
        }

        LOGGER.info("------ Creating Giveaways: {} ", botHomeRow.getHomeName());
        List<Giveaway> giveaways = serializers.getGiveawaySerializer().createGiveaways(botHomeId, commandTable);
        LOGGER.info("------ Calling BotHome() constructor: {} ", botHomeRow.getHomeName());
        BotHome botHome = new BotHome(botHomeId, homeName, botName, timeZone, commandTable, reactionTable, storageTable,
                serviceHomes, books, gameQueues, giveaways);

        LOGGER.info("<<<<<< Ending bot home creation: {} ", botHomeRow.getHomeName());
        return botHome;
    }
}
