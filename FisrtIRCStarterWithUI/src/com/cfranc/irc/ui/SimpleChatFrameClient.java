package com.cfranc.irc.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.cfranc.irc.IfClientServerProtocol;
import com.cfranc.irc.client.IfSenderModel;
import com.cfranc.irc.server.SimpleChatDb;
import com.cfranc.irc.server.User;


public class SimpleChatFrameClient extends JFrame {

	private final static String PREFIXE_RELATIF = "/com/cfranc/irc/ui/";

	// Sprint 1 remplacé par modèle JTree
	//private static ListModel<String> listModel; 
	private static DefaultTreeModel treeModel; 

	IfSenderModel sender;
	private String senderName;	

	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblSender;
	private final ResourceAction sendAction = new SendAction();
	private final ResourceAction lockAction = new LockAction();
	// Action sur le bouton Afficher la barre d'outils
	private final ResourceAction actionAfficherBarreOutils = new AfficherBarreOutilsAction();

	private boolean isScrollLocked = true;
	private boolean isAfficherBarreOutils = true;

	JToolBar toolBar;
	public final JTabbedPane tabSalons;
	public final JTree treeUtilisateur;
	private JButton btnSend;

	/**
	 * Launch the application.
	 * @throws BadLocationException 
	 */
	public static void main(String[] args) throws BadLocationException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleChatFrameClient frame = new SimpleChatFrameClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		

