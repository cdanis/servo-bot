package com.ryan_mtg.servobot.commands.chat;

import com.ryan_mtg.servobot.commands.hierarchy.CommandSettings;
import com.ryan_mtg.servobot.commands.CommandType;
import com.ryan_mtg.servobot.commands.CommandVisitor;
import com.ryan_mtg.servobot.commands.hierarchy.MessageCommand;
import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.MessageSentEvent;
import com.ryan_mtg.servobot.model.HomeEditor;

public class DeleteCommand extends MessageCommand {
    public static final CommandType TYPE = CommandType.DELETE_COMMAND_TYPE;

    public DeleteCommand(final int id, final CommandSettings commandSettings) {
        super(id, commandSettings);
    }

    @Override
    public void perform(final MessageSentEvent event, final String arguments) throws BotErrorException {
        HomeEditor homeEditor = event.getHomeEditor();

        if (arguments.isEmpty()) {
            throw new BotErrorException("Missing command name.");
        }

        if (arguments.charAt(0) != '!') {
            throw new BotErrorException("Commands must start with '!'");
        }

        String commandName = arguments.substring(1);

        if (commandName.isEmpty()) {
            throw new BotErrorException("Missing command name.");
        }

        homeEditor.deleteCommand(event.getSender(), commandName);

        MessageCommand.say(event, String.format("Command %s deleted.", commandName));
    }

    @Override
    public CommandType getType() {
        return TYPE;
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitDeleteCommand(this);
    }
}