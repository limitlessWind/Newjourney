package serialPort;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTabbedPane;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.LineBorder;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.BoxLayout;

import component.Order;
import component.OrderPanel;
import component.ParsePanel;
import component.Parser;
import component.ParserUnit;
import component.Project;
import java.awt.Component;
import java.awt.GridLayout;

public class Client extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1409186265988239619L;

	SerialReader sr = new SerialReader();
	Project presentProj;
	Document doc;
	Element rootElement;
	byte[] dataToJudge = null;
	List<OrderPanel> orderRowList = new ArrayList<>();
	List<ParsePanel> parseRowList = new ArrayList<>();
	List<Project> projects = new ArrayList<>();
	List<GraphMaker> graphs;
	int parseRowCount = 1;
	int orderRowCount = 1;
	boolean orderNewProj = false;
	boolean parserNewProj = false;
	boolean isParserTested = false;
	boolean isParserTesting = false; 		//test parser
	boolean isRecording = false;
	boolean hasGraph = false;
	String orderString = null;
	Parser parserToSave = null;
	private boolean isHex;
	private final String relProjFileName = "\\Projects.dat";
	private String fileName;
	
	private List<String> commList;
	private JComboBox port;
	private JComboBox rate;
	private JTextField directOrderTf;
	private JTextArea resultArea;
	private JLabel lblContext;
	private JTextArea receivedArea;
	private JTextArea dataArea;
	private JTextArea sendedOrderArea;
	private JToggleButton openPortBtn;
	private final JTabbedPane leftTabbedPane;
	private JLabel lblNewLabel_6;
	private JTextField orderNameTf;
/*	private JRadioButton hexRadioBtn;		//delete direct send order
	private JRadioButton asciiRadioBtn;*/
	private JComboBox<Order> ordersBox = new JComboBox<Order>();
	private JComboBox<Parser> parsersBox = new JComboBox<Parser>();
	private JComboBox<Project> projsBox;
	private JComboBox<Project> projNameBox;
	private JComboBox<Project> parseProjNameBox;
	private JTextField projNameTf;
	private JTextField parserNameTf;
	private JPanel orderPartsPane;
	private JPanel parserPanel;
	private JPanel graphPanel;
	private JTextField newParseProjTf;
	private JButton beginParsebtn;
	private JButton recordbutton;
	private JTextArea parserMessageArea;
	private JScrollPane parserMessagePane;

	public Client() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
		 //设置窗体外观为windows
    	try{  		
//            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//    		javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//    		javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            
        }catch(Exception e){
            System.out.println("设置窗体windows外观失败");
        }
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("串口交互工具");
        setName("client");
        setBounds(0, 0, 1300, 700);
		setResizable(true);
		getContentPane().setLayout(null);

		
		JPanel left_panel = new JPanel();
		left_panel.setBounds(0, 0, 404, 667);
		left_panel.setLayout(null);
		JScrollPane leftPane = new JScrollPane(left_panel);
		leftPane.setMinimumSize(new Dimension(410, 600));
		leftPane.setSize(410, 700);
		getContentPane().add(leftPane);
		
		
		JPanel port_panel = new JPanel();
		port_panel.setBounds(0, 0, 394, 78);
		port_panel.setToolTipText("");
		left_panel.add(port_panel);
		port_panel.setLayout(null);
		
		commList = SerialReader.findPort();
		port = new JComboBox();
		port.setBounds(21, 29, 81, 37);
		if (commList == null || commList.size() < 1) {
			port.setModel(new DefaultComboBoxModel(new String[] {"COM1", "COM2", "COM3", "COM4"}));
		} else {
			for (String s : commList) {
				port.addItem(s);
			}
		}
		port_panel.add(port);
		
		rate = new JComboBox();
		rate.setBounds(129, 29, 89, 37);
		rate.setModel(new DefaultComboBoxModel(
				new String[] {"300", "600", "1200", "2400","3600", "4800", "9600", "19200", "38400", "43000", "56000", "57600", "115200"}));
		port_panel.add(rate);
		
		openPortBtn = new JToggleButton("打开串口");
		openPortBtn.setBounds(248, 28, 98, 38);
		port_panel.add(openPortBtn);
		openPortBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openOrCloseSerialPort();
			}
		});
				
		leftTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		leftTabbedPane.setBounds(0, 88, 410, 502);
		
		JPanel runOrder_panel = new JPanel();
		leftTabbedPane.addTab("运行命令", runOrder_panel);
