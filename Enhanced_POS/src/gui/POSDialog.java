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

import core.entities.UserList;

public class POSDialog extends JDialog {

	private JButton infoButton;
	private JButton warningButton;
	private POSPanel pos;
	private JButton payButton;
	private JCheckBox vipCheckBox;
	private String username;
	
	public POSDialog(String _username) {
		infoButton = new JButton();
		infoButton.setForeground(Color.BLUE);
		warningButton = new JButton();
		warningButton.setForeground(Color.RED);
		payButton = new JButton("Pay");
		vipCheckBox = new JCheckBox();
		pos = new POSPanel();
		username = _username;
		infoButton.setText("Record sale information succeeded by user " + username);
		
		JPanel payPanel = new JPanel(new FlowLayout());
		payPanel.add(new JLabel("Is VIP?"));
		payPanel.add(vipCheckBox);
		payPanel.add(payButton);
		pos.add(payPanel, BorderLayout.SOUTH);
		
		payButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				pos.setVisible(false);
				add(new PayPanel(vipCheckBox.isSelected()), BorderLayout.CENTER);
				validate();
			}
			
		});
		
		setLayout(new BorderLayout());
		this.add(infoButton, BorderLayout.NORTH);
		this.add(pos, BorderLayout.CENTER);
		this.add(warningButton, BorderLayout.SOUTH);
		this.pack();
	}
	
}
