package org.jystudio.opandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jystudio.opandroid.database.service.Question;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity --->";

    TextView tvMain;

    final OkHttpClient okHttpClient = new OkHttpClient();

    private MyHandler handler = new MyHandler(this);

    static class MyHandler extends Handler {
        WeakReference<MainActivity> activityWeakReference;
        MyHandler(MainActivity activity) {
            activityWeakReference = new
                    WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String stringFromServer = (String) msg.obj;
                Log.d(TAG, "handleMessage: str" + stringFromServer);
                activityWeakReference.get().parseJSONArray(stringFromServer);
            }
        }
    }

    void parseJSONArray(String jsonData) {
        Gson gson = new Gson();
        List<Question> list = gson.fromJson(jsonData, new TypeToken<List<Question>>(){}.getType());
        for (Question question : list) {
            Log.d(TAG, "parseJSONArray: id" + question.getId() + " body " + question.getBody());
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvMain = findViewById(R.id.tvHello);

        Button btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postRequest();

                    }
                }).start();
            }
        });
    }//end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    void postRequest() {
        String url = "http://10.0.2.2/OPWebServer/queryQuestion?pageNum=1&actionFlag=json";
        final Request request = new  Request.Builder()
                .url(url)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        handler.obtainMessage(1, response.body().string()).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //Not sure why fail to connect MySql.
    void tryToConnectMySqlServer () {
        final String name = "root";
        final String password = "DB123456";
        final String dbName = "op.db";
        final String tableNameQuestion = "question";
        final String tableNameUser = "user";
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

        //private static final String DB_URL = "jdbc:mysql://localhost:3306/op.db?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
        //private static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/op.db";
        //private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/op.db?user=root&password=DB123456&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&verifyServerCertificate=false";
        final String DB_URL = "jdbc:mysql://10.0.2.2:3306/op.db?user=root&password=DB123456&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&verifyServerCertificate=false";
        try {
            Class.forName(JDBC_DRIVER);
            Log.d(TAG, "load driver ok");

            Connection cn = DriverManager.getConnection(DB_URL);
            Log.d(TAG, "get connection ok");

            String sql = "select * from user";
            Statement st = cn.createStatement();
            Log.d(TAG, "createStatement ok");

            ResultSet rs = st.executeQuery(sql);
            Log.d(TAG, "query ok");

            while (rs.next()) {
                String name2 = rs.getString("name");
                Log.d(TAG, "name is " + name2);
            }

            rs.close();
            st.close();
            cn.close();

            Log.d(TAG, "done");

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "run: class not found", e);
        }
        catch (SQLException e) {
            Log.e(TAG, "run: SQL exception", e);
        }
    }

}
