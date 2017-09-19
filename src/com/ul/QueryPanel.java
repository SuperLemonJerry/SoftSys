package com.ul;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.data.DataProcess;
import com.data.DocUtil;
import com.data.ExportExcel;
import com.data.Report;

public class QueryPanel extends JPanel{
	private MyFrame mf;
	private MyPopupMenu mpm = new MyPopupMenu();
	
	private JTextField tfSoftName;
	private JTextField tfTeamName;
	private JTable resultTab;
	private Report report;
	
	
	public QueryPanel(){
		this.setSize(600, 550);
		this.setVisible(true);
		this.setLayout(null);
		
		init();
	}
	
	public QueryPanel(MyFrame mf){
		this();
		this.mf = mf;
	}
	
	public void init(){
		
		JLabel lbTips = new JLabel("提示:右键表格进行插入、删除操作");
		lbTips.setBounds(30, 8, 200, 20);
		lbTips.setForeground(Color.red);
		this.add(lbTips);
		
		JLabel lbSoftName = new JLabel("软件名：");
		lbSoftName.setBounds(30, 30, 65, 30);
		this.add(lbSoftName);
		
		JLabel lbTeamName = new JLabel("开发团队：");
		lbTeamName.setBounds(280, 30, 65, 30);
		this.add(lbTeamName);
		
			
		tfSoftName = new JTextField();
		tfSoftName.setBounds(100, 30, 150, 30);

		this.add(tfSoftName);
		
		tfTeamName = new JTextField();
		tfTeamName.setBounds(350, 30, 150, 30);
		this.add(tfTeamName);
			
	
		resultTab = new JTable();
		this.tableRefresh();
		resultTab.addMouseListener(new MyMouse());
		JScrollPane jScrollPane = new JScrollPane(resultTab);
		jScrollPane.setBounds(5,70,580,370);
		jScrollPane.addMouseListener(new MyMouse());
		this.add(jScrollPane);
		
		JButton jbSearch = new JButton("检索");
		jbSearch.setBounds(510, 30, 60, 30);
		jbSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String softName = tfSoftName.getText();
				String teamName = tfTeamName.getText();

				report = new Report();
				report.search(softName, teamName);
				resultTab.setModel(new DefaultTableModel(report.rowData , report.columnNames));
				
			}
		});
		this.add(jbSearch);
		
		JButton jbOutExcel = new JButton("导出到Excel");
		jbOutExcel.setBounds(10, 440, 120, 27);
		jbOutExcel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File f = new File("D://软件缺陷表.xls");
				try{
					if (!f.exists()){
				
					f.createNewFile();
									
				}
					new ExportExcel().exportTable(resultTab, f);
				
				}catch(Exception e){
					e.printStackTrace();
				}
				
				JDialog jDialog = new JDialog();
				jDialog.setTitle("导出成功!");
				jDialog.setBounds(400,350,200,100);
				JLabel msg = new JLabel("导出到--->D:\\软件缺陷表.xls");
				msg.setBounds(10,10,120,30);
				jDialog.add(msg);
				jDialog.setResizable(false);
				jDialog.setVisible(true);
			}
		});
		this.add(jbOutExcel);
		
		JButton jbOutDoc = new JButton("导出缺陷报告");
		jbOutDoc.setBounds(400, 440, 120, 27);
		jbOutDoc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = resultTab.getSelectedRow();
				
				if (row <= -1){
					row = 0;
				}
				
				String id = (String)resultTab.getValueAt(row, 0);
				
				Map<String, Object> m = new DataProcess().getMap(id);
				String name = (String)m.get("softname");
				String url = "D:"+File.separator+name+"缺陷报告书.doc";
				try{
					new DocUtil().createDoc(m, "ReportModel", url);
				}catch(Exception e){
					
				}
				
				
				JDialog jDialog = new JDialog();
				jDialog.setTitle("导出成功!");
				jDialog.setBounds(400,350,200,100);
				JLabel msg = new JLabel("导出到--->"+url);
				msg.setBounds(10,10,120,30);
				jDialog.add(msg);
				jDialog.setResizable(false);
				jDialog.setVisible(true);
			}
		});
		
		this.add(jbOutDoc);
		
	}
	
	public void tableRefresh(){
		report = new Report();
		report.init();
		resultTab.setModel(new DefaultTableModel(report.rowData , report.columnNames));
	}
	
	public void selectTable(Point x){
		
		int row = resultTab.rowAtPoint(x);
		
		if (row >= 0){
			resultTab.setRowSelectionInterval(row, row);
		}
		
	}
	
	class MyPopupMenu extends JPopupMenu{
		public MyPopupMenu(){
						
			JMenuItem itemInsert = new JMenuItem("插入数据");	
			itemInsert.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					new InsertDialog();
					
					
				}
			});
			this.add(itemInsert);
			
			JMenuItem itemDelete = new JMenuItem("删除数据");
			itemDelete.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					int[] delRow = resultTab.getSelectedRows();
					
					for (int i : delRow){
						String id = (String)resultTab.getValueAt(i, 0);
						new DataProcess().delete(id);
					}
					
					tableRefresh();
					
				}
			});
			this.add(itemDelete);	
			
		
		}
	}
	
	class MyMouse extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3){
				selectTable(e.getPoint());
				mpm.show(QueryPanel.this, e.getX() + 5, e.getY() + 70);
			}
						
		}
	}
	
	class InsertDialog extends JDialog{
		private JTextField tfSoftName;
		private JTextField tfVersion;
		private JTextField tfTeamName;
		private JTextField tfTestName;
		private JTextArea taSoftproblem;
		private JTextField tfModel;
		private JTextField tfModelUnit;
		private JLabel lbMsg;
		private JComboBox cbServerity;
		private JComboBox cbPriority;

		public InsertDialog(){
			super(mf,true);
			this.setBounds(310, 200, 425, 410);
			this.setTitle("插入数据");	
			this.setLayout(null);
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter(){
			   public void windowClosing(WindowEvent e) {
				   tableRefresh();
				   dispose();
			            
			   }
			});
			
			initDialog();
			
			this.setVisible(true);
				
			
			
		}
		
		public void initDialog(){
			JLabel lbSoftName = new JLabel("软件名：");
			lbSoftName.setBounds(50, 10, 65, 30);
			lbSoftName.setForeground(Color.red);
			this.add(lbSoftName);
			
			tfSoftName = new JTextField();
			tfSoftName.setBounds(130, 10, 135, 30);
			this.add(tfSoftName);
			
			JLabel lbVersion = new JLabel("版本号：");
			lbVersion.setBounds(270, 10, 65, 30);
			lbVersion.setForeground(Color.red);
			this.add(lbVersion);
			
			tfVersion = new JTextField();
			tfVersion.setBounds(328, 10, 50, 30);
			this.add(tfVersion);
//			
			JLabel lbTeamName = new JLabel("开发人员：");
			lbTeamName.setBounds(50, 50, 65, 30);
			lbTeamName.setForeground(Color.red);
			this.add(lbTeamName);
			
			tfTeamName = new JTextField();
			tfTeamName.setBounds(130, 50, 250, 30);
			this.add(tfTeamName);
//			
			JLabel lbTestName = new JLabel("测试人员：");
			lbTestName.setBounds(50, 90, 65, 30);
			lbTestName.setForeground(Color.red);
			this.add(lbTestName);
			
			tfTestName = new JTextField();
			tfTestName.setBounds(130, 90, 250, 30);
			this.add(tfTestName);
//			
			JLabel lbSoftProblem = new JLabel("问题描述：");
			lbSoftProblem.setBounds(50, 130, 65, 30);
			lbSoftProblem.setForeground(Color.red);
			this.add(lbSoftProblem);
			
			taSoftproblem = new JTextArea();
			taSoftproblem.setBounds(130, 130, 250, 100);
			JScrollPane jsSoftproblem = new JScrollPane(taSoftproblem); 
			jsSoftproblem.setBounds(130, 130, 250, 100);
			this.add(jsSoftproblem);
//			
			JLabel lbModel = new JLabel("缺陷模块：");
			lbModel.setBounds(50, 250, 65, 30);
			this.add(lbModel);
			
			tfModel = new JTextField();
			tfModel.setBounds(130, 250, 80, 30);
			this.add(tfModel);
//			
			JLabel lbModelUnit = new JLabel("缺陷单元：");
			lbModelUnit.setBounds(240, 250, 65, 30);
			this.add(lbModelUnit);
			
			tfModelUnit = new JTextField();
			tfModelUnit.setBounds(310, 250, 80, 30);
			this.add(tfModelUnit);
//			
			JLabel severity = new JLabel("严重程度：");
			severity.setBounds(50, 290, 65, 30);
			this.add(severity);
			
			String[] serveritys = new String[]{"很严重","严重","轻微"};
			cbServerity = new JComboBox(serveritys);
			cbServerity.setBounds(130, 290, 80, 30);
			cbServerity.setSelectedIndex(0);
			this.add(cbServerity);
			
//			
			JLabel priority = new JLabel("优先级：");
			priority.setBounds(240, 290, 65, 30);
			this.add(priority);	
						
			String[] prioritys = new String[]{"P1","P2","P3","P4","P5"};
			cbPriority = new JComboBox(prioritys);
			cbPriority.setBounds(310, 290, 80, 30);
			cbPriority.setSelectedIndex(0);
			this.add(cbPriority);
			
			lbMsg = new JLabel();
			lbMsg.setBounds(240, 330, 100, 30);
			this.add(lbMsg);	
			
			JButton jbInsert = new JButton("添加");
			jbInsert.setBounds(170, 330, 60, 30);
			jbInsert.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String softName = tfSoftName.getText();
					String softVeision = tfVersion.getText();
					String teamName = tfTeamName.getText();
					String testName = tfTestName.getText();
					String softProblem = taSoftproblem.getText();
					
					String model =tfModel.getText();
					String modelunit = tfModelUnit.getText();
					String severity = (String)cbServerity.getSelectedItem();
					String priority = (String)cbPriority.getSelectedItem();
					
					if (softName == null || softName.equals("")){
						lbMsg.setText("软件名不能为空！");
						return;
					}
					if (softVeision == null || softVeision.equals("")){
						lbMsg.setText("软件版本号不能为空！");
						return;
					}
					if (teamName == null || teamName.equals("")){
						lbMsg.setText("开发团队不能为空！");
						return;
					}
					if (testName == null || testName.equals("")){
						lbMsg.setText("测试人员不能为空！");
						return;
					}
					if (softProblem == null || softProblem.equals("")){
						lbMsg.setText("问题描述不能为空！");
						return;
					}
					
					Map<String,String> m = new HashMap<String, String>();
					m.put("softname",softName);
					m.put("softveision",softVeision);
					m.put("teamname",teamName);
					m.put("testname",testName);
					m.put("softproblem",softProblem);
					
					m.put("model",model);
					m.put("modelunit",modelunit);
					m.put("severity",severity);
					m.put("priority",priority);
					
					boolean b = new DataProcess(m).insert();
					
					if (b){
						lbMsg.setText("添加成功！");
					}else {
						lbMsg.setText("添加失败");
					}
					
				}
			});
			
			this.add(jbInsert);
			
		}
	}



}
