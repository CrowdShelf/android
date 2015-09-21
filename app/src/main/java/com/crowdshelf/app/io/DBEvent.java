package com.crowdshelf.app.io;

/**
 * Created by Torstein on 21.09.2015.
 */
public class DBEvent {
    private DBEventType dbEventType;
    private String dbObjectId;

    public DBEvent(DBEventType dbEventType, String dbObjectId) {
        this.dbEventType = dbEventType;
        this.dbObjectId = dbObjectId;
    }

    public DBEventType getDbEventType() {
        return dbEventType;
    }

    public String getDbObjectId() {
        return dbObjectId;
    }
}
