package com.cfranc.irc.server;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SimpleChatDb {

	private ConnectionSGBD con;
	
	public void OuvrirBase() {
		con = new ConnectionSGBD("jdbc:sqlite:.\\bdd\\simplechat.sqlite", null, null);
	}
	
	public void fermerBase() {
		if (con != null) {
			try {
				con.getConn().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			con = null;
		}
	}

	public void ajouterUtilisateur(User user) {
		con.executeInsert("INSERT INTO UTILISATEURS (PSEUDO, NOM, PRENOM, CHEMIN_IMG, PASSWORD) VALUES (\"" 
						 + user.getLogin() + "\",\"" 
						 + user.getNom() + "\",\""
						 + user.getPrenom() + "\",\""
						 + user.getCheminImg() + "\",\""
						 + user.getPwd() + "\")");
	}
	
	public void supprimerUtilisateur(User user) {
		try {
			con.getSmt().execute("DELETE FROM UTILISATEURS WHERE PSEUDO = \"" + user.getLogin() + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void modifierUtilisateur(User user) {
		con.executeUpdate("UPDATE UTILISATEURS SET " + 
							"NOM = \"" + user.getNom() + "\", " + 
							"PRENOM = \"" + user.getPrenom() + "\", " +
							"CHEMIN_IMG = \"" + user.getCheminImg() + "\", " +
							"PASSWORD = \"" + user.getPwd() + "\" " +
							" WHERE PSEUDO = \"" + user.getLogin() + "\""); 
	}
	
	public ResultSet executeSelect(String query) {
		try {
			return con.getSmt().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
