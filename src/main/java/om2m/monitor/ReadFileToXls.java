package om2m.monitor;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadFileToXls{
	
	private long fileLength;
	private XSSFWorkbook workBook;
	logFile log;
	public static int counter = 0;
	File fileXlsx;
	
	public ReadFileToXls(logFile log){
		this.log = log;
		workBook = new XSSFWorkbook();
		fileXlsx = new File("D:/Luna-workspace/om2m.monitor/readLog.xlsx");
	}
	
	public void readFileToXls(){
		int name = 1;
		//logFile log = new logFile();
		for(File file : log.getListFile()){
			int rowNumber = 0;
			XSSFSheet logSheet = logSheet(workBook, "cse - "+ (name));
			try {								// readLine from log into xlsx
				BufferedReader br = new BufferedReader(new FileReader(file));
				String lineRead;
				while(true){
					lineRead = br.readLine();
					if(lineRead == null)
						break;
					if(lineRead.contains("MESS")){
						Row row = logSheet.createRow(rowNumber);
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue(lineRead);
						//System.out.println(lineRead);
						rowNumber++;
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			name++;
		}
		
		//export to Xlsx file 
		
        fileXlsx.getParentFile().mkdirs();
        FileOutputStream outFile;
		try {
			outFile = new FileOutputStream(fileXlsx);
			workBook.write(outFile);
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public long getfileLength(){
		return fileLength;
	}
	
	public XSSFSheet logSheet(XSSFWorkbook workBook, String sheetName){
		XSSFSheet logSheet;
		logSheet = workBook.createSheet(sheetName);
		return logSheet;
	}
	
	public XSSFWorkbook getWorkBook(){
		return workBook;
	}
	
	public void updateXls(int index, long updateFileLength, int messNumber){
		int rowNumber = workBook.getSheetAt(index).getLastRowNum()+1;
		//long updatedFileLength = log.getCurrentFileLength()[index];
		try {								// readLine from log into xlsx
			BufferedReader br = new BufferedReader(new FileReader(log.getListFile().get(index)));
			String lineRead;
			br.skip(updateFileLength);
			while(true){
				lineRead = br.readLine();
				if(lineRead == null)
					break;
				if(lineRead.contains("MESS")){
					Row row = workBook.getSheetAt(index).createRow(rowNumber);
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue(lineRead);
					//System.out.println(lineRead);
					rowNumber++;
				}
			}
			br.close();
	        
			//File testXlsx = new File("D:/Luna-workspace/om2m.monitor/readLog_test.xlsx");
			FileOutputStream outFile = new FileOutputStream(fileXlsx);
			workBook.write(outFile);
			outFile.close();
			System.out.println("done updating xls at: " + index);
			System.out.println("XLS size now: " + fileXlsx.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateAllXls(Integer[] messNumber){

		for(int i = 0; i<log.getListFile().size(); i++){
			int rowNumber = messNumber[i];
			long updatedFileLength = log.getCurrentFileLength()[i];
			try {								// readLine from log into xlsx
				BufferedReader br = new BufferedReader(new FileReader(log.getListFile().get(i)));
				String lineRead;
				br.skip(updatedFileLength);
				while(true){
					lineRead = br.readLine();
					if(lineRead == null)
						break;
					if(lineRead.contains("MESS")){
						Row row = workBook.getSheetAt(i).createRow(rowNumber);
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue(lineRead);
						//System.out.println(lineRead);
						rowNumber++;
					}
				}
				br.close();
				//File testXlsx = new File("D:/Luna-workspace/om2m.monitor/readLog_test.xlsx");
				FileOutputStream outFile = new FileOutputStream(fileXlsx);
				workBook.write(outFile);
				outFile.close();
				System.out.println("done updating all xls");
				System.out.println("XLS size now: " + fileXlsx.length());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//Test method
	public boolean checkCellBlank(int rowNumber){
		if(workBook.getSheetAt(0).getRow(rowNumber) == null){
			return false;
		}
		return true;
	}
	
	
	static class Buffer{
		private Integer[] messNumber;
		
		public Buffer(){
			
		}
	}
}