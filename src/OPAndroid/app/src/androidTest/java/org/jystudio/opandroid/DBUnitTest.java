package org.jystudio.opandroid;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jystudio.opandroid.database.dao.MyDbDao;
import org.jystudio.opandroid.database.service.MyConstant;

import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBUnitTest {
    private static Context context;

    @BeforeClass
    public static void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void getRecordCountTest(){
        MyDbDao dbDao = new MyDbDao(context);
        long count = dbDao.getRecordCount(MyConstant.DB_QUESTION_TABLE_NAME);
        assertTrue(count > 0);
    }


}
