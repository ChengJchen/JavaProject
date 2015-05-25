package com;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class FastaExtracterGUIshower {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				// Statements here
					JFrame frame = new FastaExtracterGUI();
					frame.setTitle("my way");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					// set prefered sized
					Toolkit kit  = Toolkit.getDefaultToolkit();
					Dimension screenSize = kit.getScreenSize();
					int screenHeight = screenSize.height;
					int screenWidth = screenSize.width;
					
					frame.setSize(screenWidth/4,screenHeight/8);
					frame.setLocationByPlatform(true);
				
					java.net.URL imgURL = FastaExtracterGUI.class.getResource("/images/snap.jpg");
					ImageIcon img1 = new ImageIcon(imgURL);
					frame.setIconImage(img1.getImage());
					
					frame.setVisible(true);
				// Statements end
				
			}
		});
	}

}
