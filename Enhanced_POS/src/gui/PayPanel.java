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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.Controller;

public class PayPanel extends JPanel {
	private JTextField totalPrice;
	private JTextField discounted;
	private JTextField totalSum;
	private JFormattedTextField paid;
	private JTextField refund;
	private JButton submitButton;
	private boolean isVIP;
	private POSDialog parentDialog;
	
	public PayPanel(boolean _isVIP, POSDialog _parentDialog) {
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
		submitButton = new JButton("Submit");
		isVIP = _isVIP;
		parentDialog = _parentDialog;
		
		final DecimalFormat df = Controller.getInstance().getNumberFormat();
		totalPrice.setText(df.format(Controller.getInstance().getTotalPrice()));
		totalSum.setText(df.format(Controller.getInstance().getTotalSum(isVIP)));
		discounted.setText(df.format(Controller.getInstance().getDiscounted(isVIP)));
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Controller.getInstance().getRefund(Float.valueOf(paid.getText()), isVIP) < 0) {
					parentDialog.setWarningMessage("Not enough money!");
				}
			}
		});
		
		paid.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				float amountTyped = Float.valueOf(paid.getText());
				float refundAmount = Controller.getInstance().getRefund(amountTyped, isVIP);
				if (refundAmount >= 0) {
					refund.setText(df.format(Controller.getInstance().getRefund(amountTyped, isVIP)));
				}
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
