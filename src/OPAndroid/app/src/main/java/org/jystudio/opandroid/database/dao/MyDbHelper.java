package org.jystudio.opandroid.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jystudio.opandroid.database.service.MyConstant;

public class MyDbHelper extends SQLiteOpenHelper {
    private  static  int version = 1;

    private final  static  String CREATE_TABLE = "CREATE TABLE "
            + MyConstant.DB_QUESTION_TABLE_NAME
            +"(`id` integer NOT NULL primary key autoincrement, "
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

     MyDbHelper(Context context, String name) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO onUpgrade() need implementation.
    }
}
