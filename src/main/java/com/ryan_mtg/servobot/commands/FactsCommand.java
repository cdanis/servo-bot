package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.events.MessageSentEvent;
import com.ryan_mtg.servobot.model.Book;

public class FactsCommand extends MessageCommand {
    public static final int TYPE = 2;

    private Book book;

    public FactsCommand(final int id, final int flags, final Permission permission, final Book book) {
        super(id, flags, permission);
        this.book = book;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void acceptVisitor(final CommandVisitor commandVisitor) {
        commandVisitor.visitFactsCommand(this);
    }

    @Override
    public void perform(final MessageSentEvent event, final String arguments) {
        MessageCommand.say(event, book.getRandomLine());
    }

    public Book getBook() {
        return book;
    }

    public void setBook(final Book book) {
        this.book = book;
    }
}
