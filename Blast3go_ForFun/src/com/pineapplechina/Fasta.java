package com.pineapplechina;
/* @author:CJchen
 * @date  :20150307
 * @	All for bioinformatics*China
 */
public class Fasta {
	private String seq="";
	private String id = "";
	//  Constructors:
	public Fasta (){
	}
	public Fasta (String seq){
		this.seq=seq.trim();
	}
	public Fasta (String id,String seq){
		this.id=id.trim();
		this.seq=seq.trim();
	}
	
	//Methods	
	public String getId(){
		return id.replaceFirst("^>","");
	}
	// only keep first id message
	public String getFormatId(){
		return id.replaceFirst("^>","").split("\\s+",2)[0];
	}
	public String getSeq(){
		return seq;
	}
	// return multilines seq
	public String getFormatSeq(){
		String seqTemp="";
		for(int i=0;i<=seq.length();i+=60){
			if(i+60>seq.length()){
				seqTemp=seqTemp+seq.substring(i);
			}else{
				seqTemp=seqTemp+seq.substring(i,i+60)+"\n";				
			}
		}
		return seqTemp;
	}
	public String getFormatSeq(int len){
		String seqTemp="";
		for(int i=0;i<=seq.length();i+=len){
			if(i+len>seq.length()){
				seqTemp=seqTemp+seq.substring(i);
			}else{
				seqTemp=seqTemp+seq.substring(i,i+len)+"\n";				
			}
		}
		return seqTemp;
	}
	

	public static void main(String args[]){
		Fasta fasta = new Fasta("ATCG");
		System.out.println(">"+fasta.id+"\n"+fasta.seq);
		Fasta fasta2= new Fasta("record_name","ATTTCAAGCCAAGCTTGAGCTGAGATAAACTTGTTTCCACCTCTATACTTGACTTGCCAAGAGATAATGCGGTGATGACTAGTTAGTTTATTCAGCTGGTACATCATCTCAACATTTATATTATTCATTTATTTCATTATCTTGAAGGGTTGATTTGACAGCTGAATAATTTGTGGTCTCGTCATGTCAAAGAGAAGAATTACTTGCCGTGCCATGTCTAAAGGAACGATATGGGAGCTA");
		System.out.println(">"+fasta2.getId()+"\n"+fasta2.getFormatSeq(11));
	}
}
