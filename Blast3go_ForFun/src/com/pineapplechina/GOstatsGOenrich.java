package com.pineapplechina;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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

public class GOstatsGOenrich extends JDialog{
	
	static String rScriptStatic;
	static String outputPath;

	
	static String testData="";
	
	public GOstatsGOenrich(Blast3goGUI owner,Blast3goCellReaderBatch b3go,String rScript,String outfilePath){
		super(owner,"Blast3go Viewer",true);
		
		final Blast3goCellReaderBatch blast3go = b3go;
		rScriptStatic=rScript;
		outputPath=outfilePath;

		
		Toolkit kit  = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setPreferredSize(new Dimension((int) (screenWidth/1.2),(int) (screenHeight/1.5)));
		setLayout(new GridLayout(1, 2));
		setLocationByPlatform(true);
		JPanel rInterface = new JPanel();
		rInterface.setLayout(new BorderLayout());
		add(rInterface);
		final JLabel infolabel = new JLabel("Rscript to Run || Run Info: This is an alphal version.");
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
		final JButton updateButton = new JButton("Do GOstats GO enrichment");
		controlButtonPanel.add(updateButton);
		final JButton closeButton = new JButton("Done and Exit");
		controlButtonPanel.add(closeButton);
		

		JPanel rTestSet = new JPanel();
		add(rTestSet);
		rTestSet.setLayout(new BorderLayout());
		JLabel infolabel2 = new JLabel(" 请在下方输入(ctrl+v)需富集的Query id集合(每行1个)或者选择输入文件 ");
		rTestSet.add(infolabel2,BorderLayout.NORTH);
		
		final JTextArea rTextData = new JTextArea();
		rTextData.setLineWrap(true);
		JScrollPane textdataScroll = new JScrollPane(rTextData);
		rTestSet.add(textdataScroll,BorderLayout.CENTER);
		
		final JButton chooseTextData = new JButton("Choose Erichment Test DataSet");
		rTestSet.add(chooseTextData,BorderLayout.SOUTH);
		
		chooseTextData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					JFileChooser testSetFileChooser = new JFileChooser();
					// 设定为原始输入所在路径
					testSetFileChooser.setCurrentDirectory(new File(outputPath));
					testSetFileChooser.showOpenDialog(GOstatsGOenrich.this);
					
					String inputTextDataFilePath = testSetFileChooser.getSelectedFile().getPath();
					// 更新默认文件夹 
					BufferedReader br = new BufferedReader(new FileReader(inputTextDataFilePath));
					String inline = "";
					testData="";
					while((inline = br.readLine())!=null){
						testData = testData+inline+"\n";
					}
					rTextData.setText(testData);
					br.close();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
								
			}
		});

		
		
		updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
					new Thread(new Runnable() {
						
						@Override
						public void run() {
								
								try {
									updateButton.setEnabled(false);
									closeButton.setEnabled(false);
									chooseTextData.setEnabled(false);
									infolabel.setText("Rscript to Run || Run Info: Preparing Files。。。");
									

									// 读取更新了的R脚本
									File tempScript = File.createTempFile("getTextScripts", ".r");
									PrintWriter newScripts = new PrintWriter(tempScript);
									String modScript = rText.getText();
									newScripts.println(modScript);
									System.out.println(modScript);
									newScripts.close();
									
									//制作GOstats格式。以用于输入
									File GOstatTemp = File.createTempFile("GOstatTemp", "format");
									GOstatTemp.deleteOnExit();
									blast3go.printGOstats(GOstatTemp.getAbsolutePath());
									//读取导入表格中的所有基因，作为最大的universe
									File UniverseTemp = File.createTempFile("UniverseTemp", "format");
									UniverseTemp.deleteOnExit();
									PrintWriter outTablePipe = new PrintWriter(UniverseTemp);
									String[][] outTables = blast3go.parseCellsTable();
									for(int i=0;i<blast3go.getRowsNum();i++){
										outTablePipe.print(outTables[i][0]+"\n");
									}
									outTablePipe.flush();
									outTablePipe.close();
									
									//从右框textArea中读取差异基因集合
									File testDataTemp = File.createTempFile("TestDataTemp", "format");
									testDataTemp.deleteOnExit();
									PrintWriter outTestPipe = new PrintWriter(testDataTemp);
									outTestPipe.print(rTextData.getText());
									outTestPipe.flush();
									outTestPipe.close();
									
									infolabel.setText("Rscript to Run || Run Info: Doing GO Enrichment...May take 15 minutes");
									
									String GOenrichCommand = "Rscript "+tempScript.getAbsolutePath()+" "+GOstatTemp.getAbsolutePath()+" "+UniverseTemp.getAbsolutePath()+" "+testDataTemp.getAbsolutePath()+" "+outputPath; 
									System.out.println(GOenrichCommand);
									Process p = Runtime.getRuntime().exec(GOenrichCommand);
									p.waitFor();

									if (p.exitValue()==0){
										JOptionPane.showMessageDialog(GOstatsGOenrich.this," GOstats GO erichment Finished the outfile is placed \nin the same directory with input table", "Done", JOptionPane.INFORMATION_MESSAGE);
										infolabel.setText("Rscript to Run || Run Info: GO Enrichment DONE !!");
										System.out.println("GO Enrichment Finished");
//						ImageIcon icon = new ImageIcon(path2graphStatic);
//						icon = new ImageIcon(icon.getImage().getScaledInstance(500,-1,Image.SCALE_DEFAULT));
//						imageLabel.setIcon(icon);
										repaint();				
									}else{
										infolabel.setText("Rscript to Run || Run Info: GO Enrichment FAIL !!");
										JOptionPane.showMessageDialog(GOstatsGOenrich.this,"You may havn't install some R package \nor set 'Rscript' Enviromenta Virable\nor set input file","Error !",JOptionPane.ERROR_MESSAGE);
										System.out.println("fail");
									}		

									updateButton.setEnabled(true);
									closeButton.setEnabled(true);
									chooseTextData.setEnabled(true);
									
								} catch (HeadlessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
						}
					}).start();


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
