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

	// ������ȥ���Ҿ��û���дһϵ�еľ�̬����������blastxml�ĸ�ʽת���������Ժ�ɸѡ
	public static void xml2gitable (String inputfile, String outputfile){
		
			Pattern pattern = Pattern.compile("gi\\|\\d+");
			try {
				//�趨���
				PrintWriter outPipe = new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));
				// �趨����
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
						// ��������������棬��������Ϊxml��ʹ�������ã�������ֱ�ӽ�����������Ϣ�������Ժ��д
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
								// �˴���3 �����Ժ�Ҫ�������ʽ     
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
	


