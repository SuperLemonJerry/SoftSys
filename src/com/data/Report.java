package com.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

public class Report{
	public Vector<Vector<String>> rowData = null;
	public Vector<String> columnNames = null;
	
	public Report(){
		rowData = new Vector<Vector<String>>();
		columnNames = new Vector<String>();
		
		columnNames.add("ID");
		columnNames.add("软件名");
		columnNames.add("版本信息");
		columnNames.add("开发团队名");
		columnNames.add("测试人员名");
		columnNames.add("缺陷信息");
	}
	
	public void init(){		
		String sql = "select * from report_table";
		executeSql(sql);

	}
	
	public void executeSql(String sql){
		CommonDAO con = CommonDAO.getCommonDAO();
		ResultSet rs = con.executeSelect(sql);
		try{
			//单行信息		
			while (rs.next()){
				Vector<String> info = new Vector<String>();
				info.add(String.valueOf(rs.getInt("id")));
				info.add(rs.getString("softName"));
				info.add(rs.getString("softVersion"));
				info.add(rs.getString("teamName"));
				info.add(rs.getString("testName"));
				info.add(rs.getString("softProblem"));
				
				rowData.add(info);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void search(String softname, String teamname){
		
		String sql = "";
		
		if (softname == null || softname.equals("")){
			if (teamname == null || teamname.equals("")){
				init();
			}else {
				sql = "select * from report_table where teamName like '%" + teamname +"%'";
			}
		}else {
			
			if (teamname == null || teamname.equals("")){
				sql = "select * from report_table where softName like '%" + softname +"%'";
			}else {
				sql = "select * from report_table where softName like '%" + softname +"%' and teamName like '%"+teamname+"%'";
			}
		}
		
		executeSql(sql);
	}
	
	
	

}
