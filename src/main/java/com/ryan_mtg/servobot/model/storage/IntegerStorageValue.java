package com.ryan_mtg.servobot.model.storage;

public class IntegerStorageValue extends StorageValue {
    public static final int TYPE = 1;
    private int value;

    public IntegerStorageValue(final int id, final String name, final int value) {
        super(id, name);
        this.value = value;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String getTypeName() {
        return "Integer";
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }
}