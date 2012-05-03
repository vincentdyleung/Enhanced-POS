package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoaderDialog extends JDialog {
	private JButton startButton;
	
	public LoaderDialog() {
		startButton = new JButton("Start");
		JLabel startButtonLabel = new JLabel("Start a new POS");
		JPanel panel = new JPanel();
		
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				startPOS();
			}
			
		});
		panel.add(startButtonLabel);
		panel.add(startButton);
		this.add(panel);
	}
	
	private void startPOS() {
		LoginDialog login = new LoginDialog();
		login.setVisible(true);
	}
}
