package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Controller;
import core.OutOfStockException;

/**
 * POS Dialog to be shown when Start button is clicked in Loader Dialog
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class POSDialog extends JDialog{

	private JButton infoButton;
	private JButton warningButton;
	private POSPanel posPanel;
	private PayPanel payPanel;
	private JButton payButton;
	private JButton submitButton;
	private JButton backButton;
	private JCheckBox vipCheckBox;
	private JCheckBox eventDiscountCheckBox;
	private String username;
	private Controller controller;
	private JPanel payButtonPanel;
	
	/**
	 * Constructor of POSDialog
	 * @param _username
	 */
	public POSDialog(String _username) {
		infoButton = new JButton();
		infoButton.setForeground(Color.BLUE);
		payButton = new JButton("Pay");
		vipCheckBox = new JCheckBox();
		posPanel = new POSPanel(this);
		username = _username;
		infoButton.setText("Record sale information succeeded by user " + username);
		warningButton = new JButton();
		warningButton.setForeground(Color.RED);
		submitButton = new JButton("Submit");
		backButton = new JButton("Back");
		eventDiscountCheckBox = new JCheckBox();
		controller = new Controller();
		
		payButtonPanel = new JPanel(new FlowLayout());
		payButtonPanel.add(new JLabel("Is VIP?"));
		payButtonPanel.add(vipCheckBox);
		payButtonPanel.add(new JLabel("Apply Event Discount?"));
		payButtonPanel.add(eventDiscountCheckBox);
		payButtonPanel.add(payButton);
		posPanel.add(payButtonPanel, BorderLayout.SOUTH);
		
		payButton.addActionListener(new ActionListener() {
			/**
			 * Turn to Pay Panel when Pay button is clicked
			 */
			public void actionPerformed(ActionEvent e) {
				if (controller.getOrderList().isEmpty()) {
					setWarningMessage("Invalid sales information");
					return;
				}
				remove(posPanel);
				payPanel = new PayPanel(vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected(), POSDialog.this);
				JPanel submitPanel = new JPanel(new FlowLayout());
				submitPanel.add(submitButton);
				submitPanel.add(backButton);
				payPanel.add(submitPanel, BorderLayout.SOUTH);
				add(payPanel, BorderLayout.CENTER);
				infoButton.setText("Please pay the following:");
				validate();
				pack();
			}
		});
		
		submitButton.addActionListener(new ActionListener() {
			/**
			 * Submit the payment when Submit button is clicked
			 * Submit button is only visible in Pay Panel
			 */
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (controller.getRefund(payPanel.getPaidAmount(), vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected()) < 0) {
						setWarningMessage("Not enough money!");
						return;
					}
					try {
						controller.updateGlobalStockLevels();
					} catch (OutOfStockException e1) {
						setWarningMessage("Stock not enough");
						return;
					}
					String log = "Purchase:\n" + username + " " + vipCheckBox.isSelected() + " " + 
							controller.getTotalPrice() + " " +
							controller.getDiscounted(vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected()) + " " +
							payPanel.getPaidAmount() + " " + 
							controller.getRefund(payPanel.getPaidAmount(), vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected()) + "\n";
					for (String itemID : controller.getOrderList().keySet()) {
						log += itemID + " " + controller.getOrderList().get(itemID) + ", ";
					}
					log += "\n\n";
					controller.addLog(log);
					controller.clearCart();
					posPanel = new POSPanel(POSDialog.this);
					vipCheckBox.setSelected(false);
					eventDiscountCheckBox.setSelected(false);
					posPanel.add(payButtonPanel, BorderLayout.SOUTH);
					remove(payPanel);
					add(posPanel, BorderLayout.CENTER);
					infoButton.setText("Record sale information succeeded by user " + username);
					clearWarningMessage();
					pack();
 				} catch (NumberFormatException e) {
					setWarningMessage("Please enter correct amount");
				}
			}
		});
		
		backButton.addActionListener(new ActionListener() {
			/**
			 * Go back to POS Panel when Back button is clicked
			 * Back button is only visible in Pay Panel
			 */
			public void actionPerformed(ActionEvent arg0) {
				payPanel.setVisible(false);
				infoButton.setText("Record sale information succeeded by user " + username);
				clearWarningMessage();
				add(posPanel, BorderLayout.CENTER);
				pack();
			}
		});
		
		setLayout(new BorderLayout());
		this.add(infoButton, BorderLayout.NORTH);
		this.add(warningButton, BorderLayout.SOUTH);
		this.add(posPanel, BorderLayout.CENTER);
		this.pack();
	}
	
	public void setWarningMessage(String msg) {
		warningButton.setText(msg);
	}
	
	public void clearWarningMessage() {
		warningButton.setText("");
	}
	
	public Controller getController() {
		return controller;
	}

}
