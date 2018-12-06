package org.jystudio.opandroid.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    static  int version = 1;

    final static String name = "op.db";

    final  static  String CREATE_TABLE = "CREATE TABLE `question` ("
            +"`id` integer NOT NULL primary key autoincrement, "
            +"`title` varchar(20) ,"
            +"`body` text NOT NULL ,"
            +"`answer` text ,"
            +"`submitter` varchar(20) DEFAULT '小明',"
            +"`modifier` varchar(20) DEFAULT '我不知道',"
            +"`lastmodify` datetime DEFAULT NULL,"
            +"`language` varchar(10) DEFAULT 'common',"
            +"`category` varchar(10) DEFAULT NULL ,"
            +"`company` varchar(20) DEFAULT NULL ,"
            +"`rate` int(1) DEFAULT '1',"
            +"`imgpath` varchar(256) DEFAULT NULL ,"
            +"`heat` int(1) DEFAULT '1' ,"
            +"`syncflag` int(1) DEFAULT '0' ,"
            +"`blame` int(1) DEFAULT '0' ,"
            +"`duplicate` int(1) DEFAULT '0');"
            ;

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDbHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO need implementation.
    }
}
