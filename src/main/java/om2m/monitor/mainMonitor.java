package om2m.monitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;




public class mainMonitor {
	monitorGUI gui;
	Timestamp sysTimeStamp;
	ReadFileToXls readFile;
	
	static logFile log;
	static Buffer buffer;
	final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
	
	public mainMonitor() throws IOException{
		
		gui = new monitorGUI(executorService);
		
		Date sysDate = new Date();
		sysTimeStamp = new Timestamp(sysDate.getTime()); 
		gui.textArea.append("System current time: " + sysTimeStamp +"\n");
		
		//input log file -> xls
		log = new logFile();
		readFile = new ReadFileToXls(log);
		readFile.readFileToXls();
		buffer = new Buffer(readFile.getWorkBook());
	}
	
	
	//main
	public static void main(String[] args) throws IOException{
		final mainMonitor main = new mainMonitor();
		main.buffer.putMessToBuffer(); // put 1st mess in all sheet to buffer
		//final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	    main.executorService.scheduleAtFixedRate(new Runnable() {
	        public void run() {
	        	Monitoring(main);
	        	for(int i = 0; i < main.buffer.getBuffer().length; i++){
	        		System.out.println(main.buffer.getBuffer()[i] + " /*/*/ ");
	        	}
	        	System.out.println("testing thread");
	        }
	    }, 0, 1, TimeUnit.SECONDS);
	
	}
	
	//monitoring method
	private static void Monitoring(mainMonitor main){
		System.out.println("Still reading "+ main.readFile.counter);
		main.readFile.counter++;
		String latestMess = null;
		if(buffer.getBuffer() != null){
			latestMess = buffer.getLatestMess(main.sysTimeStamp);			
			if(latestMess != null){
				main.gui.textArea.append(latestMess + "\n");//need extract
				main.gui.textArea.append("Current thread: "+ Thread.currentThread().getName() + "\n");
				main.gui.textArea.append("-------------------------------------------------------------------------------------------- \n");
			}

			main.sysTimeStamp = buffer.getLatestTimestamp();
			System.out.println(main.sysTimeStamp);//update latest Timestamp
			System.out.println("Testing!"); //
		
			
			//log.updateFile(); // new logfiles  object to update new file.length()
			int latestSheetIndex = buffer.getLatestSheetIndex();
			System.out.println("latest Sheet index: "+ latestSheetIndex);
			Integer[] messNumber = buffer.getMessNumber();
			for(Integer a : messNumber){ //
				System.out.println(a); //
			}
			
			//if(latestSheetIndex == -1){
			main.readFile.updateAllXls(messNumber);				
			//}
			//if(latestSheetIndex != -1){
				//main.readFile.updateXls(latestSheetIndex, 
						//log.getListFile().get(latestSheetIndex).length(), messNumber[latestSheetIndex]);				
			//}
			
			if(buffer.getBuffer() != null){				
				buffer.putMessToBuffer();			
			}
		}
		
	}
}