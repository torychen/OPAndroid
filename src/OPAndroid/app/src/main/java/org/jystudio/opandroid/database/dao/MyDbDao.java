package org.jystudio.opandroid.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.jystudio.opandroid.database.service.DatabaseTableVersion;
import org.jystudio.opandroid.database.service.IDBService;
import org.jystudio.opandroid.database.service.MyConstant;
import org.jystudio.opandroid.database.service.Question;

import java.util.List;
import java.util.Map;

public class MyDbDao implements IDBService {
    private static final String TAG = "MyDbDao -->>";
    private MyDbHelper dbHelper;
    private SQLiteDatabase database = null;

    public MyDbDao(Context context, boolean isForUt) {
        if (isForUt){
            dbHelper = new MyDbHelper(context, MyConstant.DB_NAME_UT);
        } else {
            dbHelper = new MyDbHelper(context, MyConstant.DB_NAME);
        }
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
    public List<Map<String,Object>> findRecordsByLastModify(String tableName, String lastModify, int count){
        return null;
    }


    @Override
    public List<Map<String,Object>> findLocalNewRecords(String tableName){
        return null;
    }

    @Override
    public Map<String, Object> findRecordById(String tableName, long id) {
        return null;
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
    public Map<String, Object> sync2ServerDb(String tableName, Object record) {
        return null;
    }


    @Override
    public boolean insert2ServerDb(String tableName, Object record){
        return false;
    }



    @Override
    public boolean sync2Local(String tableName, Object record) {
        boolean flag;
        Question question = (Question) record;
        long orgId = question.getId();
        if (isConflictId(tableName, orgId)) {
            flag = updateIdToNewMax(tableName, orgId);

            if (!flag) {
                Log.e(TAG, "sync2Local: updateIdToNewMax() failed!", null);
                return flag;
            }
        }

        flag = insertRecord(tableName, question);

        return flag;
    }


    @Override
    public boolean insert2Local(String tableName, Object record) {
        boolean flag = false;

        Question question = (Question) record;

        //To avoid conflicts of the server created records,
        // update id and lastmodify of a record which is client create and try to insert to local database.
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

    /**
     * insert exactly the input record except the 'id' field.
     * @param tableName
     * @param question
     * @return
     */
    private boolean insertRecordAutoId(String tableName, Question question) {
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
    public boolean delRecord(String tableName, Object record){
        return  false;
    }

    public boolean isConflictId(String tableName, long id) {
        boolean isConflicted = true;

        //Invalid id need to check details, so return conflict.
        if (id <= 0) {
            return isConflicted;
        }

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


    boolean execSql(String sql, String [] strings, boolean isWriteableDb) {
        boolean flag = false;
        database = null;
        try {

            if (isWriteableDb) {
                database = dbHelper.getWritableDatabase();
            } else {
                database = dbHelper.getReadableDatabase();
            }

            database.execSQL(sql, strings);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }

        return flag;
    }



}
