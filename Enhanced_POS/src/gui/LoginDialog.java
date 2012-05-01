package gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameInput;
	private JPasswordField passwordInput;
	private JButton signinButton;
	private JButton closeButton;
	
	public LoginDialog() {
		usernameInput = new JTextField();
		passwordInput = new JPasswordField();
		signinButton = new JButton("Sign In");
		closeButton = new JButton("Close");
		
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
		
		
		this.setLayout(new GridLayout(3, 1, 0, 0));
		this.add(usernamePanel);
		this.add(passwordPanel);
		this.add(buttonPanel);
		this.pack();
	}
}
