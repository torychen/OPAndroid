package org.jystudio.opandroid;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jystudio.opandroid.database.dao.MyDbDao;
import org.jystudio.opandroid.database.service.DatabaseTableVersion;
import org.jystudio.opandroid.database.service.MyConstant;
import org.jystudio.opandroid.database.service.Question;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBUnitTest {
    private static Context context;
    private static MyDbDao dbDao;
    private static final String TAG = "DBUnitTest" + MyConstant.DBG_LOG_PREFIX;

    private static final String TABLE_NAME = MyConstant.DB_QUESTION_TABLE_NAME;

    private static final long RESERVED_VALID_ID = 1;
    private static final long ID_FOR_TEST = 2;

    private static final String BODY_TEST_PATTERN = "!@#$123456";



    @BeforeClass
    public static void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        dbDao = new MyDbDao(context, true);
    }

    @AfterClass
    public static void tearDown() {
        dbDao = null;
        context = null;
    }


    //UT pass
    @Ignore
    public void getRecordCountTest(){
        Question question = new Question("the lifecycle of a Service.");
        boolean flag = dbDao.insert2Local(TABLE_NAME, question);
        assertTrue(flag);

        long count = dbDao.getRecordCount(TABLE_NAME);
        assertTrue(count > 0);
    }

    //UT pass
    @Ignore
    public void getTableVersionTest() {
        DatabaseTableVersion tableVersion = dbDao.getTableVersion(TABLE_NAME);
        Log.d(TAG, "getTableVersionTest: " + tableVersion.getRecordsNum());
        Log.d(TAG, "getTableVersionTest: " + tableVersion.getLastModify());

        assertTrue(tableVersion.getRecordsNum() > 0);
    }

    @Test
    public  void isConflictIdTest() {
        long invalidId = 0;
        assertTrue(dbDao.isConflictId(TABLE_NAME, invalidId));

        long maxId = dbDao.getMaxId(TABLE_NAME);
        assertTrue(maxId > 0);

        assertTrue(dbDao.isConflictId(TABLE_NAME, maxId));

        //To test valid id but not exist yet.
        assertTrue(!dbDao.isConflictId(TABLE_NAME, RESERVED_VALID_ID));
    }

    @Test
    public void sync2LocalTestNoConflictedId() {
        //del the record before test to make sure no conflicted id.
        if (dbDao.isConflictId(TABLE_NAME, ID_FOR_TEST)) {
            dbDao.delRecord(TABLE_NAME, ID_FOR_TEST);
        }

        Question question = new Question(BODY_TEST_PATTERN);
        question.setId(ID_FOR_TEST);

        boolean flag = dbDao.sync2Local(TABLE_NAME, question);
        assertTrue(flag);

        question = (Question) dbDao.findRecordById(TABLE_NAME, ID_FOR_TEST);
        if (question != null) {
            assertEquals(BODY_TEST_PATTERN, question.getBody());
        } else {
            //Something wrong.
            assertTrue(false);
        }

        //del the record.
        flag = dbDao.delRecord(TABLE_NAME, ID_FOR_TEST);
        assertTrue(flag);

        question = (Question) dbDao.findRecordById(TABLE_NAME, ID_FOR_TEST);
        assertTrue(question == null);
    }

    @Test
    public void sync2LocalTestConflictedId() {
        //del the record before test to make sure no conflicted id.
        if (dbDao.isConflictId(TABLE_NAME, ID_FOR_TEST)) {
            dbDao.delRecord(TABLE_NAME, ID_FOR_TEST);
        }

        Question question = new Question(BODY_TEST_PATTERN);
        question.setId(ID_FOR_TEST);

        dbDao.sync2Local(TABLE_NAME, question);

        long maxId = dbDao.getMaxId(TABLE_NAME);

        //Try to insert the same id record with different body. the previous id record should be
        //updated to the new max id.
        final String TEMP_STRING = "temp-for-test";
        Question sameIdQuestion = new Question(TEMP_STRING);
        sameIdQuestion.setId(ID_FOR_TEST);
        boolean flag = dbDao.sync2Local(TABLE_NAME, sameIdQuestion);
        assertTrue(flag);

        long newMaxId = dbDao.getMaxId(TABLE_NAME);
        assertEquals(maxId + 1, newMaxId);

        question = (Question) dbDao.findRecordById(TABLE_NAME, newMaxId);
        assertEquals(BODY_TEST_PATTERN, question.getBody());

        question = (Question) dbDao.findRecordById(TABLE_NAME, ID_FOR_TEST);
        assertEquals(TEMP_STRING, question.getBody());

        dbDao.delRecord(TABLE_NAME, newMaxId);
        dbDao.delRecord(TABLE_NAME, ID_FOR_TEST);
    }

    @Test
    public void findLocalNewRecordsTest () {
        boolean flag;
        final int RECORDS_CNT = 3;
        for (int i = 0; i < RECORDS_CNT; i++) {
            Question question = new Question("test findLocalNewRecordsTest" + i);
            flag = dbDao.insert2Local(TABLE_NAME, question);
            assertTrue(flag);
        }

        List<Object> list = dbDao.findLocalNewRecords(TABLE_NAME);
        assertTrue(list != null);
        assertTrue(list.size() >= 3);

        //For debug purpose.
        Question question;
        for (Object object : list) {
            question = (Question) object;
            Log.d(TAG, "findLocalNewRecordsTest: " +
                    question.toString());
        }
    }

    
    @Test
    public void findRecordsByLastModifyTest() {
        //Clean up db by del the record before test.
        if (dbDao.isConflictId(TABLE_NAME, ID_FOR_TEST)) {
            dbDao.delRecord(TABLE_NAME, ID_FOR_TEST);
        }

        final String expected = "test findRecordsByLastModifyTest";
        Question question = new Question(expected);
        question.setId(ID_FOR_TEST);

        final String beginDateTime = "9999-01-01 00:00:00";
        final String endDateTime = "9999-01-01 00:00:01";
        question.setLastmodify(endDateTime);

        boolean flag = dbDao.sync2Local(TABLE_NAME, question);
        assertTrue(flag);

        List<Object> list = dbDao.findRecordsByLastModify(TABLE_NAME, beginDateTime);
        question = (Question) list.get(0);
        assertTrue(question != null);
        assertEquals(expected, question.getBody());

        /*//For debug purpose.
        Question question;
        for (Object object : list) {
            question = (Question) object;
            Log.d(TAG, "findLocalNewRecordsTest: " +
                    question.toString());
        }*/

        dbDao.delRecord(TABLE_NAME, ID_FOR_TEST);
    }













}
