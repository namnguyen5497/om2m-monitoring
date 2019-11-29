package om2m.monitor;
import java.io.File;
import java.util.ArrayList;



public class logFile {
	

	private String[] logPath = {"D:/Work_bk/IN-MN-Standard/in-cse/win32/win32/x86_64/log.out",
								"D:/Work_bk/IN-MN-Standard/mn-cse-1/win32/win32/x86_64/log.out"};
								//"D:/Work_bk/IN-MN-Standard/mn-cse-2/win32/win32/x86_64/log.out",
								//"D:/Work_bk/IN-MN-Standard/mn-cse-3/win32/win32/x86_64/log.out"};
	
	private ArrayList<File> listFile = new ArrayList<File>();
	private Long[] listFileLength = new Long[logPath.length];
	
	
	
	//constructor: put file into ArrayList
	public logFile(){
		for(int i = 0; i<logPath.length; i++){
			listFile.add(new File(logPath[i]));
			listFileLength[i] = listFile.get(i).length();
		}
	}
	
	public Long[] getCurrentFileLength(){
		return listFileLength;
	}
	
	
	//get list of Files
	public ArrayList<File> getListFile(){
		return listFile;
	}
	
	public void updateFile(){
		listFile.clear();
		for(int i = 0; i<logPath.length; i++){
			listFile.add(new File(logPath[i]));
			listFileLength[i] = listFile.get(i).length();
		}
	}
	
	//Check log file existence
	public boolean checkFileExistence(File file){
		if(file.exists())
			return true;
		return false;
	}
	
	public boolean checkAllFileExistence(ArrayList<File> listFile){
		boolean checkFile = true;
		while(!checkFile){
			for(int i = 0; i < listFile.size(); i++){
				checkFile = true;
				if(!listFile.get(i).exists()){
					checkFile = false;
					break;
				}
			}
		}
		return checkFile;
	}
	
}