//		leftTabbedPane.setBackgroundAt(0, SystemColor.textHighlight);
		runOrder_panel.setLayout(null);
		
		JLabel lblNewLabel_7 = new JLabel("选择命令");
		lblNewLabel_7.setFont(new Font("宋体", Font.PLAIN, 13));
		lblNewLabel_7.setBounds(10, 58, 85, 49);
		runOrder_panel.add(lblNewLabel_7);
		

		ordersBox.setBounds(86, 64, 161, 36);
		runOrder_panel.add(ordersBox);
		
		parsersBox = new JComboBox<>();
		parsersBox.setBounds(86, 118, 161, 36);
		runOrder_panel.add(parsersBox);
		
		JButton sendOrderBtn = new JButton("发送");
		sendOrderBtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		sendOrderBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChosenOrder();
			}
		});
		sendOrderBtn.setBounds(257, 64, 93, 36);
		runOrder_panel.add(sendOrderBtn);
		
		parserMessageArea = new JTextArea(3, 25);
		parserMessagePane = new JScrollPane(parserMessageArea);
		parserMessagePane.setVisible(false);
		parserMessagePane.setBounds(10, 320, 379, 109);
		runOrder_panel.add(parserMessagePane);
		
		JLabel lblNewLabel_8 = new JLabel("选择项目");
		lblNewLabel_8.setFont(new Font("宋体", Font.PLAIN, 13));
		lblNewLabel_8.setBounds(10, 23, 66, 25);
		runOrder_panel.add(lblNewLabel_8);
		
		projsBox = new JComboBox<>();
		projsBox.setBounds(86, 10, 161, 36);
		projsBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (projsBox.getSelectedItem() == null) {
					return;
				}
				setOrdersBox();
				
			}
		});
		runOrder_panel.add(projsBox);
		
		JLabel lblNewLabel_14 = new JLabel("解析格式");
		lblNewLabel_14.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_14.setBounds(10, 117, 85, 36);
		runOrder_panel.add(lblNewLabel_14);
		
		JLabel label = new JLabel("记录数据");
		label.setFont(new Font("SimSun", Font.PLAIN, 12));
		label.setBounds(10, 180, 66, 25);
		runOrder_panel.add(label);
		
		recordbutton = new JButton("开始记录");
		recordbutton.setFont(new Font("SimSun", Font.PLAIN, 12));
		recordbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				beginRecord();
			}
		});
		recordbutton.setBounds(105, 169, 126, 49);
		runOrder_panel.add(recordbutton);
		
		JButton button_1 = new JButton("保存");
		button_1.setFont(new Font("SimSun", Font.PLAIN, 12));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveRecordedDoc();
			}
		});
		button_1.setBounds(296, 169, 93, 49);
		runOrder_panel.add(button_1);
		
		JButton button = new JButton("查看");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lookUpPaser();
			}
		});
		button.setBounds(257, 118, 93, 36);
		runOrder_panel.add(button);
		
		JPanel order_panel = new JPanel();
		order_panel.setBounds(0, 110, 404, 406);
		order_panel.setLayout(null);
		leftTabbedPane.addTab("编辑命令", order_panel);
		left_panel.add(leftTabbedPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setFont(new Font("SimSun", Font.PLAIN, 12));
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(10, 88, 379, 29);
		order_panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel = new JLabel("命令组成");
		lblNewLabel.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel.setPreferredSize(new Dimension(80, 20));
		lblNewLabel.setMaximumSize(new Dimension(48, 30));
		panel_1.add(lblNewLabel);
		
		lblContext = new JLabel();
		lblContext.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblContext.setHorizontalAlignment(SwingConstants.CENTER);
		lblContext.setHorizontalTextPosition(SwingConstants.CENTER);
		lblContext.setPreferredSize(new Dimension(100, 20));
		lblContext.setText("内容");
		panel_1.add(lblContext);
		
		JLabel label_1 = new JLabel("长度(可选）");
		label_1.setFont(new Font("SimSun", Font.PLAIN, 12));
		label_1.setPreferredSize(new Dimension(80, 20));
		panel_1.add(label_1);
		
		JLabel lblNewLabel_1 = new JLabel("十六进制？");
		lblNewLabel_1.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setPreferredSize(new Dimension(80, 20));
		panel_1.add(lblNewLabel_1);
		
		/*JLabel label = new JLabel("命令");			//delete direct send order
		label.setBounds(10, 398, 53, 29);
		order_panel.add(label);
		
		directOrderTf = new JTextField();
		directOrderTf.setBounds(73, 398, 172, 29);
		order_panel.add(directOrderTf);
		directOrderTf.setColumns(50);
		
		JButton directSendButton = new JButton("直接发送");
		directSendButton.setBounds(271, 398, 93, 29);
		order_panel.add(directSendButton);
		directSendButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {				
				directSendOrder();
			}			
		});*/
		
		JButton sendOrderButton = new JButton("发送命令");
		sendOrderButton.setFont(new Font("SimSun", Font.PLAIN, 12));
		sendOrderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendOrder();
			}
		});
		sendOrderButton.setBounds(73, 402, 130, 37);
		order_panel.add(sendOrderButton);
		
		lblNewLabel_6 = new JLabel("命令名称");
		lblNewLabel_6.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_6.setBounds(10, 50, 83, 28);
		order_panel.add(lblNewLabel_6);
		
		orderNameTf = new JTextField();
		orderNameTf.setToolTipText("请不要与已有命令名重复");
		orderNameTf.setBounds(73, 51, 121, 28);
		order_panel.add(orderNameTf);
		orderNameTf.setColumns(10);
		
		JButton saveOrderBtn = new JButton("保存命令");
		saveOrderBtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		saveOrderBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveOrder();
			}
		});
		saveOrderBtn.setBounds(272, 402, 93, 37);
		order_panel.add(saveOrderBtn);
		
