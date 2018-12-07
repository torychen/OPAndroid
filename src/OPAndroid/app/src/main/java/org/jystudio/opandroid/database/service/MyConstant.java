package org.jystudio.opandroid.database.service;

public class MyConstant {
    final public static String DBG_LOG_PREFIX = "-->>";

    final public static String DB_NAME = "op.db";
    final public static String DB_NAME_UT = "oput.db";
    final public static String DB_QUESTION_TABLE_NAME = "question";
    final public static String DB_QUESTION_TABLE_ID = "id";
    final public static String DB_QUESTION_TABLE_TITLE = "title";
    final public static String DB_QUESTION_TABLE_BODY = "body";
    final public static String DB_QUESTION_TABLE_ANSWER = "answer";
    final public static String DB_QUESTION_TABLE_SUBMITTER = "submitter";
    final public static String DB_QUESTION_TABLE_MODIFIER = "modifier";
    final public static String DB_QUESTION_TABLE_LASTMODIFY = "lastmodify";
    final public static String DB_QUESTION_TABLE_LANGUAGE = "language";
    final public static String DB_QUESTION_TABLE_CATEGORY = "category";
    final public static String DB_QUESTION_TABLE_COMPANY = "company";
    final public static String DB_QUESTION_TABLE_RATE = "rate";
    final public static String DB_QUESTION_TABLE_IMGPATH = "imgpath";
    final public static String DB_QUESTION_TABLE_HEAT = "heat";
    final public static String DB_QUESTION_TABLE_SYNCFLAG = "syncflag";
    final public static String DB_QUESTION_TABLE_BLAME = "blame";
    final public static String DB_QUESTION_TABLE_DUPLICATE = "duplicate";

    //Sync flag
    final public static int SYNC_FLAG_SERVER_ADD = 0;
    final public static int SYNC_FLAG_SERVER_MODIFY = 1;
    final public static int SYNC_FLAG_SERVER_DEL = 2;
    final public static int SYNC_FLAG_LOCAL_ADD = 3;
    final public static int SYNC_FLAG_LOCAL_MODIFY = 4;
    final public static int SYNC_FLAG_LOCAL_DEL = 5;

    //the constant timestamp when client insert to client database.
    //Ensure the timestamp is less than any record which is inserted to server database.
    final public static String MY_D_DAY_DATETIME = "1970-12-01 00:00:00";

    //the beginning id for client insert to client database.
    //Try to avoid id conflict with record whish is inserted into server database.
    final public static int ID_BEGINNING_FOR_CLIENT_INSERT = 9999999;
}
