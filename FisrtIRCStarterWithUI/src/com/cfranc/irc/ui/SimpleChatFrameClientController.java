package com.cfranc.irc.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.tree.TreePath;


public class SimpleChatFrameClientController implements MouseListener {

	private SimpleChatFrameClient frameClient;
	
	
	/**
	 * @param frameClient
	 */
	public SimpleChatFrameClientController(SimpleChatFrameClient frameClient) {
		super();
		this.frameClient = frameClient;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount()==2) {
			//Ajout nouvel onglet
			TreePath selPath = frameClient.treeUtilisateur.getPathForLocation(e.getX(), e.getY());
			System.out.println("double-clic : " + selPath.getLastPathComponent());
			String tabTitre = selPath.getLastPathComponent().toString();

			frameClient.ajouterOnglet(tabTitre);									
		}		
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
