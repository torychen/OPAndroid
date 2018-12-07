package org.jystudio.opandroid;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jystudio.opandroid.database.dao.MyDbDao;
import org.jystudio.opandroid.database.service.MyConstant;
import org.jystudio.opandroid.database.service.Question;

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

    @BeforeClass
    public static void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        dbDao = new MyDbDao(context);
    }

    @AfterClass
    public static void tearDown() {
        dbDao = null;
        context = null;
    }

    @Test
    public void getRecordCountTest(){
        Question question = new Question("the lifecycle of a Service.");
        boolean flag = dbDao.insert2Local(MyConstant.DB_QUESTION_TABLE_NAME, question);
        assertTrue(flag);

        long count = dbDao.getRecordCount(MyConstant.DB_QUESTION_TABLE_NAME);
        assertTrue(count > 0);
    }

    @Test
    public void getTableVersionTest() {

    }


}
