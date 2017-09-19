package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CommonDAO {
	private String DBDriver = null;
	private String url = null;
	private String user = null;
	private String password = null;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	private static final CommonDAO commonADO = new CommonDAO();
	
	private CommonDAO(){
		DBDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		url = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=SoftTest";
		user = "sa";
		password = "123456";
		
		try {
			Class.forName(DBDriver);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			
			System.out.println("������ʾ���޷��������ݿ�");
			System.out.println("����ԭ��1���޷�ʹ���û�sa������123456��¼���ݿ�");
			System.out.println("����ԭ��2��1433���ݿ�˿���δ��");
			
			e.printStackTrace();
		} 
	}
	
	public static CommonDAO getCommonDAO(){
		return commonADO;
	}
	
	public ResultSet executeSelect(String sql){
		if(sql.toLowerCase().indexOf("select") != -1){
			try {
				rs = stmt.executeQuery(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}
	
	public int executeUpdate(String sql){
		int result = 0;
		String str = sql.toLowerCase();
		if(str.indexOf("update")!=-1 || str.indexOf("insert")!=-1 || str.indexOf("delete")!=-1){
			try {
				result = stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public Connection getConn(){
		return conn;
	}
	
	public Statement getStmt(){
		return stmt;
	}
	
	public void closeDB(){
		try {
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
	}

}
