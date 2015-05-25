package com;


import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;










import com.pineapplechina.Blast3goCell;
import com.pineapplechina.Blast3goCellReaderBatch;
import com.pineapplechina.Blastxml2q2giTable;
import com.pineapplechina.GOlevel2barViewer;
import com.pineapplechina.GOstatsGOenrich;

public class Blast3goGUI extends JFrame {
	
	// 静态变量，用于与子类沟通
	static String inputGIPath="";
	static String inputXMLPath="";
	static String inputXML2GIPath="";
	static String inputrawGOmapDB="";	
	static String inputGOmapDB="";

	static String inputDirectory=".";
//	static String outputDirectory=".";
	
	// 设置系列共享变量或者对象
//	static int MaxRow = 100000;
	TableModel model = new DefaultTableModel(Blast3goCell.colsNames, 0);
	JTable table= new JTable(model);
	
	Blast3goCellReaderBatch q2giReader;
	
	// 设置系列输出文件所在，方便重新读取制表
	static String outPutQuery2GOmap = "Query2GoMapResult.b3g";
	static String outPutGOstats = "Q2GOasGOstats.b3g";
	
	static String packageGoTerm = "packageGoterm.b3g";
	static String GoLevel2Count = "golevel2Count.b3g";
	static String GoLevel2graph = "golevel2Count.b3g.tif";
	static String originalinfo =" This is the alpha version. ";
	
	
	
//	static int preDBcomf;
	
