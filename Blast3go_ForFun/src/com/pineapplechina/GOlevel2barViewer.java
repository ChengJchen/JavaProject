package com.pineapplechina;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.Blast3goGUI;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GOlevel2barViewer extends JDialog{
	
	static String rScriptStatic;
	static String dataStatic;
	static String realGraphStatic;
	static String path2graphStatic;
	
	public GOlevel2barViewer(Blast3goGUI owner,String rScript,String data,String realGraph,String path2graph){
		super(owner,"Blast3go Viewer",true);
		
		rScriptStatic=rScript;
		dataStatic=data;
		realGraphStatic=realGraph;
		path2graphStatic=path2graph;
		
		Toolkit kit  = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		
//		setSize(screenWidth/2,screenHeight/2);
		
		setPreferredSize(new Dimension((int) (screenWidth/1.2),(int) (screenHeight/1.5)));
//		setPreferredSize(new Dimension(1000,600));		
		setLayout(new GridLayout(1, 2));
		
		JPanel rInterface = new JPanel();
		rInterface.setLayout(new BorderLayout());
		add(rInterface);
		JLabel infolabel = new JLabel("Rscript to Run");
		rInterface.add(infolabel,BorderLayout.NORTH);
		String inline=null;
		String allline="";	
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(rScript));

			while((inline = br.readLine())!=null){
				allline = allline+inline+"\n";
			}
			br.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		final JTextArea rText = new JTextArea(allline);
		rText.setLineWrap(true);
		JScrollPane textScroll = new JScrollPane(rText);

		
		rInterface.add(textScroll,BorderLayout.CENTER);
		
		JPanel controlButtonPanel = new JPanel();
		rInterface.add(controlButtonPanel,BorderLayout.SOUTH);
		
		
		controlButtonPanel.setLayout(new GridLayout(1, 2));
		JButton updateButton = new JButton("Update Image");
		controlButtonPanel.add(updateButton);
		JButton closeButton = new JButton("Done and Exit");
		controlButtonPanel.add(closeButton);
		
		
		
		JPanel rGraph = new JPanel();
		add(rGraph);
		rGraph.setLayout(new BorderLayout());
		JLabel infolabel2 = new JLabel(" png 示意图  || tiff 300dpi图在输入文件夹");
		rGraph.add(infolabel2,BorderLayout.NORTH);
		
		final JLabel imageLabel = new JLabel();
		rGraph.add(imageLabel,BorderLayout.CENTER);
		

		
		
		updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					
					File tempScript = File.createTempFile("getTextScripts", ".r");
					tempScript.deleteOnExit();
					PrintWriter newScripts = new PrintWriter(tempScript);
					String modScript = rText.getText();
					newScripts.println(modScript);
					System.out.println(modScript);
					newScripts.close();
					
					
					String level2graphCommand = "Rscript "+tempScript.getAbsolutePath()+" "+dataStatic+" "+realGraphStatic+" "+path2graphStatic; 
					System.out.println(level2graphCommand);
					System.out.println(level2graphCommand);
					Process p = Runtime.getRuntime().exec(level2graphCommand);
					p.waitFor();

					if (p.exitValue()==0){
						
						
						ImageIcon icon = new ImageIcon(path2graphStatic);
						icon = new ImageIcon(icon.getImage().getScaledInstance(500,-1,Image.SCALE_DEFAULT));
						imageLabel.setIcon(icon);
						repaint();				
					}else{
						JOptionPane.showMessageDialog(GOlevel2barViewer.this,"You may havn't install ggplot2 \nor set 'Rscript' Enviromenta Virable\nor set input file","Error !",JOptionPane.ERROR_MESSAGE);
						System.out.println("fail");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}
		});
		pack();
	}
}
