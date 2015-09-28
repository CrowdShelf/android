package com.crowdshelf.app.io;

/**
 * Created by Torstein on 21.09.2015.
 */
public enum DBEventType {
    USER_CHANGED,
    BOOK_CHANGED,
    BOOKINFO_CHANGED,
    CROWD_CHANGED,
    USER_BOOKS_CHANGED,
    CROWD_BOOKS_CHANGED,

    USER_CREATED,
    BOOK_CREATED,
    CROWD_CREATED,

    ADD_BOOKINFO_USERSHELF,
    SCAN_COMPLETE_GET_BOOKINFO,
    LOGIN
}