/*		ButtonGroup bg = new ButtonGroup();		//delete direct send order
		hexRadioBtn = new JRadioButton("十六进制");
		bg.add(hexRadioBtn);
		hexRadioBtn.setSelected(true);
		hexRadioBtn.setBounds(26, 433, 121, 23);
		order_panel.add(hexRadioBtn);
		
		asciiRadioBtn = new JRadioButton("ASCII");
		bg.add(asciiRadioBtn);
		asciiRadioBtn.setBounds(149, 433, 121, 23);
		order_panel.add(asciiRadioBtn);*/
		
		JLabel lblNewLabel_11 = new JLabel("项目名称");
		lblNewLabel_11.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_11.setBounds(9, 10, 54, 28);
		order_panel.add(lblNewLabel_11);
			
		projNameBox = new JComboBox<>();
		
		projNameBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (projNameBox.getSelectedItem() == null) {
					return;
				}
				if (((Project)projNameBox.getSelectedItem()).toString() == "新建项目"){
					orderNewProj = true;
					projNameTf.setVisible(true);		
				}else {
					orderNewProj = false;
					projNameTf.setVisible(false);
				}
			}
			
		});
		projNameBox.setBounds(73, 14, 121, 29);
		order_panel.add(projNameBox);
		
		projNameTf = new JTextField();
		projNameTf.setBounds(217, 14, 148, 29);
		projNameTf.setColumns(15);
		projNameTf.setVisible(false);
		order_panel.add(projNameTf);
		
		JScrollPane orderJsp = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		orderJsp.setBounds(10, 112, 379, 248);
		order_panel.add(orderJsp);
		
		orderPartsPane = new JPanel();
//		orderPartsPane.setPreferredSize(new Dimension(379, 205));			//设置此项，大小固定，scrollPane不再起作用
		orderPartsPane.setBounds(10, 117, 379, 205);
//		order_panel.add(orderPartsPane);
		orderJsp.setViewportView(orderPartsPane);
		orderPartsPane.setLayout(new BoxLayout(orderPartsPane, BoxLayout.Y_AXIS));
		
		OrderPanel originRow = new OrderPanel(1);
		orderRowList.add(originRow);
		orderPartsPane.add(originRow);
		
		JButton addRowBtn = new JButton("加一行");
		addRowBtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		addRowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addOrderRow();
			}
		});
		addRowBtn.setBounds(296, 364, 93, 28);
		order_panel.add(addRowBtn);
		
		JButton decRowBtn = new JButton("减一行");
		decRowBtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		decRowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decOrderRow();
			}
		});
		decRowBtn.setBounds(204, 364, 93, 28);
		order_panel.add(decRowBtn);
		
		JPanel defParserPanel = new JPanel();
		leftTabbedPane.addTab("解析格式", defParserPanel);
		defParserPanel.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("格式名称");
		lblNewLabel_2.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(10, 45, 68, 27);
		defParserPanel.add(lblNewLabel_2);
		
		parserNameTf = new JTextField();
		parserNameTf.setHorizontalAlignment(SwingConstants.LEFT);
		parserNameTf.setBounds(88, 47, 118, 24);
		defParserPanel.add(parserNameTf);
		parserNameTf.setColumns(20);
		
		JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);				//test for ScrollPane
		scrollPane.setBounds(0, 82, 399, 301);
		defParserPanel.add(scrollPane);
		
		parserPanel = new JPanel();						
		parserPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		parserPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		parserPanel.setBounds(10, 44, 389, 303);
