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
@Table(name = "reaction")
public class ReactionRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int filter;

    @Column(name = "filter_value")
    private int filterValue;

    @Column(name = "bot_home_id")
    private int botHomeId;

    @Size(max = Validation.MAX_EMOTE_LENGTH)
    private String emote;

    private boolean secure;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(final int filter) {
        this.filter = filter;
    }

    public int getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(final int filterValue) {
        this.filterValue = filterValue;
    }

    public int getBotHomeId() {
        return botHomeId;
    }

    public void setBotHomeId(final int botHomeId) {
        this.botHomeId = botHomeId;
    }

    public String getEmote() {
        return emote;
    }

    public void setEmote(final String emote) {
        this.emote = emote;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(final boolean secure) {
        this.secure = secure;
    }
}
