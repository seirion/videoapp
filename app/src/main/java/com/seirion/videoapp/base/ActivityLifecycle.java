package com.seirion.videoapp.base;

public enum ActivityLifecycle {
    CREATE(0),
    START(0x1),
    RESUME(0x2),
    PAUSE(0x4),
    STOP(0x8),
    DESTROY(0x10);

    private final int index;

    ActivityLifecycle(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}