//		defParserPanel.add(parserPanel);
//		scrollPane.add(parserPanel);
		scrollPane.setViewportView(parserPanel);
		parserPanel.setLayout(new BoxLayout(parserPanel, BoxLayout.Y_AXIS));
		
		JPanel headerPanel = new JPanel();
		headerPanel.setMaximumSize(new Dimension(32767, 50));
		FlowLayout flowLayout = (FlowLayout) headerPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		headerPanel.setPreferredSize(new Dimension(389, 40));
		headerPanel.setFont(new Font("宋体", Font.PLAIN, 12));
		headerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		parserPanel.add(headerPanel);
		
		JLabel lblNewLabel_3 = new JLabel("名称");
		lblNewLabel_3.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_3.setPreferredSize(new Dimension(80, 30));
		headerPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("位置");
		lblNewLabel_4.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_4.setPreferredSize(new Dimension(70, 30));
		headerPanel.add(lblNewLabel_4);
		
		JLabel label_3 = new JLabel("内容(可选)");
		label_3.setFont(new Font("SimSun", Font.PLAIN, 12));
		label_3.setPreferredSize(new Dimension(70, 30));
		headerPanel.add(label_3);
		
		JLabel lblNewLabel_5 = new JLabel("放大倍数(可选)");
		lblNewLabel_5.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_5.setToolTipText("可选项");
		lblNewLabel_5.setPreferredSize(new Dimension(80, 30));
		headerPanel.add(lblNewLabel_5);
		
		JLabel lblNewLabel_12 = new JLabel("类型");
		lblNewLabel_12.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_12.setPreferredSize(new Dimension(70, 30));
		headerPanel.add(lblNewLabel_12);
		
		JLabel lblNewLabel_13 = new JLabel("定义位");
		lblNewLabel_13.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel_13.setPreferredSize(new Dimension(54, 30));
		headerPanel.add(lblNewLabel_13);
		
		ParsePanel originParsePane = new ParsePanel();
		parseRowList.add(originParsePane);
		parserPanel.add(originParsePane);
		
		JButton addPaserRowBtn = new JButton("加一行");
		addPaserRowBtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		addPaserRowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPaserRow();
			}
		});
		addPaserRowBtn.setBounds(306, 385, 93, 27);
		defParserPanel.add(addPaserRowBtn);
		
		JButton decPaserRowBtn = new JButton("减一行");
		decPaserRowBtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		decPaserRowBtn.setBounds(214, 385, 93, 27);
		defParserPanel.add(decPaserRowBtn);
		decPaserRowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decPaserRow();
			}
		});
		
		JButton saveParserBtn = new JButton("保存");
		saveParserBtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		saveParserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveParser();
			}
		});
		saveParserBtn.setBounds(214, 418, 93, 34);
		defParserPanel.add(saveParserBtn);
		
		beginParsebtn = new JButton("开始解析");
		beginParsebtn.setFont(new Font("SimSun", Font.PLAIN, 12));
		beginParsebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beginParse();
			}
		});
		beginParsebtn.setBounds(55, 418, 93, 34);
		defParserPanel.add(beginParsebtn);
		
		JLabel label_2 = new JLabel("项目名称");
		label_2.setFont(new Font("SimSun", Font.PLAIN, 12));
		label_2.setBounds(10, 10, 68, 27);
		defParserPanel.add(label_2);
		
		parseProjNameBox = new JComboBox<Project>();
		
		parseProjNameBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (parseProjNameBox.getSelectedItem() == null) {
					return;
				}
				if (((Project)parseProjNameBox.getSelectedItem()).toString() == "新建项目"){
					parserNewProj = true;
					newParseProjTf.setVisible(true);		
				}else {
					parserNewProj = false;
					newParseProjTf.setVisible(false);
				}
			}
			
		});
		parseProjNameBox.setBounds(88, 6, 118, 27);
		defParserPanel.add(parseProjNameBox);
		
		newParseProjTf = new JTextField();
		newParseProjTf.setBounds(218, 6, 118, 27);
		newParseProjTf.setColumns(10);
		newParseProjTf.setVisible(false);
		defParserPanel.add(newParseProjTf);

		
		JPanel panel = new JPanel();
		panel.setBounds(0, 589, 404, 78);
		left_panel.add(panel);
		panel.setLayout(null);
		
		resultArea = new JTextArea();
		resultArea.setBounds(20, 20, 357, 37);
		panel.add(resultArea);
		resultArea.setColumns(10);
		
	
		JPanel result_panel = new JPanel();
		result_panel.setBounds(414, 0, 579, 657);
		result_panel.setLayout(null);
		
		JTabbedPane rightTabbedPane = new JTabbedPane(SwingConstants.TOP);
		rightTabbedPane.setBounds(414, 0, 870, 662);
		rightTabbedPane.addTab("接发数据", result_panel);
		getContentPane().add(rightTabbedPane);
		
		receivedArea = new JTextArea();
		JScrollPane raScrollPane = new JScrollPane(receivedArea);
		receivedArea.setEditable(false);
