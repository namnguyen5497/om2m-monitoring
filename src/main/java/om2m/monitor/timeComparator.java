package om2m.monitor;

import java.sql.Timestamp;

public class timeComparator {
	private static Timestamp latest;
	
	private String[] buffer;
	
	public timeComparator(String[] buffer, Timestamp latest){
		this.latest = latest;
		this.buffer = buffer;
	}
	
	//get the index of CSE whose messsage got printed
	public int indexSheetWithOutputMess(){
		int indexSheetWithOutputMess = 0;
		
		//check if buffer is null
		boolean isNull = true;
		for(int i = 0; i< buffer.length; i++){
			if(buffer[i] != null)
				isNull = false;
		}
		if(isNull){
			indexSheetWithOutputMess = -1;
		}
		
		//find 1st non-null element and set min value
		Timestamp timeStamp;
		long min_diff = 0;
		for(int i = 0; i<buffer.length; i++){
			if(buffer[i] != null){
				timeStamp =Timestamp.valueOf(buffer[i].split("\\|")[0]);	
				min_diff = timeStamp.getTime() - latest.getTime();
				break;
			}
		}
		
		long diff;
		// fine diff time of all elements and compare to min diff 
		for(int i = 0; i<buffer.length; i++){
			if(buffer[i] != null){
				timeStamp = Timestamp.valueOf(buffer[i].split("\\|")[0]);
				diff = timeStamp.getTime() - latest.getTime();
				if(diff <= min_diff){
					min_diff = diff;
					indexSheetWithOutputMess =  i;
				}
			}
		}
	
		if(indexSheetWithOutputMess != -1){
			setLatestTimestamp(Timestamp.valueOf(buffer[indexSheetWithOutputMess].split("\\|")[0]));
		}
		return indexSheetWithOutputMess;
	}
	
	
	public void setLatestTimestamp(Timestamp timeStamp){
		latest = timeStamp;
	}
	public Timestamp getLatestTimestamp(){
		
		return latest;
	}
	
}
