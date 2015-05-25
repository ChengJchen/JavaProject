package com.pineapplechina;

public class Blast3goCell {
	private String queryId="";
	private String GInum="";
	private String GOnum="";
	private String Keggnum="";
	public static int colsNum = 4;
	public static String[] colsNames = {"Query ID","GeneBank ID","GO ID","KEGG ID"};
	public Blast3goCell(String[] newRecord){
		this.queryId=newRecord[0];
		this.GInum = newRecord[1];
		if (newRecord.length>2){
			this.GOnum = newRecord[2];
		}
		if (newRecord.length>3){
			this.Keggnum = newRecord[3];			
		}
	}
	
//	public String[] parseToString(){
//		return new String[]{queryId,GInum,GOnum,Keggnum};
//	}
	
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public void setGInum(String gInum) {
		this.GInum = gInum;
	}
	public void setGOnum(String gOnum) {
		this.GOnum = gOnum;
	}
	public void setKeggnum(String keggnum) {
		this.Keggnum = keggnum;
	}
	public String GetQuertId(){
		return queryId;
	}
	public String GetGInum(){
		return GInum;
	}
	public String GetGOnum(){
		return GOnum;
	}
	public String GetKeggnum(){
		return Keggnum;
	}
}
