package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.MessageSentEvent;
import com.ryan_mtg.servobot.model.HomeEditor;
import com.ryan_mtg.servobot.model.User;

public class JailBreakCommand extends MessageCommand {
    public static final int TYPE = 26;
    private String prisonRole;
    private String variableName;

    public JailBreakCommand(final int id, final int flags, final Permission permission, final String prisonRole,
                            final String variableName) {
        super(id, flags, permission);
        this.prisonRole = prisonRole;
        this.variableName = variableName;
    }

    public String getPrisonRole() {
        return prisonRole;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitJailBreakCommand(this);
    }

    @Override
    public void perform(final MessageSentEvent event, final String arguments) throws BotErrorException {
        User sender = event.getSender();

        int count = event.getHome().clearRole(prisonRole);

        HomeEditor homeEditor = event.getHomeEditor();
        homeEditor.remoteStorageVariables(variableName);

        if (count > 0) {
            MessageCommand.say(event,
                    String.format("%s broke %d inmates out of '%s'!", sender.getName(), count, prisonRole));
        }
    }
}