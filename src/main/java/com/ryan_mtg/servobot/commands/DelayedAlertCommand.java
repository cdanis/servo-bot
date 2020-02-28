package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.events.BotErrorException;
import com.ryan_mtg.servobot.events.HomeEvent;
import com.ryan_mtg.servobot.utility.Validation;

import java.time.Duration;

public class DelayedAlertCommand extends HomeCommand {
    public static final int TYPE = 16;
    private Duration delay;
    private String alertToken;

    public DelayedAlertCommand(final int id, final int flags, final Permission permission,
                               final Duration delay, final String alertToken) throws BotErrorException {
        super(id, flags, permission);
        this.delay = delay;
        this.alertToken = alertToken;

        Validation.validateStringLength(alertToken, Validation.MAX_TRIGGER_LENGTH, "Alert token");
    }

    public Duration getDelay() {
        return delay;
    }

    public String getAlertToken() {
        return alertToken;
    }

    @Override
    public void perform(final HomeEvent homeEvent) {
        homeEvent.getHomeEditor().scheduleAlert(delay, alertToken);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitDelayedAlertCommand(this);
    }
}