		Scanner sc=new Scanner(System.in);
		String line=""; //$NON-NLS-1$
		while(!line.equals(".bye")){ //$NON-NLS-1$
			line=sc.nextLine();			
		}
	}

	public void sendMessage() {
		String msgToSend = textField.getText();

		if (tabSalons.getSelectedIndex() != -1) {
			String nomSalon = tabSalons.getTitleAt(tabSalons.getSelectedIndex());

			if (!nomSalon.equals("Salon général")) {
				msgToSend = IfClientServerProtocol.PRIVATE + nomSalon + "#" + msgToSend; 
			}
		}

		System.out.println("message to send " + msgToSend);
		sender.setMsgToSend(msgToSend);
	}

	public SimpleChatFrameClient() {
		// Sprint 1 --> JTree
		//this(null, new DefaultListModel<String>(), SimpleChatClientApp.defaultDocumentModel());
		this(null, new DefaultTreeModel(new DefaultMutableTreeNode(Messages.getString("SimpleChatFrameClient.libRacine"))), SimpleChatClientApp.getDefaultDocumentModel());
	}

	/**
	 * Create the frame.
	 */
	//public SimpleChatFrameClient(IfSenderModel sender, ListModel<String> clientListModel, Document documentModel) {
	public SimpleChatFrameClient(IfSenderModel sender, DefaultTreeModel clientTreeModel, Document documentModel) {
		super();
		this.sender=sender;
		this.sender.setFrameClient(this);
		//Sprint 1 modelListe --> modele JTree
		//this.listModel=clientListModel;
		this.treeModel = clientTreeModel;

		setTitle(Messages.getString("SimpleChatFrameClient.4")); //$NON-NLS-1$

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println(">>windowClosing(WindowEvent e)");
				actionDeconnexion();
			}
		});

		setBounds(100, 100, 661, 443);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(Messages.getString("SimpleChatFrameClient.5")); //$NON-NLS-1$
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenuItem mntmEnregistrerSous = new JMenuItem(Messages.getString("SimpleChatFrameClient.6")); //$NON-NLS-1$
		mnFile.add(mntmEnregistrerSous);
		JSeparator separator1 = new JSeparator();
		mnFile.add(separator1);
		JMenuItem mntmDeconnexion = new JMenuItem(Messages.getString("SimpleChatFrameClient.libDeconnexion")); //$NON-NLS-1$
		mntmDeconnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionDeconnexion();
			}
		});
		mnFile.add(mntmDeconnexion);

		JMenu mnAffichage = new JMenu(Messages.getString("SimpleChatFrameClient.14")); //$NON-NLS-1$
		mnAffichage.setMnemonic('A');
		menuBar.add(mnAffichage);

		JCheckBoxMenuItem chckbxmntmAfficherBarreOutils = new JCheckBoxMenuItem(actionAfficherBarreOutils);
		chckbxmntmAfficherBarreOutils.setSelected(this.isAfficherBarreOutils);
		mnAffichage.add(chckbxmntmAfficherBarreOutils);

		JMenu mnOutils = new JMenu(Messages.getString("SimpleChatFrameClient.7")); //$NON-NLS-1$
		mnOutils.setMnemonic('O');
		menuBar.add(mnOutils);

		JMenuItem mntmEnvoyer = new JMenuItem(Messages.getString("SimpleChatFrameClient.8")); //$NON-NLS-1$
		mntmEnvoyer.setAction(sendAction);
		mnOutils.add(mntmEnvoyer);

		JSeparator separator2 = new JSeparator();
		mnOutils.add(separator2);

		JCheckBoxMenuItem chckbxmntmNewCheckItem = new JCheckBoxMenuItem(lockAction);
		mnOutils.add(chckbxmntmNewCheckItem);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);

		// Sprint 1 : adapter JList en JTree 
		//		JList<String> list = new JList<String>(listModel);
		//		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//		list.addListSelectionListener(new ListSelectionListener() {
		//			public void valueChanged(ListSelectionEvent e) {
		//				int iFirstSelectedElement=((JList)e.getSource()).getSelectedIndex();
		//				if(iFirstSelectedElement>=0 && iFirstSelectedElement<listModel.getSize()){
		//					senderName=listModel.getElementAt(iFirstSelectedElement);
		//					getLblSender().setText(senderName);
		//				}
		//				else{
		//					getLblSender().setText("?"); //$NON-NLS-1$
		//				}
		//			}
		//		});
		//		list.setMinimumSize(new Dimension(100, 0));
		//		splitPane.setLeftComponent(list);

		treeModel.addTreeModelListener(new MyTreeModelListener());

		treeUtilisateur = new JTree(treeModel);		
		treeUtilisateur.setRootVisible(false);
		treeUtilisateur.setPreferredSize(new Dimension(100, 0));
		treeUtilisateur.setMinimumSize(new Dimension(100, 0));
		treeUtilisateur.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Enable tool tips.
		ToolTipManager.sharedInstance().registerComponent(treeUtilisateur);

		treeUtilisateur.setCellRenderer(new MyRenderer());

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
		treeUtilisateur.addMouseListener(new SimpleChatFrameClientController(this));


		splitPane.setLeftComponent(treeUtilisateur);

		tabSalons = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabSalons);

		JTextPane textArea = new JTextPane((StyledDocument)documentModel);
		//textArea.setEnabled(false);
		JScrollPane scrollPaneText=new JScrollPane(textArea);
		tabSalons.addTab("Salon général", null, scrollPaneText, null);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(textArea, popupMenu);

		JCheckBoxMenuItem chckbxmntmLock = new JCheckBoxMenuItem(Messages.getString("SimpleChatFrameClient.10")); //$NON-NLS-1$
		chckbxmntmLock.setEnabled(isScrollLocked);
		popupMenu.add(chckbxmntmLock);
		chckbxmntmLock.addActionListener(lockAction);

		scrollPaneText.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if(isScrollLocked){
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				}				
			}
		});
		//		JTree treeUtilisateur = new JTree();
		//		JScrollPane scrollPaneUtilisateur = new JScrollPane(treeUtilisateur);
		//		contentPane.add(scrollPaneUtilisateur, BorderLayout.WEST);
		//		scrollPaneUtilisateur.setVisible(false);
		//

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel_1.add(panel);

		lblSender = new JLabel("?"); //$NON-NLS-1$
		lblSender.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSender.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSender.setPreferredSize(new Dimension(100, 14));
		lblSender.setMinimumSize(new Dimension(100, 14));

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0),
				Messages.getString("SimpleChatFrameClient.12")); //$NON-NLS-1$
		textField.getActionMap().put(Messages.getString("SimpleChatFrameClient.13"), sendAction); //$NON-NLS-1$

		btnSend = new JButton(sendAction);
		btnSend.setMnemonic(KeyEvent.VK_ENTER);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(lblSender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGap(10)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
								.addComponent(lblSender, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
								.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				);
		panel.setLayout(gl_panel);

		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		JButton button = toolBar.add(sendAction);

		JButton btnDeconnexion = new JButton(Messages.getString("SimpleChatFrameClient.btnNewButton.text")); //$NON-NLS-1$
		btnDeconnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionDeconnexion();
			}
		});
		toolBar.add(btnDeconnexion);
	}

	public void ajouterOnglet(String titreOnglet) {
		JTextArea textArea = new JTextArea();
		textArea.setDocument(SimpleChatClientApp.getDefaultDocumentModel());
		if (isSalonExist(titreOnglet) == false) {			
			this.tabSalons.addTab(titreOnglet, null, new JScrollPane(textArea), null);			
			this.sender.ajouterModeleSalon(titreOnglet, (StyledDocument) textArea.getDocument());
			this.tabSalons.setSelectedIndex(this.tabSalons.getTabCount()-1);
		}						
	}

	/**
	 * Test existence onglet 
	 * @param titre de l'onglet
	 * @return true si l'onglet existe déjà dans tabSalons
	 */
	private boolean isSalonExist(String tabTitre) {
		for (int i = 0; i < this.tabSalons.getTabCount(); i++) {
			if (this.tabSalons.getTitleAt(i).equals(tabTitre)) {
				return true;
			}
		}

		return false;
	}

	public JLabel getLblSender() {
		return lblSender;
	}

	private abstract class ResourceAction extends AbstractAction {
		public ResourceAction() {
		}
	}

	private class SendAction extends ResourceAction{	
		private Icon getIcon(){
			return new ImageIcon(SimpleChatFrameClient.class.getResource("send_16_16.jpg")); //$NON-NLS-1$
		}
		public SendAction(){
			putValue(NAME, Messages.getString("SimpleChatFrameClient.3")); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("SimpleChatFrameClient.2")); //$NON-NLS-1$
			putValue(SMALL_ICON, getIcon());
		}
		public void actionPerformed(ActionEvent e) {
			sendMessage();
		}
	}

	private class LockAction extends ResourceAction{	
		public LockAction(){
			putValue(NAME, Messages.getString("SimpleChatFrameClient.1")); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("SimpleChatFrameClient.0")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			isScrollLocked=(!isScrollLocked);
		}
	}

	/**
	 * Action sur le menuitem "Afficher la barre d'outils
	 * @author Administrateur
	 *
	 */
	private class AfficherBarreOutilsAction extends ResourceAction{	
		public AfficherBarreOutilsAction(){
			putValue(NAME, Messages.getString("SimpleChatFrameClient.15")); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("SimpleChatFrameClient.16")); //$NON-NLS-1$
		}

		public void actionPerformed(ActionEvent e) {
			isAfficherBarreOutils = !isAfficherBarreOutils;
			afficherBarreOutils(isAfficherBarreOutils);
		}
	}


	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public void afficherBarreOutils(boolean afficher) {
		this.toolBar.setVisible(afficher);
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
			System.out.println("treeNodesInserted");
		}
		public void treeNodesRemoved(TreeModelEvent e) {
			System.out.println("treeNodesRemoved");
		}
		public void treeStructureChanged(TreeModelEvent e) {
			System.out.println("treeStructureChanged");
		}
	}

	public JTree getTreeUtilisateur() {
		return treeUtilisateur;
	}

	/**
	 * 
	 */
	public void actionDeconnexion() {
		try {
			sender.quitServer();
			this.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public JButton getBtnSend() {
		return btnSend;
	}

	public String fabriqueToolTip(Object value) {
		String result = "";
		String reqSelect;

		String loginUser;
		String password;
		String nom = "?";
		String prenom = "?";
		String cheminImg = "";

		// Récupération des infos du User
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		loginUser = (String)(node.getUserObject());

		reqSelect = "SELECT PSEUDO, NOM, PRENOM, CHEMIN_IMG, PASSWORD FROM UTILISATEURS WHERE PSEUDO = '" + loginUser + "';";

		SimpleChatDb db = new SimpleChatDb();
		db.OuvrirBase();
		ResultSet rsSelect = db.executeSelect(reqSelect);
		try {
			if (!rsSelect.next()) {
				//ResultSet Vide
			} else {
				nom = rsSelect.getString("NOM");
				prenom = rsSelect.getString("PRENOM");
				cheminImg = rsSelect.getString("CHEMIN_IMG");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.fermerBase();

		// Image test : http://fc04.deviantart.net/fs70/f/2010/102/b/8/Avatar_by_oohorusoo.png
		result = "<html><img src=\"" + getClass().getResource(PREFIXE_RELATIF + cheminImg) + "\" alt=\"Avatar\" /><UL><LI>Login : " + loginUser + "</LI><LI>Nom : " + nom + "</LI><LI>Prénom : " + prenom + "</LI></UL></html>";
		//System.out.println(">>result= " + result);

		return result;

	}

	/**
	 * Renderer perso pour le JTree
	 * @author Administrateur
	 *
	 */
	private class MyRenderer extends DefaultTreeCellRenderer {
		//Icon tutorialIcon;
		//User user;

		public MyRenderer() {
			super();
		}

		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean sel,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(
					tree, value, sel,
					expanded, leaf, row,
					hasFocus);
			if (leaf) {
				setToolTipText(fabriqueToolTip(value)); 
			} else {
				setToolTipText("<Noeud non-feuille>");
			}

			return this;
		}
	}

}
