package com.cfranc.irc.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.cfranc.irc.client.ClientToServerThread;
import com.cfranc.irc.server.ClientConnectThread;

/**
 * Client de la messagerie IRC.
 * 
 * @author Administrateur
 *
 */
public class SimpleChatClientApp implements ActionListener {

	private final static String PREFIXE_RELATIF = "/com/cfranc/irc/ui/";
	// Styles
	public static final String BOLD_ITALIC = "BoldItalic";
    public static final String GRAY_PLAIN = "Gray";
    public static final String SMILEY01 = "IconSmiley01";
    public static final String SMILEY02 = "IconSmiley02";
    public static final String SMILEY03 = "IconSmiley03";
    public static final String SMILEY04 = "IconSmiley04";
    public static final String SMILEY05 = "IconSmiley05";
    public static final String SMILEY06 = "IconSmiley06";
    public static final String SMILEY07 = "IconSmiley07";
    public static final String SMILEY08 = "IconSmiley08";
    public static final String SMILEY09 = "IconSmiley09";
    public static final String SMILEY10 = "IconSmiley10";
    public static final String SMILEY11 = "IconSmiley11";
    public static final String SMILEY12 = "IconSmiley12";
    // Tokens
    public static final String TOKEN01 = ":)";
    public static final String TOKEN02 = ":(";

	// Thread de communication avec le Server
    private static ClientToServerThread clientToServerThread;

	static String[] ConnectOptionNames = { "Connect" };	
    static String   ConnectTitle = "Connection Information";
    
    Socket socketClientServer;
    int serverPort;
    String serverName;
    String clientName;
    String clientPwd;
    
	private SimpleChatFrameClient frame;
	private SimpleChatConnexionDlg connexionDlg;
	
