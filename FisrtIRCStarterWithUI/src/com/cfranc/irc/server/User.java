package com.cfranc.irc.server;

public class User {

	// Test de modif
	
	private String login;
	private String pwd;
	private String nom;
	private String prenom;
	private String cheminImg;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getCheminImg() {
		return cheminImg;
	}
	public void setCheminImg(String cheminImg) {
		this.cheminImg = cheminImg;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public User(String login, String pwd) {
		super();
		this.login = login;
		this.pwd = pwd;
	}	
	
}
