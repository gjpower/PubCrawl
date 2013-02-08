package ie.tcd.pubcrawl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CoinView extends SurfaceView implements SurfaceHolder.Callback {

	public ThreadForGames thread;
	private CoinAnimation flipping;
	private Bitmap coinBitmap;
	public int centerX, centerY;
	boolean firstTouch = true;
	
	// constructors
	public CoinView(Context context)
	{
		super(context);
		centerX = 100;
		centerY = 500;
		getHolder().addCallback(this);	// intercepts events
		thread = new ThreadForGames(getHolder(), this); 	// thread for game loop
		setFocusable(true);				// makes it able to handle events  
        coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin_anim);
		flipping = new CoinAnimation(coinBitmap, centerX, centerY, 150, 150, 20, 24);
	}

	
	// ronseal functions 
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
	{
		// TODO Auto-generated method stub
		
	}
	// functions in this moved to onTouch so it dont start straight away 
	public void surfaceCreated(SurfaceHolder holder) 
	{
		//thread.Set_Running(false);
		//thread.start();
		
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
		if(firstTouch)
		{
			thread.Set_Running(true);
			thread.start();
			firstTouch = false;
		}
		return super.onTouchEvent(event);
	}
	
	protected void onDraw(Canvas canvas)
	{
		
	}

	public void Update_Bitmap()
	{
		flipping.Update(System.currentTimeMillis());
	}


	public void Renderer(Canvas canvas)
	{
		flipping.Draw(canvas);
	}

}
