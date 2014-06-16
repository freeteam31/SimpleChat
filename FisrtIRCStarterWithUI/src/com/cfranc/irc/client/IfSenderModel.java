package com.cfranc.irc.client;

import java.io.IOException;

import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

public interface IfSenderModel {

	public abstract void setMsgToSend(String msgToSend);
	
	public abstract void quitServer() throws IOException;
	
	public abstract void ajouterModeleSalon(String nomSalon, StyledDocument document);

}