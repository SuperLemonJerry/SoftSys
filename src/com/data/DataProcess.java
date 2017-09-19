package com.data;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DataProcess {
	
	private Map<String, String> data = new HashMap<String, String>();
	private CommonDAO commonDAO = CommonDAO.getCommonDAO();
	
	
	public DataProcess(){
		
	}
	
	public DataProcess(Map<String, String> data){
		this.data = data;
	}
	
	public boolean insert(){
		String softName = data.get("softname");
		String softVeision = data.get("softveision");
		String teamName = data.get("teamname");
		String testName = data.get("testname");
		String softProblem = data.get("softproblem");
		
		String model = data.get("model");
		String modelunit = data.get("modelunit");
		String severity = data.get("severity");
		String priority = data.get("priority");
		
		String testTime = "";
		
		String sql = "insert into report_table(softName,softVersion,modelName,modelUnitName,teamName,testName,testTime,severity,priority,softProblem) values";
		String value = "('"+softName+"','"+softVeision+"','"+model+"','"+modelunit+"','"+teamName+"','"+testName+"','"+testTime+"','"+severity+"','"+priority+"','"+softProblem+"')";
		
		
		String insertSql = sql + value;
		
		int b = commonDAO.executeUpdate(insertSql);
		
		if (b >= 0){
			return true;
		}else {
			return false;
		}
	}
	
	public void delete(String id){
	
		String sql = "delete from report_table where id =" + id ;
		
		commonDAO.executeUpdate(sql);
	}
	
	public Map<String, Object> getMap(String id){
		String sql = "select * from report_table where id =" + id ;
		Map<String, Object> m = new HashMap<String, Object>();
		
		ResultSet rs = commonDAO.executeSelect(sql);
		try{
			while (rs.next()){
				String softname = rs.getString("softName");
				String model = rs.getString("modelName");
				String modelunit = rs.getString("modelUnitName");
				String version = rs.getString("softVersion");
				String teamname = rs.getString("teamName");
				String testname = rs.getString("testName");
				String softproblem = rs.getString("softProblem");
				String severity = rs.getString("severity");
				String priority = rs.getString("priority");
				
				m.put("id",id);
				m.put("softname", softname);
				
				if (model == null){
					model = " ";
				}
				if (modelunit == null){
					modelunit = " ";
				}
				
				if (severity == null){
					severity = "ÑÏÖØ";
				}
				
				if (priority == null){
					priority = "P1";
				}
				
				
				m.put("model", model);
				m.put("modelunit", modelunit);
				m.put("version",version);
				m.put("teamname", teamname);
				m.put("testname", testname);
				m.put("softproblem", softproblem);
				m.put("severity", severity);
				m.put("priority", priority);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return m;
	}
	
}
