package com.ryan_mtg.servobot.model.editors;

import com.ryan_mtg.servobot.data.factories.StorageValueSerializer;
import com.ryan_mtg.servobot.error.UserError;
import com.ryan_mtg.servobot.model.storage.IntegerStorageValue;
import com.ryan_mtg.servobot.model.storage.StorageTable;
import com.ryan_mtg.servobot.model.storage.StorageValue;

import javax.transaction.Transactional;

public class StorageValueEditor {
    private int contextId;
    private final StorageTable storageTable;
    private final StorageValueSerializer storageValueSerializer;

    public StorageValueEditor(final int contextId, final StorageTable storageTable, final StorageValueSerializer storageValueSerializer) {
        this.contextId = contextId;
        this.storageTable = storageTable;
        this.storageValueSerializer = storageValueSerializer;
    }

    public StorageValue getStorageValue(final String name) throws UserError {
        StorageValue.validateName(name);
        StorageValue storageValue = storageTable.getStorage(name);
        if (storageValue == null) {
            throw new UserError(String.format("No value with name %s.", name));
        }
        return storageValue;
    }

    @Transactional(rollbackOn = Exception.class)
    public IntegerStorageValue incrementStorageValue(final String name) throws UserError {
        StorageValue value = getStorageValue(name);
        if (value instanceof IntegerStorageValue) {
            IntegerStorageValue integerValue = (IntegerStorageValue) value;
            integerValue.setValue(integerValue.getValue() + 1);
            storageValueSerializer.save(integerValue, contextId);
            return integerValue;
        }
        throw new UserError("%s is not a number", name);
    }

    @Transactional(rollbackOn = Exception.class)
    public StorageValue setStorageValue(final String name, final String value) throws UserError {
        StorageValue storageValue = getStorageValue(name);
        if (storageValue instanceof IntegerStorageValue) {
            IntegerStorageValue integerValue = (IntegerStorageValue) storageValue;
            try {
                integerValue.setValue(Integer.parseInt(value));
            } catch (Exception e) {
                throw new UserError("Invalid value %s.", value);
            }
            storageValueSerializer.save(integerValue, contextId);
        } else {
            throw new UserError("%s has an unknown type of value.", storageValue.getName());
        }
        return storageValue;
    }
}