//		receivedArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		raScrollPane.setBounds(89, 208, 655, 178);
		result_panel.add(raScrollPane);
		receivedArea.setColumns(32);
		
		dataArea = new JTextArea();
		JScrollPane daScrollPane = new JScrollPane(dataArea);
//		dataArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		dataArea.setFont(new Font("楷体", Font.PLAIN, 13));
		dataArea.setLineWrap(true);
		dataArea.setColumns(32);
		daScrollPane.setBounds(89, 422, 655, 186);
		result_panel.add(daScrollPane);
		
		sendedOrderArea = new JTextArea();
		JScrollPane soaScrollPane = new JScrollPane(sendedOrderArea);
//		sendedOrderArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		sendedOrderArea.setColumns(32);
		soaScrollPane.setBounds(89, 27, 655, 138);
		result_panel.add(soaScrollPane);
		
		JLabel lblNewLabel_9 = new JLabel("已发送");
		lblNewLabel_9.setFont(new Font("宋体", Font.PLAIN, 13));
		lblNewLabel_9.setBounds(10, 83, 54, 30);
		result_panel.add(lblNewLabel_9);
		
		JLabel lblNewLabel_10 = new JLabel("已接收");
		lblNewLabel_10.setFont(new Font("宋体", Font.PLAIN, 13));
		lblNewLabel_10.setBounds(10, 285, 54, 15);
		result_panel.add(lblNewLabel_10);
		
		JPanel panel_3 = new JPanel();
		rightTabbedPane.addTab("图像", panel_3);
		panel_3.setLayout(null);
		
		graphPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) graphPanel.getLayout();
		flowLayout_1.setVgap(1);
		flowLayout_1.setHgap(1);
		graphPanel.setBounds(0, 0, 865, 564);
		panel_3.add(graphPanel);
		
		readOrdersFromFile();
		refreshBoxes();		//initial projects comboBox-es

         new Thread(new JudgeData()).start();

	}


	protected void lookUpPaser() {
		if (parserMessagePane.isVisible()) {
			parserMessagePane.setVisible(false);
			return;
		}
		Parser parser = (Parser) parsersBox.getSelectedItem();
		StringBuilder upline = new StringBuilder();
		StringBuilder downline = new StringBuilder();
		for (ParserUnit pu : parser.getParserUnits()) {
			String location = pu.getLocation();
			String content = pu.getContent();
			upline.append(location + "  ");
			if (content == null  || content.isEmpty()) {	//no content
				downline.append(pu.getName() + "  ");
			} else {
				downline.append(content + "  ");
			}
			
		}
/*		int length = 10;
		upline.setLength(length);
		
		for (ParserUnit pu : parser.getParserUnits()) {
			String location = pu.getLocation();
			String content = pu.getContent();
			int[] locals = Utils.location2Ints(location);
			if (locals[locals.length-1] > length - 1) {
				length = locals[locals.length-1] + 1;
				upline.setLength(length);
			}
			if (content == null  || content.isEmpty()) {	//no content				
				if (locals.length == 1) {
					upline.setCharAt(locals[0], '-');
				} else {
					int i = locals[0];
					while (i <= locals[1]) {
						upline.setCharAt(i, '-');
						i++;
					}
				}
			} else {
				if (locals.length == 1) {
					upline.setCharAt(locals[0], content.charAt(0));
				} else {				
					upline.replace(locals[0], locals[1], content);
				}
			}
		}
		int i = 0;
		while (i < length) {
			downline.append(i);
			i++;
		}*/
		parserMessageArea.setText(upline.toString() + "\n" + downline.toString());
		parserMessagePane.setVisible(true);
		
	}


	protected void saveRecordedDoc() {
		if (rootElement == null) {
			resultArea.setText("无数据，无法保存");
			return;
		}
		if (doc == null) {
			doc = new Document(rootElement);
		}
		XMLOutputter xmlOut = new XMLOutputter();
		try {
			File file = new File(System.getProperty("user.dir") + "\\records\\" + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);		
			xmlOut.output(doc, outStream);
			outStream.close();
			resultArea.setText("保存成功");
		} catch (IOException e) {
			   e.printStackTrace();
			   resultArea.setText("保存失败");
		} 

	}


	protected void beginRecord() {
		
		if (!isRecording) {
			isRecording = true;
			recordbutton.setText("停止记录");
		} else {
			isRecording = false;
			recordbutton.setText("开始记录");
		}
		
	}


	protected void beginParse() {
		if (!sr.isOpen) {
			resultArea.setText("串口未打开");
			return;
		}
		if (isParserTesting) {
			isParserTesting = false;
			beginParsebtn.setText("开始解析");
			return;
		}
		List<ParserUnit> unitList = new ArrayList<>();
		for (ParsePanel p : parseRowList) {
			if (p.getParserUnit() != null) {
				unitList.add(p.getParserUnit());
			} else {
				resultArea.setText("解析格式不正确，无法运行");
				return;
			}
		}
		parserToSave = new Parser();
		parserToSave.setParserUnits(unitList);
		
		isParserTesting = true;
		beginParsebtn.setText("停止解析");
		isParserTested = true;
	}


	protected void saveParser() {
		if (!isParserTested) {
			resultArea.setText("请先测试此解析格式");
			return;
		}
		String parserName = parserNameTf.getText().trim();
		if (parserName == null || parserName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入命令名称", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (parserNewProj) {
			String projName = newParseProjTf.getText().trim();
			if (projName == null || projName.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入项目名称", "", JOptionPane.WARNING_MESSAGE);
				return;
			}	
			parserToSave.setName(parserName);
			List<Parser> list = new ArrayList<>();
			list.add(parserToSave);
			Project p = new Project(projName, null, list);
			projects.add(p);
		} else {
			parserToSave.setName(parserName);
			Project p = (Project)parseProjNameBox.getSelectedItem();
			if (p.getParserList() == null) {
				List<Parser> list = new ArrayList<>();
				p.setParserList(list);
			}
			p.getParserList().add(parserToSave);
		}	
		refreshBoxes();
		parsersBox.updateUI();
		resultArea.setText("格式添加成功");
		parserToSave = null;
		
		isParserTesting = false;
		isParserTested = false;
	}


	private void refreshBoxes() {
		projsBox.removeAllItems();
		projNameBox.removeAllItems();
		parseProjNameBox.removeAllItems();
		for(Project p : projects) {
			projsBox.addItem(p);
			projNameBox.addItem(p);
			parseProjNameBox.addItem(p);
		}
		parseProjNameBox.addItem(new Project("新建项目"));
		projNameBox.addItem(new Project("新建项目"));
	}


	protected void setOrdersBox() {
		presentProj = (Project)projsBox.getSelectedItem();
		ordersBox.removeAllItems();
		parsersBox.removeAllItems();
		if (presentProj.getOrderList() != null) {
			for (Order o : presentProj.getOrderList()) {
				ordersBox.addItem(o);
			}
		}
		if (presentProj.getParserList() != null) {
			for (Parser p : presentProj.getParserList()) {
				parsersBox.addItem(p);
			}
		}
	}


	protected void decOrderRow() {
		if (orderRowCount == 1) {
			return;
		}
		orderPartsPane.remove(orderRowList.remove(--orderRowCount));
		orderPartsPane.updateUI();
		System.out.println(orderRowCount);
	}

	protected void addOrderRow() {
		OrderPanel newRow = new OrderPanel(++orderRowCount);
		orderRowList.add(newRow);
		orderPartsPane.add(newRow);
		orderPartsPane.updateUI();
		System.out.println(orderRowCount);
	}

	protected void decPaserRow() {
		if (parseRowCount == 1) {
			return;
		}
		parserPanel.remove(parseRowList.remove(--parseRowCount));
		parserPanel.updateUI();
		System.out.println(parseRowCount);
	}

	protected void addPaserRow() {
		ParsePanel newRow = new ParsePanel();
		parseRowList.add(newRow);
		parseRowCount++;
		parserPanel.add(newRow);
		parserPanel.updateUI();
		System.out.println(parseRowCount);
	}

	private void closeWindow() {
		 if (projects != null && !projects.isEmpty()) {
			 try {
					 File file = new File(System.getProperty("user.dir") + relProjFileName);
					 if (!file.exists()) {
						 file.createNewFile();
					 }
					FileOutputStream fos = new FileOutputStream(file);
				    ObjectOutputStream oos = new ObjectOutputStream(fos);	        		       		        
				    oos.writeObject(projects);
				    oos.close();
				    System.out.println("已保存在" + System.getProperty("user.dir") + relProjFileName);
			 } catch(IOException e) {
				 e.printStackTrace();
			 }
		 }
		if(sr.isOpen) {
			try {
				sr.close();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}	 
		 System.exit(0);
	}

	@SuppressWarnings("unchecked")
	private void readOrdersFromFile() {
		 try {        		       		        
	            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + relProjFileName);
		        ObjectInputStream ois = new ObjectInputStream(fis);
		        projects = (List<Project>) ois.readObject();
		        ois.close();
		        for (Project p : projects) {
		        	System.out.println(p.toString());
		        }
		        System.out.println("done");

		
		} catch (FileNotFoundException e) {
			projects = new ArrayList<Project>();
			JOptionPane.showMessageDialog(null, "未检测到可用命令", "", JOptionPane.WARNING_MESSAGE);			
			e.printStackTrace();
		} catch (NullPointerException e){
			projects = new ArrayList<Project>();
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		} 
		 if (projects != null && !projects.isEmpty()) {
			 System.out.println("检测到" + projects.size() + "个project");
			 for (int i = 0; i < projects.size(); i++) {
				 projsBox.addItem(projects.get(i));
			 }
			 if (projects.get(0).getOrderList() != null) {
				 for (Order o : projects.get(0).getOrderList()) {
					 ordersBox.addItem(o);
				 }
			 }
			 if (projects.get(0).getParserList() != null) {
				 for (Parser p : projects.get(0).getParserList()) {
					 parsersBox.addItem(p);
				 }
			 }
		 }
		
	}

	protected void sendChosenOrder() {
		isParserTesting = false;
		
		Order order = (Order) ordersBox.getSelectedItem();
		String code = order.getCode();
		System.out.println("order is: " + code);
		if(code == null || code.isEmpty()) {
			JOptionPane.showMessageDialog(null, "无法获取此命令", "", JOptionPane.WARNING_MESSAGE);
			resultArea.setText("此命令失效，请删除后新建命令");
			return;
		}
		try {
			sr.sendToPort(code);
			sendedOrderArea.append(Utils.addSpace(code) + "\n");
		} catch(SerialPortException e) {
			resultArea.setText(e.getMessage());
		} catch(NullPointerException e) {
			resultArea.setText("串口未打开");
		}
	}

	protected void saveOrder() {
		if (orderString ==null || orderString.isEmpty()) {
			resultArea.setText("请先运行命令，再保存");		
			return;
		}
		String orderName = orderNameTf.getText().trim();
		if (orderName == null || orderName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入命令名称", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		Order order = new Order(orderName, orderString);
		if (orderNewProj) {
			String projName = projNameTf.getText().trim();
			if (projName == null || projName.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入项目名称", "", JOptionPane.WARNING_MESSAGE);
				return;
			}		
			List<Order> list = new ArrayList<>();
			list.add(order);
			Project p = new Project(projName, list, null);
			projects.add(p);		
			
		} else {
			Project p = (Project)projNameBox.getSelectedItem();
			if (p.getOrderList() == null) {
				List<Order> list = new ArrayList<>();
				p.setOrderList(list);
			}
			p.getOrderList().add(order);
		
		}
		refreshBoxes();
		ordersBox.updateUI();
		orderString = null;
		resultArea.setText("已保存");
	}

	protected void sendOrder() {
		StringBuilder sb = new StringBuilder();
		for(OrderPanel op : orderRowList) {
			String s = op.getOrderString();		
			if (s == null || s.isEmpty()) {
				return;
			}
			sb.append(s);
		}
		orderString = sb.toString();
		try{
			sr.sendToPort(orderString);
			sendedOrderArea.append(Utils.addSpace(orderString) + "\n");
			//探测解析格式，开始解析
		} catch (SerialPortException e) {
			resultArea.setText(e.getMessage());
		}catch(NullPointerException e) {
			resultArea.setText("串口未打开");
		}
	}

	protected void directSendOrder() {
		try {
			String directOrder = directOrderTf.getText().trim();
			if (!directOrder.isEmpty()) {
				JOptionPane.showMessageDialog(null, "命令不能为空", "", JOptionPane.WARNING_MESSAGE);
			} else {
				String delSpace = directOrder.replace(" ", "");	//delete space
				sr.sendToPort(delSpace);
			}		
		} catch (SerialPortException e) {
			resultArea.setText(e.getMessage());
		}
		
	}

	public static void main(String args[]) {
	        java.awt.EventQueue.invokeLater(new Runnable() {
	            @Override
				public void run() {
//	            	WebLookAndFeel.install();
	            	try {
	            		BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
						org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
						UIManager.put("RootPane.setupButtonVisible", false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                new Client().setVisible(true);
	            }
	        });
	 }
	

	private void openOrCloseSerialPort() {
		if (!sr.isOpen) {
			
			String portId =  port.getSelectedItem().toString();
			String rateId = rate.getSelectedItem().toString();
			String message = "打开串口" + portId +":" +rateId;
			try {
				sr.openPort(portId, rateId);
				sr.addObserver(this);
				successOpenPort(message);
			} catch (SerialPortException e) {
				failOpenPort(e.getMessage());
			}
		} else {
			try {
				sr.close();
				setToggleBtnOpen();
			} catch (SerialPortException e) {
				resultArea.setText(e.getMessage());
			}
		}
	}

	private void failOpenPort(String message) {
		System.out.println(message);
		JOptionPane.showMessageDialog(null, message, "", JOptionPane.WARNING_MESSAGE);
		resultArea.setText(message);
		setToggleBtnOpen();
	}

	private void successOpenPort(String message) {
		System.out.println(message);
		resultArea.setText(message);
		setToggleBtnClose();
		
	}

	private void setToggleBtnOpen() {
		port.setEnabled(true);
		openPortBtn.setText("打开串口");
		openPortBtn.setToolTipText("打开串口");
	}
	
	private void setToggleBtnClose() {
		port.setEnabled(false);
		openPortBtn.setText("关闭串口");
		openPortBtn.setToolTipText("关闭串口");
	}

	@Override
	public void update(Observable o, Object arg) {
		 dataToJudge = (byte[])arg;

	}

	private void parseData(byte[] data) {
		
		Map[] maps = new Map[2];
		StringBuilder sb = new StringBuilder();
		if (isParserTesting) {
			if (!parserToSave.judgeParser(data)) {
				resultArea.setText("无法识别此解析格式");
				return;
			}
			maps = parserToSave.parse(data);
			Map<String, Double> graphMap = maps[0];		//generate graph here
			Map<String, Integer> noGraphMap = maps[1];
			for (String s : graphMap.keySet()) {
				sb.append(s + ": " + graphMap.get(s) + "\n");
			}
			for (String s : noGraphMap.keySet()) {
				sb.append(s + ": " + noGraphMap.get(s) + "\n");
			}
			dataArea.setText(sb.toString());
		} else {
			if (presentProj.getParserList() == null || presentProj.getParserList().isEmpty()) {
				resultArea.setText("无可用解析格式");
				return;
			}
			
			for (Parser parser : presentProj.getParserList()) {
				if (parser.judgeParser(data)) {
					maps = parser.parse(data);
					Map<String, Double> graphMap = maps[0];		//generate graph here
					Map<String, String> noGraphMap = maps[1];
						
					if (isRecording) {
						String now = Utils.time();
						String replacednow = now.replace(":", "-");
						if (rootElement  == null) {
							fileName = presentProj.getName() + replacednow + ".xml";
							rootElement = new Element(presentProj.getName());			
						} 
						Element element = new Element(parser.getName());
						element.setAttribute("time", now);
						for (String s : graphMap.keySet()) {
							element.addContent(new Element(s).setText("" + graphMap.get(s)));
						}
						for (String s : noGraphMap.keySet()) {
							element.addContent(new Element(s).setText(noGraphMap.get(s)));
						}
						rootElement.addContent(element);
					}
					
					for (String s : graphMap.keySet()) {
						sb.append(s + ": " + graphMap.get(s) + "\n");
					}
					for (String s : noGraphMap.keySet()) {
						sb.append(s + ": " + noGraphMap.get(s) + "\n");
					}
					dataArea.setText(sb.toString());		// save data
					
					
					if (hasGraph) {
						int index = 0;
						for (String s : graphMap.keySet()) {
							double d = graphMap.get(s);
							graphs.get(index).addTotalObservation(d);
							index++;
						}
						
					} else {
						graphs = new ArrayList<>();
						int number = graphMap.size();
						int rows = number % 2 == 1 ? (number + 1) / 2: number / 2;
						graphPanel.setLayout(new GridLayout(rows, 2, 0, 0));
						for (String s : graphMap.keySet()) {
							GraphMaker gm = new GraphMaker(s);
							graphPanel.add(gm);
							gm.addTotalObservation(graphMap.get(s));		
							graphs.add(gm);
						}
						System.out.println("哎呀，没图没真相");
						hasGraph = true;
					}

					return;			//break;
				} 
			}
			
		}		
		//no useful parser, transfer to ASCII
		resultArea.setText("无可用解析格式, 解析为ASCII码");
		dataArea.append(new String(data));
	}

	
	private void showReceived(byte[] param) {	
		String hexString = Utils.addSpace(Utils.hexBytes2String(param));
		receivedArea.append(hexString + "\n");
		System.out.println("received: " + hexString);
	}
	
private class JudgeData implements Runnable {

	@Override
	public void run() {
		while (true) {
			if (dataToJudge != null) {
				showReceived(dataToJudge);			
				parseData(dataToJudge);		//parse data here
				dataToJudge = null;
				System.out.println("data showed");
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
		
	}
	
}
}


