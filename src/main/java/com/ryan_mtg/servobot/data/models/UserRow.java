package com.ryan_mtg.servobot.data.models;

import com.ryan_mtg.servobot.utility.Validation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class UserRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int flags;

    @Column(name = "twitch_id")
    private int twitchId;

    @Column(name = "discord_id")
    private long discordId;

    @Column(name = "twitch_username")
    @Size(max = Validation.MAX_USERNAME_LENGTH)
    private String twitchUsername;

    @Column(name = "discord_username")
    @Size(max = Validation.MAX_USERNAME_LENGTH)
    private String discordUsername;

    @Column(name = "arena_username")
    @Size(max = Validation.MAX_USERNAME_LENGTH)
    private String arenaUsername;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(final int flags) {
        this.flags = flags;
    }

    public int getTwitchId() {
        return twitchId;
    }

    public void setTwitchId(final int twitchId) {
        this.twitchId = twitchId;
    }

    public long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(final long discordId) {
        this.discordId = discordId;
    }

    public String getTwitchUsername() {
        return twitchUsername;
    }

    public void setTwitchUsername(final String twitchUsername) {
        this.twitchUsername = twitchUsername;
    }

    public String getDiscordUsername() {
        return discordUsername;
    }

    public void setDiscordUsername(final String discordUsername) {
        this.discordUsername = discordUsername;
    }

    public String getArenaUsername() {
        return arenaUsername;
    }

    public void setArenaUsername(final String arenaUsername) {
        this.arenaUsername = arenaUsername;
    }
}
