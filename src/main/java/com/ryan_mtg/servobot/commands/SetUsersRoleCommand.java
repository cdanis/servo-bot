package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.commands.hierarchy.CommandSettings;
import com.ryan_mtg.servobot.commands.hierarchy.MessageCommand;
import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.MessageSentEvent;
import com.ryan_mtg.servobot.model.Home;
import com.ryan_mtg.servobot.model.User;
import com.ryan_mtg.servobot.model.scope.SimpleSymbolTable;
import com.ryan_mtg.servobot.utility.Validation;
import lombok.Getter;

public class SetUsersRoleCommand extends MessageCommand {
    public static final CommandType TYPE = CommandType.SET_USERS_ROLE_COMMAND_TYPE;

    @Getter
    private String role;

    @Getter
    private String message;

    public SetUsersRoleCommand(final  int id, final CommandSettings commandSettings, final String role,
                               final String message) throws BotErrorException {
        super(id, commandSettings);
        this.role = role;
        this.message = message;

        Validation.validateStringLength(role, Validation.MAX_ROLE_LENGTH, "Role");
    }

    @Override
    public void perform(final MessageSentEvent event, final String arguments) throws BotErrorException {
        Home home = event.getHome();
        User user = home.getUser(arguments);

        if (!home.hasRole(user, role)) {
            home.setRole(user, role);

            SimpleSymbolTable symbolTable = new SimpleSymbolTable();
            symbolTable.addValue("input", arguments);
            symbolTable.addValue("user", user.getName());
            symbolTable.addValue("role", role);

            MessageCommand.say(event, symbolTable, message);
        }
    }

    @Override
    public CommandType getType() {
        return TYPE;
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitSetUsersRoleCommand(this);
    }
}