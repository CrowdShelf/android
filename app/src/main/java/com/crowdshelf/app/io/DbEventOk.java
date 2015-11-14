package com.crowdshelf.app.io;

/**
 * Created by Torstein on 21.09.2015.
 */
public class DbEventOk {
    private DbEventType dbEventType;
    private String dbObjectId;

    public DbEventOk(DbEventType dbEventType, String dbObjectId) {
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
