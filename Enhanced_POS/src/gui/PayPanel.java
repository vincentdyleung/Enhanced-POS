package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.entities.Item;
import core.entities.ItemList;

public class PayPanel extends JPanel {
	private JTextField totalPrice;
	private JTextField discounted;
	private JTextField totalSum;
	private JFormattedTextField paid;
	private JTextField refund;
	private boolean isVIP;
	private boolean isEventDiscount;
	private POSDialog parentDialog;
	private JList shoppingCartList;
	
	public PayPanel(boolean _isVIP, boolean _isEventDiscount, POSDialog _parentDialog) {
		totalPrice = new JTextField(10);
		totalPrice.setEnabled(false);
		discounted = new JTextField(10);
		discounted.setEnabled(false);
		totalSum = new JTextField(10);
		totalSum.setEnabled(false);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		paid = new JFormattedTextField(nf);
		paid.setText("0.0");
		refund = new JTextField("-");
		refund.setEnabled(false);
		isVIP = _isVIP;
		isEventDiscount = _isEventDiscount;
		parentDialog = _parentDialog;
		
		final DecimalFormat df = parentDialog.getController().getNumberFormat();
		totalPrice.setText(df.format(parentDialog.getController().getTotalPrice()));
		totalSum.setText(df.format(parentDialog.getController().getTotalSum(isVIP, isEventDiscount)));
		discounted.setText(df.format(parentDialog.getController().getDiscounted(isVIP, isEventDiscount)));
		
		paid.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				try {
					float amountTyped = Float.valueOf(paid.getText());
					float refundAmount = parentDialog.getController().getRefund(amountTyped, isVIP, isEventDiscount);
					if (refundAmount >= 0) {
						refund.setText(df.format(parentDialog.getController().getRefund(amountTyped, isVIP, isEventDiscount)));
					} else {
						refund.setText("");
					}
					parentDialog.setWarningMessage("");
				} catch (NumberFormatException ex) {
					parentDialog.setWarningMessage("Please enter correct amount");
				}
			}
			
		});
		
		DefaultListModel shoppingCartListModel = new DefaultListModel();
		for (String itemID : parentDialog.getController().getOrderList().keySet()) {
			Item item = ItemList.getInstance().getItemById(itemID);
			int amount = parentDialog.getController().getOrderList().get(itemID);
			float subTotal = item.getPrice() * amount;
			String cartDisplay = item.getItemName() + " " + amount + " = " + df.format(subTotal);
			shoppingCartListModel.addElement(cartDisplay);
		}
		
		shoppingCartList = new JList(shoppingCartListModel);
		JPanel textFieldPanel = new JPanel(new GridLayout(5, 2, 5, 5));
		textFieldPanel.add(new JLabel("Total Price: HK$"));
		textFieldPanel.add(totalPrice);
		textFieldPanel.add(new JLabel("Discounted: Hk$"));
		textFieldPanel.add(discounted);
		textFieldPanel.add(new JLabel("Total Sum: HK$"));
		textFieldPanel.add(totalSum);
		textFieldPanel.add(new JLabel("Paid: HK$"));
		textFieldPanel.add(paid);
		textFieldPanel.add(new JLabel("Refund: HK$"));
		textFieldPanel.add(refund);
		
		setLayout(new BorderLayout());
		add(shoppingCartList, BorderLayout.WEST);
		add(textFieldPanel, BorderLayout.EAST);
	}
	
	public float getPaidAmount() {
		return Float.valueOf(paid.getText());
	}
	
}
