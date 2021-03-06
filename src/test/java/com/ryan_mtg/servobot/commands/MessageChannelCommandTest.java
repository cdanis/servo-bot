package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.commands.chat.MessageChannelCommand;
import com.ryan_mtg.servobot.commands.hierarchy.Command;
import com.ryan_mtg.servobot.commands.hierarchy.CommandSettings;
import com.ryan_mtg.servobot.commands.hierarchy.RateLimit;
import com.ryan_mtg.servobot.error.BotHomeError;
import com.ryan_mtg.servobot.error.UserError;
import com.ryan_mtg.servobot.events.HomeEvent;
import com.ryan_mtg.servobot.model.Channel;
import com.ryan_mtg.servobot.model.Home;
import com.ryan_mtg.servobot.model.ServiceHome;
import com.ryan_mtg.servobot.model.scope.Scope;
import org.junit.Before;
import org.junit.Test;

import static com.ryan_mtg.servobot.model.ObjectMother.mockChannel;
import static com.ryan_mtg.servobot.model.ObjectMother.mockHome;
import static com.ryan_mtg.servobot.model.ObjectMother.mockHomeEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageChannelCommandTest {
    private static final int ID = 1;
    private static final CommandSettings COMMAND_SETTINGS =
            new CommandSettings(Command.DEFAULT_FLAGS, Permission.MOD, new RateLimit());
    private static final int SERVICE_TYPE = 3;
    private static final int OTHER_SERVICE_TYPE = 2;
    private static final String CHANNEL_NAME = "channel_name";
    private static final String MESSAGE = "message";

    private MessageChannelCommand command;
    private Home home;
    private Channel channel;

    @Before
    public void setUp() throws UserError {
        command = new MessageChannelCommand(ID, COMMAND_SETTINGS, SERVICE_TYPE, CHANNEL_NAME, MESSAGE);
        home = mockHome();
        channel = mockChannel();
    }

    @Test
    public void testPerformOnSameService() throws BotHomeError, UserError {
        when(home.getChannel(CHANNEL_NAME, SERVICE_TYPE)).thenReturn(channel);
        HomeEvent homeEvent = mockHomeEvent(home, SERVICE_TYPE);

        command.perform(homeEvent);

        verify(homeEvent).say(eq(channel), any(Scope.class), eq(MESSAGE));
    }

    @Test
    public void testPerformOnDifferentService() throws BotHomeError, UserError {
        ServiceHome serviceHome = mock(ServiceHome.class);
        when(serviceHome.getChannel(CHANNEL_NAME)).thenReturn(channel);

        HomeEvent homeEvent = mockHomeEvent(home, OTHER_SERVICE_TYPE);
        when(homeEvent.getServiceHome(SERVICE_TYPE)).thenReturn(serviceHome);

        command.perform(homeEvent);

        verify(homeEvent).say(eq(channel), any(Scope.class), eq(MESSAGE));
    }
}