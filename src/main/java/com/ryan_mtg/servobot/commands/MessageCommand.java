package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.model.Channel;
import com.ryan_mtg.servobot.model.Message;

public abstract class MessageCommand extends Command {
    abstract public void perform(Message message, String arguments);

    public MessageCommand(final int id) {
        super(id);
    }

    protected static void say(Message message, final String text) {
        Channel channel = message.getChannel();
        channel.say(text);
    }
}
