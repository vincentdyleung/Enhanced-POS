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
import core.entities.UserList;

public class POSDialog extends JDialog{

	private JButton infoButton;
	private JButton warningButton;
	private POSPanel posPanel;
	private PayPanel payPanel;
	private JButton payButton;
	private JButton submitButton;
	private JCheckBox vipCheckBox;
	private JCheckBox eventDiscountCheckBox;
	private String username;
	private Controller controller;
	
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
		eventDiscountCheckBox = new JCheckBox();
		controller = new Controller();
		System.out.println(controller);
		
		JPanel payButtonPanel = new JPanel(new FlowLayout());
		payButtonPanel.add(new JLabel("Is VIP?"));
		payButtonPanel.add(vipCheckBox);
		payButtonPanel.add(new JLabel("Apply Event Discount?"));
		payButtonPanel.add(eventDiscountCheckBox);
		payButtonPanel.add(payButton);
		posPanel.add(payButtonPanel, BorderLayout.SOUTH);
		
		
		
		payButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (controller.getOrderList().isEmpty()) {
					setWarningMessage("Invalid sales information");
					return;
				}
				posPanel.setVisible(false);
				payPanel = new PayPanel(vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected(), POSDialog.this);
				payPanel.add(submitButton, BorderLayout.SOUTH);
				add(payPanel, BorderLayout.CENTER);
				infoButton.setText("Please pay the following:");
				validate();
			}
		});
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (controller.getRefund(payPanel.getPaidAmount(), vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected()) < 0) {
						setWarningMessage("Not enough money!");
						return;
					}
					String log = "Purchase:\n" + username + " " + vipCheckBox.isSelected() + " " + 
							controller.getTotalPrice() + " " +
							controller.getDiscounted(vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected()) + " " +
							controller.getTotalSum(vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected()) + " " +
							payPanel.getPaidAmount() + " " + 
							controller.getRefund(payPanel.getPaidAmount(), vipCheckBox.isSelected(), eventDiscountCheckBox.isSelected());
					for (String itemID : controller.getOrderList().keySet()) {
						log += itemID + " " + controller.getOrderList().get(itemID) + ", ";
					}
					log += "\n\n";
					controller.addLog(log);
 				} catch (NumberFormatException e) {
					setWarningMessage("Please enter correct amount");
				}
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
