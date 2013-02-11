package ie.tcd.pubcrawl;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class CoinThread extends Thread // created by the coinview 
{

	private boolean running;
	public static boolean flipping;
	private SurfaceHolder surfaceHolder;
	private CoinView coinView;
	Canvas canvas;
	
		// counstructor 
	public CoinThread(SurfaceHolder surfaceHolder, CoinView coinView)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.coinView = coinView;
		flipping = false;
	}
	
		// for changing the private bool
	public void Set_Running(boolean running)
	{
		this.running = running;
	}
	
		// ronseal 
	public void run()
	{
		
		while(running)
		{
			canvas = null;
			try
			{
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder)
				{
					
					if(flipping)
					{
						this.coinView.Update_Bitmap(); // calls the update in coin animation
						this.coinView.Renderer(canvas); // calls the draw in coin animation
					}
					else
					{
						this.coinView.Renderer(canvas);
					}
				}
			}
			finally 
			{
				if (canvas != null)
				{
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		System.out.println("no longer runing");
	}

}