	public Blast3goGUI(){
		// Rserve 只支持建立一个通道 不方便，此处抛出异常并在主面板中进行捕捉
		// 无奈之下，只能抛出异常，用shower处理吧
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		table.setAutoCreateRowSorter(true);
		JScrollPane tableJPane = new JScrollPane(table);
		this.add(tableJPane);
		// 设置菜单栏
		JMenuBar bar=new JMenuBar();
		this.setJMenuBar(bar);
		final JMenu file = new JMenu("File");
		bar.add(file);
		
		JMenuItem dbprepare = new JMenuItem("Prepare GI2go Map DB");
		file.add(dbprepare);
		JMenuItem dbsetting = new JMenuItem("Choose GI2go Map DB");
		file.add(dbsetting);
		JMenu setInput = new JMenu("Load Input File");
		file.add(setInput);

		
		JMenuItem loadq2gi = new JMenuItem("Load Query2GI Map File");
		setInput.add(loadq2gi);
		JMenuItem loadblastxml = new JMenuItem("Load Blastxml File");
		setInput.add(loadblastxml);
		//
//		loadblastxml.setEnabled(false);
		
//		JMenuItem setResult = new JMenuItem("Set Output Directory");
//		file.add(setResult);
		
		JMenu saveResult = new JMenu("Save As");
		file.add(saveResult);
//		JMenuItem saveDef = new JMenuItem("Defualt Save Mode");
//		saveResult.add(saveDef);
		JMenuItem saveTable = new JMenuItem("Save as Current Table");
		saveResult.add(saveTable);
		JMenuItem saveGOstats = new JMenuItem("Save as GOstats format");
		saveResult.add(saveGOstats);
		
		JMenuItem exitItem = new JMenuItem("Exit Blast3go");
		file.add(exitItem);
		
		final JMenu gomappingMenu = new JMenu("GO Mapper");
		bar.add(gomappingMenu);
		JMenuItem gomapping = new JMenuItem("Run GO mapping");
		gomappingMenu.add(gomapping);
		JMenuItem rmgomapping = new JMenuItem("Remove GO mapping");
		gomappingMenu.add(rmgomapping);
		rmgomapping.setEnabled(false);
		
		
		final JMenu keggmappingMenu = new JMenu("KEGG Mapper");
		keggmappingMenu.setEnabled(false);
		bar.add(keggmappingMenu);
		JMenuItem keggmapping = new JMenuItem("Run KEGG mapping");
		keggmappingMenu.add(keggmapping);
		keggmapping.setEnabled(false);
		JMenuItem rmkeggmapping = new JMenuItem("Remove KEGG Mapping");
		keggmappingMenu.add(rmkeggmapping);
		rmkeggmapping.setEnabled(false);
		
		final JMenu GandS = new JMenu ("Graphic && Static");
		bar.add(GandS);
//		JMenu invokeR = new JMenu ("Start/Close Rserve");
//		GandS.add(invokeR);
//		JMenuItem startRserve = new JMenuItem ("Start Rserve");
//		invokeR.add(startRserve);
//		JMenuItem closeRserve = new JMenuItem("Close Rserve");
//		invokeR.add(closeRserve);
		JMenu goStat = new JMenu ("GO Stater");
		GandS.add(goStat);
		JMenuItem goLevel2package = new JMenuItem ("GO Term Package");
		goStat.add(goLevel2package);
		JMenuItem goLevel2Graphic = new JMenuItem ("GO Level2 Term Graph");
		goStat.add(goLevel2Graphic);
		JMenuItem goErichMent = new JMenuItem("GOstats GO Erichment");
		goStat.add(goErichMent);
		
		JMenu keggStat = new JMenu ("Kegg Stater");
		GandS.add(keggStat);
		keggStat.setEnabled(false);
		
		JMenu about = new JMenu("About");
		bar.add(about);
		JMenuItem aboutBlast3go = new JMenuItem(" Why Blast3go ?");
		JMenuItem aboutQqGroup = new JMenuItem("Join in bioinformatics*中国");
		JMenuItem abourMe = new JMenuItem("Contact Me");
		about.add(aboutBlast3go);
		about.add(aboutQqGroup);
		about.add(abourMe);
		
		
		
		JLabel inFosperator = new JLabel(" || RUN STATUS:");
		bar.add(inFosperator);
		final JLabel waitInfo = new JLabel();
		
		waitInfo.setText(originalinfo);
		bar.add(waitInfo);
//		waitInfo.setVisible(false);
//		String[][] scells ={{"123","123123","",""},{"d2q3d2q3","fawfeaw","f224f4","dqw34f234"},{"","","",""},{"afeawe","ferf","",""}};
//		String[] columnuNames= {"Query ID","GeneBank ID","GO ID","KEGG ID"};

		dbprepare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser infilechooser = new JFileChooser();
				infilechooser.setCurrentDirectory(new File(inputDirectory));
				infilechooser.showOpenDialog(Blast3goGUI.this);
				inputrawGOmapDB = infilechooser.getSelectedFile().getPath();
				// 更新默认文件夹 
				inputDirectory = infilechooser.getSelectedFile().getParent();
				waitInfo.setText(" DB building May take about 60 minutes ... ");
				
				// 哈哈！！！
				// 将主进程隔离在外面，则可实现线程内运行，就不会出现黑屏了 -- 不过还是不知道为什么
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
							file.setEnabled(false);
							gomappingMenu.setEnabled(false);
							keggmappingMenu.setEnabled(false);
							GandS.setEnabled(false);
							
							if(JOptionPane.showConfirmDialog(Blast3goGUI.this, "Be sure to use '"+inputrawGOmapDB+"' to make a DB ? ",
									"Confirm Message", JOptionPane.YES_NO_CANCEL_OPTION) == 0){
								// 对数据库进行处理，并同时将完成的DB文件传出，方便后续操作
								// 没办法了，只能构建内部类
								
			//					class building_run implements Runnable{
			//						public void run() {
			//							// TODO Auto-generated method stub
			//							
			//						}						
			//					}
			//					building_run cur_run = new building_run();
			//					new Thread(cur_run).run();
								inputGOmapDB=prepareDB(inputrawGOmapDB);
			//					waitInfo.setText(" Pleaser wait for DB building...");
								waitInfo.setText(" Database building Finished! ");
								// 因为建库时间较长，还是增加弹窗
								
							}else {
								file.setEnabled(true);
								gomappingMenu.setEnabled(true);
								keggmappingMenu.setEnabled(true);
								GandS.setEnabled(true);
								waitInfo.setText(" Database building Canceled! ");
							};
							JOptionPane.showMessageDialog(Blast3goGUI.this, " Databases Preparation Finished and the Resultant DB file is place in \n"+inputGOmapDB, " Run Info ", JOptionPane.INFORMATION_MESSAGE);
							
							
							file.setEnabled(true);
							gomappingMenu.setEnabled(true);
							keggmappingMenu.setEnabled(true);
							GandS.setEnabled(true);
							System.out.println("Preparation Finished !\n");
						
						
					}
				}).start();

				
				// YES 0 
				// NO 1
				// CACEL 2	
				
				
