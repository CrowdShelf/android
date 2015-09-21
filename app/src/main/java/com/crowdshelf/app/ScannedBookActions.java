package com.crowdshelf.app;

/**
 * Created by markuslund92 on 14.09.15.
 */
public enum ScannedBookActions {
    ADD(0) , BORROW(1), RETURN(2), UNKNOWN(3);

    public final int value;

    ScannedBookActions(int value) {
        this.value = value;
    }

    public static ScannedBookActions fromValue(int value) {
        for (ScannedBookActions result : values()) {
            if (result.value == value) return result;
        }
        return UNKNOWN;
    }
}
