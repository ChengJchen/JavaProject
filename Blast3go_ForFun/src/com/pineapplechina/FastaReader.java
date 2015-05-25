package com.pineapplechina;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FastaReader {
	//Constructor:
	
	//Method:
	private Scanner reader;
	private	String temp;	
	private String seq ="";
	private String id="";
	public FastaReader(String filename){
		try {
			reader = new Scanner(new File(filename));
			// reader the first readcord's id
			temp = reader.nextLine();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean finalFlag=false;
	public Fasta nextFasta(){
		id=temp;
		while(reader.hasNextLine()){
			temp = reader.nextLine();
			if (temp.startsWith(">")){
				return new Fasta(id,seq);
			}else{
				seq=seq+temp;
				if (!reader.hasNextLine()) finalFlag=true;
			}
		}
		//get the final fasta record
		if(finalFlag){
			finalFlag=false;
			return new Fasta(id,seq);
		}
		return new Fasta();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length<1){
			System.out.println("Error:\n\tCan't found input file\n"+"================================\n");
		}
		FastaReader fastareader = new FastaReader(args[0]);
		while(true){
			Fasta fastaIterator = fastareader.nextFasta();
			if (fastaIterator.getId()==""){
				break;
			}
			System.out.print(">"+fastaIterator.getFormatId()+"\n"+fastaIterator.getFormatSeq()+"\n");
		}
	}
	
}
