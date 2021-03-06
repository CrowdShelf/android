package com.crowdshelf.app.io;

/**
 * Created by Torstein on 21.09.2015.
 */
public enum DbEventType {
    BOOKINFO_CHANGED,
    USER_BOOKS_CHANGED,
    USER_CROWDS_CHANGED,
    USER_CROWD_BOOKS_CHANGED,
    REDRAW_USER_BOOKS,

    /*
    These should not be necessary:
    USER_CHANGED,
    BOOK_CHANGED,
    CROWD_CHANGED,
    */
    USER_CREATED,
    BOOK_CREATED,
    CROWD_CREATED,
    CROWD_BOOKS_READY,
    CROWD_READY_GET_BOOKS,

    CREATE_BOOK_AFTER_ADD,
    ADD_BOOK_USERSHELF,
    SCAN_COMPLETE_GET_BOOKINFO,
    BOOK_INFO_RECEIVED_ADD_TO_USERSHELF,
    BOOK_REMOVED,
    GET_BOOK_AFTER_ADD,
    LOGIN,

    ON_START_USER_BOOKS_READY,
    ON_START_USER_CROWDS_READY,
    ON_START_USER_CROWD_BOOKS_READY,

    UserListActivity_USER_READY, EditCrowdActivity_ADD_USERS, EditCrowdActivity_USERNAME_RECEIVED, GET_USER, NONE // To be used instead of null when you want to call a method which requires a DBEventType but you don't to handle the response
    ;
}
