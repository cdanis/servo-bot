package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.MessageSentEvent;
import com.ryan_mtg.servobot.model.Home;
import com.ryan_mtg.servobot.model.scope.FunctorSymbolTable;
import com.ryan_mtg.servobot.utility.Validation;

public class SetUsersRoleCommand extends MessageCommand {
    public static final int TYPE = 27;
    private String role;
    private String message;

    public SetUsersRoleCommand(final  int id, final int flags, final Permission permission, final String role,
                               final String message)
            throws BotErrorException {
        super(id, flags, permission);
        this.role = role;
        this.message = message;

        Validation.validateStringLength(role, Validation.MAX_ROLE_LENGTH, "Role");
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void perform(final MessageSentEvent event, final String arguments) throws BotErrorException {
        Home home = event.getHome();

        String userName = arguments;
        if (userName == null) {
            throw new BotErrorException("No one specified.");
        }

        if (userName.startsWith("@")) {
            userName = userName.substring(1);
        }

        if (home.isHigherRanked(userName, event.getSender())) {
            home.setRole(event.getSender(), role);

            MessageCommand.say(event, "I see through your tricks! I'm checking %sender% into the greybar hotel.");
        } else {
            home.setRole(userName, role);

            FunctorSymbolTable symbolTable = new FunctorSymbolTable();
            symbolTable.addValue("input", arguments);
            symbolTable.addValue("user", userName);
            symbolTable.addValue("role", role);

            MessageCommand.say(event, symbolTable, message);
        }
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitSetUsersRoleCommand(this);
    }
}
