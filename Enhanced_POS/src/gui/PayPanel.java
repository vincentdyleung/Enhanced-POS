package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import conf.GlobalConfiguration;

import core.entities.Item;
import core.entities.ItemList;

/**
 * Panel for showing the billing information and submitting the amount to pay
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
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
	
	/**
	 * Constructor of PayPanel
	 * @param _isVIP
	 * @param _isEventDiscount
	 * @param _parentDialog
	 */
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
				
			}

			public void keyPressed(KeyEvent e) {
				
			}

			/**
			 * Calculate the change right after the amount is typed in
			 */
			public void keyReleased(KeyEvent e) {
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
		String currency = GlobalConfiguration.getInstance().getCurrency().show();
		textFieldPanel.add(new JLabel("Total Price: " + currency));
		textFieldPanel.add(totalPrice);
		textFieldPanel.add(new JLabel("Discounted: " + currency));
		textFieldPanel.add(discounted);
		textFieldPanel.add(new JLabel("Total Sum: " + currency));
		textFieldPanel.add(totalSum);
		textFieldPanel.add(new JLabel("Paid: " + currency));
		textFieldPanel.add(paid);
		textFieldPanel.add(new JLabel("Refund: " + currency));
		textFieldPanel.add(refund);
		
		setBorder(new EmptyBorder(0, 5, 5, 0));
		setLayout(new BorderLayout(20, 5));
		add(shoppingCartList, BorderLayout.WEST);
		add(textFieldPanel, BorderLayout.EAST);
	}
	
	public float getPaidAmount() {
		return Float.valueOf(paid.getText());
	}
	
}
