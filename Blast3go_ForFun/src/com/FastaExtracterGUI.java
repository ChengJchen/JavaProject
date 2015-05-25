package com;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.GridLayout;



import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



public class FastaExtracterGUI extends JFrame {
	
	public FastaExtracterGUI () { 
//		this.setLayout(new GridLayout(1,2));
		GridBagLayout cjGridBag = new GridBagLayout();
		this.setLayout(cjGridBag);
		
		JLabel fastafile = new JLabel("Fasta file:");
		this.add(fastafile);
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		
		cons.gridwidth=0;
		cons.gridheight=1;
		cons.weightx=1;
		cons.weighty=1;
		cjGridBag.setConstraints(fastafile,cons);
				
		
		JLabel idlist = new JLabel("IdList file:");
		this.add(idlist);
		cons.gridwidth=1;
		cons.gridheight=1;
		cons.weightx=1;
		cons.weighty=1;
		cjGridBag.setConstraints(idlist,cons);		
		
		JTextField inputtext = new JTextField();
		this.add(inputtext);
		cons.gridwidth=0;
		cons.gridheight=2;
		cons.weightx=1;
		cons.weighty=1;
		cjGridBag.setConstraints(inputtext,cons);	
		
//		java.net.URL imgURL = FastaExtracterGUI.class.getResource("/images/snap.jpg");
//		ImageIcon img1 = new ImageIcon(imgURL);
//		this.setIconImage(img1.getImage());
		
//		this.pack();
	}
		
}
