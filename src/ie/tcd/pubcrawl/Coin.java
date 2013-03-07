package ie.tcd.pubcrawl;

public class Coin  {
	
	static double height;	
	static double radius;
	static double decelerationConst;
	static double time;
	static double initVel;
	static double distTraveled;	
	static double deceleration;
	static double circumfrence;
	
	// constructor 
	public Coin()
	{
		height = 2;		// coin size based on €2
		radius = 13;
		decelerationConst = 429; // positive because all the negatives cancel later
		deceleration = ((12*decelerationConst)/(3*(radius*radius)+(height*height)));
		circumfrence = (3.14159265359*radius);
	}
	
	// main function - returns outcome of coin toss
	public String Flip_Coin(float init)
	{
		
		String outcome;
		int flips = 0 ;
		
		initVel =  init/10;	// scales the float and makes the difference between flips bigger
		
		Calc_Time();
		Calc_Dist();
		flips = Calc_Flips();
		
		
		int rem = flips%2;
		
		System.out.println(flips + "   " + rem);
		
		if (rem == 1)
		{
			outcome = "Heads";
		}
		else 
		{
			outcome = "Tails";
		}
			
				
		return outcome;
	}
	
	// calculates the time taken for the coin to stop
	public static void Calc_Time()
	{
		time = initVel/deceleration;
	}
	
	// calculates the distance the a point on the edge of the coin has traveled 
	public static void Calc_Dist()
	{
		distTraveled = ((initVel*initVel)*time)/(2*deceleration);
	}
	
	// calculates the number of times the coin rotates through 180deg
	public static int Calc_Flips()
	{
		int flips = 0;
		while(distTraveled > 0)
		{
			distTraveled = distTraveled - circumfrence;
			flips ++;
		}
		
		return flips;

	}
}
