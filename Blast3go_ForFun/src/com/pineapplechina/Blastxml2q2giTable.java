package com.pineapplechina;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class Blastxml2q2giTable {

	// 想来想去，我觉得还是写一系列的静态方法，用于blastxml的格式转换，方便以后筛选
	public static void xml2gitable (String inputfile, String outputfile){
		
			Pattern pattern = Pattern.compile("gi\\|\\d+");
			try {
				//设定输出
				PrintWriter outPipe = new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));
				// 设定输入
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(inputfile)));
				XMLInputFactory factory = XMLInputFactory.newInstance();
				XMLStreamReader parser = factory.createXMLStreamReader(in);
				outPipe.print("ParsedFromXML\tCurFileName:xml2gi.table");
				boolean flag=false;
				while(parser.hasNext()){
					int event = parser.next();
					if (event ==XMLStreamConstants.START_ELEMENT){
						String localname=parser.getLocalName();
						
						if (localname.equals("Iteration_query-def")){
							flag=true;
							outPipe.println("");
						}
						
//						if (localname.equals("Hit_id")){
//							flag=true;
					// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						// 这个解析不到后面，可能是因为xml中使用了引用，但是能直接解析到物种信息，哈哈以后改写
//						}else if(localname.equals("Hit_def")){
//							flag=true;
//							System.out.println("aaa");
//					}
						
					}else if(event ==XMLStreamConstants.CHARACTERS){
							String input = parser.getText();
							if (flag) {
//								System.out.println(input);
								outPipe.print(input+"\t");
								flag=false;
							}
							Matcher matcher = pattern.matcher(input);
							while(matcher.find()){
								// 此处加3 或许以后要看编码格式     
								outPipe.print(input.substring(matcher.start()+3, matcher.end())+" ");
							}
					}

				}
				outPipe.println("");
				outPipe.flush();
				outPipe.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	}
//	public static void main (String[] args){
//		String infile = "C:\\Users\\ccj\\Desktop\\sample.xml";
//		xml2gitable(infile,"oo");
//	//	String outfile = "outfile";
//	}
	
}
	


