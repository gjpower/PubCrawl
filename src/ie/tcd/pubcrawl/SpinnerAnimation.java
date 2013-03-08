package ie.tcd.pubcrawl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;


public class SpinnerAnimation {
	
	private Bitmap bitmap;      // the animation sequence		
    public Rect sourceRect;    // the rectangle to be drawn from the animation bitmap	    
    private int frameNr;        // number of frames in animation
    private int currentFrame;   // the current frame
    public int framePeriod;    // milliseconds between each frame (1000/fps)
    private int spriteWidth;    // the width of the sprite to calculate the cut out rectangle
    private int spriteHeight;   // the height of the sprite
    private int x;              // the X coordinate of the object (top left of the image)
    private int y;              // the Y coordinate of the object (top left of the image
    private long frameTicker;
    public int numShown;

	public SpinnerAnimation(Bitmap bitmap, int x, int y, int width, int height, int fps, int frameCount) 
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
        numShown = 0;
		
	}
	
	public void Update(long gameTime, int numPos) 
	{
		if (gameTime > frameTicker + framePeriod) 
		{
			numShown ++;
			if(numShown == numPos/24)
			{
				SpinnerView.thread.numTurns ++;
			}
	        frameTicker = gameTime;
	        // increment the frame
	        currentFrame--;	
	        if (currentFrame < 0) 
	        {
	            currentFrame = (frameNr-1);
	        }
	    }
	    // define the rectangle to cut out sprite
	    this.sourceRect.left = currentFrame * spriteWidth;
	    this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}
	
	public void Draw(Canvas canvas)
	{
	        // where to draw the sprite
			canvas.drawColor(Color.GREEN); // resets the back ground
	        Rect destRect = new Rect(x, y, x + spriteWidth, y + spriteHeight);
	        canvas.drawBitmap(bitmap, sourceRect, destRect, null);	
    }
	
}
