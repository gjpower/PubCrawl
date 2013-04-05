package ie.tcd.pubcrawl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class SpinnerAnimation {
	
	private Bitmap bitmap;      // the animation sequence		
    public Rect sourceRect;     // the rectangle to be drawn from the animation bitmap	    
    private int frameNr;        // number of frames in animation
    private int currentFrame;   // the current frame
    public int framePeriod;     // milliseconds between each frame (1000/fps)
    private int spriteWidth;    // the width of the sprite to calculate the cut out rectangle
    private int spriteHeight;   // the height of the sprite
    private int x;              // the X coordinate of the object (top left of the image)
    private int y;              // the Y coordinate of the object (top left of the image
    private long frameTicker;
    public int numShown;
    public Rect screenRect, bRect;
    public Bitmap background;

	public SpinnerAnimation(Bitmap bitmap,Bitmap background, int x, int y, int width, int height, int fps, int frameCount) 
	{
		this.bitmap = bitmap;
		this.background = background;
        currentFrame = 0;
        frameNr = frameCount;
        spriteWidth = bitmap.getWidth() / frameCount;
        spriteHeight = bitmap.getHeight();
        this.x = x;
        this.y = y;
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
        screenRect = new Rect(0,0, SpinnerGames.Width, SpinnerGames.Height);
        bRect = new Rect(0,0, background.getWidth(), background.getHeight());
        framePeriod = 1000 / fps;
        frameTicker = 0l;
        numShown = 0;
		
	}
	
	public void Update(long gameTime, int numPos) 
	{
		if (gameTime > frameTicker + framePeriod) 
		{
			numShown ++;
			if(numShown == 24/numPos)
			{
				SpinnerView.thread.numTurns ++;
				numShown = 0;
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
			canvas.drawBitmap(background, bRect, screenRect, null);
	        Rect destRect = new Rect(x - (spriteWidth/2), y - (spriteHeight/2), x + (spriteWidth/2), y + (spriteHeight/2));
	        canvas.drawBitmap(bitmap, sourceRect, destRect, null);	
    }
	
}
