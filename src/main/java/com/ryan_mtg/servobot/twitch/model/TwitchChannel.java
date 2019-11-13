package com.ryan_mtg.servobot.twitch.model;

import com.github.twitch4j.chat.TwitchChat;
import com.ryan_mtg.servobot.model.Channel;
import com.ryan_mtg.servobot.model.Emote;
import com.ryan_mtg.servobot.model.Home;
import com.ryan_mtg.servobot.model.User;

public class TwitchChannel implements Channel, Home {
    private TwitchChat twitchChat;
    private String channelName;

    public TwitchChannel(final TwitchChat twitchChat, final String channelName) {
        this.twitchChat = twitchChat;
        this.channelName = channelName;
    }

    @Override
    public Home getHome() {
        return this;
    }

    @Override
    public void say(final String message) {
        twitchChat.sendMessage(channelName, message);
    }

    @Override
    public String getName() {
        return channelName;
    }

    @Override
    public Channel getChannel(final String channelName, final int serviceType) {
        if (serviceType != TwitchService.TYPE) {
            return null;
        }
        if (channelName.equals(getName())) {
            return this;
        }
        throw new IllegalArgumentException(channelName + " is not a channel for " + getName());
    }

    @Override
    public boolean isStreamer(final User user) {
        return user.getName().toLowerCase().equals(getName());
    }

    @Override
    public String getRole(final User user, final int serviceType) {
        return "Unable to determine";
    }

    @Override
    public Emote getEmote(final String emoteName) {
        return null;
    }
}
