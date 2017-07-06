package component;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class BitFrame extends JFrame {
	

	/**
	 * 定义位的小窗口
	 */
	private static final long serialVersionUID = 6839124983749690032L;
	private JTable table = null;
	private int rows = 1;
	List<BitDef> bitDefs;
	DefaultTableModel tableModel;

	public BitFrame() {
		setTitle("位定义");	
		setBounds(200, 100, 537, 424);
		getContentPane().setLayout(null);	
		
		String[][] data = new String[1][2];
		String[] colNames = {"name", "location"};
		table = new JTable();			
		tableModel = new DefaultTableModel(data, colNames);
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setFillsViewportHeight(true);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMinWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(80);
		table.setToolTipText("双击输入参数");
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 10, 426, 309);
		getContentPane().add(scrollPane);
		
		
		JButton saveBtn = new JButton("保存");
		saveBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
			}
		});
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveDataAndHide();				
			}
			
		});
		saveBtn.setBounds(166, 336, 146, 40);
		getContentPane().add(saveBtn);
		
		JButton btnNewButton = new JButton("加一行");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addRow();
			}
		});
		btnNewButton.setBounds(436, 112, 85, 23);
		getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("减一行");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decRow();
			}
		});
		btnNewButton_1.setBounds(436, 156, 85, 23);
		getContentPane().add(btnNewButton_1);	
		setVisible(true);	
	}
	protected void decRow() {
		if (table.isEditing())			table.getCellEditor().stopCellEditing();	
		if(rows==1) return;
		tableModel.removeRow(--rows);		

	}
	protected void addRow() {
		if (table.isEditing())			table.getCellEditor().stopCellEditing();	
		tableModel.addRow(new String[]{});
		rows++;
		System.out.println(rows);
		
	}
	protected void saveDataAndHide() {
		if (table.isEditing())			table.getCellEditor().stopCellEditing();				//在丢失焦点时也告诉 JTable 我们需要接受这个值	停止编辑
		bitDefs = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			String name = (String) table.getModel().getValueAt(i, 0);
			System.out.println(name);
			String location = (String) table.getModel().getValueAt(i, 1);
			System.out.println(location);
			if (name == null || name.isEmpty() || location == null ||location.isEmpty()) {
				JOptionPane.showMessageDialog(null, "第" + (i + 1) +"行不能为空", null, JOptionPane.INFORMATION_MESSAGE);
				return;
			} else {
				bitDefs.add(new BitDef(name, location));
			}		
		}
		for (BitDef l : bitDefs) {
			System.out.println(l.toString());
		}
		this.setVisible(false);
	}

	public static void main(String[] args) {
		new BitFrame();
	}
}
