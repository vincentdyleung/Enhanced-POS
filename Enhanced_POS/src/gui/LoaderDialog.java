package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import core.Loader;

/**
 * Loader Dialog for starting a new POS
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class LoaderDialog extends JDialog {
	private JButton startButton;
	
	/**
	 * Constructor for LoaderDialog
	 */
	public LoaderDialog() {
		startButton = new JButton("Start");
		JLabel startButtonLabel = new JLabel("Start a new POS");
		JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		startButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				startPOS();
			}
			
		});
		panel.add(startButtonLabel);
		panel.add(startButton);
		this.add(panel);
		this.pack();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("POS Exit");
				Loader.getInstance().getPOSThreadManager().shutdown();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Start a new POS when Start button is clicked
	 * Use the thread pool of Loader to manage the threads
	 */
	private void startPOS() {
		LoginDialog login = new LoginDialog();
		login.setLocationRelativeTo(this);
		Thread newPOS = new Thread(login);
		Loader.getInstance().getPOSThreadManager().execute(newPOS);
	}
}
