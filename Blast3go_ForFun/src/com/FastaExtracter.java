package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import com.pineapplechina.Fasta;
import com.pineapplechina.FastaReader;

public class FastaExtracter {
	public static void main (String args[]){
		if (args.length<2){
			String usage="Usage:\n\tjava -jar FastaExtracter.jar inputFastaFile inputList\n";
			System.out.println(usage+"===========================================\n");
			System.exit(1);
		}

		Scanner listreader;
		HashSet<String> needrecord = new HashSet<String>();
		try {
			listreader = new Scanner(new File(args[1]));

			// read the list and make a hash
			while(listreader.hasNext()){
				// trim those white space and remove the '>'
				needrecord.add(listreader.nextLine().trim().replaceFirst("^>",""));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// read and print fasta records
		FastaReader fastareader = new FastaReader(args[0]);	
		while(true){
			Fasta fastaiterator = fastareader.nextFasta();
			if (fastaiterator.getId().equals("")) break;
			if (needrecord.contains(fastaiterator.getId())){
				System.out.print(">"+fastaiterator.getId()+"\n"+fastaiterator.getSeq()+"\n");
			}else if (needrecord.contains(fastaiterator.getFormatId())){
				System.out.print(">"+fastaiterator.getId()+"\n"+fastaiterator.getFormatSeq(20)+"\n");
			}else{
				;
			}
		}
	}
}
