package com.cfranc.irc.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.cfranc.irc.IfClientServerProtocol;
import com.cfranc.irc.server.ServerToClientThread;
import com.cfranc.irc.server.User;
import com.cfranc.irc.ui.SimpleChatClientApp;
import com.cfranc.irc.ui.SimpleChatFrameClient;

public class ClientToServerThread extends Thread implements IfSenderModel{
	
	private final static String PREFIXE_RELATIF = "/com/cfranc/irc/ui/";
	
	private SimpleChatFrameClient frameClient;
	public void setFrameClient(SimpleChatFrameClient frameClient) {
		this.frameClient = frameClient;
	}

	private Socket socket = null;
	private DataOutputStream streamOut = null;
	private DataInputStream streamIn = null;
	private BufferedReader console = null;
	String login,pwd;
	// Sprint 1 : liste des utilisateurs --> JTree
	//DefaultListModel<String> clientListModel;
	DefaultTreeModel clientTreeModel;

	StyledDocument documentModel;
	
	public static HashMap<String, StyledDocument> documentModelSalons = new HashMap<String, StyledDocument>();
	
	static {
		Collections.synchronizedMap(documentModelSalons);
	}
	
	//Ajout du modèle à la liste
	public void ajouterModeleSalon(String nomSalon, StyledDocument documentModel) {
		if (!documentModelSalons.containsKey(nomSalon)) {
			documentModelSalons.put(nomSalon, documentModel);
		}
	}

	// Sprint 1 : liste des utilisateurs --> JTree
	//public ClientToServerThread(StyledDocument documentModel, DefaultListModel<String> clientListModel, Socket socket, String login, String pwd) {
	public ClientToServerThread(StyledDocument documentModel, DefaultTreeModel clientTreeModel, Socket socket, String login, String pwd) {
		super();
		this.documentModel=documentModel;
		
		//Ajout du modèle à la liste
		ajouterModeleSalon("Salon général", documentModel);
		
		//this.clientListModel=clientListModel;
		this.clientTreeModel = clientTreeModel;
		this.socket = socket;
		this.login = login;
		this.pwd = pwd;
	}

