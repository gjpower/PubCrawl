package ie.tcd.pubcrawl;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class SpinnerThread extends Thread {

	private boolean running;
	private SurfaceHolder surfaceHolder;
	private SpinnerView spinnerView;
	static Canvas canvas;
	public boolean waiting, started;
	public int numTurns, numPos, reqNumTurns;
	
	public SpinnerThread(SurfaceHolder surfaceHolder, SpinnerView spinnerView) 
	{
		super();
		this.spinnerView = spinnerView;
		this.spinnerView = spinnerView;
		running = false;
		numPos = 12;
		numTurns = 0;
		reqNumTurns = (numPos / 24) + (numPos * ((numPos/24)* 24));
		waiting = true;
	}

	public void Set_Running(boolean state) 
	{
		running = state;
	}

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
					if(!waiting)
					{
						if(numTurns > reqNumTurns)
						{
							spinnerView.spinning.Update(System.currentTimeMillis(), numPos);
							spinnerView.spinning .Draw(canvas);
						}
						if(numTurns < reqNumTurns)
						{
							waiting = true;
						}					
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
		
	}
	
	public boolean Get_Running()
	{
		return running;
	}

}
