package org.jystudio.opandroid.database.service;

public class DatabaseTableVersion {
    private long recordsNum;
    private String lastModify;

    public DatabaseTableVersion(long recordsNum, String lastModify) {
        this.recordsNum = recordsNum;
        this.lastModify = lastModify;
    }

    public boolean isEqual(DatabaseTableVersion databaseTableVersion) {
        boolean flag = false;
        if ((this.recordsNum == databaseTableVersion.getRecordsNum()) &&
                (this.lastModify.equals(databaseTableVersion.getLastModify()))) {
            flag = true;
        }

        return flag;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public long getRecordsNum() {
        return recordsNum;
    }

    public void setRecordsNum(long recordsNum) {
        this.recordsNum = recordsNum;
    }
}
