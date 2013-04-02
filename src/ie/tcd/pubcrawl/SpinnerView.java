package ie.tcd.pubcrawl;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SpinnerView extends SurfaceView implements SurfaceHolder.Callback{

	public static SpinnerThread thread;
	private int centerX, centerY;
	public SpinnerAnimation spinning;
	private Bitmap bottle, background;
	private Random rand;
	public int numRotations;
	
	public SpinnerView(Context context, int num)
	{
		super(context);
		getHolder().addCallback(this);	// intercepts events
		thread = new SpinnerThread(getHolder(), this); 	// thread for game loop
		thread.numPos = num;
		centerX = SpinnerGames.Width / 2;
		centerY = SpinnerGames.Height / 2;
		setFocusable(true);				// makes it able to handle events 
		/* bottle = BitmapFactory.decodeResource(getResources(), R.drawable.bottle_anim);
		if(thread.numPos == 4)
		{
			background = BitmapFactory.decodeResource(getResources(),R.drawable.sbg4);
		}
		else if (thread.numPos == 6 )
		{
			background = BitmapFactory.decodeResource(getResources(),R.drawable.sbg6);
		}
		else if (thread.numPos == 8 )
		{
			background = BitmapFactory.decodeResource(getResources(),R.drawable.sbg8);
		}
		else if (thread.numPos == 12 )
		{
			background = BitmapFactory.decodeResource(getResources(),R.drawable.sbg12);
		} */
		spinning = new SpinnerAnimation(bottle,background, centerX, centerY, 256, 256, 30, 24);
		rand = new Random(System.currentTimeMillis());
		numRotations = 0; 
				
	}

	// ronseal functions 
		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
		{
			// TODO Auto-generated method stub
			
		}
	 
		public void surfaceCreated(SurfaceHolder holder) 
		{
				thread.Set_Running(true);
				thread.start();
			
		}

		public void surfaceDestroyed(SurfaceHolder holder) 
		{
			boolean retry = true;
			while (retry)
			{
				try
				{
					thread.join();
					retry =  false;
				}
				catch(InterruptedException e)
				{
					// make it try again if failed
				}
			}	
		}
		
		public boolean onTouchEvent(MotionEvent event)
		{
			if (!thread.Get_Running())
			{
				numRotations = rand.nextInt(thread.numPos);
				thread.reqNumTurns = numRotations + (2* thread.numPos);
				thread.numTurns = 0;
				spinning.numShown = 0;
				thread.waiting =  false;
				//thread.Set_Running(true);
			}
			else
			{
				if(thread.waiting)
				{
					System.out.println("re calculating");
					numRotations = rand.nextInt(thread.numPos);
					thread.reqNumTurns = numRotations + (2* thread.numPos);
					thread.numTurns = 0;
					spinning.numShown = 0;
					thread.waiting = false;
				}
			}
			return super.onTouchEvent(event);
		}
}
