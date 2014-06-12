package com.cfranc.irc.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Classe g�rant les affichages de messages "fixes" (exemple libell�s des barres de menu de l'ihm).
 * Les libell�s se trouvent dans la ressource fichier "messages.properties".<BR>
 * 
 * <b>Utilisation :</b><BR>
 * - appeler la m�thode <code>getString</code> en indiquant la cl� (voir fichier .properties) pour obtenir le libell� 
 * 
 * @author Administrateur
 *
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.cfranc.irc.ui.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Retourne le libell� indiqu� dans le fichier "messages.properties".
	 * 
	 * @param key cl� devant se trouver dans le fichier "messages.properties" 
	 * @return
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
