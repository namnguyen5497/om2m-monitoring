package om2m.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Buffer {
	private static Integer[] messNumber;
	private static String[] buffer;
	private static XSSFWorkbook workBook;
	private static Timestamp latestTimestamp;
	private static int latestSheetIndex;
	public static FileInputStream inputStream;
	
	
	public Buffer(XSSFWorkbook workBook) {
		this.workBook = workBook;
		buffer = new String[workBook.getNumberOfSheets()];
		messNumber = new Integer[workBook.getNumberOfSheets()];
		Arrays.fill(messNumber, 0);
	}
	
	
	public static void putMessToBuffer( ) {
		for(int i = 0; i <workBook.getNumberOfSheets(); i++){
			buffer[i] = getMess(workBook.getSheetAt(i),messNumber[i]);
		}
	}
	
	
	//get Mess to put into Buffer
		private static String getMess(Sheet sheet, int rowNumber){
			if(sheet.getRow(rowNumber) == null){
				return null;
			}
			return sheet.getRow(rowNumber).getCell(0).getStringCellValue();
		}
		
	//retrieve buffer:
	public static String[] getBuffer(){
		return buffer;
	}
	
	//get Latest mess to put to gui
	public static String getLatestMess(Timestamp latest){
		timeComparator compare = new timeComparator(buffer, latest);
		latestSheetIndex = compare.indexSheetWithOutputMess();
		if(latestSheetIndex != -1){
			//latestSheetIndex = compare.indexSheetWithOutputMess();
			setMessNumber(latestSheetIndex, true);//increase mess number of sheet that considered latest
			latestTimestamp = compare.getLatestTimestamp();
			return buffer[latestSheetIndex];
		}
		setMessNumber(0, false);
		return null;
		
	}
	
	public int getLatestSheetIndex(){
		return latestSheetIndex;
	}
	
	//set messNumber of cse that was considered latest
	private static void setMessNumber(int index, boolean update){
		if(update == true){
			messNumber[index]++;
		}
	}
	
	//Test
	public Integer[] getMessNumber(){
		return messNumber;
	}
	
	//Get latest timeStamp to update to systimeStamp
	public Timestamp getLatestTimestamp(){
		return latestTimestamp;
	}
	
}
