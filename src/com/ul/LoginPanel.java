package com.ul;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.data.CommonDAO;

public class LoginPanel extends JPanel{
	private MyFrame mf;
	
	private JTextField tfName;
	private JPasswordField tfPw;
	
	public LoginPanel(){
		
		this.setSize(600, 550);
		this.setVisible(true);
		this.setLayout(null);
		
		init();
	}
	
	public LoginPanel(MyFrame mf){
		this();
		this.mf = mf;
	}
	
	public void init(){
		
		JLabel Lbwelcom = new JLabel("软件缺陷管理系统");
		Lbwelcom.setBounds(92, 33, 380, 70);
		Lbwelcom.setFont(new Font("华文新魏", Font.BOLD, 40));
		this.add(Lbwelcom);
		
		JLabel Lbname = new JLabel("用户名：");
		Lbname.setBounds(140, 140, 55, 30);
		Lbname.setBackground(Color.gray);
		Lbname.setOpaque(true);
		this.add(Lbname);
		
		JLabel Lbpw = new JLabel("密    码：");
		Lbpw.setBounds(140, 210, 55, 30);
		Lbpw.setBackground(Color.gray);		
		Lbpw.setOpaque(true);
		this.add(Lbpw);
//		
		tfName = new JTextField();
		tfName.setBounds(200, 140, 250, 30);
		this.add(tfName);
//		
		tfPw = new JPasswordField();
		tfPw.setBounds(200, 210, 250, 30);
		this.add(tfPw);
		
		Monitor m = new Monitor();
		
		JButton jbLogin = new JButton("登陆");
		jbLogin.setBounds(120, 300, 60, 30);
		jbLogin.setActionCommand("LOGIN");
		jbLogin.addActionListener(m);
		this.add(jbLogin);
//		
		JButton jbReset = new JButton("重置");
		jbReset.setBounds(345, 300, 60, 30);
		jbReset.setActionCommand("RESET");
		jbReset.addActionListener(m);
		this.add(jbReset);
		
	}
	
	class Monitor implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent action) {
			
			String command = action.getActionCommand();
			if (command.equals("LOGIN")){
				String name = tfName.getText();
				String pw = new String(tfPw.getPassword());
				CommonDAO commonDAO = CommonDAO.getCommonDAO();
				String sql = "select * from user_table where uName = '" + name + "' and pw = '" + pw +"'" ;			
				
				ResultSet rs = commonDAO.executeSelect(sql);

				try {
					if (rs.next()){
						mf.addPanel(new QueryPanel(mf));
						return;
					}
				}catch(Exception e){
					System.out.println("执行查询异常");
				}
				
				final JDialog jDialog = new JDialog(mf,"账号或密码不正确");
				jDialog.setBounds(330, 230, 300, 200);
				jDialog.setLayout(null);
				jDialog.setVisible(true);
				
				JLabel tips = new JLabel("提示：请输入正确的账号密码!");
				tips.setBounds(40,20,240,30);
				jDialog.add(tips);
				
				JButton ok = new JButton("确定");
				ok.setBounds(100, 100, 60, 30);
				jDialog.add(ok);
				ok.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						jDialog.dispose();
						
					}
				});
				
				tfPw.setText("");
			}else if (command.equals("RESET")){
				tfName.setText("");
				tfPw.setText("");
			}		
		}	
	}
}
