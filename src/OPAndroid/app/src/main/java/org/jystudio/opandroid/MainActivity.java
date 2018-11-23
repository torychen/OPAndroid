package org.jystudio.opandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity --->";
    private final static String name = "root";
    private final static String password = "DB123456";
    private final static String dbName = "op.db";
    private final static String tableNameQuestion = "question";
    private final static String tableNameUser = "user";
    private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    //private static final String DB_URL = "jdbc:mysql://localhost:3306/op.db?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
    //private static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/op.db";
    //private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/op.db?user=root&password=DB123456&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&verifyServerCertificate=false";
    private static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/op.db?user=root&password=DB123456&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&verifyServerCertificate=false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                                String name = rs.getString("name");
                                Log.d(TAG, "name is " + name);
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
                }).start();
            }
        });
    }//end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}
