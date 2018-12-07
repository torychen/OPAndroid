package org.jystudio.opandroid.database.service;

public class DatabaseTableVersion {
    private String recordsNum;
    private String lastModify;

    public DatabaseTableVersion(String recordsNum, String lastModify) {
        this.recordsNum = recordsNum;
        this.lastModify = lastModify;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public String getRecordsNum() {
        return recordsNum;
    }

    public void setRecordsNum(String recordsNum) {
        this.recordsNum = recordsNum;
    }
}
