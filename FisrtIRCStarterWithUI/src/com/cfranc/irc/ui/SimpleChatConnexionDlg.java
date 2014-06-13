package com.cfranc.irc.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class SimpleChatConnexionDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();	
	private final ConnectionPanel connectionPanel;
	private final JButton btnConnexion = new JButton("Connexion");
	private final JButton btnNouveau = new JButton("Nouveau");
	
	public ConnectionPanel getConnectionPanel() {
		return connectionPanel;
	}

	public JButton getBtnConnexion() {
		return btnConnexion;
	}

	public JButton getBtnNouveau() {
		return btnNouveau;
	}
	  

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SimpleChatConnexionDlg dialog = new SimpleChatConnexionDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SimpleChatConnexionDlg() {
		
		setTitle("IRC - Connexion");
		setModal(true);
		setBounds(100, 100, 306, 179);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		connectionPanel = new ConnectionPanel();
		contentPanel.add(connectionPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			//Bouton connexion
			{				
				buttonPane.add(btnConnexion);
				getRootPane().setDefaultButton(btnConnexion);
			}
			
			//Bouton Nouveau
			{
				btnNouveau.setActionCommand("Cancel");
				buttonPane.add(btnNouveau);
			}
		}
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

}
