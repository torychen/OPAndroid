package org.jystudio.opandroid.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jystudio.opandroid.database.service.IDBService;
import org.jystudio.opandroid.database.service.MyConstant;

import java.util.List;
import java.util.Map;

public class MyDbDao implements IDBService {
    private MyDbHelper dbHelper;
    private SQLiteDatabase database;

    public MyDbDao(Context context) {
        dbHelper = new MyDbHelper(context);
    }

    @Override
    public Map<String, Object> getDbVersion() {
        return null;
    }

    @Override
    public long getRecordCount(String tableName) {
        long count = 0;
        try {
            database = dbHelper.getReadableDatabase();

            String sql = "select count(*) from " + MyConstant.DB_QUESTION_TABLE_NAME;
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            count = cursor.getLong(0);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return count;
    }

    private void closeDb() {
        if (database != null) {
            database.close();
        }
    }

    @Override
    public String getLasModify(){
        return null;
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
