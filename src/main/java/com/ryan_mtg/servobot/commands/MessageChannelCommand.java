package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.HomeEvent;
import com.ryan_mtg.servobot.model.Channel;
import com.ryan_mtg.servobot.model.scope.FunctorSymbolTable;
import com.ryan_mtg.servobot.model.scope.Scope;
import com.ryan_mtg.servobot.utility.Validation;

public class MessageChannelCommand extends HomeCommand {
    public static final int TYPE = 4;
    private final int serviceType;
    private final String channelName;
    private final String message;

    public MessageChannelCommand(final int id, final int flags, final Permission permission,
            final int serviceType, final String channelName, final String message) throws BotErrorException {
        super(id, flags, permission);
        this.serviceType = serviceType;
        this.channelName = channelName;
        this.message = message;

        Validation.validateStringLength(channelName, Validation.MAX_CHANNEL_NAME_LENGTH, "Channel name");
        Validation.validateStringLength(message, Validation.MAX_TEXT_LENGTH, "Message");
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void perform(final HomeEvent homeEvent) throws BotErrorException {
        Channel channel = homeEvent.getHome().getChannel(channelName, serviceType);
        FunctorSymbolTable symbolTable = new FunctorSymbolTable();
        symbolTable.addValue("commandCount", "");
        Scope commandScope = new Scope(homeEvent.getScope(), symbolTable);
        Command.say(channel, homeEvent, commandScope, message);
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitMessageChannelCommand(this);
    }

    public int getServiceType() {
        return serviceType;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getMessage() {
        return message;
    }
}
