package om2m.monitor;

public class queryHandler{
	private String query;
	
	
	public queryHandler(String query){
		this.query = query;
	}
	
	public boolean checkValidQuery(){
		if(query.equals("in") || query.equals("mn"))
			return true;
		return false;
	}

	public String filteredResult(String line){
		String[] mess = line.split("\\|");
		String time = mess[0];
		String from = mess[1];
		String operation = mess[4];
		return time + " - " + from + " - " + operation ;
	}

}
