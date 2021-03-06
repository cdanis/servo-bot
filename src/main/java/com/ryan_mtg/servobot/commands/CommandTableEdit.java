package com.ryan_mtg.servobot.commands;

import com.ryan_mtg.servobot.commands.hierarchy.Command;
import com.ryan_mtg.servobot.commands.trigger.Trigger;
import com.ryan_mtg.servobot.model.alerts.AlertGenerator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommandTableEdit {
    @Getter
    private List<Command> deletedCommands = new ArrayList<>();

    @Getter
    private Map<Command, Integer> savedCommands = new HashMap<>();

    @Getter
    private List<Trigger> deletedTriggers = new ArrayList<>();

    @Getter
    private Map<Trigger, Integer> savedTriggers = new IdentityHashMap<>();
    private Map<Command, Trigger> savedCommandToTriggerMap = new HashMap<>();
    private Map<Command, Consumer<Command>> commandSaveCallbackMap = new HashMap<>();
    private Map<Trigger, BiConsumer<Integer, Trigger>> triggerSaveCallbackMap = new IdentityHashMap<>();

    @Getter
    private Map<AlertGenerator, Integer> savedAlertGenerators = new HashMap<>();

    @Getter
    private List<AlertGenerator> deletedAlertGenerators = new ArrayList<>();

    public void delete(final Command command) {
        deletedCommands.add(command);
    }

    public void save(final int contextId, final Command command) {
        savedCommands.put(command, contextId);
    }

    public void save(final int contextId, final Command command, final Consumer<Command> commandSaveCallback) {
        save(contextId, command);
        commandSaveCallbackMap.put(command, commandSaveCallback);
    }

    public void save(final int botHomeId, final Command command, final Trigger trigger,
            final Consumer<Command> commandSaveCallback, final BiConsumer<Integer, Trigger> aliasSaveCallback) {
        savedCommands.put(command, botHomeId);
        savedCommandToTriggerMap.put(command, trigger);
        commandSaveCallbackMap.put(command, commandSaveCallback);
        triggerSaveCallbackMap.put(trigger, aliasSaveCallback);
    }

    public void save(final int commandId, final Trigger trigger,
                     final BiConsumer<Integer, Trigger> triggerSaveCallback) {
        savedTriggers.put(trigger, commandId);
        triggerSaveCallbackMap.put(trigger, triggerSaveCallback);
    }

    public void commandSaved(final Command command) {
        if (savedCommandToTriggerMap.containsKey(command)) {
            savedTriggers.put(savedCommandToTriggerMap.get(command), command.getId());
        }
        if (commandSaveCallbackMap.containsKey(command)) {
            commandSaveCallbackMap.get(command).accept(command);
        }
    }

    public void triggerSaved(final Trigger trigger) {
        triggerSaveCallbackMap.get(trigger).accept(savedTriggers.get(trigger), trigger);
    }

    public void delete(final Trigger trigger) {
        deletedTriggers.add(trigger);
    }

    public void save(final int botHomeId, final AlertGenerator alertGenerator) {
        savedAlertGenerators.put(alertGenerator, botHomeId);
    }

    public void delete(final AlertGenerator alertGenerator) {
        deletedAlertGenerators.add(alertGenerator);
    }

    public void merge(final CommandTableEdit commandTableEdit) {
        deletedCommands.addAll(commandTableEdit.deletedCommands);
        savedCommands.putAll(commandTableEdit.savedCommands);

        deletedTriggers.addAll(commandTableEdit.deletedTriggers);

        savedTriggers.putAll(commandTableEdit.savedTriggers);
        savedCommandToTriggerMap.putAll(commandTableEdit.savedCommandToTriggerMap);
        commandSaveCallbackMap.putAll(commandTableEdit.commandSaveCallbackMap);
        triggerSaveCallbackMap.putAll(commandTableEdit.triggerSaveCallbackMap);

        savedAlertGenerators.putAll(commandTableEdit.savedAlertGenerators);
        deletedAlertGenerators.addAll(deletedAlertGenerators);
    }

    private <Key, Value> void merge(final Map<Key, Value> map, final Map<Key, Value> sourceMap) {
        sourceMap.forEach((key, value) -> map.merge(key, value,(value1, value2) -> value1));
    }
}
