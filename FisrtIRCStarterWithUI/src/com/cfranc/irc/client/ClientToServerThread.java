package com.cfranc.irc.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.cfranc.irc.IfClientServerProtocol;
import com.cfranc.irc.ui.SimpleChatClientApp;

public class ClientToServerThread extends Thread implements IfSenderModel{
	private Socket socket = null;
	private DataOutputStream streamOut = null;
	private DataInputStream streamIn = null;
	private BufferedReader console = null;
	String login,pwd;
	// Sprint 1 : liste des utilisateurs --> JTree
	//DefaultListModel<String> clientListModel;
	DefaultTreeModel clientTreeModel;

	StyledDocument documentModel;

	// Sprint 1 : liste des utilisateurs --> JTree
	//public ClientToServerThread(StyledDocument documentModel, DefaultListModel<String> clientListModel, Socket socket, String login, String pwd) {
	public ClientToServerThread(StyledDocument documentModel, DefaultTreeModel clientTreeModel, Socket socket, String login, String pwd) {
		super();
		this.documentModel=documentModel;
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

	public void receiveMessage(String user, String line){
		Style styleBI = ((StyledDocument)documentModel).getStyle(SimpleChatClientApp.BOLD_ITALIC);
		Style styleGP = ((StyledDocument)documentModel).getStyle(SimpleChatClientApp.GRAY_PLAIN);
		receiveMessage(user, line, styleBI, styleGP);
	}

	public void receiveMessage(String user, String line, Style styleBI,
			Style styleGP) {
		
		//String nomSalon = "Salon priv� : #guest";
		String nomSalon = null;
		
		try {        	
			if (nomSalon==null) {
			documentModel.insertString(documentModel.getLength(), user+" : ", styleBI);
			documentModel.insertString(documentModel.getLength(), line+"\n", styleGP);
			}else {
//				for (int i = 0; i < tabSalons.getTabCount(); i++) {
//				if (tabSalons.getTitleAt(i).equals(tabSalons)) {
//					JScrollPane a = (JScrollPane) tabSalons.getTabComponentAt(i);
//					JTextArea b = (JTextArea) a.getViewport().getView();
//					
//					b.getDocument().insertString(documentModel.getLength(), user+" : ", styleBI); //$NON-NLS-1$
//					b.getDocument().insertString(documentModel.getLength(), line+"\n", styleGP); //$NON-NLS-1$
//					
//				}
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}				        	
	}

	void readMsg() throws IOException{
		String line = streamIn.readUTF();
		
		System.out.println(">>ClientToServerThread.readMsg --> " + line);

		if (line.startsWith(IfClientServerProtocol.ADD)) {
			String newUser = line.substring(IfClientServerProtocol.ADD.length());
			
			// On ne cr�� un nouveau noeud que s'il n'existe pas d�j�
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
				((DefaultMutableTreeNode)clientTreeModel.getRoot()).add(new DefaultMutableTreeNode(newUser));
				clientTreeModel.reload();
				receiveMessage(newUser, " entre dans le salon...");
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
					receiveMessage(delUser, " quitte le salon...");
				}
			}
			
		} else {
			String[] userMsg=line.split(IfClientServerProtocol.SEPARATOR);
			String user=userMsg[1];
			receiveMessage(user, userMsg[2]);
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
			streamOut.writeUTF("#"+login+"#"+msgToSend);
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

