package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.MessageSentEvent;
import com.ryan_mtg.servobot.model.BotEditor;
import com.ryan_mtg.servobot.model.Channel;
import com.ryan_mtg.servobot.model.User;
import org.junit.Test;

import static com.ryan_mtg.servobot.model.ObjectMother.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SetArenaUsernameCommandTest {
    private static final int ID = 1;
    private static final boolean SECURE = true;
    private static final Permission PERMISSION = Permission.MOD;
    private static final String ARENA_NAME = "name#12345";

    @Test
    public void testPerform() throws BotErrorException {
        SetArenaUsernameCommand command = new SetArenaUsernameCommand(ID, SECURE, PERMISSION);

        BotEditor botEditor = mockBotEditor();
        Channel channel = mockChannel();
        User user = mockUser();
        MessageSentEvent event = mockMessageSentEvent(botEditor, channel, user);

        command.perform(event, ARENA_NAME);

        verify(botEditor).setArenaUsername(user, ARENA_NAME);
        verify(channel).say("Username added.");
    }

    @Test(expected = BotErrorException.class)
    public void testThrowsWhenUsernameIsTooLong() throws BotErrorException {
        SetArenaUsernameCommand command = new SetArenaUsernameCommand(ID, SECURE, PERMISSION);

        BotEditor botEditor = mockBotEditor();
        Channel channel = mockChannel();
        MessageSentEvent event = mockMessageSentEvent(botEditor, channel);

        try {
            command.perform(event, "This is a really long username#12345");
        } finally {
            verify(botEditor, never()).setArenaUsername(any(User.class), anyString());
            verify(channel, never()).say(anyString());
        }
    }


    @Test(expected = BotErrorException.class)
    public void testThrowsWhenUsernameDoesNotHaveNumber() throws BotErrorException {
        SetArenaUsernameCommand command = new SetArenaUsernameCommand(ID, SECURE, PERMISSION);

        BotEditor botEditor = mockBotEditor();
        Channel channel = mockChannel();
        MessageSentEvent event = mockMessageSentEvent(botEditor, channel);

        try {
            command.perform(event, "name");
        } finally {
            verify(botEditor, never()).setArenaUsername(any(User.class), anyString());
            verify(channel, never()).say(anyString());
        }
    }
}
