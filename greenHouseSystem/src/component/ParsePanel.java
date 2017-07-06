package component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class ParsePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8936213180711102780L;

	private JTextField nameTf;
	private JTextField locateTf;
	private JTextField contentTf;
	private JTextField rateTf;
	private JComboBox<String> codingBox;
	private BitFrame bitFrame;
	private boolean bitDefed = false;
			
													//389, 303
	public ParsePanel() {	
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setHgap(1);
		this.setLayout(flowLayout);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setMaximumSize(new Dimension(500, 45));
		setPreferredSize(new Dimension(475, 32));
		
		nameTf = new JTextField();
		nameTf.setPreferredSize(new Dimension(80, 30));
		this.add(nameTf);
		
		
		locateTf = new JTextField();
		locateTf.setPreferredSize(new Dimension(70, 30));
		locateTf.setToolTipText("从0开始。如有多个，用 - (减号)隔开");
		add(locateTf);
		
		contentTf = new JTextField();
		contentTf.setPreferredSize(new Dimension(70, 30));
		add(contentTf);
		
		rateTf = new JTextField();
		rateTf.setPreferredSize(new Dimension(70, 30));	//-30
		add(rateTf);
		
		codingBox = new JComboBox<>(new String[]{"16进制", "ASCII"});
		codingBox.setPreferredSize(new Dimension(90, 30));
		add(codingBox);
		
		JButton bitButton = new JButton("定义位");
		bitButton.setPreferredSize(new Dimension(75, 30));
		add(bitButton);
		bitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (bitDefed) {
					bitFrame.setVisible(true);
				} else {
					bitFrame = new BitFrame();
					bitDefed  = true;
				}
			}
			
		});
	}

	private String getpName() {
		return nameTf.getText().trim();
	}
	
	private String getLocate() {
		return locateTf.getText().trim();
	}
	
	private String getContent() {
		return contentTf.getText().trim();
	}
	
	private double getRate() {
		try {
			String s = rateTf.getText().trim();
			if (s == null || s.isEmpty()) {
				return 1;
			}
			double rate = Double.parseDouble(s);
			return rate;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private boolean isAscii() {
		if (codingBox.getSelectedItem() != "16进制") {
			return true;
		} else {
			return false;
		}
	}
	
	private List<BitDef> getBitDef() {
		if (bitDefed) {
			return bitFrame.bitDefs;
		}
		return null;
	}
	
	public ParserUnit getParserUnit() {
		if (this.getLocate() == null || getLocate().isEmpty()) {
			JOptionPane.showMessageDialog(null, "位置不能为空", "", JOptionPane.WARNING_MESSAGE);	
			return null;
		}
		if (getRate() == 0) {
			JOptionPane.showMessageDialog(null, "请输入非0整数", "", JOptionPane.WARNING_MESSAGE);	
			return null;
		}
		ParserUnit pu = new ParserUnit();
		pu.setName(getpName());
		pu.setLocation(getLocate());
		pu.setContent(getContent());
		pu.setRate(getRate());
		pu.setAscii(isAscii());
		pu.setBitDefs(getBitDef());
		return pu;
	}
	
}
