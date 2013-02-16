package ie.tcd.pubcrawl;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CoinView extends SurfaceView implements SurfaceHolder.Callback {

	public CoinThread thread;
	public CoinAnimation flipping;
	private Bitmap coinBitmap;
	public int centerX, centerY;
	boolean firstTouch = true;
	private Random rand;
	public static int numRotations;
	
	// constructors
	public CoinView(Context context)
	{
		super(context);
		
		//
		// need to find a way to get the view size for coin location 
		//

		
		getHolder().addCallback(this);	// intercepts events
		thread = new CoinThread(getHolder(), this); 	// thread for game loop
		centerX = 200;//CoinThread.canvas.getWidth() / 2;
		centerY = 200;//CoinThread.canvas.getHeight() / 2;
		setFocusable(true);				// makes it able to handle events  
        coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin_anim);
		flipping = new CoinAnimation(coinBitmap, centerX, centerY, 150, 150, 20, 24);
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
		if(CoinThread.waiting)
		{
			CoinThread.heads = Flip_Coin();
			CoinThread.waiting = false; 
			if(!thread.Get_Running())
			{
				thread.run();
			}
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

	public boolean Flip_Coin()
	{
		boolean flip = rand.nextBoolean();
		System.out.println("from flip coin" + flip);
		return flip;
			
	}




	public void Draw_Static(Canvas canvas) 
	{
		flipping.Draw(canvas);
		
	}
}
















