package component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import serialPort.Utils;


public class OrderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8387567413730536695L;
	private JTextField contentTf;
	private JTextField lengthTf;
	private JComboBox<String> typeBox;
	
	public OrderPanel(int i) {
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setHgap(1);
		this.setLayout(flowLayout);
		this.setBorder(new LineBorder(new Color(0, 0, 0)));
		setMaximumSize(new Dimension(450, 45));
		setPreferredSize(new Dimension(379, 40));
		
		JLabel lblNewLabel = new JLabel("part " + i);
		lblNewLabel.setPreferredSize(new Dimension(60, 30));
		lblNewLabel.setMaximumSize(new Dimension(78, 30));
		this.add(lblNewLabel);
		
		contentTf = new JTextField();	
		contentTf.setMaximumSize(new Dimension(170, 40));
		contentTf.setPreferredSize(new Dimension(150, 30));	
		this.add(contentTf);
		
		lengthTf = new JTextField();
		lengthTf.setPreferredSize(new Dimension(50, 30));
		this.add(lengthTf);
		
		typeBox = new JComboBox<>(new String[]{"Hex", "ASCII", "int","float"});
		typeBox.setPreferredSize(new Dimension(100, 30));
		this.add(typeBox);
	}
	
	public String getContent() {
		String content = contentTf.getText().trim();
		if (this.getType() == "Hex") {
			return content.replace(" ", "");
		}
		return contentTf.getText().trim();
	}
	
	private int getLength() {
		if(lengthTf.getText() == null || lengthTf.getText().trim().isEmpty()) {
			return -1;
		} 
		return Integer.parseInt(lengthTf.getText().trim());
	}
	
	private String getType() {
		
		return (String) typeBox.getSelectedItem();

	}
	
	public String getOrderString() {
		String content = getContent();
		int length = getLength();
		String type = getType();
		StringBuilder sb = new StringBuilder();
		if(type == "Hex") {
			if(!content.isEmpty()) {	
				if(length == -1) {		//empty length
					return content;
				} else if(length > 0) {
					if(content.length() < length) {
						if (content.length() % 2 == 1) {
							JOptionPane.showMessageDialog(null, "长度或内容有误", "", JOptionPane.WARNING_MESSAGE);	
							return null;
						} else {						
							for (int i = length/(content.length()/2); i > 0; i--) {
								sb.append(content);
							}
							return sb.toString();
						}
					} else if(content.length() == length) {
						return content;
					} else {
						JOptionPane.showMessageDialog(null, "长度或内容有误", "", JOptionPane.WARNING_MESSAGE);			
						return null;
					}
				}
			} else {		//用0 填充
				if (length != -1) {
					if (length > 0) {
						return Utils.generateZero(length);
					}
				} else {
					JOptionPane.showMessageDialog(null, "长度或内容有误", "", JOptionPane.WARNING_MESSAGE);			
					return null;
				}
			}
		} else if(type == "ASCII") {
			if(!content.isEmpty()) {	
				if(length == -1) {		//empty length
					return Utils.string2Hex(content);
				} else if(length > 0) {
					if(content.length() < length) {												
						for (int i = length/content.length(); i > 0; i--) {
							sb.append(content);
						}
						return Utils.string2Hex(sb.toString());						
					} else if(content.length() == length) {
						return Utils.string2Hex(content);
					} else {
						JOptionPane.showMessageDialog(null, "长度或内容有误", "", JOptionPane.WARNING_MESSAGE);			
						return null;
					}
				}
			} else {		//用0 填充
				if (length != -1) {
					if (length > 0) {
						return Utils.generateZero(length);
					}
				} else {
					JOptionPane.showMessageDialog(null, "长度或内容有误", "", JOptionPane.WARNING_MESSAGE);			
					return null;
				}
			}
		} else if(type == "int") {	//整数的16进制，不足位以0填充，e.s.  1(d)--01(h)
			int i = Integer.parseInt(content);
			String s = Integer.toHexString(i);
			return (s.length() % 2 == 1) ? "0" + s : s;
		} else {	//float
			float f = Float.parseFloat(content);
			return Integer.toHexString(Float.floatToIntBits(f));
		}
		return null;
	}
	
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.setSize(379, 40);
		OrderPanel test = new OrderPanel(1);
		jf.add(test);
		jf.setVisible(true);
	}
}
