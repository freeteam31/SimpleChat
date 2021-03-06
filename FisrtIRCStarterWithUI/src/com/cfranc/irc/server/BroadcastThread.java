package com.cfranc.irc.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.cfranc.irc.IfClientServerProtocol;

public class BroadcastThread extends Thread {
	
	public static HashMap<User, ServerToClientThread> clientTreadsMap = new HashMap<User, ServerToClientThread>();
	
	static {
		Collections.synchronizedMap(clientTreadsMap);
	}
	
	public static boolean addClient(User user, ServerToClientThread serverToClientThread){
		boolean res=true;
		if (clientTreadsMap.containsKey(user)) {
			res = false;
		} else {
			clientTreadsMap.put(user, serverToClientThread);
			
			//Envoi du nouveau client aux clients d�j� connect�s
			notifierAjoutClient(user);
			
			//Envoi des utilisateurs d�j� connect�s au nouveau client
			ajouterClientsConnectes(user);
		}
		
		return res;
	}

	private static void ajouterClientsConnectes(User user) {
		for (User mapKey : clientTreadsMap.keySet()) {
			clientTreadsMap.get(user).post(IfClientServerProtocol.ADD + mapKey.getLogin());
		}
	}

	private static void notifierAjoutClient(User user) {
		for (User mapKey : clientTreadsMap.keySet()) {
			clientTreadsMap.get(mapKey).post(IfClientServerProtocol.ADD + user.getLogin());
		}
	}

	/**
	 * Envoi d'un message � tous les utilisateurs
	 * @param sender
	 * @param msg
	 */
	public static void sendMessage(User sender, String msg) {
		
		System.out.println("sendMessage : "+"#"+sender.getLogin()+"#"+msg);
		
		String destinataire = null;
		
		if (msg.startsWith("PRIVATE")) {
			String[] tab = msg.split(IfClientServerProtocol.SEPARATOR);
			destinataire = tab[1];
		}
		
		Collection<ServerToClientThread> clientTreads = clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		
		//Boucle sur les threads client
		while (receiverClientThreadIterator.hasNext()) {
			ServerToClientThread clientThread = (ServerToClientThread) receiverClientThreadIterator.next();
			
			if (msg.startsWith("PRIVATE")) {
				if (clientThread.getUser().getLogin().equals(destinataire)) {
					clientThread.post("#" + sender.getLogin() + "#" + msg);
				}

				if (clientThread.getUser().getLogin().equals(sender.getLogin())) {
					clientThread.post("#" + sender.getLogin() + "#" + msg);
				}
			} else {
				clientThread.post("#" + sender.getLogin() + "#" + msg);
			}
		}
	}
	
	/**
	 * D�connexion d'un client :
	 * - on notifie aux autres clients la d�connexion 
	 */
	public static void removeClient(User user) {
		System.out.println(">>BroadcastThread.removeClient("+user+")");
		
		notifierRemoveClient(user);
		
		clientTreadsMap.remove(user);
		
		ClientConnectThread.supprimerUserTreeModelServer(user);
	}
	
	private static void removeClientsConnectes(User user) {
		for (User mapKey : clientTreadsMap.keySet()) {
			System.out.println("@@removeClientsConnectes");
			clientTreadsMap.get(user).post(IfClientServerProtocol.DEL + mapKey.getLogin());
		}
	}

	private static void notifierRemoveClient(User user) {
		for (User mapKey : clientTreadsMap.keySet()) {
//			System.out.println("@@notifierRemoveClient to "+clientTreadsMap.get(mapKey));
			clientTreadsMap.get(mapKey).post(IfClientServerProtocol.DEL + user.getLogin());
		}
	}
	
	public static boolean accept(User user){
		boolean res=true;
		if (clientTreadsMap.containsKey(user)) {
			res= false;
		}
		return res;
	}
}
