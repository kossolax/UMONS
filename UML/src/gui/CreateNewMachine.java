package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import framework.Machine;
import framework.modules.*;
import framework.payement.*;
import framework.stockage.*;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.Color;

public final class CreateNewMachine extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JDialog frame;
	private JTextField textMachineName;
	private JCheckBox checkCafe, checkBonbon, checkBoisson, checkFreeze, checkFridge, checkStore, checkEau, checkBoiler, checkTokken, checkPiece, checkCard;
	private static Machine returnValue;


	public static Machine getNewMachine(JFrame parent) {
		returnValue = null;
		CreateNewMachine form = new CreateNewMachine(parent);
		form.setVisible(true);
		return returnValue;
	}
	private CreateNewMachine(JFrame parent) {
		super(parent);
		this.setLocationRelativeTo(parent);
		initialize();
	}
	
	public void checkBoxClicked(ActionEvent actionEvent) {
		JCheckBox source = (JCheckBox) actionEvent.getSource();
		boolean selected = source.isSelected();
		
		if( source == checkCafe ) {
	        checkEau.setSelected(selected);
	        checkBoiler.setSelected(selected);
	        checkStore.setSelected(selected);
	        
	        checkEau.setEnabled(!selected);
	        checkBoiler.setEnabled(!selected);
	        checkStore.setEnabled(!selected);
		}
		else if( (source == checkBonbon && !checkBoisson.isSelected()) || (source == checkBoisson && !checkBonbon.isSelected()) ) {
			checkFridge.setSelected(selected);
			checkFridge.setEnabled(!selected);
		}
	}
	public void createMachine() {
		String name = textMachineName.getText().trim();
		if( name.length() <= 3 ) {
			JOptionPane.showMessageDialog(frame, "Le nom de la machine doit faire plus de 3 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Machine machine = new Machine(textMachineName.getText());
		
		if( checkEau.isSelected() )
			machine.addModule( new Water(true) );
		if( checkBoiler.isSelected() )
			machine.addModule( new Boiler(true) );
		
		if( checkFreeze.isSelected() )
			machine.addModule( new Freeze(Integer.MAX_VALUE) );
		if( checkFridge.isSelected() )
			machine.addModule( new Cooling(Integer.MAX_VALUE) );
		if( checkStore.isSelected() )
			machine.addModule( new Classic(Integer.MAX_VALUE) );
		
		//if( checkPiece.isSelected() )
		//	machine.addModule( new aCoin.Coin(Integer.MAX_VALUE) );
		if( checkTokken.isSelected() )
			machine.addModule( new Token() );
		if( checkCard.isSelected() )
			machine.addModule( new Carte() );
		
		if( machine.countModule() == 0 ) {
			JOptionPane.showMessageDialog(frame, "Aucun module n'a été sélectionné", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		returnValue = machine;
		this.dispose();
	}
	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		this.setResizable(false);
		this.setModalityType(ModalityType.DOCUMENT_MODAL);
		this.setTitle("Cr\u00E9ation d'une nouvelle machine");
		this.setBounds(100, 100, 600, 400);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(340, 20, 244, 105);
		this.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNomDeLa = new JLabel("Nom de la machine:");
		lblNomDeLa.setBounds(10, 13, 160, 14);
		panel.add(lblNomDeLa);
		lblNomDeLa.setHorizontalAlignment(SwingConstants.LEFT);
		
		textMachineName = new JTextField();
		textMachineName.setBounds(10, 38, 224, 20);
		panel.add(textMachineName);
		textMachineName.setColumns(10);
		
		JPanel blockModule = new JPanel();
		blockModule.setBounds(10, 136, 210, 105);
		this.getContentPane().add(blockModule);
		blockModule.setLayout(null);
		
		JPanel blockBoiler = new JPanel();
		blockBoiler.setBounds(110, 25, 100, 80);
		blockModule.add(blockBoiler);
		blockBoiler.setLayout(null);
		
		checkBoiler = new JCheckBox("Chauffe eau");
		checkBoiler.setBounds(0, 57, 100, 23);
		blockBoiler.add(checkBoiler);
		
		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 100, 50);
		blockBoiler.add(label);
		label.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\boiler.png"));
		
		JPanel blockWater = new JPanel();
		blockWater.setBounds(0, 25, 100, 80);
		blockModule.add(blockWater);
		blockWater.setLayout(null);
		
		checkEau = new JCheckBox("Eau");
		checkEau.setBounds(0, 57, 100, 23);
		blockWater.add(checkEau);
		
		JLabel label_1 = new JLabel("");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(0, 0, 100, 50);
		blockWater.add(label_1);
		label_1.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\eau.png"));
		
		JLabel lblModuleComplmentaire = new JLabel("Module compl\u00E9mentaire");
		lblModuleComplmentaire.setBounds(10, 11, 141, 14);
		blockModule.add(lblModuleComplmentaire);
		
		JPanel BlockStorage = new JPanel();
		BlockStorage.setBounds(10, 252, 320, 105);
		this.getContentPane().add(BlockStorage);
		BlockStorage.setLayout(null);
		
		JPanel blockClassic = new JPanel();
		blockClassic.setBounds(220, 25, 100, 80);
		BlockStorage.add(blockClassic);
		blockClassic.setLayout(null);
		
		checkStore = new JCheckBox("Classique");
		checkStore.setBounds(0, 57, 100, 23);
		blockClassic.add(checkStore);
		
		JLabel label_4 = new JLabel("");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(0, 0, 100, 50);
		blockClassic.add(label_4);
		label_4.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\box.png"));
		
		JPanel blockFrigo = new JPanel();
		blockFrigo.setBounds(110, 25, 100, 80);
		BlockStorage.add(blockFrigo);
		blockFrigo.setLayout(null);
		
		checkFridge = new JCheckBox("Frigo");
		checkFridge.setBounds(0, 57, 100, 23);
		blockFrigo.add(checkFridge);
		
		JLabel label_3 = new JLabel("");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(0, 0, 100, 50);
		blockFrigo.add(label_3);
		label_3.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\frigot.png"));
		
		JPanel blockFreeze = new JPanel();
		blockFreeze.setBounds(0, 25, 100, 80);
		BlockStorage.add(blockFreeze);
		blockFreeze.setLayout(null);
		
		checkFreeze = new JCheckBox("Congelateur");
		checkFreeze.setBounds(0, 57, 100, 23);
		blockFreeze.add(checkFreeze);
		
		JLabel label_2 = new JLabel("");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(0, 0, 100, 50);
		blockFreeze.add(label_2);
		label_2.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\freeze.png"));
		
		JLabel lblStockage = new JLabel("Type de stockage");
		lblStockage.setBounds(10, 11, 216, 14);
		BlockStorage.add(lblStockage);
		
		JPanel blockPreset = new JPanel();
		blockPreset.setBounds(10, 20, 320, 105);
		this.getContentPane().add(blockPreset);
		blockPreset.setLayout(null);
		
		JPanel blockCafe = new JPanel();
		blockCafe.setBounds(0, 25, 100, 80);
		blockPreset.add(blockCafe);
		blockCafe.setLayout(null);
		
		
		checkCafe = new JCheckBox("Caf\u00E9");
		checkCafe.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent evt) { checkBoxClicked(evt); }});
		checkCafe.setBounds(0, 57, 100, 23);
		blockCafe.add(checkCafe);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 100, 50);
		blockCafe.add(lblNewLabel);
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\cafe.png"));
		
		JPanel blockBonbon = new JPanel();
		blockBonbon.setBounds(110, 25, 100, 80);
		blockPreset.add(blockBonbon);
		blockBonbon.setLayout(null);
		
		checkBonbon = new JCheckBox("Confiserie");
		checkBonbon.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent evt) { checkBoxClicked(evt); }});
		checkBonbon.setBounds(0, 57, 100, 23);
		blockBonbon.add(checkBonbon);
		
		JLabel label_5 = new JLabel("");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\bonbon.png"));
		label_5.setBounds(0, 0, 100, 50);
		blockBonbon.add(label_5);
		
		JPanel blockBoisson = new JPanel();
		blockBoisson.setBounds(220, 25, 100, 80);
		blockPreset.add(blockBoisson);
		blockBoisson.setLayout(null);
		
		checkBoisson = new JCheckBox("Boisson");
		checkBoisson.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent evt) { checkBoxClicked(evt); }});
		checkBoisson.setBounds(0, 57, 100, 23);
		blockBoisson.add(checkBoisson);
		
		JLabel label_6 = new JLabel("");
		label_6.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\soft.png"));
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setBounds(0, 0, 100, 50);
		blockBoisson.add(label_6);
		
		JLabel lblPrdfini = new JLabel("Pr\u00E9d\u00E9fini:");
		lblPrdfini.setBounds(10, 10, 192, 14);
		blockPreset.add(lblPrdfini);
		
		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { dispose(); }});
		btnAnnuler.setBackground(Color.RED);
		btnAnnuler.setBounds(495, 334, 89, 23);
		this.getContentPane().add(btnAnnuler);
		
		JButton btnValider = new JButton("Valider");
		btnValider.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { createMachine(); } });
		btnValider.setBackground(Color.GREEN);
		btnValider.setBounds(396, 334, 89, 23);
		this.getContentPane().add(btnValider);
		
		JPanel blockPaiement = new JPanel();
		blockPaiement.setLayout(null);
		blockPaiement.setBounds(264, 136, 320, 105);
		this.getContentPane().add(blockPaiement);
		
		JPanel blockCard = new JPanel();
		blockCard.setLayout(null);
		blockCard.setBounds(110, 25, 100, 80);
		blockPaiement.add(blockCard);
		
		
		checkCard = new JCheckBox("Carte");
		checkCard.setBounds(0, 57, 100, 23);
		blockCard.add(checkCard);
		
		JLabel label_7 = new JLabel("");
		label_7.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\card.png"));
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setBounds(0, 0, 100, 50);
		blockCard.add(label_7);
		
		JPanel blockCash = new JPanel();
		blockCash.setLayout(null);
		blockCash.setBounds(0, 25, 100, 80);
		blockPaiement.add(blockCash);
		
		
		checkPiece = new JCheckBox("Pi\u00E8ce");
		checkPiece.setBounds(0, 57, 100, 23);
		blockCash.add(checkPiece);
		
		JLabel label_8 = new JLabel("");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\coin.png"));
		label_8.setBounds(0, 0, 100, 50);
		blockCash.add(label_8);
		
		JLabel lblModuleDePaiement = new JLabel("Module de paiement");
		lblModuleDePaiement.setBounds(10, 11, 141, 14);
		blockPaiement.add(lblModuleDePaiement);
		
		JPanel blockTokken = new JPanel();
		blockTokken.setLayout(null);
		blockTokken.setBounds(220, 25, 100, 80);
		blockPaiement.add(blockTokken);
		
		checkTokken = new JCheckBox("Jeton");
		checkTokken.setBounds(0, 57, 100, 23);
		blockTokken.add(checkTokken);
		
		JLabel label_9 = new JLabel("");
		label_9.setIcon(new ImageIcon("C:\\Users\\KoSSoLaX\\Documents\\GitHub\\UMONS\\UML\\data\\img\\tokken.png"));
		label_9.setHorizontalAlignment(SwingConstants.CENTER);
		label_9.setBounds(0, 0, 100, 50);
		blockTokken.add(label_9);
	}
}
