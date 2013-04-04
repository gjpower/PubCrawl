package ie.tcd.pubcrawl;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class diceView2 extends SurfaceView implements Runnable {

	SurfaceHolder newHolder;
	public Thread animationThread;
	private boolean isRunning;
	Random rand;
	Random rand1;
	private Rect srcRec;
	private Rect srcRec1;
	private int spriteWidth;
	private int spriteHeight;
	public boolean end;
	public boolean pause;
	private float offSet;
	private float offSet1;
	private Bitmap dice;
	
	public diceView2(Context context) {
		super(context);
		rand = new Random(10);
		rand1 = new Random(30);
		//dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice);
		spriteWidth = 67;
		spriteHeight = 67;
		newHolder = getHolder();
		animationThread = new Thread(this);
		isRunning = true;
		end = false;
		pause = false;
		srcRec = new Rect(0, 0, spriteWidth, spriteHeight);
		srcRec1 = new Rect(0, 0, spriteWidth, spriteHeight);
		animationThread.start();  
		
		
	}
	
	

	public void run() {
		while(isRunning)
		{
			if(!newHolder.getSurface().isValid())
			{
				continue;
			}
			if(!pause)
			{
				offSet = rand.nextInt(6);
				offSet1 = rand1.nextInt(6);
				
				Canvas canvas = newHolder.lockCanvas();
				//Dice 1
				Rect destRect = new Rect(0, 0, spriteWidth, spriteHeight);
				canvas.drawBitmap(dice, srcRec, destRect, null);
				//Dice 2
				Rect destRect1 = new Rect(spriteWidth, 0, spriteWidth*2, spriteHeight);
				canvas.drawBitmap(dice, srcRec1, destRect1, null);
				newHolder.unlockCanvasAndPost(canvas);
				//Dice 1
				this.srcRec.left = (int) (offSet * spriteWidth);
				this.srcRec.right = this.srcRec.left + spriteWidth;
				//Dice 2
				this.srcRec1.left = (int) (offSet1 * spriteWidth);
				this.srcRec1.right = this.srcRec1.left + spriteWidth;
			
			}

			if(end)
			{
				isRunning = false;
			}
		}
		
	}
	public boolean onTouchEvent(MotionEvent event) {
		if(pause)
		{
			pause = false;
		}
		else
		{
			pause = true;
		}
		return super.onTouchEvent(event);
	}
	
	

}
