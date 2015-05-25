package com.pineapplechina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class Blast3goCellReaderBatch {
	private String tableName;
	public Blast3goCellReaderBatch(String filename){
		this.tableName = filename;
//		System.out.println(tableName);
	}
	
	private ArrayList<Blast3goCell> allCell = new ArrayList<Blast3goCell>();
	private int rowsCounter=0;
//	private Scanner cellscanner;
//	private BufferedReader cellBufferReader;
	// Methods:
	public void Read(){

//			cellscanner = new Scanner(new File(tableName));
			
			try {
				BufferedReader br=new BufferedReader(new FileReader(tableName));
				String cellLiner;
				while((cellLiner=br.readLine())!=null){
					String[] newRecord = {"","","",""};
//					String[] raw = cellLiner.trim().split("\\t");
					// 若是trim则会导致不能重新读取到原来的表格，尝试不进行trim()
					String[] raw = cellLiner.split("\\t");
				System.out.println(cellLiner);
//				System.out.println(newRecord[1]);
					// 若是不存在blast结果，则
					if (raw.length==1){
						newRecord[0]=raw[0];
						newRecord[1]="No Hits";
						newRecord[2]="";
						newRecord[3]="";
					}else if (raw.length == 2){
							newRecord[0]=raw[0];
							newRecord[1]=raw[1];
							newRecord[2]="";
							newRecord[3]="";	
					}else if (raw.length == 3){
						newRecord[0]=raw[0];
						newRecord[1]=raw[1];
						newRecord[2]=raw[2];
						newRecord[3]="";	
					}else {
						;
					}
				
					allCell.add(new Blast3goCell(newRecord));
//				System.out.println(newRecord[1]);
					rowsCounter++;
				}
				
				br.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
	}
	
	public int getRowsNum(){
		return rowsCounter;
	}
	
	public String[][] parseCellsTable(){
		String[][] b3gCells = new String[rowsCounter][Blast3goCell.colsNum];
		int rowcount=0;
		for (Iterator<Blast3goCell> iterator = allCell.iterator(); iterator.hasNext();) {
			Blast3goCell rec = iterator.next();
			
			b3gCells[rowcount][0] = rec.GetQuertId();
			b3gCells[rowcount][1] = rec.GetGInum();
			b3gCells[rowcount][2] = rec.GetGOnum();
			b3gCells[rowcount][3] = rec.GetKeggnum();
//			System.out.println(b3gCells[rowcount][0]+" "+b3gCells[rowcount][1]);
			rowcount++;
		}
		return b3gCells;
	}
	
	public ArrayList<Blast3goCell> getAllCells(){
		return allCell;
	}
	
	public void printTable(String outTableName){
		try {
			PrintWriter outTablePipe = new PrintWriter(new File(outTableName));
			String[][] outTables = this.parseCellsTable();
			for(int i=0;i<rowsCounter;i++){
				for(int j=0;j<Blast3goCell.colsNum-1;j++){
					outTablePipe.print(outTables[i][j]+"\t");
				}
				outTablePipe.print(outTables[i][Blast3goCell.colsNum-1]+"\n");
//				outTablePipe.println();
			}
			outTablePipe.flush();
			outTablePipe.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printGOstats(String outTableName){
		try {
			PrintWriter outTablePipe = new PrintWriter(new File(outTableName));
			String[][] outTables = this.parseCellsTable();
			for(int i=0;i<rowsCounter;i++){
//				outTablePipe.println(outTables[i][0]+"\tISA\t"+outTables[i][2]);
				outTables[i][2]=outTables[i][2].replaceAll("\\[", "").replaceAll("\\]", "");
				for(String gg:outTables[i][2].split(",\\s+")){
					// 对于无GO注释信息的，没必要放置进去
					if (gg.equalsIgnoreCase("")) break;
					outTablePipe.println(gg+"\tISA\t"+outTables[i][0]);
				}

			}
			outTablePipe.flush();
			outTablePipe.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
