package com.tory.opandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private final static String name = "root";
    private final static String password = "DB123456";
    private final static String dbName = "op.db";
    private final static String tableNameQuestion = "question";
    private final static String tableNameUser = "user";
    private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";


    private static final String TAG = "MainActivity --->";



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
                                Log.d(TAG, "onCreate: load driver ok");

                                try {
                                    //java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/op.db", name, password);
                                    java.sql.Connection cn = DriverManager.getConnection(
                                            "jdbc:mysql://127.0.0.1:3306/op.db?user=root&password=DB123456useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false");

                                    String sql = "select * from user";

                                    Statement st= cn.createStatement();
                                    ResultSet rs=st.executeQuery(sql);
                                    while(rs.next()){
                                        String name=rs.getString("name");
                                        Log.d(TAG, "name is " + name);
                                    }

                                    rs.close();
                                    st.close();
                                    cn.close();

                                    Log.d(TAG, "done");

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}
