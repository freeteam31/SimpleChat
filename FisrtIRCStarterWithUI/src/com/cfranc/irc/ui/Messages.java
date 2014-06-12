package com.cfranc.irc.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Classe gérant les affichages de messages "fixes" (exemple libellés des barres de menu de l'ihm).
 * Les libellés se trouvent dans la ressource fichier "messages.properties".<BR>
 * 
 * <b>Utilisation :</b><BR>
 * - appeler la méthode <code>getString</code> en indiquant la clé (voir fichier .properties) pour obtenir le libellé 
 * 
 * @author Administrateur
 *
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.cfranc.irc.ui.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Retourne le libellé indiqué dans le fichier "messages.properties".
	 * 
	 * @param key clé devant se trouver dans le fichier "messages.properties" 
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