	public void open() throws IOException {
		console = new BufferedReader(new InputStreamReader(System.in));
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(socket.getOutputStream());
	}

	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (streamIn != null)
			streamIn.close();
		if (streamOut != null)
			streamOut.close();
	}

	public void receiveMessage(String user, String line, String nomSalon){
		//String nomSalon = "Salon privé : #guest";
	
System.out.println("receiveMessage() : user = " + user + ", line = " + line);		
		if (nomSalon==null) {
			nomSalon = "Salon général";
		}
		 		
		StyledDocument documentSalon = documentModelSalons.get(nomSalon);
		
		//Ajout de l'onglet privé s'il n'existe pas
		if (documentSalon==null) {
			this.frameClient.ajouterOnglet(nomSalon); 
			documentSalon = documentModelSalons.get(nomSalon);
		}
		
		// Ajout des styles
		SimpleChatClientApp.ajouterStyles(documentSalon);
		
		Style styleBI = ((StyledDocument)documentSalon).getStyle(SimpleChatClientApp.BOLD_ITALIC);
		Style styleGP = ((StyledDocument)documentSalon).getStyle(SimpleChatClientApp.GRAY_PLAIN);
		//Style styleImage = ((StyledDocument)documentSalon).getStyle(SimpleChatClientApp.SMILEY01);
		
		//receiveMessage(user, line, styleBI, styleGP, styleImage, documentSalon);
		//
		try {
			// Test
			System.out.println(">>Ecriture: line = " + line);
			
			documentSalon.insertString(documentSalon.getLength(), user + " : ", styleBI);
			//documentSalon.insertString(documentSalon.getLength(), line + "\n", styleGP);
			
			decoupeLineSmiley01(line, "\\:\\)", ":)", "IconSmiley09", documentSalon, styleGP);
			
			//Retour chariot
			documentSalon.insertString(documentSalon.getLength(), "\n", styleGP);
			
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * @param line
	 * @param documentSalon
	 * @param styleGP
	 * @param styleImage
	 * @throws BadLocationException
	 */
	public void decoupeLineSmiley01(String line, String lineDelimiter, String smiley, String refSmiley, StyledDocument documentSalon,
			Style styleGP) throws BadLocationException {
		
		String[] tabLignesSmiley01 = line.split(lineDelimiter);
		for (int i = 0; i < tabLignesSmiley01.length; i++) {
			if ((i != 0) && (i != tabLignesSmiley01.length)) {
				documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
			}
			decoupeLineSmiley02(tabLignesSmiley01[i], "\\:\\(", ":(", "IconSmiley05", documentSalon, styleGP);
		}
		if (line.endsWith(smiley)) {
			documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
		}
	}

	/**
	 * @param line
	 * @param documentSalon
	 * @param styleGP
	 * @param styleImage
	 * @throws BadLocationException
	 */
	public void decoupeLineSmiley02(String line, String lineDelimiter, String smiley, String refSmiley, StyledDocument documentSalon,
			Style styleGP) throws BadLocationException {
		
		String[] tabLignesSmiley01 = line.split(lineDelimiter);
		for (int i = 0; i < tabLignesSmiley01.length; i++) {
			if ((i != 0) && (i != tabLignesSmiley01.length)) {
				documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
			}
			decoupeLineSmiley03(tabLignesSmiley01[i], "\\;\\)", ";)", "IconSmiley12", documentSalon, styleGP);
		}
		if (line.endsWith(smiley)) {
			documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
		}
	}

	/**
	 * @param line
	 * @param documentSalon
	 * @param styleGP
	 * @param styleImage
	 * @throws BadLocationException
	 */
	public void decoupeLineSmiley03(String line, String lineDelimiter, String smiley, String refSmiley, StyledDocument documentSalon,
			Style styleGP) throws BadLocationException {
		
		String[] tabLignesSmiley01 = line.split(lineDelimiter);
		for (int i = 0; i < tabLignesSmiley01.length; i++) {
			if ((i != 0) && (i != tabLignesSmiley01.length)) {
				documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
			}
			decoupeLineSmiley05(tabLignesSmiley01[i], "\\:\\|", ":|", "IconSmiley02", documentSalon, styleGP);
		}
		if (line.endsWith(smiley)) {
			documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
		}
	}

	/**
	 * @param line
	 * @param documentSalon
	 * @param styleGP
	 * @param styleImage
	 * @throws BadLocationException
	 */
//	public void decoupeLineSmiley04(String line, String lineDelimiter, String smiley, String refSmiley, StyledDocument documentSalon,
//			Style styleGP) throws BadLocationException {
//		
//		String[] tabLignesSmiley01 = line.split(lineDelimiter);
//		for (int i = 0; i < tabLignesSmiley01.length; i++) {
//			if ((i != 0) && (i != tabLignesSmiley01.length)) {
//				documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
//			}
//			decoupeLineSmiley05(tabLignesSmiley01[i], "\\:\\x", ":x", "IconSmiley10", documentSalon, styleGP);
//		}
//		if (line.endsWith(smiley)) {
//			documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
//		}
//	}

	/**
	 * @param line
	 * @param documentSalon
	 * @param styleGP
	 * @param styleImage
	 * @throws BadLocationException
	 */
	public void decoupeLineSmiley05(String line, String lineDelimiter, String smiley, String refSmiley, StyledDocument documentSalon,
			Style styleGP) throws BadLocationException {
		
		String[] tabLignesSmiley01 = line.split(lineDelimiter);
		for (int i = 0; i < tabLignesSmiley01.length; i++) {
			if ((i != 0) && (i != tabLignesSmiley01.length)) {
				documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
			}
			decoupeLineSmiley06(tabLignesSmiley01[i], "\\:\\(", ":(", "IconSmiley02", documentSalon, styleGP);
		}
		if (line.endsWith(smiley)) {
			documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
		}
	}

	/**
	 * @param line
	 * @param documentSalon
	 * @param styleGP
	 * @param styleImage
	 * @throws BadLocationException
	 */
	public void decoupeLineSmiley06(String line, String lineDelimiter, String smiley, String refSmiley, StyledDocument documentSalon,
			Style styleGP) throws BadLocationException {
		
		String[] tabLignesSmiley01 = line.split(lineDelimiter);
		for (int i = 0; i < tabLignesSmiley01.length; i++) {
			if ((i != 0) && (i != tabLignesSmiley01.length)) {
				documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
			}
			documentSalon.insertString(documentSalon.getLength(), tabLignesSmiley01[i], styleGP);
		}
		if (line.endsWith(smiley)) {
			documentSalon.insertString(documentSalon.getLength(), refSmiley, ((StyledDocument)documentSalon).getStyle(refSmiley));
		}
	}
	
	
//	public void receiveMessage(String user, String line, Style styleBI, Style styleGP, Style styleImage, StyledDocument documentSalon) {
//		
//	}

	void readMsg() throws IOException{
		String line = streamIn.readUTF();
		
		System.out.println(">>ClientToServerThread.readMsg --> " + line);

		if (line.startsWith(IfClientServerProtocol.ADD)) {
			String newUser = line.substring(IfClientServerProtocol.ADD.length());
			
			// On ne créé un nouveau noeud que s'il n'existe pas déjà
			boolean noeudExiste = false;
			TreeNode noeudParcours = null;
			Object objectParcours = null;
			for (int i = 0; i < ((DefaultMutableTreeNode)clientTreeModel.getRoot()).getChildCount(); i++) {
				noeudParcours = ((DefaultMutableTreeNode)clientTreeModel.getRoot()).getChildAt(i);
				objectParcours = ((DefaultMutableTreeNode)noeudParcours).getUserObject();
				if (((String)objectParcours).equals(newUser)) {
					noeudExiste = true;
				}
			}	
			
			if (!noeudExiste) {
				DefaultMutableTreeNode noeudAjoute = new DefaultMutableTreeNode(newUser);
				((DefaultMutableTreeNode)clientTreeModel.getRoot()).add(noeudAjoute);
				clientTreeModel.reload();
				receiveMessage(newUser, " entre dans le salon...", null);
			}
			
		} else if(line.startsWith(IfClientServerProtocol.DEL)){
			String delUser = line.substring(IfClientServerProtocol.DEL.length());

			// On supprime le noeud
			TreeNode noeudParcours = null;
			Object objectParcours = null;
			for (int i = 0; i < ((DefaultMutableTreeNode)clientTreeModel.getRoot()).getChildCount(); i++) {
				noeudParcours = ((DefaultMutableTreeNode)clientTreeModel.getRoot()).getChildAt(i);
				objectParcours = ((DefaultMutableTreeNode)noeudParcours).getUserObject();
				if (((String)objectParcours).equals(delUser)) {
					clientTreeModel.removeNodeFromParent((DefaultMutableTreeNode)noeudParcours);
					clientTreeModel.reload();
					receiveMessage(delUser, " quitte le salon...", null);
				}
			}
			
		} else {
			String[] userMsg = line.split(IfClientServerProtocol.SEPARATOR);
			String emetteur = userMsg[1];
			String destinataire = null;
			String nomSalon = null;
			String msg = null;
			if (userMsg[2].equals("PRIVATE")) {
				destinataire = userMsg[3];
				if (login.equals(emetteur)) {
					nomSalon = destinataire;
				} else {
					nomSalon = emetteur;
				}

				msg = userMsg[4];
				System.out.println("login = " + login + ", emetteur = " + emetteur + ", destinataire = " + destinataire + ", nomSalon = " + nomSalon);	
			} else {
				msg = userMsg[2];
				nomSalon = "Salon général";
			}
			
			
			receiveMessage(emetteur, msg, nomSalon);
		}
	}

	String msgToSend=null;

	/* (non-Javadoc)
	 * @see com.cfranc.irc.client.IfSenderModel#setMsgToSend(java.lang.String)
	 */
	@Override
	public void setMsgToSend(String msgToSend) {
		this.msgToSend = msgToSend;
	}

	private boolean sendMsg() throws IOException{
		boolean res=false;
		if(msgToSend!=null){
			if (msgToSend.startsWith(IfClientServerProtocol.PRIVATE)) {
				streamOut.writeUTF("#"+login+msgToSend);
			} else {
				streamOut.writeUTF("#"+login+"#"+msgToSend);
			}
			msgToSend=null;
			streamOut.flush();
			res=true;
		}
		return res;
	}

	public void quitServer() throws IOException{
		//System.out.println(">>quitServer() -->" + IfClientServerProtocol.DEL+login);
		streamOut.writeUTF(IfClientServerProtocol.DEL + login);
		streamOut.flush();
		done = true;
	}

	boolean done;
	@Override
	public void run() {
		try {
			open();
			done = !authentification();
			while (!done) {
				try {
					if(streamIn.available()>0){
						readMsg();
					}

					if(!sendMsg()){
						Thread.sleep(100);
					}
				} 
				catch (IOException | InterruptedException ioe) {
					ioe.printStackTrace();
					done = true;
				}
			}
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean authentification() {
		boolean res=false;
		String loginPwdQ;
		try {
			while(streamIn.available()<=0){
				Thread.sleep(100);
			}
			loginPwdQ = streamIn.readUTF();
			if(loginPwdQ.equals(IfClientServerProtocol.LOGIN_PWD)){
				streamOut.writeUTF(IfClientServerProtocol.SEPARATOR+this.login+IfClientServerProtocol.SEPARATOR+this.pwd);
			}
			while(streamIn.available()<=0){
				Thread.sleep(100);
			}
			String acq=streamIn.readUTF();
			if(acq.equals(IfClientServerProtocol.OK)){
				res=true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res=false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;		
	}

}

