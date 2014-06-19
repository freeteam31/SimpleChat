package com.cfranc.irc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


public class SimpleChatFrameClientController implements MouseListener, TreeModelListener, TreeSelectionListener, ActionListener, WindowListener {

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

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		DefaultMutableTreeNode node;
		node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());

		/*
		 * If the event lists children, then the changed
		 * node is the child of the node we've already
		 * gotten.  Otherwise, the changed node and the
		 * specified node are the same.
		 */

		int index = e.getChildIndices()[0];
		node = (DefaultMutableTreeNode)(node.getChildAt(index));

		System.out.println("The user has finished editing the node.");
		System.out.println("New value: " + node.getUserObject());
		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionDeconnexion();		
	}
	
	/**
	 * 
	 */
	public void actionDeconnexion() {
		try {
			this.frameClient.sender.quitServer();
			this.frameClient.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println(">>windowClosing(WindowEvent e)");
		actionDeconnexion();		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree treeUtilisateur = (JTree) e.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeUtilisateur.getLastSelectedPathComponent();

		/* if nothing is selected */ 
		if (node == null) return;

		/* retrieve the node that was selected */ 
		Object nodeInfo = node.getUserObject();
		System.out.println(">>valueChanged : " + nodeInfo);
		// .....
		/* React to the node selection. */
		// ...		
	}

}
