package com.cfranc.irc.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.cfranc.irc.IfClientServerProtocol;

public class BroadcastThread extends Thread {
	
	public static HashMap<User, ServerToClientThread> clientTreadsMap=new HashMap<User, ServerToClientThread>();
	static{
		Collections.synchronizedMap(clientTreadsMap);
	}
	
	public static boolean addClient(User user, ServerToClientThread serverToClientThread){
		boolean res=true;
		if(clientTreadsMap.containsKey(user)){
			res=false;
		}
		else{
			clientTreadsMap.put(user, serverToClientThread);
			
			//Envoi du nouveau client aux clients déjà connectés
			notifierAjoutClient(user);
			
			//Envoi des utilisateurs déjà connectés au nouveau client
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
	 * Envoi d'un message à tous les utilisateurs
	 * @param sender
	 * @param msg
	 */
	public static void sendMessage(User sender, String msg){
		
		Collection<ServerToClientThread> clientTreads=clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		
		//Boucle sur les threads client
		while (receiverClientThreadIterator.hasNext()) {
			ServerToClientThread clientThread = (ServerToClientThread) receiverClientThreadIterator.next();
			clientThread.post("#"+sender.getLogin()+"#"+msg);
			
			System.out.println("sendMessage : "+"#"+sender.getLogin()+"#"+msg);
		}
	}
	
	public static void removeClient(User user){
		clientTreadsMap.remove(user);
	}
	
	public static boolean accept(User user){
		boolean res=true;
		if(clientTreadsMap.containsKey(user)){
			res= false;
		}
		return res;
	}
}
