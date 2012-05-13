package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import core.entities.UserList;

/**
 * Login Dialog for user log in
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class LoginDialog extends JDialog implements Runnable{
	private JTextField usernameInput;
	private JPasswordField passwordInput;
	private JButton signinButton;
	private JButton closeButton;
	private JButton infoButton;
	private JButton warningButton;
	private UserList users;
	
	/**
	 * Constructor of LoginDialog
	 */
	public LoginDialog() {
		usernameInput = new JTextField();
		passwordInput = new JPasswordField();
		signinButton = new JButton("Sign In");
		closeButton = new JButton("Close");
		infoButton = new JButton();
		infoButton.setForeground(Color.BLUE);
		warningButton = new JButton();
		warningButton.setForeground(Color.RED);
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
			/**
			 * Close the dialog when Close button is clicked
			 */
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		signinButton.addActionListener(new ActionListener() {
			/**
			 * Sign in to the POS with username and password provided by user
			 * Validate the username and password with the record, show warning message if 
			 * username and password is wrong
			 */
			public void actionPerformed(ActionEvent e) {
				String username = usernameInput.getText();
				String password = passwordInput.getText();
				if (username.equals("") || password.equals("")) {
					warningButton.setText("Invalid username or password");
					pack();
				} else {
					if (users.verifyUser(username, password)) {
						dispose();
						POSDialog pos = new POSDialog(username);
						pos.setLocationRelativeTo(LoginDialog.this);
						pos.setVisible(true);
					} else {
						warningButton.setText("Invalid username or password");
						pack();
					}
				}
			}
			
		});
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
		mainPanel.add(usernamePanel);
		mainPanel.add(passwordPanel);
		mainPanel.add(buttonPanel);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(10, 10));
		add(mainPanel, BorderLayout.CENTER);
		add(warningButton, BorderLayout.SOUTH);
		add(infoButton, BorderLayout.NORTH);
		
		
		addWindowListener(new WindowAdapter() {
			/**
			 * Focus on usernameInput when Login Dialog opens
			 */
			public void windowOpened(WindowEvent e) {
				usernameInput.requestFocus();
			}
		});
		this.pack();
	}

	/**
	 * Implemented run method for the Runnable interface
	 * Show Login Dialog when a new thread is created
	 */
	public void run() {
		if (UserList.getInstance().isInitialized()) {
			infoButton.setText("Connection succeeded, please sign in!");
		} else {
			warningButton.setText("Connection failed");
		}
		pack();
		setVisible(true);
	}

}
