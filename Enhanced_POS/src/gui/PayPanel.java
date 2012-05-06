package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.Controller;

public class PayPanel extends JPanel {
	private JTextField totalPrice;
	private JTextField discounted;
	private JTextField totalSum;
	private JTextField paid;
	private JTextField refund;
	private JButton submitButton;
	private boolean isVIP;
	
	public PayPanel(boolean _isVIP) {
		totalPrice = new JTextField(10);
		totalPrice.setEnabled(false);
		discounted = new JTextField(10);
		discounted.setEnabled(false);
		totalSum = new JTextField(10);
		totalSum.setEnabled(false);
		paid = new JTextField(10);
		refund = new JTextField("-");
		refund.setEnabled(false);
		submitButton = new JButton("Submit");
		isVIP = _isVIP;
		
		DecimalFormat df = Controller.getInstance().getNumberFormat();
		totalPrice.setText(df.format(Controller.getInstance().getTotalPrice()));
		totalSum.setText(df.format(Controller.getInstance().getTotalSum(isVIP)));
		discounted.setText(df.format(Controller.getInstance().getDiscounted(isVIP)));
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
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
		add(textFieldPanel, BorderLayout.EAST);
		add(submitButton, BorderLayout.SOUTH);
	}
}
