package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import core.Controller;
import core.OutOfStockException;
import core.entities.Item;
import core.entities.ItemList;

/**
 * Panel for adding products to the shopping cart
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class POSPanel extends JPanel {

	private JTextField idInput;
	private JFormattedTextField amountIDInput;
	private JFormattedTextField amountClickInput;
	private JButton addButton;
	private JButton toRightButton;
	private JButton toLeftButton;
	private JButton clearButton;
	private POSDialog parentDialog;
	private JList productList;
	private JList shoppingCartList;
	private DefaultListModel shoppingCartListModel;
	
	/**
	 * Constructor of POSPanel
	 * @param parent
	 */
	public POSPanel(POSDialog parent) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		idInput = new JTextField(8);
		amountIDInput = new JFormattedTextField(nf);
		amountIDInput.setColumns(8);
		amountClickInput = new JFormattedTextField(nf);
		addButton = new JButton("Add");
		toRightButton = new JButton("-->");
		toLeftButton = new JButton("<--");
		clearButton = new JButton("Clear");
		parentDialog = parent;

		JLabel idLabel = new JLabel("ID:");
		JLabel amountLabel = new JLabel("Amount:");
		JPanel idInputPanel = new JPanel();
		idInputPanel.setBorder(new EmptyBorder(0, 5, 5, 0));
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
		productList = new JList(productListModel) {
			/**
			 * Set a customized size for the productList
			 */
			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension (200, 300);
			}
		};
		productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		productList.setVisibleRowCount(20);
		JScrollPane selectionPane = new JScrollPane(productList);
		
		JPanel selectButtonsPanel = new JPanel();
		selectButtonsPanel.setLayout(new GridLayout(5, 1, 5, 5));
		selectButtonsPanel.add(new JLabel("Amount:"));
		selectButtonsPanel.add(amountClickInput);
		selectButtonsPanel.add(toRightButton);
		selectButtonsPanel.add(toLeftButton);
		selectButtonsPanel.add(clearButton);
		
		shoppingCartListModel = new DefaultListModel();
		shoppingCartList = new JList(shoppingCartListModel) {
			/**
			 * Set a customized size for the shoppingCartList
			 */
			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(200, 300);
			}
		};
		JScrollPane shoppingCartPane = new JScrollPane(shoppingCartList);
		
		toRightButton.addActionListener(new ActionListener() {
			/**
			 * Add a selected item to the shopping cart when To Right button is clicked
			 */
			public void actionPerformed(ActionEvent arg0) {
				if (productList.isSelectionEmpty()) {
					parentDialog.setWarningMessage("Please select product");
					return;
				}
				if (amountClickInput.getText().equals("")) {
					parentDialog.setWarningMessage("Please input correct amount");
				}
				try {
					String selectedItemName = (String) productList.getSelectedValue();
					Item selectedItem = ItemList.getInstance().getItemByName(selectedItemName);
					addToCart(selectedItem, Integer.valueOf(amountClickInput.getText()));
					parentDialog.clearWarningMessage();
				} catch (NumberFormatException e) {
					parentDialog.setWarningMessage("Please input correct amount");
				} catch (OutOfStockException e) {
					parentDialog.setWarningMessage("Stock not enough");
				}
			}
		});
		
		toLeftButton.addActionListener(new ActionListener() {
			/**
			 * Remove the selected item from shopping cart when To Left button is clicked
			 */
			public void actionPerformed(ActionEvent arg0) {
				if (shoppingCartList.isSelectionEmpty()) {
					parentDialog.setWarningMessage("Please select in the shopping cart!");
					return;
				}
				String selectedItemName = ((String) shoppingCartList.getSelectedValue()).split(" ")[0];
				removeFromCart(selectedItemName);
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			/**
			 * Clear the whole shopping cart when Clear button is clicked
			 */
			public void actionPerformed(ActionEvent arg0) {
				parentDialog.getController().clearCart();
				updateShoppingCartList();
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			/**
			 * Add the item specified by the item ID to the shopping cart
			 */
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (idInput.getText().equals("") && amountIDInput.getText().equals("")) {
						parentDialog.setWarningMessage("Please input valid product ID and amount");
						return;
					} 
					Item item = ItemList.getInstance().getItemById(idInput.getText());
					addToCart(item, Integer.valueOf(amountIDInput.getText()));
					parentDialog.clearWarningMessage();
				} catch (NumberFormatException e) {
					parentDialog.setWarningMessage("Please input valid product ID and amount");
				} catch (NullPointerException e) {
					parentDialog.setWarningMessage("Product " + idInput.getText() + " does not exist");
				} catch (OutOfStockException e) {
					parentDialog.setWarningMessage("Stock not enough!");
				}
			}
		});

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(20, 5));
		add(idInputPanel, BorderLayout.NORTH);
		add(selectionPane, BorderLayout.WEST);
		add(selectButtonsPanel, BorderLayout.CENTER);
		add(shoppingCartPane, BorderLayout.EAST);
	}
	
	/**
	 * Add the item to the shopping cart and update the display
	 * @param item
	 * @param amount
	 * @throws OutOfStockException
	 */
	private void addToCart(Item item, int amount) throws OutOfStockException {
		parentDialog.getController().addToCart(item, amount);
		updateShoppingCartList();
	}
	
	/**
	 * Remove the selected item from shopping cart and update the display
	 * @param itemName
	 */
	private void removeFromCart(String itemName) {
		Item selectedItem = ItemList.getInstance().getItemByName(itemName);
		parentDialog.getController().removeFromCart(selectedItem);
		updateShoppingCartList();
	}
	
	/**
	 * Loop through the shopping cart from the controller and add them to the display
	 */
	public void updateShoppingCartList() {
		shoppingCartListModel.clear();
		DecimalFormat df = parentDialog.getController().getNumberFormat();
		Iterator<Entry<String, Integer>> it = parentDialog.getController().getOrderList().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pairs = it.next();
			Item i = ItemList.getInstance().getItemById(pairs.getKey());
			float subTotal = i.getPrice() * pairs.getValue();
			String cartDisplay = i.getItemName() + " "  + pairs.getValue() + " = " + df.format(subTotal); 
			shoppingCartListModel.addElement(cartDisplay);
		}
	}
}
