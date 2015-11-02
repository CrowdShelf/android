package com.crowdshelf.app.io;

/**
 * Created by Torstein on 21.09.2015.
 */
public class DbEvent {
    private DbEventType dbEventType;
    private String dbObjectId;

    public DbEvent(DbEventType dbEventType, String dbObjectId) {
        this.dbEventType = dbEventType;
        this.dbObjectId = dbObjectId;
    }

    public DbEventType getDbEventType() {
        return dbEventType;
    }

    public String getDbObjectId() {
        return dbObjectId;
    }
}
