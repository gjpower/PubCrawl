package ie.tcd.pubcrawl;

import android.widget.ImageView;

public class Pub {

	public String name;
	

	
	public int rating;
	
	public String ratingString;

	ImageView image;	
	public Pub() {

		name = "Pub Name";
	
		rating = 0;	
		
		ratingString = String.valueOf(rating);
		
	/*	image = new ImageView(null);
		image.setImageResource(R.drawable.pub_photo);*/
			
	}
	
	public Pub(String _name, int _rating) {
		
		name = _name;

		rating = _rating;
		
		ratingString = String.valueOf(rating);
	
	}
	



}








