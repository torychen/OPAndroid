package org.jystudio.opandroid.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import org.jystudio.opandroid.database.service.DatabaseTableVersion;
import org.jystudio.opandroid.database.service.IDBService;
import org.jystudio.opandroid.database.service.MyConstant;
import org.jystudio.opandroid.database.service.Question;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jystudio.opandroid.database.service.MyConstant.*;

public class MyDbDao implements IDBService {
    private static final String TAG = "MyDbDao -->>";
    private MyDbHelper dbHelper;
    private SQLiteDatabase database = null;

    private int resultCount = 10;

    public MyDbDao (Context context) {
        dbHelper = new MyDbHelper(context, MyConstant.DB_NAME);
    }

    public MyDbDao(Context context, boolean isForUt) {
        if (isForUt){
            dbHelper = new MyDbHelper(context, MyConstant.DB_NAME_UT);
        } else {
            dbHelper = new MyDbHelper(context, MyConstant.DB_NAME);
        }
    }


    private boolean validateCount(final int count) {
        return count > 0 && count < 99999;
    }

    @Override
    public boolean setResultCount(int count) {
        boolean flag = false;
        if (validateCount(count)) {
            flag = true;
            resultCount = count;
        }

        return flag;
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
                long recordsNum = cursor.getLong(0);
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
    public List<Object> findRecords(String tableName, String sql){
        //ok Log.d(TAG, "findRecords: the sql is " + sql);

        List<Object> list = new ArrayList<>();
        Question question;
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                question = getQuestion(cursor);
                list.add(question);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return list;
    }


    @Override
    public List<Object> findRecordsByLastModify(String tableName, String lastModify) {
        return findRecordsByLastModify(tableName, lastModify, resultCount);
    }

    @Override
    public List<Object> findRecordsByLastModify(String tableName, String lastModify, int count) {
        if (!validateCount(count)) {
            return null;
        }

        String sql = "select * from " +
                tableName +
                " where lastmodify>" +
                "\'" + lastModify + "\'" +
                " limit " + Integer.toString(count);

        return findRecords(tableName, sql);
    }


    @Override
    public List<Object> findLocalNewRecords(String tableName) {
        return findLocalNewRecords(tableName, resultCount);
    }

    @Override
    public List<Object> findLocalNewRecords(String tableName, int count){
        if (!validateCount(count)) {
            return null;
        }

        String sql = "select * from " +
                tableName +
                " where syncflag>=" +
                SYNC_FLAG_LOCAL_ADD +
                " limit " +
                Integer.toString(count);


        return findRecords(tableName, sql);
    }

    @Override
    public Object findRecordById(String tableName, final long id) {
        if (id <= 0) {
            return null;
        }

        Question question = null;
        try {
            database = dbHelper.getReadableDatabase();

            String sql = "select * from " + tableName + " where id=" + Long.toString(id);
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                question = getQuestion(cursor);
            }

            cursor.close();

            //Log.d(TAG, "findRecordById: the quesion is " + question.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return question;
    }

    @NonNull
    private Question getQuestion(Cursor cursor) {
        Question question;
        question = new Question("na");

        question.setId(cursor.getLong(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_ID)));
        question.setTitle(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_TITLE)));
        question.setBody(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_BODY)));
        question.setAnswer(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_ANSWER)));
        question.setSubmitter(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_SUBMITTER)));
        question.setModifier(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_MODIFIER)));
        question.setLastmodify(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_LASTMODIFY)));
        question.setLanguage(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_LANGUAGE)));
        question.setCategory(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_CATEGORY)));
        question.setCompany(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_COMPANY)));
        question.setRate(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_RATE)));
        question.setImgpath(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_IMGPATH)));
        question.setHeat(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_HEAT)));
        question.setSyncflag(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_SYNCFLAG)));
        question.setBlame(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_BLAME)));
        question.setDuplicate(cursor.getString(cursor.getColumnIndex(MyConstant.DB_QUESTION_TABLE_DUPLICATE)));
        return question;
    }


    @Override
    public  long getMaxId(String tableName){
        long id = 0;
        try {
            database = dbHelper.getReadableDatabase();

            String sql = "select max(id) from " + tableName;
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                id = cursor.getLong(0);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return id;
    }


    @Override
    public boolean updateRecord(String tableName, Object record) {
        Question question = (Question) record;
        long orgId = question.getId();
        if (orgId <= 0) {
            return false;
        }

        boolean flag = false;
        try {
            database = dbHelper.getWritableDatabase();
            StringBuilder sqlBuilder = new StringBuilder("update  ");
            sqlBuilder.append(tableName + " set ");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_TITLE + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_BODY + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_ANSWER + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_SUBMITTER + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_MODIFIER + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_LASTMODIFY + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_LANGUAGE + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_CATEGORY + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_COMPANY + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_RATE + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_IMGPATH + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_HEAT + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_SYNCFLAG + "=?,");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_BLAME + "=?,");
            //last field
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_DUPLICATE + "=?" );

            sqlBuilder.append(" where id=" + Long.toString(orgId));

            String sql = sqlBuilder.toString();

            //OK Log.d(TAG, "updateRecord: the sql is: " + sql);

            String [] strings = new String[] {
                    question.getTitle(),
                    question.getBody(),
                    question.getAnswer(),
                    question.getSubmitter(),
                    question.getModifier(),
                    question.getLastmodify(),
                    question.getLanguage(),
                    question.getCategory(),
                    question.getCompany(),
                    question.getRate(),
                    question.getImgpath(),
                    question.getHeat(),
                    question.getSyncflag(),
                    question.getBlame(),
                    question.getDuplicate()
            };

            database.execSQL(sql, strings);

            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return flag;
    }

    @Override
    public  boolean updateIdToNewMax(String tableName, long orgId) {
        if (orgId <= 0) {
            return false;
        }

        long maxId = getMaxId(tableName);
        if (maxId <= 0) {
            return false;
        }

        maxId++;
        boolean flag = false;
        try {
            database = dbHelper.getWritableDatabase();

            String sql = "update " + tableName + " set id=" + Long.toString(maxId) + " where id=" + Long.toString(orgId);
            Log.d(TAG, "updateIdToNewMax: the sql is " + sql);

            database.execSQL(sql);
            flag = true;
            Log.d(TAG, "updateIdToNewMax: orgId is" + Long.toString(orgId) + "new id is " + Long.toString(maxId) );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return flag;
    }


    @Override
    public Map<String, Object> sync2Server(String tableName, Object record) {
        Question question = (Question) record;

        //The new input record should be local add or local modify
        int syncflag = Integer.parseInt(question.getSyncflag());
        if (syncflag < SYNC_FLAG_LOCAL_ADD) {
            return null;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String lastmodify = timestamp.toString();
        question.setLastmodify(lastmodify);
        Log.d(TAG, "sync2Server: lastmodify is " + lastmodify);

        Map<String, Object> map = null;

        //Handle local add, local modify separately.
        if (SYNC_FLAG_LOCAL_ADD == syncflag) {
            question.setSyncflag(Integer.toString(SYNC_FLAG_SERVER_ADD));
            //Directly insert it,
            // id will be automatically corrected.
            // lastmodify should be updated to current.
            // syncflag should be updated SYNC_FLAG_SERVER_ADD.
            boolean flag = insert2Server(tableName, question);
            if (flag) {
                Long maxId = getMaxId(tableName);
                if (maxId > 0) {
                    map = new HashMap<>();
                    map.put(DB_QUESTION_TABLE_ID, maxId);
                    map.put(DB_QUESTION_TABLE_LASTMODIFY, lastmodify);

                    Log.d(TAG, "sync2Server: lastmodify is " + lastmodify);
                    Log.d(TAG, "sync2Server: id is " + maxId);
                }
            }
        } else if (SYNC_FLAG_LOCAL_MODIFY == syncflag) {
            //id no change
            // lastmodify should be updated to current.
            // syncflag should be updated SYNC_FLAG_SERVER_MODIFY.
            question.setSyncflag(Integer.toString(SYNC_FLAG_SERVER_MODIFY));
            boolean flag = updateRecord(tableName, question);
            if (flag) {
                map = new HashMap<>();
                map.put(DB_QUESTION_TABLE_ID, question.getId());
                map.put(DB_QUESTION_TABLE_LASTMODIFY, lastmodify);

                Log.d(TAG, "sync2Server: SYNC_FLAG_LOCAL_MODIFY lastmodify is " + lastmodify);
                Log.d(TAG, "sync2Server: SYNC_FLAG_LOCAL_MODIFY id is " + question.getId());
            }

        } else {
            Log.e(TAG, "sync2Server: error not support syncflag", null);
        }

        return map;
    }


    @Override
    public boolean sync2Local(String tableName, Object record) {
        boolean flag = false;
        final Question question = (Question) record;

        //The new input record should be server add or server modify.
        int syncflag = Integer.parseInt(question.getSyncflag());
        if (syncflag > SYNC_FLAG_SERVER_MODIFY) {
            return false;
        }

        Log.d(TAG, "sync2Local: syncflag is " + question.getSyncflag());

        long orgId = question.getId();
        Question orgRecord = (Question) findRecordById(tableName, orgId);
        if (orgRecord != null) {
            Log.d(TAG, "sync2Local: org record is not null");

            //if the org record is inserted / modify by local,
            //update its id to new max then insert the new record.
            syncflag =  Integer.parseInt(orgRecord.getSyncflag());
            if (syncflag >= SYNC_FLAG_LOCAL_ADD ) {
                flag = updateIdToNewMax(tableName, orgId);
                if (!flag) {
                    Log.e(TAG, "sync2Local: updateIdToNewMax() failed!", null);
                    return false;
                }
            } else {
                //The org record is inserted / modified by server,
                //update it accordingly.

                Log.d(TAG, "sync2Local: try to update");

                flag = updateRecord(tableName, question);
                if (!flag) {
                    Log.e(TAG, "sync2Local: updateRecord() fail.", null);
                }
                Log.d(TAG, "sync2Local: updateRecord " + flag);
                return flag;
            }
        }

        //Insert directly since local db no such record.
        flag = insertRecord(tableName, question);

        return flag;
    }


    @Override
    public boolean insert2Local(String tableName, Object record) {
        boolean flag;

        Question question = (Question) record;
        //The new input record should be local add.
        int syncflag = Integer.parseInt(question.getSyncflag());
        if (syncflag != SYNC_FLAG_LOCAL_ADD) {
            return false;
        }

        //To avoid conflicts of the server created records,
        //update 'id' and 'lastmodify' of a record which is client create and try to insert to local database.
        question.setLastmodify(MyConstant.MY_D_DAY_DATETIME);

        long id = getMaxId(tableName);
        Log.d(TAG, "insert2Local: max id is " + Long.toString(id) + "\n");

        //This is the first local record to be inserted, use the special id instead.
        if(id < MyConstant.ID_BEGINNING_FOR_CLIENT_INSERT) {
            question.setId(MyConstant.ID_BEGINNING_FOR_CLIENT_INSERT);
        } else {
            //There already have some local records, then id++
            id++;
            question.setId(id);
        }

        Log.d(TAG, "insert2Local: question id is" + question.getId() + "\n");

        flag = insertRecord(tableName, question);

        return flag;
    }

    /**
     * insert a record exactly the same as the input.
     * @param tableName the table name
     * @param question  the record.
     * @return true or false
     */
    private boolean insertRecord(String tableName, Question question) {
        boolean flag = false;
        try {
            database = dbHelper.getWritableDatabase();
            StringBuilder sqlBuilder = new StringBuilder("insert into  ");
            sqlBuilder.append(tableName + "(");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_ID + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_TITLE + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_BODY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_ANSWER + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_SUBMITTER + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_MODIFIER + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_LASTMODIFY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_LANGUAGE + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_CATEGORY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_COMPANY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_RATE + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_IMGPATH + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_HEAT + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_SYNCFLAG + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_BLAME + ",");
            //last field
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_DUPLICATE + ")");

            //16 space holder
            sqlBuilder.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            String sql = sqlBuilder.toString();

            //ut pass Log.d(TAG, "insert2Local: the sql is: " + sql);

            String [] strings = new String[] {
                    Long.toString(question.getId()),
                    question.getTitle(),
                    question.getBody(),
                    question.getAnswer(),
                    question.getSubmitter(),
                    question.getModifier(),
                    question.getLastmodify(),
                    question.getLanguage(),
                    question.getCategory(),
                    question.getCompany(),
                    question.getRate(),
                    question.getImgpath(),
                    question.getHeat(),
                    question.getSyncflag(),
                    question.getBlame(),
                    question.getDuplicate()
            };

            //why only show part of strings? Log.d(TAG, "insert2Local: the values is" + strings.toString());

            database.execSQL(sql, strings);

            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }
        return flag;
    }

    @Override
    public boolean insert2Server(String tableName, Object record){
        Question question = (Question) record;

        //The new input record should be server add.
        int syncflag = Integer.parseInt(question.getSyncflag());
        if (syncflag != SYNC_FLAG_SERVER_ADD) {
            return false;
        }

        boolean flag = false;
        try {
            database = dbHelper.getWritableDatabase();
            StringBuilder sqlBuilder = new StringBuilder("insert into  ");
            sqlBuilder.append(tableName + "(");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_TITLE + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_BODY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_ANSWER + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_SUBMITTER + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_MODIFIER + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_LASTMODIFY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_LANGUAGE + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_CATEGORY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_COMPANY + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_RATE + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_IMGPATH + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_HEAT + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_SYNCFLAG + ",");
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_BLAME + ",");
            //last field
            sqlBuilder.append(MyConstant.DB_QUESTION_TABLE_DUPLICATE + ")");

            //15 space holder
            sqlBuilder.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            String sql = sqlBuilder.toString();

            //ut pass Log.d(TAG, "insert2Local: the sql is: " + sql);

            String [] strings = new String[] {
                    question.getTitle(),
                    question.getBody(),
                    question.getAnswer(),
                    question.getSubmitter(),
                    question.getModifier(),
                    question.getLastmodify(),
                    question.getLanguage(),
                    question.getCategory(),
                    question.getCompany(),
                    question.getRate(),
                    question.getImgpath(),
                    question.getHeat(),
                    question.getSyncflag(),
                    question.getBlame(),
                    question.getDuplicate()
            };

            database.execSQL(sql, strings);

            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }
        return flag;
    }

    @Override
    public boolean delRecord(String tableName, long id){
        if (id <= 0) {
            return  false;
        }

        boolean flag = false;
        try {
            database = dbHelper.getWritableDatabase();

            String sql = "delete from " + tableName + " where id=" + Long.toString(id);
            //ok Log.d(TAG, "delRecord: the sql is " + sql);

            database.execSQL(sql);
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return flag;
    }

    public boolean isConflictId(String tableName, long id) {
        //Invalid id need to check details, so return conflict.
        if (id <= 0) {
            return true;
        }

        boolean isConflicted = true;
        try {
            database = dbHelper.getReadableDatabase();

            String sql = "select id from " + tableName + " where id=" + Long.toString(id);
            //pass ut Log.d(TAG, "isConflictId: sql is:" + sql);

            Cursor cursor = database.rawQuery(sql, null);
            if (!cursor.moveToFirst()) {
                //pass ut Log.d(TAG, "isConflictId: cursor return nothing. ");
                //find nothing, so NOT conflict.
                isConflicted = false;
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return isConflicted;
    }

}
