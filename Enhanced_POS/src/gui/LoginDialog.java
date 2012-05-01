package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import conf.GlobalConfiguration;
import core.entities.UserList;

public class LoginDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameInput;
	private JPasswordField passwordInput;
	private JButton signinButton;
	private JButton closeButton;
	private UserList users;
	
	public LoginDialog() {
		usernameInput = new JTextField();
		passwordInput = new JPasswordField();
		signinButton = new JButton("Sign In");
		closeButton = new JButton("Close");
		users = UserList.getInstance();
		
		JLabel usernameLabel = new JLabel("Username:");
		JPanel usernamePanel = new JPanel(new GridLayout(1, 2, 5, 5));
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameInput);
	
		JLabel passwordLabel = new JLabel("Password:");
		JPanel passwordPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordInput);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		buttonPanel.add(signinButton);
		buttonPanel.add(closeButton);
		
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int choice = JOptionPane.showConfirmDialog(null, "Exit POS?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		
		signinButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String username = usernameInput.getText();
				String password = passwordInput.getText();
				if (username.equals("") || password.equals("")) {
					JOptionPane.showConfirmDialog(null, "Both Username and Password are required", "Error", JOptionPane.OK_OPTION);
				} else {
					if (users.verifyUser(username, password)) {
						POSDialog pos = new POSDialog();
						pos.setVisible(true);
					}
				}
			}
			
		});
		this.setLayout(new GridLayout(3, 1, 0, 0));
		this.add(usernamePanel);
		this.add(passwordPanel);
		this.add(buttonPanel);
		this.pack();
	}
}
