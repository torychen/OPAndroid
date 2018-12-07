package org.jystudio.opandroid.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jystudio.opandroid.database.service.DatabaseTableVersion;
import org.jystudio.opandroid.database.service.IDBService;
import org.jystudio.opandroid.database.service.MyConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDbDao implements IDBService {
    private MyDbHelper dbHelper;
    private SQLiteDatabase database = null;

    public MyDbDao(Context context) {
        dbHelper = new MyDbHelper(context);
    }

    private void closeDb() {
        if (database != null) {
            database.close();
        }
    }

    @Override
    public DatabaseTableVersion getTableVersion(String tableName) {
        DatabaseTableVersion tableVersion = null;

        try {
            database = dbHelper.getReadableDatabase();

            String sql = "select count(*), max(lastmodify)   from " + tableName;
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                String recordsNum = cursor.getString(0);
                String lastModify = cursor.getString(1);
                tableVersion = new DatabaseTableVersion(recordsNum, lastModify);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return tableVersion;
    }

    @Override
    public long getRecordCount(String tableName) {
        long count = 0;
        try {
            database = dbHelper.getReadableDatabase();

            String sql = "select count(*) from " + tableName;
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                count = cursor.getLong(0);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return count;
    }

    @Override
    public String getLasModify(String tableName) {
        String lastModify = null;

        try {
            database = dbHelper.getReadableDatabase();

            String sql = "select max(lastmodify) from " + tableName;

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                lastModify = cursor.getString(0);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return lastModify;
    }


    @Override
    public List<Map<String,Object>> findRecordsByLastModify(String lastModify, int count){
        return null;
    }


    @Override
    public List<Map<String,Object>> findLocalNewRecords(){
        return null;
    }

    @Override
    public Map<String, Object> findRecordById(int id) {
        return null;
    }


    @Override
    public  int getMaxId(){
        return -1;
    }


    @Override
    public  boolean updateIdToMax(int id) {
        return false;
    }


    @Override
    public Map<String, Object> sync2ServerDb(Object record) {
        return null;
    }


    @Override
    public boolean insert2ServerDb(Object record){
        return false;
    }



    @Override
    public boolean sync2Local(Object record) {
        return false;
    }


    @Override
    public boolean insert2Local(Object record) {
        return false;
    }

    @Override
    public boolean delRecord(Object record){
        return  false;
    }

}
