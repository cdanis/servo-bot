package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.commands.chat.FactsCommand;
import com.ryan_mtg.servobot.commands.hierarchy.Command;
import com.ryan_mtg.servobot.commands.hierarchy.CommandSettings;
import com.ryan_mtg.servobot.commands.hierarchy.RateLimit;
import com.ryan_mtg.servobot.error.BotHomeError;
import com.ryan_mtg.servobot.events.CommandInvokedEvent;
import com.ryan_mtg.servobot.model.books.Book;
import org.junit.Test;

import static com.ryan_mtg.servobot.model.ObjectMother.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FactsCommandTest {
    private static final int ID = 1;
    private static final CommandSettings COMMAND_SETTINGS =
            new CommandSettings(Command.DEFAULT_FLAGS, Permission.MOD, new RateLimit());
    private static final String ARGUMENTS = "argument other_argument";
    private static final String LINE = "line";

    @Test
    public void testPerform() throws BotHomeError {
        Book book = mock(Book.class);
        when(book.getRandomLine()).thenReturn(LINE);

        FactsCommand command = new FactsCommand(ID, COMMAND_SETTINGS, book);

        CommandInvokedEvent event = mockCommandInvokedEvent(ARGUMENTS);

        command.perform(event);

        verify(event).say(LINE);
    }
}