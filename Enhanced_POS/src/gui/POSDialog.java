package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import core.Controller;
import core.entities.Item;
import core.entities.ItemList;

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
	private JList productList;
	private JList shoppingCartList;
	private DefaultListModel shoppingCartListModel;
	
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
		
		shoppingCartList = new JList();
		
		JLabel idLabel = new JLabel("ID:");
		JLabel amountLabel = new JLabel("Amount:");
		JPanel idInputPanel = new JPanel();
		idInputPanel.add(idLabel);
		idInputPanel.add(idInput);
		idInputPanel.add(amountLabel);
		idInputPanel.add(amountIDInput);
		idInputPanel.add(addButton);
		
		
		DefaultListModel productListModel = new DefaultListModel();
		HashMap<String, Item> products = ItemList.getInstance().getItems();
		Iterator<Entry<String, Item>> it = products.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Item> pairs = (Map.Entry<String, Item>) it.next();
			productListModel.addElement(pairs.getValue().getItemName());
		}
		productList = new JList(productListModel);
		productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		productList.setVisibleRowCount(20);
		JScrollPane selectPane = new JScrollPane(productList);
		
		JPanel selectButtonsPanel = new JPanel();
		selectButtonsPanel.setLayout(new GridLayout(5, 1, 5, 5));
		selectButtonsPanel.add(amountLabel);
		selectButtonsPanel.add(amountClickInput);
		selectButtonsPanel.add(toRightButton);
		selectButtonsPanel.add(toLeftButton);
		selectButtonsPanel.add(clearButton);
		
		shoppingCartListModel = new DefaultListModel();
		JScrollPane shoppingCartPane = new JScrollPane(shoppingCartList);
		
		JPanel payPanel = new JPanel();
		JLabel vipLabel = new JLabel("Is VIP?");
		payPanel.add(vipLabel);
		payPanel.add(vipCheckBox);
		payPanel.add(payButton);
		
		toRightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String selectedItemName = (String) productList.getSelectedValue();
				Item selectedItem = ItemList.getInstance().getItemByName(selectedItemName);
				addToCart(selectedItem, Integer.valueOf(amountClickInput.getText()));
			}
			
		});
		
		this.setLayout(new BorderLayout());
		this.add(idInputPanel, BorderLayout.NORTH);
		this.add(selectPane, BorderLayout.WEST);
		this.add(selectButtonsPanel, BorderLayout.CENTER);
		this.add(shoppingCartPane, BorderLayout.EAST);
		this.add(payPanel, BorderLayout.SOUTH);
		this.pack();
	}
	
	private void addToCart(Item item, int amount) {
		Controller.getInstance().addToCart(item, amount);
		float subTotal = item.getPrice() * amount;
		DecimalFormat df = new DecimalFormat("##.0");
		String cartDisplay = item.getItemName() + " "  + amount + " = " + df.format(subTotal);
		shoppingCartListModel.addElement(cartDisplay);
	}
}
