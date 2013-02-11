package ie.tcd.pubcrawl;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ThreadForGames extends Thread
{

	private boolean running;
	private SurfaceHolder surfaceHolder;
	private CoinView coinView;
	Canvas canvas;
	
		// counstructor 
	public ThreadForGames(SurfaceHolder surfaceHolder, CoinView coinView)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.coinView = coinView;
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
					this.coinView.Update_Bitmap();
					this.coinView.Renderer(canvas);
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
	}

}


