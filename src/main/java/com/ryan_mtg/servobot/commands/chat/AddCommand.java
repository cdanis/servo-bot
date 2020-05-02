package com.ryan_mtg.servobot.commands.chat;

import com.ryan_mtg.servobot.commands.CommandType;
import com.ryan_mtg.servobot.commands.CommandVisitor;
import com.ryan_mtg.servobot.commands.Permission;
import com.ryan_mtg.servobot.commands.hierarchy.Command;
import com.ryan_mtg.servobot.commands.hierarchy.MessageCommand;
import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.MessageSentEvent;
import com.ryan_mtg.servobot.model.HomeEditor;
import com.ryan_mtg.servobot.utility.CommandParser;
import com.ryan_mtg.servobot.utility.Flags;
import com.ryan_mtg.servobot.utility.Strings;

import java.util.List;
import java.util.regex.Pattern;

public class AddCommand extends MessageCommand {
    public static final CommandType TYPE = CommandType.ADD_COMMAND_TYPE;

    private static final Pattern COMMAND_PATTERN = Pattern.compile("!\\w+");
    private static final Pattern FLAG_PATTERN = Pattern.compile("(@|=|\\+|-|->)\\w+");
    private static final CommandParser COMMAND_PARSER = new CommandParser(COMMAND_PATTERN, FLAG_PATTERN);

    public AddCommand(final int id, final int flags, final Permission permission) {
        super(id, flags, permission);
    }

    @Override
    public CommandType getType() {
        return TYPE;
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitAddCommand(this);
    }

    @Override
    public void perform(final MessageSentEvent event, final String arguments) throws BotErrorException {
        CommandParser.ParseResult parseResult = COMMAND_PARSER.parse(arguments);

        String command = parseResult.getCommand();
        switch (parseResult.getStatus()) {
            case NO_COMMAND:
                throw new BotErrorException("No command to add.");
            case COMMAND_MISMATCH:
                if (!command.startsWith("!")) {
                    throw new BotErrorException("Commands must start with a '!'.");
                }
                throw new BotErrorException(String.format("%s doesn't look like a command.", command));
        }
        command = command.substring(1);

        int commandFlags = DEFAULT_FLAGS;
        Permission permission = Permission.ANYONE;
        HomeEditor homeEditor = event.getHomeEditor();
        String input = parseResult.getInput();
        List<String> flags = parseResult.getFlags();
        if (!flags.isEmpty()) {
            String alias = null;

            for (String flag : flags) {
                if (flag.startsWith("->")) {
                    if (alias != null) {
                        throw new BotErrorException("Only one alias is allowed");
                    }
                    alias = flag.substring(2);
                } else switch (flag) {
                    case "=admin":
                        permission = Permission.ADMIN;
                        break;
                    case "=streamer":
                        permission = Permission.STREAMER;
                        break;
                    case "=mod":
                        permission = Permission.MOD;
                        break;
                    case "=sub":
                        permission = Permission.SUB;
                        break;
                    case "=all":
                        permission = Permission.ANYONE;
                        break;
                    case "+live":
                        commandFlags = Flags.setFlag(commandFlags, Command.ONLY_WHILE_STREAMING_FLAG, true);
                        break;
                    case "+anytime":
                    case "-live":
                        commandFlags = Flags.setFlag(commandFlags, Command.ONLY_WHILE_STREAMING_FLAG, false);
                        break;
                    case "+twitch":
                        commandFlags = Flags.setFlag(commandFlags, Command.TWITCH_FLAG, true);
                        break;
                    case "-twitch":
                        commandFlags = Flags.setFlag(commandFlags, Command.TWITCH_FLAG, false);
                        break;
                    case "+discord":
                        commandFlags = Flags.setFlag(commandFlags, Command.DISCORD_FLAG, true);
                        break;
                    case "-discord":
                        commandFlags = Flags.setFlag(commandFlags, Command.DISCORD_FLAG, false);
                        break;
                    default:
                        throw new BotErrorException(String.format("Unknown flag %s", flag));
                }
            }

            if (alias != null) {
                if (input != null) {
                    throw new BotErrorException("Aliased command can't have text");
                }

                boolean added = homeEditor.aliasCommand(command, alias);

                if (added) {
                    MessageCommand.say(event, String.format("Command %s added.", command));
                } else {
                    MessageCommand.say(event, String.format("Command %s modified.", command));
                }
                return;
            }
        }

        if (Strings.isBlank(input)) {
            throw new BotErrorException(String.format("%s doesn't do anything.", command));
        }

        boolean added = homeEditor.addCommand(command,
                new TextCommand(Command.UNREGISTERED_ID, commandFlags, permission, input));

        if (added) {
            MessageCommand.say(event, String.format("Command %s added.", command));
        } else {
            MessageCommand.say(event, String.format("Command %s modified.", command));
        }
    }
}
