package ie.tcd.pubcrawl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CoinView extends SurfaceView implements SurfaceHolder.Callback {

	public CoinThread thread;
	private CoinAnimation flipping;
	private Bitmap coinBitmap;
	public int centerX, centerY;
	boolean firstTouch = true;
	
	// constructors
	public CoinView(Context context)
	{
		super(context);
		
		//
		// need to find a way to get the view size for coin location 
		//
		centerX = 200;
		centerY = 200;
		
		getHolder().addCallback(this);	// intercepts events
		thread = new CoinThread(getHolder(), this); 	// thread for game loop
		setFocusable(true);				// makes it able to handle events  
        coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin_anim);
		flipping = new CoinAnimation(coinBitmap, centerX, centerY, 150, 150, 20, 24);
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
		//float y = event.getY();
		//float y2 = event.getY(event.getPointerCount());
		//System.out.println(y + "   " + y2);
		CoinThread.flipping = true;
		thread.run();
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
