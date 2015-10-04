package com.crowdshelf.app;

/**
 * Created by markuslund92 on 14.09.15.
 */
public enum ScannedBookActions {
    ADD_BUTTON_CLICKED(0),
    BORROW_BUTTON_CLICKED(1),
    RETURN_BUTTON_CLICKED(2),
    IS_OWNER(3),
    IS_RENTING_BOOK(4),
    NOT_OWNING_OR_RENTING(5),
    REMOVE_BUTTON_CLICKED(6),
    UNKNOWN(10), BOOK_BORROWED(7);

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
