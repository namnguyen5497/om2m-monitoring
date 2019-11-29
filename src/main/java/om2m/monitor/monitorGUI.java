package om2m.monitor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;


public class monitorGUI implements ActionListener {
	private JFrame mainFrame;
	private JPanel monitorPanel;
	private JPanel queryPanel;
	private JScrollPane scrollerMonitor;
	private JScrollPane scrollerQuery;
	private JTextField queryText;
	public JTextArea textArea;
	public JTextArea searchArea;
	private JButton searchButton;
	
	private boolean searchPressed;
	private String queryString;
	final ScheduledExecutorService executorService;
	queryHandler qHandler;
	
	public monitorGUI(ScheduledExecutorService executorService){
		this.executorService = executorService;
		//Frame setting
		mainFrame = new JFrame();
		mainFrame.setSize(new Dimension(600,700));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Panel setting
		monitorPanel = new JPanel();
		monitorPanel.setBorder(new TitledBorder(new EtchedBorder(),"Display"));
		
		//JText setting
		textArea = new JTextArea(30, 50);
		textArea.setEditable(false);
		
		//Caret for auto scrolling for newest message
		 DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		 caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		 
		//Scroller setting
		scrollerMonitor = new JScrollPane(textArea);
		scrollerMonitor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Query panel
		//***
		//***
		//
		queryPanel = new JPanel();
		GridBagLayout grid = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		queryPanel.setBorder(new TitledBorder(new EtchedBorder(),"Search"));
		queryPanel.setLayout(grid);
		
		queryText = new JTextField(30);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 5;
		queryPanel.add(queryText, gbc);
		
		searchButton = new JButton("Search");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(5,0,0,0);
		queryPanel.add(searchButton,gbc);
		searchButton.addActionListener(this);
		
		searchArea = new JTextArea();
		searchArea.setEditable(false);
		DefaultCaret caretSearch = (DefaultCaret)searchArea.getCaret();
		 caretSearch.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollerQuery = new JScrollPane(searchArea);
		scrollerQuery.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 5;
		gbc.ipady = 400;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10,0,0,0);
		queryPanel.add(scrollerQuery, gbc);
		//
		//**
		//**
		
		monitorPanel.add(scrollerMonitor);
		mainFrame.add(monitorPanel, BorderLayout.WEST);
		mainFrame.add(queryPanel, BorderLayout.EAST);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	
	public void actionPerformed(ActionEvent e) {
		searchArea.setText("");
		if(e.getSource() == searchButton){
			queryString = queryText.getText();
			queryText.setText("");
			qHandler = new queryHandler(queryString);
			executorService.scheduleAtFixedRate(new Runnable(){
				public void run(){
					if(qHandler.checkValidQuery()){
						searchArea.setText("");
						searchArea.append(Thread.currentThread().getName() + "\n");
						for(String line : textArea.getText().split("\\n")){
							if(line.contains("in")){
								searchArea.append(qHandler.filteredResult(line) + "\n");								
							}
						}
					}
					if((qHandler.checkValidQuery() == false) || queryString.isEmpty()){
						if(queryString.isEmpty()){
							searchArea.setText("");
							searchArea.append("Empty query!");
						}
						if((qHandler.checkValidQuery() == false) && !queryString.isEmpty()){
							searchArea.setText("");
							searchArea.append("Error query!");
						}
					}
				}
			}, 0, 2, TimeUnit.SECONDS);
		}	
	}
}

