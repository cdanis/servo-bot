package com.ryan_mtg.servobot.twitch.model;

import com.github.twitch4j.chat.TwitchChat;
import com.ryan_mtg.servobot.model.Channel;

public class TwitchChannelOnly implements Channel {
    private TwitchChat twitchChat;
    private String channelName;

    public TwitchChannelOnly(final TwitchChat twitchChat, final String channelName) {
        this.twitchChat = twitchChat;
        this.channelName = channelName;
    }

    @Override
    public void say(final String message) {
        if (!message.isEmpty()) {
            twitchChat.sendMessage(channelName, message);
        }
    }

}
