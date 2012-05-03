package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

public class POSDialog extends JDialog {
	private JTextField idInput;
	private JTextField amountIDInput;
	private JTextField amountClickInput;
	private JButton addButton;
	private JButton toRightButton;
	private JButton toLeftButton;
	private JButton clearButton;
	private JButton payButton;
	private JCheckBox vipCheckBox;
	private JScrollPane selectPane;
	private JScrollPane shoppingCartPane;
	private ListModel productList;
	
	public POSDialog() {
		idInput = new JTextField(20);
		amountIDInput = new JTextField(20);
		amountClickInput = new JTextField(20);
		addButton = new JButton("Add");
		toRightButton = new JButton("-->");
		toLeftButton = new JButton("<--");
		clearButton = new JButton("Clear");
		payButton = new JButton("Pay");
		vipCheckBox = new JCheckBox();
		selectPane = new JScrollPane();
		shoppingCartPane = new JScrollPane();
		
		JLabel idLabel = new JLabel("ID:");
		JLabel amountLabel = new JLabel("Amount:");
		JPanel idInputPanel = new JPanel();
		idInputPanel.add(idLabel);
		idInputPanel.add(idInput);
		idInputPanel.add(amountLabel);
		idInputPanel.add(amountIDInput);
		idInputPanel.add(addButton);
		
		JPanel selectPanel = new JPanel();
		selectPanel.add(selectPane);
		
		JPanel selectButtonsPanel = new JPanel();
		selectButtonsPanel.setLayout(new GridLayout(5, 1, 5, 5));
		selectButtonsPanel.add(amountLabel);
		selectButtonsPanel.add(amountClickInput);
		selectButtonsPanel.add(toRightButton);
		selectButtonsPanel.add(toLeftButton);
		selectButtonsPanel.add(clearButton);
		
		JPanel shoppingCartPanel = new JPanel();
		shoppingCartPanel.add(shoppingCartPane);
		
		JPanel payPanel = new JPanel();
		JLabel vipLabel = new JLabel("Is VIP?");
		payPanel.add(vipLabel);
		payPanel.add(vipCheckBox);
		payPanel.add(payButton);
		
		this.setLayout(new BorderLayout());
		this.add(idInputPanel, BorderLayout.NORTH);
		this.add(selectPanel, BorderLayout.WEST);
		this.add(selectButtonsPanel, BorderLayout.CENTER);
		this.add(shoppingCartPane, BorderLayout.EAST);
		this.add(payPanel, BorderLayout.SOUTH);
		this.pack();
	}
}