	// Modèle du JTree affichant les utilisateurs
	DefaultTreeModel clientTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(Messages.getString("SimpleChatFrameClient.libRacine")));
	// Objet d'affichage des messages
	public StyledDocument documentModel = new DefaultStyledDocument();

	/**
	 * Retourne un objet d'affichage des messages
	 * @return
	 */
	public static DefaultStyledDocument getDefaultDocumentModel() {
		DefaultStyledDocument res = new DefaultStyledDocument();
		
		ajouterStyles(res);

		return res;
	}
	
	/**
	 * Ajoute les style au doc stylisé
	 * @param doc
	 */
	public static void ajouterStyles(StyledDocument doc) {
	    Style styleDefault = (Style) doc.getStyle(StyleContext.DEFAULT_STYLE);
	    
	    doc.addStyle(BOLD_ITALIC, styleDefault);
	    Style styleBI = doc.getStyle(BOLD_ITALIC);
	    StyleConstants.setBold(styleBI, true);
	    StyleConstants.setItalic(styleBI, true);
	    StyleConstants.setForeground(styleBI, Color.black);	    

	    doc.addStyle(GRAY_PLAIN, styleDefault);
        Style styleGP = doc.getStyle(GRAY_PLAIN);
        StyleConstants.setBold(styleGP, false);
        StyleConstants.setItalic(styleGP, false);
        StyleConstants.setForeground(styleGP, Color.lightGray);
        
        for (int i = 1; i < 10; i++) {
        	String ref = "IconSmiley0" + i;
        	System.out.println(">>Ajout du style " + ref + ", ressource = " + PREFIXE_RELATIF + "Smiley0" + i + ".jpg");
            doc.addStyle(ref, styleDefault);
            Style styleImage = doc.getStyle(ref);
            StyleConstants.setAlignment(styleImage, StyleConstants.ALIGN_CENTER);
            ImageIcon img = new ImageIcon(ClassLoader.getSystemClassLoader().getClass().getResource(PREFIXE_RELATIF + "Smiley0" + i + ".jpg"));
            if (img != null) {
                StyleConstants.setIcon(styleImage, img);
            }
		}
        
        doc.addStyle(SMILEY10, styleDefault);
        Style styleImage = doc.getStyle(SMILEY10);
        StyleConstants.setAlignment(styleImage, StyleConstants.ALIGN_CENTER);
        ImageIcon img = new ImageIcon(ClassLoader.getSystemClassLoader().getClass().getResource(PREFIXE_RELATIF + "Smiley10.jpg"));
        if (img != null) {
            StyleConstants.setIcon(styleImage, img);
        }

        doc.addStyle(SMILEY11, styleDefault);
        styleImage = doc.getStyle(SMILEY11);
        StyleConstants.setAlignment(styleImage, StyleConstants.ALIGN_CENTER);
        img = new ImageIcon(ClassLoader.getSystemClassLoader().getClass().getResource(PREFIXE_RELATIF + "Smiley11.jpg"));
        if (img != null) {
            StyleConstants.setIcon(styleImage, img);
        }
        
        doc.addStyle(SMILEY12, styleDefault);
        styleImage = doc.getStyle(SMILEY12);
        StyleConstants.setAlignment(styleImage, StyleConstants.ALIGN_CENTER);
        img = new ImageIcon(ClassLoader.getSystemClassLoader().getClass().getResource(PREFIXE_RELATIF + "Smiley12.jpg"));
        if (img != null) {
            StyleConstants.setIcon(styleImage, img);
        }
	}
	
	

	/**
	 * Affichage de la frame du client
	 */
	public void displayClient() {
		
		// Init GUI
		// Sprint 1 : List --> JTree
		this.frame = new SimpleChatFrameClient(clientToServerThread, clientTreeModel, documentModel);
		
		this.frame.setTitle(this.frame.getTitle()+" : "+clientName+" connected to "+serverName+":"+serverPort);
		((JFrame)this.frame).setVisible(true);
		this.frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				quitApp(SimpleChatClientApp.this);
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	/**
	 * Masque la frame du client
	 */
	public void hideClient() {
		// Init GUI
		((JFrame)this.frame).setVisible(false);
	}

	/**
	 * Appel de l'écran de connexion utilisateur
	 */
    void displayConnectionDialog() {
		connexionDlg = new SimpleChatConnexionDlg();
		connexionDlg.getBtnConnexion().addActionListener(new ClientConnexion());
		connexionDlg.getBtnNouveau().addActionListener(new NouveauProfil());
		connexionDlg.setVisible(true);
	}
    
    /**
     * Connection d'un client au serveur, à savoir :
     * - création d'un socketClientServer
     * - création et démarrage d'un clientToServerThread
     * 
     * @return
     */
    private boolean connectClient() {
		System.out.println("Establishing connection. Please wait ...");
		try {
			socketClientServer = new Socket(this.serverName, this.serverPort);
			// Start connection services
			clientToServerThread = new ClientToServerThread(documentModel, clientTreeModel, socketClientServer, clientName, clientPwd);
			clientToServerThread.start();
			System.out.println("Connected: " + socketClientServer);
			return true;
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
		return false;
	}
    
	/**
	 * Lancement de l'application Client, à savoir :
	 * - instanciation d'un SimpleChatClientApp
	 * - affichage de l'écran de connexion SimpleChatConnexionDlg
	 * - affichage de l'écran client SimpleChatFrameClient
	 */
	public static void main(String[] args) {
		final SimpleChatClientApp app = new SimpleChatClientApp();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					app.displayConnectionDialog();
					app.displayClient();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		// Le client reste en execution tant que ".bye" n'est pas saisi
		Scanner sc=new Scanner(System.in);
		String line="";
		while (!line.equals(".bye")) {
			line=sc.nextLine();			
		}
		quitApp(app);
	}

	/**
	 * Fermeture de l'application Client
	 * @param app
	 */
	private static void quitApp(final SimpleChatClientApp app) {
		try {
			app.clientToServerThread.quitServer();
			app.socketClientServer.close();
			app.hideClient();
			System.out.println("SimpleChatClientApp : fermée");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Test " + e.getSource());
	}
	
	/**
	 * Classe implémentant le clic sur le bouton "Connexion" de l'écran de connexin du client
	 * @author Administrateur
	 *
	 */
	class ClientConnexion implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("ClientConnexion.actionPerformed");
			
			serverPort = Integer.parseInt(connexionDlg.getConnectionPanel().getServerPortField().getText());
			serverName = connexionDlg.getConnectionPanel().getServerField().getText();
			clientName = connexionDlg.getConnectionPanel().getUserNameField().getText();
			clientPwd = connexionDlg.getConnectionPanel().getPasswordField().getText();

			if (connectClient() == true) {
				connexionDlg.setVisible(false);
			}
		}
		
	}
	
	/**
	 * Classe implémentant le clic sur le bouton "Nouveau" de l'écran de connexin du client
	 * @author Administrateur
	 *
	 */
	class NouveauProfil implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			FicheUtilisateur.afficher();
		}
		
	}

}
