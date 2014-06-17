package com.cfranc.irc.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.Choice;

import javax.swing.BoxLayout;
import javax.swing.Box;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;
import javax.swing.JComboBox;

import com.cfranc.irc.server.SimpleChatDb;
import com.cfranc.irc.server.User;

public class FicheUtilisateur extends JDialog {

	// Localisation des ressources images
	//private final static String PREFIXE_RELATIF = "resources/com/cfranc/irc/ui/";
	private final static String PREFIXE_RELATIF = "/com/cfranc/irc/ui/";

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldLogin;
	private JTextField textFieldNom;
	private JTextField textFieldPrenom;
	private JPasswordField passwordField;
	private JLabel lblImageAvatar;

	// ComboBox des avatars
	private String nomFicImageSelectionnee = "Avatar01.jpg";
	
	
	
	//private ImageIcon imageSelectionnee = new ImageIcon(PREFIXE_RELATIF + "Avatar01.jpg");
	private ImageIcon imageSelectionnee = new ImageIcon(getClass().getResource(PREFIXE_RELATIF + "Avatar01.jpg"));
	
	private JPanel panelImageAvatar;

	private String[] listAvatar = { "Avatar01.jpg", "Avatar02.jpg", "Avatar03.jpg", "Avatar04.jpg", "Avatar05.jpg", "Avatar06.jpg", "Avatar07.jpg", "Avatar08.jpg" };
	private JComboBox comboAvatar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FicheUtilisateur dialog = new FicheUtilisateur();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FicheUtilisateur() {
		setBounds(100, 100, 722, 528);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		panelImageAvatar = new JPanel();
		contentPanel.add(panelImageAvatar, BorderLayout.WEST);
		panelImageAvatar.setLayout(new BorderLayout(0, 0));
		lblImageAvatar = new JLabel("");
		panelImageAvatar.add(lblImageAvatar, BorderLayout.NORTH);
		chargerImage("Avatar01.jpg");
		{
			JPanel panelInfosUtilisateur = new JPanel();
			contentPanel.add(panelInfosUtilisateur, BorderLayout.CENTER);
			GridBagLayout gbl_panelInfosUtilisateur = new GridBagLayout();
			gbl_panelInfosUtilisateur.columnWidths = new int[]{80, 250, 0};
			gbl_panelInfosUtilisateur.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
			gbl_panelInfosUtilisateur.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_panelInfosUtilisateur.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			panelInfosUtilisateur.setLayout(gbl_panelInfosUtilisateur);
			{
				JLabel lblLogin = new JLabel("Login");
				GridBagConstraints gbc_lblLogin = new GridBagConstraints();
				gbc_lblLogin.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblLogin.weightx = 1.0;
				gbc_lblLogin.anchor = GridBagConstraints.LINE_START;
				gbc_lblLogin.insets = new Insets(5, 5, 5, 5);
				gbc_lblLogin.gridx = 0;
				gbc_lblLogin.gridy = 0;
				panelInfosUtilisateur.add(lblLogin, gbc_lblLogin);
			}
			{
				textFieldLogin = new JTextField();
				GridBagConstraints gbc_textFieldLogin = new GridBagConstraints();
				gbc_textFieldLogin.weightx = 1.0;
				gbc_textFieldLogin.anchor = GridBagConstraints.LINE_START;
				gbc_textFieldLogin.fill = GridBagConstraints.HORIZONTAL;
				gbc_textFieldLogin.insets = new Insets(5, 5, 5, 5);
				gbc_textFieldLogin.gridx = 1;
				gbc_textFieldLogin.gridy = 0;
				panelInfosUtilisateur.add(textFieldLogin, gbc_textFieldLogin);
				textFieldLogin.setPreferredSize(new Dimension(400, 20));
				textFieldLogin.setMinimumSize(new Dimension(400, 20));
				textFieldLogin.setMaximumSize(new Dimension(400, 20));
				textFieldLogin.setColumns(10);
			}
			{
				JLabel lblPassword = new JLabel("Password");
				GridBagConstraints gbc_lblPassword = new GridBagConstraints();
				gbc_lblPassword.anchor = GridBagConstraints.LINE_START;
				gbc_lblPassword.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblPassword.insets = new Insets(5, 5, 5, 5);
				gbc_lblPassword.gridx = 0;
				gbc_lblPassword.gridy = 1;
				panelInfosUtilisateur.add(lblPassword, gbc_lblPassword);
			}
			{
				passwordField = new JPasswordField();
				passwordField.setPreferredSize(new Dimension(400, 20));
				passwordField.setMinimumSize(new Dimension(400, 20));
				passwordField.setMaximumSize(new Dimension(400, 20));
				GridBagConstraints gbc_passwordField = new GridBagConstraints();
				gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
				gbc_passwordField.insets = new Insets(5, 5, 5, 5);
				gbc_passwordField.gridx = 1;
				gbc_passwordField.gridy = 1;
				panelInfosUtilisateur.add(passwordField, gbc_passwordField);
			}
			{
				JLabel lblNom = new JLabel("Nom");
				GridBagConstraints gbc_lblNom = new GridBagConstraints();
				gbc_lblNom.anchor = GridBagConstraints.LINE_START;
				gbc_lblNom.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblNom.insets = new Insets(5, 5, 5, 5);
				gbc_lblNom.gridx = 0;
				gbc_lblNom.gridy = 2;
				panelInfosUtilisateur.add(lblNom, gbc_lblNom);
			}
			{
				textFieldNom = new JTextField();
				textFieldNom.setPreferredSize(new Dimension(400, 20));
				textFieldNom.setMinimumSize(new Dimension(400, 20));
				textFieldNom.setMaximumSize(new Dimension(400, 20));
				textFieldNom.setColumns(10);
				GridBagConstraints gbc_textFieldNom = new GridBagConstraints();
				gbc_textFieldNom.fill = GridBagConstraints.HORIZONTAL;
				gbc_textFieldNom.insets = new Insets(5, 5, 5, 5);
				gbc_textFieldNom.gridx = 1;
				gbc_textFieldNom.gridy = 2;
				panelInfosUtilisateur.add(textFieldNom, gbc_textFieldNom);
			}

			{
				JLabel lblPrenom = new JLabel("Prenom");
				lblPrenom.setAlignmentY(Component.TOP_ALIGNMENT);
				GridBagConstraints gbc_lblPrenom = new GridBagConstraints();
				gbc_lblPrenom.anchor = GridBagConstraints.LINE_START;
				gbc_lblPrenom.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblPrenom.insets = new Insets(5, 5, 5, 5);
				gbc_lblPrenom.gridx = 0;
				gbc_lblPrenom.gridy = 3;
				panelInfosUtilisateur.add(lblPrenom, gbc_lblPrenom);
			}
			{
				textFieldPrenom = new JTextField();
				textFieldPrenom.setPreferredSize(new Dimension(400, 20));
				textFieldPrenom.setMinimumSize(new Dimension(400, 20));
				textFieldPrenom.setMaximumSize(new Dimension(400, 20));
				textFieldPrenom.setColumns(10);
				GridBagConstraints gbc_textFieldPrenom = new GridBagConstraints();
				gbc_textFieldPrenom.fill = GridBagConstraints.HORIZONTAL;
				gbc_textFieldPrenom.insets = new Insets(5, 5, 5, 5);
				gbc_textFieldPrenom.gridx = 1;
				gbc_textFieldPrenom.gridy = 3;
				panelInfosUtilisateur.add(textFieldPrenom, gbc_textFieldPrenom);
			}
			{
				JLabel lblAvatar = new JLabel("Avatar");
				lblAvatar.setAlignmentY(0.0f);
				GridBagConstraints gbc_lblAvatar = new GridBagConstraints();
				gbc_lblAvatar.anchor = GridBagConstraints.LINE_START;
				gbc_lblAvatar.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblAvatar.insets = new Insets(5, 5, 5, 5);
				gbc_lblAvatar.gridx = 0;
				gbc_lblAvatar.gridy = 4;
				panelInfosUtilisateur.add(lblAvatar, gbc_lblAvatar);
			}
			{
				comboAvatar = new JComboBox(listAvatar);
				comboAvatar.setSelectedIndex(0);
				comboAvatar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					    chargerImage((String)getComboAvatar().getSelectedItem());
					}
				});
				GridBagConstraints gbc_comboAvatar = new GridBagConstraints();
				gbc_comboAvatar.insets = new Insets(5, 5, 5, 5);
				gbc_comboAvatar.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboAvatar.gridx = 1;
				gbc_comboAvatar.gridy = 4;
				panelInfosUtilisateur.add(comboAvatar, gbc_comboAvatar);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Enregistrer");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Enregistrement de l'utilisateur en BDD
						// On teste si :
						// - le login est correctement renseigné
						String loginUtilisateur = getTextFieldLogin().getText(); 
						if ((loginUtilisateur != null) && (!loginUtilisateur.equals(""))) {
							SimpleChatDb db = new SimpleChatDb();
							db.OuvrirBase();
							User newUser = new User(loginUtilisateur, getPasswordField().getText());
							newUser.setNom(getTextFieldNom().getText());
							newUser.setPrenom(getTextFieldPrenom().getText());
							newUser.setCheminImg((String)getComboAvatar().getSelectedItem());
							db.ajouterUtilisateur(newUser);
							db.fermerBase();
							
							SwingUtilities.windowForComponent((Component)e.getSource()).dispose();
							
						} else {
							System.out.println("Login incorrect !");
						}
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.windowForComponent((Component)e.getSource()).dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}

	/**
	 * Launch the application.
	 */
	public static void afficher() {
		try {
			FicheUtilisateur dialog = new FicheUtilisateur();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModal(true);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JLabel getLblImageAvatar() {
		return lblImageAvatar;
	}

	public void chargerImage(String nomFicImage) {
		//ImageIcon img = new ImageIcon(PREFIXE_RELATIF + nomFicImage);
		ImageIcon img = new ImageIcon(getClass().getResource(PREFIXE_RELATIF + nomFicImage));
		getLblImageAvatar().setIcon(img);
		getPanelImageAvatar().setSize(img.getIconWidth(), img.getIconHeight());
	}

	public JPanel getPanelImageAvatar() {
		return panelImageAvatar;
	}
	public JComboBox getComboAvatar() {
		return comboAvatar;
	}
	public JTextField getTextFieldLogin() {
		return textFieldLogin;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public JTextField getTextFieldNom() {
		return textFieldNom;
	}
	public JTextField getTextFieldPrenom() {
		return textFieldPrenom;
	}
}
