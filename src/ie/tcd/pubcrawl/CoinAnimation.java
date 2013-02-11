package ie.tcd.pubcrawl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

public class CoinAnimation 
{
	
		private Bitmap bitmap;      // the animation sequence
		
	    private Rect sourceRect;    // the rectangle to be drawn from the animation bitmap
	    
	    private int frameNr;        // number of frames in animation
	    private int currentFrame;   // the current frame
	    public int framePeriod;    // milliseconds between each frame (1000/fps)
	    private int spriteWidth;    // the width of the sprite to calculate the cut out rectangle
	    private int spriteHeight;   // the height of the sprite
	    private int x;              // the X coordinate of the object (top left of the image)
	    private int y;              // the Y coordinate of the object (top left of the image)
	    private int slow;			// for slowing down the rotations
	    int mod = -1;
	    private long frameTicker;   // the time of the last frame update
	    
	    //private Rect destRect;
	    private Rect heads;
	    private Rect tails;


	    // constructor 
public CoinAnimation(Bitmap bitmap, int x, int y, int width, int height, int fps, int frameCount) 
	{
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        currentFrame = 0;
        frameNr = frameCount;
        spriteWidth = bitmap.getWidth() / frameCount;
        spriteHeight = bitmap.getHeight();
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
        framePeriod = 1000 / fps;
        frameTicker = 0l;
        slow = 10 / fps;
        heads = new Rect(1950 , 0, 2100, spriteHeight );
        tails = new Rect(0, 0, spriteWidth, spriteHeight);
        
    }

		// changes the displayed image based on elapsed time 
public void Update(long gameTime) 
	{
		if(y <= 50)  // top
		{
			mod = 1;
		}
		if (y >= 550) // bottom 
		{
			mod=0;
			CoinThread.flipping = false;
		}
		if (gameTime > frameTicker + framePeriod + slow) 
		{
	        frameTicker = gameTime;
	        // increment the frame
	        currentFrame--;
	        y += 10*mod;
	        if (currentFrame < 0) 
	        {
	            currentFrame = (frameNr-1);
	            slow += slow;
	            Log.i("", "360");
	        }
	    }
	    // define the rectangle to cut out sprite
	    this.sourceRect.left = currentFrame * spriteWidth;
	    this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}

	// these 3 are fairly self explanatory 
public void Draw(Canvas canvas)
	{
		if(!CoinThread.flipping)
		{
			if (CoinGames.outcome == "heads")
			{
				canvas.drawColor(Color.GREEN);
				while(!(sourceRect.equals(heads)))
				{
					Rotate_To_Heads(System.currentTimeMillis(), canvas);
				}
				Rect destRect = new Rect(Get_X(), Get_Y(), Get_X() + spriteWidth, Get_Y() + spriteHeight);
				canvas.drawBitmap(bitmap, heads, destRect, null);
			}
			else
			{
				
				canvas.drawColor(Color.GREEN);
				while(!(sourceRect.equals(tails)))
				{
					Rotate_To_Tails(System.currentTimeMillis(), canvas);
				}
				Rect destRect = new Rect(Get_X(), Get_Y(), Get_X() + spriteWidth, Get_Y() + spriteHeight);
				canvas.drawBitmap(bitmap, tails, destRect, null);
			}
		}
		else // if it is flipping
		{
	        // where to draw the sprite
			canvas.drawColor(Color.GREEN); 
	        Rect destRect = new Rect(Get_X(), Get_Y(), Get_X() + spriteWidth, Get_Y() + spriteHeight);
	        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
		}
		
    }

 

public void Rotate_To_Heads(long gameTime, Canvas canvas)
	{
		if (gameTime > frameTicker + framePeriod) 
		{
	        frameTicker = gameTime;
	        // increment the frame
	        currentFrame--;
	        if (currentFrame < 0) 
	        {
	            currentFrame = (frameNr-1);
	        }
	    }
	    this.sourceRect.left = currentFrame * spriteWidth;
	    this.sourceRect.right = this.sourceRect.left + spriteWidth;
		canvas.drawColor(Color.GREEN); 
        Rect destRect = new Rect(Get_X(), Get_Y(), Get_X() + spriteWidth, Get_Y() + spriteHeight);
        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}

public void Rotate_To_Tails(long gameTime, Canvas canvas)
	{
		System.out.println("roatating to tails");
		if (gameTime > frameTicker + framePeriod + slow) 
		{
	        frameTicker = gameTime;
	        // increment the frame
	        currentFrame--;
	        if (currentFrame < 0) 
	        {
	            currentFrame = (frameNr-1);
	        }
	    }
	    this.sourceRect.left = currentFrame * spriteWidth;
	    this.sourceRect.right = this.sourceRect.left + spriteWidth;
		canvas.drawColor(Color.GREEN); 
	    Rect destRect = new Rect(Get_X(), Get_Y(), Get_X() + spriteWidth, Get_Y() + spriteHeight);
	    canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	
		return;
	}


private int Get_Y() 
	{
		
		return y;
	}

private int Get_X()
	{
		
		return x;
	}

}

