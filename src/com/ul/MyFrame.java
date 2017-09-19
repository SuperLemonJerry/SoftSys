package com.ul;

import java.awt.Menu;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MyFrame extends JFrame{
	
	private JPanel CurrentPanel; 
	private JMenuBar jMenuBar;
	private JMenu filemenu;
	private JMenuItem importfile;
	
	private MyFrame mf;
	
	public MyFrame(){
				
		this.setBounds(220, 150,600,500);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("软件缺陷管理系统");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mf = this;
		
		CurrentPanel = new JPanel();
		this.add(CurrentPanel);
		init();
//		menu();
		
		this.repaint();
	}
	
	public void init(){
		this.addPanel(new LoginPanel(this));
//		this.addPanel(new QueryPanel(this));
	}
	
	public void menu(){
		jMenuBar = new JMenuBar();
		
		filemenu = new JMenu("文件");
		importfile = new JMenuItem("打开");		
		filemenu.add(importfile);
		jMenuBar.add(filemenu);
			
		this.setJMenuBar(jMenuBar);
		
	}
	
	public void addPanel(JPanel panel){
		JPanel oldPanel = CurrentPanel;
		this.remove(oldPanel);
		this.add(panel);
		CurrentPanel = panel;
		
		this.repaint();

	}
	
}