//				System.out.println(preDBcomf);
				
			}
		});	
		
		dbsetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser infilechooser = new JFileChooser();
				infilechooser.setCurrentDirectory(new File(inputDirectory));
				infilechooser.showOpenDialog(Blast3goGUI.this);
				inputGOmapDB = infilechooser.getSelectedFile().getPath();
				waitInfo.setText(" Use '"+inputGOmapDB+"' as DataBase");
				// 更新默认文件夹 
				inputDirectory = infilechooser.getSelectedFile().getParent();
			}
		});	
	
		
		loadq2gi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser infilechooser = new JFileChooser();
				infilechooser.setCurrentDirectory(new File(inputDirectory));
				infilechooser.showOpenDialog(Blast3goGUI.this);
				inputGIPath = infilechooser.getSelectedFile().getPath();
				// 更新默认文件夹 
				inputDirectory = infilechooser.getSelectedFile().getParent();
//				System.out.println(inputGIPath);
				waitInfo.setText(" Loading Query2GI tables! ");
				q2giReader = new Blast3goCellReaderBatch(inputGIPath);
				q2giReader.Read();
				setTables(q2giReader.parseCellsTable());
				waitInfo.setText(" Query2GI tables Loaded ! ");
			}
		});	
		
		loadblastxml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final JFileChooser infilechooser = new JFileChooser();
				infilechooser.setCurrentDirectory(new File(inputDirectory));
				infilechooser.showOpenDialog(Blast3goGUI.this);
				inputXMLPath = infilechooser.getSelectedFile().getPath();
				if (new File(inputXMLPath).canRead()){
					file.setEnabled(false);
					gomappingMenu.setEnabled(false);
					keggmappingMenu.setEnabled(false);
					GandS.setEnabled(false);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							// 更新默认文件夹 
							inputDirectory = infilechooser.getSelectedFile().getParent();
							inputXML2GIPath = inputDirectory+File.separator+"xml2gi.table";
							waitInfo.setText(" Parsing BlastXML file! ");
							// 调用写好的静态方法，直接解析blastxml为gitable 实际也方便下次直接读取，无需再次解析
							Blastxml2q2giTable.xml2gitable(inputXMLPath, inputXML2GIPath);
							waitInfo.setText(" Loading Query2GI tables! ");
							q2giReader = new Blast3goCellReaderBatch(inputXML2GIPath);
							q2giReader.Read();
							setTables(q2giReader.parseCellsTable());
							
							file.setEnabled(true);
							gomappingMenu.setEnabled(true);
							keggmappingMenu.setEnabled(true);
							GandS.setEnabled(true);
							
							waitInfo.setText(" Query2GI tables Loaded ! ");
	
						}
					}).start();;
										
				}else{
					;
				}


			}
		});
		
		
		saveTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser infilechooser = new JFileChooser();
				infilechooser.setCurrentDirectory(new File(inputDirectory));
				infilechooser.showSaveDialog(Blast3goGUI.this);
				outPutQuery2GOmap = infilechooser.getSelectedFile().getPath();
				// 更新默认文件夹 
				q2giReader.printTable(outPutQuery2GOmap);
				waitInfo.setText(" Finished ! Outputfile is "+outPutQuery2GOmap);
			}
		});
		
		saveGOstats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser infilechooser = new JFileChooser();
				infilechooser.setCurrentDirectory(new File(inputDirectory));
				infilechooser.showSaveDialog(Blast3goGUI.this);
				outPutGOstats = infilechooser.getSelectedFile().getPath();
				// 更新默认文件夹 
				q2giReader.printGOstats(outPutGOstats);
				waitInfo.setText(" Finished ! Outputfile is "+outPutGOstats);
			}
		});		
		
		exitItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
		gomapping.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
					
				
				
				// 直接加一个判断,判断是否存在DB文件
				if (new File(inputGOmapDB).canRead()){
					
	
					
					// swing 使用Thread 是否 是开一个线程去运行，但实际上还是阻塞的，所以必须放在最后，实际则是把需要运行和运行后的处理放在新线程里

						new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								waitInfo.setText(" Contucting GO Mapping !");
								file.setEnabled(false);
								gomappingMenu.setEnabled(false);
								keggmappingMenu.setEnabled(false);
								GandS.setEnabled(false);
								
								
								query2go();

								waitInfo.setText(" GO Mapping Finished !");						
								setTables(q2giReader.parseCellsTable());
								
								file.setEnabled(true);
								gomappingMenu.setEnabled(true);
								keggmappingMenu.setEnabled(true);
								GandS.setEnabled(true);
								
								JOptionPane.showMessageDialog(Blast3goGUI.this, " GO mapping Finished \n", " Run Info ", JOptionPane.INFORMATION_MESSAGE);
								
							}
						}).start();

						// TODO Auto-generated catch block

				}else{
					JOptionPane.showMessageDialog(Blast3goGUI.this,"Please choose the Mapper Database","Error !",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		
//		startRserve.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String returnInfo = startRserve();
//				System.out.println(returnInfo);
//				if(returnInfo.equals("Yes")){
//					JOptionPane.showMessageDialog(Blast3goGUI.this,"Start Rserve: Success !","Finished !",JOptionPane.INFORMATION_MESSAGE);
//				}else{
//					JOptionPane.showMessageDialog(Blast3goGUI.this,"Start Rserve: Fail !","Error !",JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		});
//		
//		closeRserve.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				closeRserve();
//			}
//		});
		
		goLevel2package.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
							try {
								file.setEnabled(false);
								gomappingMenu.setEnabled(false);
								keggmappingMenu.setEnabled(false);
								GandS.setEnabled(false);
								
								HashMap<String, String> level2GO2defination = new HashMap<String, String>();
								HashMap<String, Integer> level2GOcounter = new HashMap<String, Integer>();


										File level2graph = File.createTempFile("level2grap", ".gostattable");
										level2graph.deleteOnExit();
										String tempFileName = level2graph.getPath();
										
										
										q2giReader.printGOstats(tempFileName);
										String packageGOterm = getStripts("/rscripts/packageGOterm.r","packageGOterm","r");
										
//								File level2pack = File.createTempFile("level2pack", ".goallframe");
										String tempFileName2 = inputDirectory+File.separator+packageGoTerm;
										waitInfo.setText(" Packaging GO Term !");	
										String packageCommand = "Rscript "+packageGOterm+" "+tempFileName+" "+tempFileName2;
										System.out.println(packageCommand);
										// 20150318 尼玛，还没打包
										
										
										Process p = Runtime.getRuntime().exec(packageCommand);
										p.waitFor();
										
										if (p.exitValue()==0){
											waitInfo.setText(" Counting GO level2 Term basing on B3G_DB!");
											
//									waitInfo.setText(" Stating GO Term !");
											BufferedReader br = new BufferedReader(new InputStreamReader(Blast3goGUI.class.getClass().getResourceAsStream("/rscripts/GOtermLevel2.txt")));
											String inline;
											while((inline = br.readLine())!=null){
												level2GO2defination.put(inline.split("\\t")[0], inline.split("\\t", 2)[1]);
											}
											
//						for (String ss:level2GO2defination.keySet()){
//							System.out.print("key:"+ss+"value:"+level2GO2defination.get(ss)+"\n");
//						}
											int count;
											BufferedReader gostatPipe = new BufferedReader(new FileReader(tempFileName2));
											while((inline = gostatPipe.readLine())!=null){
												String cur_go=inline.split("\t")[0];
												if(level2GO2defination.containsKey(cur_go)){
													String cur_def = level2GO2defination.get(cur_go);
													if (level2GOcounter.containsKey(cur_def)){
														
														count = level2GOcounter.get(cur_def)+1;
														level2GOcounter.put(cur_def,count);
//										System.out.println(" curdef:"+cur_def+" count: "+count);
													}else{
														level2GOcounter.put(cur_def,1);
													}
												}
											}
											gostatPipe.close();
										
//						for (String ss:level2GOcounter.keySet()){
//							System.out.print("key: "+ss+" value: "+level2GOcounter.get(ss)+"\n");
//						}

//										File level2count = File.createTempFile("level2count", ".stat");
//										level2count.deleteOnExit();
//						String tempFilecount = level2count.getPath();
										PrintWriter countPipe = new PrintWriter(inputDirectory+File.separator+GoLevel2Count);
										for (String ss:level2GOcounter.keySet()){
											countPipe.print(ss+"\t"+level2GOcounter.get(ss)+"\n");
										}				
										countPipe.flush();
										countPipe.close();


									}else{
										waitInfo.setText(" GO Term Packing : FAIL !");
										JOptionPane.showMessageDialog(Blast3goGUI.this,"GO Term Packing : FAIL ! \n It may due to that you havan't \n biocLite(\"GO.db\")","Error !",JOptionPane.ERROR_MESSAGE);
										System.out.println("Package FAIL");
										
									}
							} catch (HeadlessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								GandS.setEnabled(true);	
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							file.setEnabled(true);
							gomappingMenu.setEnabled(true);
							keggmappingMenu.setEnabled(true);
							GandS.setEnabled(true);	
					}
				}).start();

			}
			
		});
		
		
		goLevel2Graphic.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				inputDirectory+File.separator+GoLevel2Count
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
							try {
								
								file.setEnabled(false);
								gomappingMenu.setEnabled(false);
								keggmappingMenu.setEnabled(false);
								GandS.setEnabled(false);	
								
								String graphlevel2rsrcipt = getStripts("/rscripts/graphicGoLevel2.r", "level2grapher", "r");
			//				GoLevel2graph
								
								File pngfile = File.createTempFile("pngScaled", ".png");
								
//								String level2graphCommand = "Rscript "+graphlevel2rsrcipt+" "
//										+inputDirectory+File.separator+GoLevel2Count+" "
//										+inputDirectory+File.separator+GoLevel2graph+" "+pngfile.getAbsolutePath(); 
//								System.out.println(level2graphCommand);
//								Process p = Runtime.getRuntime().exec(level2graphCommand);
//								p.waitFor();
//								
//								if (p.exitValue()==0){
//								waitInfo.setText("Graph Finished!...placed in inputfileDirectory");
									
									
								if(pngfile.canRead()){
									
									GOlevel2barViewer dialog  = new GOlevel2barViewer(
											Blast3goGUI.this,
											graphlevel2rsrcipt,
											inputDirectory+File.separator+GoLevel2Count,
											inputDirectory+File.separator+GoLevel2graph,
											pngfile.getAbsolutePath());
									dialog.setVisible(true);
											
								}else{
									System.out.println("Fail or Not found tiff");
								}
										
										
			
								
								
//								}else{
//									waitInfo.setText("Graph Failed!...placed in inputfileDirectory");
//									JOptionPane.showMessageDialog(Blast3goGUI.this,"GO Term Graphing : FAIL ! \n It may due to that you havan't \n install.package(\"ggplot2\")","Error !",JOptionPane.ERROR_MESSAGE);
//								}
								
								
								
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							file.setEnabled(true);
							gomappingMenu.setEnabled(true);
							keggmappingMenu.setEnabled(true);
							GandS.setEnabled(true);						
						
					}
				}).start();
				
			}
		});
		
		goErichMent.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
							file.setEnabled(false);
							gomappingMenu.setEnabled(false);
							keggmappingMenu.setEnabled(false);
							GandS.setEnabled(false);	
							String graphlevel2rsrcipt = getStripts("/rscripts/GOstatsGOenrich.r", "GOstatErich", "r");
				//				GoLevel2graph
							GOstatsGOenrich dialog  = new GOstatsGOenrich(
									Blast3goGUI.this,
									q2giReader,
									graphlevel2rsrcipt,
									inputDirectory
									);
							dialog.setVisible(true);
							file.setEnabled(true);
							gomappingMenu.setEnabled(true);
							keggmappingMenu.setEnabled(true);
							GandS.setEnabled(true);							
							
					}
				}).start();
				

			}
		});
		
		
		//about menuitem
		aboutBlast3go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(Blast3goGUI.this," For Fun.\n将通过调用R，模仿并实现blast2go大部分功能\n只是希望通过做一点对群友可能有用的东西\n来完成java的入门训练\n有疑问有BUG随时加入我们的QQ群联系", "关于Blast3go", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		aboutQqGroup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(Blast3goGUI.this," 加入我们的QQ群：bioinformatics*中国   \n QQ群号:[276151571]\n 学生多，学术氛围浓厚，无辈分隔阂，只谈学习 ", "关于我们的QQ群", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		abourMe.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(Blast3goGUI.this,"CJchen from South China Agricultural University \n My Blog: www.pineapplechina.com ", "about me", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		//
		
		
		
		java.net.URL imgURL = FastaExtracterGUI.class.getResource("/images/snap.jpg");
		ImageIcon img1 = new ImageIcon(imgURL);
		this.setIconImage(img1.getImage());
	}
	
	
	// 完全替换整个表格的内容
	public void setTables (Object[][] objects){
		table.setModel(new DefaultTableModel(objects,Blast3goCell.colsNames));
	}
	
	public String prepareDB(String inputFile){
		//Scanner rawDBscanner;
		String outputFile="NullDB";
		try {
			//rawDBscanner = new Scanner(new File(inputFile));
			BufferedReader br;
			if (inputFile.endsWith("gz")){
				br=new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(new File(inputFile)))));
			}else{
				br=new BufferedReader(new FileReader(inputFile));				
			}
			outputFile = inputFile + ".DB";
			PrintWriter outPipe = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
			//		outPipe.
			Pattern giPattern = Pattern.compile("\\d+");
			Pattern GOPattern = Pattern.compile("GO");
			//while (rawDBscanner.hasNext()) {
				//String[] rawdata = rawDBscanner.nextLine().trim().split("\\t");
			String[] rawdata;
			String newLine;
			while ((newLine=br.readLine())!=null) {
				rawdata = newLine.trim().split("\\t");
				String gi = rawdata[4];
				String GOs = rawdata[7];

				if(giPattern.matcher(gi).find() && GOPattern.matcher(GOs).find()){
//					System.out.println("G22G22");					
					for (String s:gi.split(";\\s")){
						outPipe.println(s+"\t"+GOs);
//						System.out.println("GIGI");
					}	
				}
			}
			// 可能没flush一次会非常耗费时间
			outPipe.flush();
			outPipe.close();
			br.close();

		} catch (Exception e) {
			// TODO: handle exception
			;
		}
		return outputFile;
	}	
	
	public void query2go (){

		ArrayList<Blast3goCell> newAllCells = q2giReader.getAllCells();
		HashMap<String,ArrayList<String>> gi2query = new HashMap<String,ArrayList<String>>();
		for (Iterator<Blast3goCell> iterator = newAllCells.iterator(); iterator.hasNext();) {
			Blast3goCell rec = iterator.next();
			String query = rec.GetQuertId();
			String GIs = rec.GetGInum();
			for (String gg:GIs.split("\\s")){
				if(gi2query.containsKey(gg)){
					ArrayList<String>  addGI = gi2query.get(gg);
//					System.out.println("--"+gg);
//					System.out.println(gi2query.get(gg));
					addGI.add(query);
					gi2query.put(gg,addGI);
//					System.out.println(gi2query.get(gg));
				}else{
					ArrayList<String> newQuery = new ArrayList<String>();
					newQuery.add(query);
					gi2query.put(gg, newQuery);
				}
			}
		}
		
//		for(String aa:gi2query.keySet()){
////			for (Iterator<String> iterator = aa.iterator(); iterator.hasNext();) {
//				System.out.println("=="+aa);
////			}
//		};
		HashMap<String,ArrayList<String>> query2GOs = new HashMap<String,ArrayList<String>>();
		

			// 更新输出文件路径
//			outPutQuery2GOmap = outputDirectory+File.separator+"Query2GoMapResult.b3g";
//			PrintWriter outGOmapresult = new PrintWriter(outputDirectory+File.separator+"Query2GoMapResult.b3g");
//			JOptionPane.showMessageDialog(Blast3goGUI.this,"CJchen from South China Agricultural University \n My Blog: www.pineapplechina.com ", "about me", JOptionPane.INFORMATION_MESSAGE);
			try {
				BufferedReader gi2goPipe = new BufferedReader(new FileReader(new File(inputGOmapDB)));
				String cur;
				
				while((cur=gi2goPipe.readLine())!=null){
					String gi = cur.split("\\t")[0];
					String GOs = cur.split("\\t")[1];
//				String[] GOs = cur.split(";\\s");
//				System.out.println(gi);
					if (gi2query.containsKey(gi)){
						ArrayList<String> allquery = gi2query.get(gi);
						for (Iterator<String> iterator = allquery.iterator(); iterator.hasNext();) {
							String curQuery = iterator.next();
//						System.out.println(curQuery+"\t"+GOs);
							if(query2GOs.containsKey(curQuery)){
								ArrayList<String>  addGOs = query2GOs.get(curQuery);
//							System.out.println("--"+gg);
//							System.out.println(gi2query.get(gg));
//							System.out.println(curQuery);
//							System.out.println(query2GOs.get(curQuery));
								addGOs.add(GOs);
								query2GOs.put(curQuery,addGOs);
//							System.out.println(query2GOs.get(curQuery));
							}else{
								ArrayList<String> newGOs = new ArrayList<String>();
								newGOs.add(GOs);
								query2GOs.put(curQuery, newGOs);
							}
						}
					}else{
						;
					}
				}
				gi2goPipe.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		// 如果可能尽量清理，方便java 回收内存
//		gi2query.clear();
		
		// 声明一个hashmap用于存储去冗余后的query2go，同时逐步清楚query2go
		HashMap<String, String> Q2GO = new HashMap<String, String>();

		try {
//			outPutQuery2GOmap = outputDirectory+File.separator+"Query2GoMapResult.b3g";
//			PrintWriter outGOmapresult = new PrintWriter(outputDirectory+File.separator+"Query2GoMapResult.b3g");
//		query2GOs
			for(String aa:query2GOs.keySet()){
//				System.out.println(aa);
				ArrayList<String> through = query2GOs.get(aa);
				HashSet<String> rmRedandency = new HashSet<String>();
				for (Iterator<String> iterator = through.iterator(); iterator.hasNext();) {
//					System.out.println(aa);
					for (String ss:iterator.next().split(";\\s")){
						rmRedandency.add(ss);	
					}
				}
				System.out.println(aa+rmRedandency);
				String modGO = rmRedandency.toString();
//				query2GOs.remove(aa);
				Q2GO.put(aa,modGO);
			}
//			outGOmapresult.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Iterator<Blast3goCell> iterator = newAllCells.iterator(); iterator.hasNext();) {
			Blast3goCell rec = iterator.next();
			String query = rec.GetQuertId();
//			String GIs = rec.GetGInum();
//			String GOs = rec.GetGOnum();
			if (Q2GO.containsKey(query)){
				rec.setGOnum(Q2GO.get(query));				
			}
		}
		
	}
	
	public String getStripts (String jarRelativePath2script,String tempfilePrefix,String tempfileSuffix){ 
		String cur_script=null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(Blast3goGUI.class.getClass().getResourceAsStream(jarRelativePath2script)));


				  // 使用creatrTempFile()可用于建立temp文件
//				  String tempFilename = tempfilePrefix; // 不知道-1是否可用
				  File tempFile = File.createTempFile(tempfilePrefix, "."+tempfileSuffix); 
				
				  							// 通过此设定，会在 程序退出的时候，同时清理掉中间文件
				  tempFile.deleteOnExit();
				
								PrintWriter temppipe = new PrintWriter(tempFile);
							  String inline;
								  while((inline=br.readLine())!=null){
									  // 增加回车行，这样同时避免了一些有的没的的
									  temppipe.println(inline);
								  }
								  temppipe.close();

							  br.close();
							  cur_script = tempFile.getAbsolutePath();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(cur_script);
		return cur_script;
	}
	
}
//	public String startRserve() {
//	      
//		  BufferedReader br = new BufferedReader(new InputStreamReader(Blast3goGUI.class.getClass().getResourceAsStream("/rscripts/StartRserve.r")));
//		  Process p;
//		  String startRresult="";
//		  try {
//			  				// 使用creatrTempFile()可用于建立temp文件
//			File tempFile = File.createTempFile("StartRservetemp", ".r"); 
////	    		  new File("temp_StartRserve.r");
//							// 通过此设定，会在 程序退出的时候，同时清理掉中间文件
//			tempFile.deleteOnExit();
//						PrintWriter temppipe = new PrintWriter(tempFile);
//						  String inline;
//						  while((inline=br.readLine())!=null){
//							  // 增加回车行，这样同时避免了一些有的没的的
//							  temppipe.println(inline);
//						  }
//						  temppipe.close();
//						  br.close();
//						  String startRserve="Rscript "+tempFile.getAbsolutePath();
////  			  .replaceFirst("^/","");
//
//						  System.out.println(startRserve);
//						  
//						  
//						  p = Runtime.getRuntime().exec(startRserve);
////				  BufferedReader inBr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//						  p.waitFor();
////						  tempFile.delete();
//						  if (p.exitValue()==0){
//							  startRresult= "Yes";
//						  }else{
//							  System.out.println("Start Rscript Error");
//							  startRresult= "NO";
//						  }
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return startRresult;	  
////		      while(()!=null){
////				  rserveInfo=rserveInfo+line;
////				  System.out.println(line+"ooooooooooooo");
////			  }
////				  rserveInfo = rserveInfo+inBr.readLine();
////				  rserveInfo = rserveInfo+inBr.readLine();
//
//
//	}
//	
//	public void closeRserve (){
//		  try {
//			RConnection c = new RConnection();
//			  REXP x = c.eval("R.version.string");
//			  System.out.println(x.asString());
//			  c.shutdown();
//		} catch (RserveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (REXPMismatchException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	


