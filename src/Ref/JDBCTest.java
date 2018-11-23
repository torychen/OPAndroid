package com.tory.jdbc;

import java.sql.*;

public class JDBCTest {

	public static void main(String[] args) {
		String driver = "com.mysql.jdbc.Driver";
		String URL = "jdbc:mysql://localhost:3306/op.db";
		final String DB_URL = "jdbc:mysql://127.0.0.1:3306/op.db?user=root&password=DB123456&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false";
		
        ResultSet rs = null;
        Statement st = null;
        String sql = "select * from user";
        
		Connection con = null;
		try
		{
			Class.forName(driver);
			System.out.println("load driver ok.");
		}
		catch(java.lang.ClassNotFoundException e)
		{
			System.out.println("Cant't load Driver");
		}
		
		try   
		{                                                                               
			con=DriverManager.getConnection(DB_URL);
			System.out.println("Connect Successfull.");
			
			st=con.createStatement();
            rs=st.executeQuery(sql);
            if(rs!=null) {
            	ResultSetMetaData rsmd = rs.getMetaData();
            	int countcols = rsmd.getColumnCount();
            	for(int i=1;i<=countcols;i++) {
            		if(i>1) System.out.print(";");
            		System.out.print(rsmd.getColumnName(i)+" ");
            	}
            	System.out.println("");
            	while(rs.next()) {
            		System.out.print(rs.getString("id")+"  ");
            		System.out.print(rs.getString("name")+"  ");
            		System.out.print(rs.getString("password")+"  ");
            		
            		
            	}
            }
  
            
            rs.close();
            st.close();
            con.close();
            
            System.out.println("done");

		} 
		catch(Exception e)
		{
			System.out.println("Connect fail:" + e.getMessage());
		}

	}

}
