package com.crowdshelf.app.io;

/**
 * Created by Torstein on 21.09.2015.
 */
public class DbEventFailure {
    private DbEventType dbEventType;
    private int reponseCode;

    public DbEventFailure(DbEventType dbEventType, int responseCode) {
        this.dbEventType = dbEventType;
        this.reponseCode = responseCode;
    }

    public DbEventType getDbEventType() {
        return dbEventType;
    }

    public int getResponseCode() {
        return reponseCode;
    }
}
