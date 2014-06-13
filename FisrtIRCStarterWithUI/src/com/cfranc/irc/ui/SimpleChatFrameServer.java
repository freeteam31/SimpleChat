package com.cfranc.irc.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JTree;

public class SimpleChatFrameServer extends JFrame{

	public StyledDocument model=null;
	//public DefaultListModel<String> clientListModel=null;
	private static DefaultTreeModel clientTreeModel; 
	private JTree treeUtilisateur;
			
	public SimpleChatFrameServer(int port, StyledDocument model,  DefaultTreeModel clientTreeModel) {
		super("ISM - IRC Server Manager");
		this.model = model;
		this.clientTreeModel = clientTreeModel;
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 702, 339);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextPane textPane = new JTextPane(model);
		scrollPane.setViewportView(textPane);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				
			}
		});
		
		JScrollPane scrollPaneList = new JScrollPane();
		getContentPane().add(scrollPaneList, BorderLayout.WEST);
		
		clientTreeModel.addTreeModelListener(new MyTreeModelListener());

		treeUtilisateur = new JTree(clientTreeModel);
		treeUtilisateur.setRootVisible(false);
		treeUtilisateur.setPreferredSize(new Dimension(100, 0));
		treeUtilisateur.setMinimumSize(new Dimension(100, 0));
		treeUtilisateur.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeUtilisateur.addTreeSelectionListener(new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		        treeUtilisateur.getLastSelectedPathComponent();

		    /* if nothing is selected */ 
		        if (node == null) return;

		    /* retrieve the node that was selected */ 
		        Object nodeInfo = node.getUserObject();
		    	System.out.println(">>valueChanged : " + nodeInfo);
		       // .....
		    /* React to the node selection. */
		       // ...
		    }
		});

		final JLabel statusBar=new JLabel("");
		getContentPane().add(statusBar, BorderLayout.SOUTH);

//		final JList<String> list = new JList<String>(clientListModel);
//		list.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				String clientSelected=list.getSelectedValue().toString();
//				statusBar.setText(clientSelected);
//			}
//		});
//		list.setMinimumSize(new Dimension(200,0));
//		scrollPaneList.setViewportView(list);
		scrollPaneList.setViewportView(treeUtilisateur);
	}
	
	public JTree getTreeUtilisateur() {
		return treeUtilisateur;
	}
	
	/**
	 * 
	 * @author Administrateur
	 *
	 */
	private class MyTreeModelListener implements TreeModelListener {
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
		public void treeNodesInserted(TreeModelEvent e) {
			clientTreeModel.reload();
			System.out.println("treeNodesInserted");
		}
		public void treeNodesRemoved(TreeModelEvent e) {
			clientTreeModel.reload();
			System.out.println("treeNodesRemoved");
		}
		public void treeStructureChanged(TreeModelEvent e) {
			//clientTreeModel.reload();
			System.out.println("treeStructureChanged");
		}
	}

	
}