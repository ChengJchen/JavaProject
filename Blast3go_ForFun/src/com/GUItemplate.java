package com;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GUItemplate {

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
					
					frame.setSize(screenWidth/2,screenHeight/2);
					frame.setLocationByPlatform(true);
					
					// set frame icon
					Image img = new ImageIcon("icon.gif").getImage();
					frame.setIconImage(img);
					
					
					frame.setVisible(true);
				// Statements end
				
			}
		});
	}

}
