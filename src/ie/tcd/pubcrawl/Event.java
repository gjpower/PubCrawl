package ie.tcd.pubcrawl;

public class Event {

	String name, organiser, description;
	int day, month, year, hour, minute;
	
	String timeDate;
	
	String listPubs;
	
	String profilePic;
	
	int id;
	
	
	public Event() {
		
		name = "Event Name";
		organiser = "Organiser Name";
		description = "Event Description";
		
		timeDate = "Event Date and Time";
		
		listPubs = "List of Pubs";
		
		profilePic = "Event Profile Picture";
		
		id = 0;
		
		
	}
	
	public Event(String _name, String _organiser, String _description, String _timeDate, String _listPubs, 
			String _profilePic, int _id) {
		
		name = _name;
		organiser = _organiser;
		description = _description;
		
		timeDate = _timeDate;
		
		listPubs = _listPubs;
		
		profilePic = _profilePic;
		
		id = _id;
		
	}
	
}
