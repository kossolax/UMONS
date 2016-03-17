package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import framework.Machine;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class InitWindow {

	private JFrame frame;
	private Vector<Machine>lstMachine;
	private JComboBox<Machine> list;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InitWindow window = new InitWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InitWindow() {
		lstMachine = new Vector<Machine>();
		initialize();
	}
	public void createNewMachine() {
		Machine machine = CreateNewMachine.getNewMachine(frame);
		if( machine != null )
			lstMachine.add(machine);
		
		list.setEnabled( lstMachine.size() > 0 );				
		list.updateUI();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("UMONS Framework Distributeur");
		frame.setBounds(100, 100, 450, 180);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		list = new JComboBox<Machine>(lstMachine);
		list.setEnabled(false);
		list.setBounds(19, 44, 158, 23);
		frame.getContentPane().add(list);
		
		JButton btnCharger = new JButton("Charger");
		btnCharger.setEnabled(false);
		btnCharger.setBounds(196, 44, 229, 23);
		frame.getContentPane().add(btnCharger);
		
		JButton btnNouveauDistributeur = new JButton("Nouveau distributeur");
		btnNouveauDistributeur.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent arg0) { createNewMachine(); } });
		btnNouveauDistributeur.setBounds(19, 98, 406, 23);
		frame.getContentPane().add(btnNouveauDistributeur);
	}

}
