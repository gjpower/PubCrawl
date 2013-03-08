package ie.tcd.pubcrawl;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class CoinThread extends Thread // created by the coinview 
{

	private boolean running;
	public static boolean heads;
	public static boolean waiting;
	private SurfaceHolder surfaceHolder;
	private CoinView coinView;
	static Canvas canvas;
	int done ;
	public static int numRotations= 0;
	public static int toHeads = 0;
	
		// counstructor 
	public CoinThread(SurfaceHolder surfaceHolder, CoinView coinView)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.coinView = coinView;
		heads = false;
		waiting = true;
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
					if(!waiting)
					{
						if(heads)
						{
							if(numRotations < 8)
							{
								this.coinView.flipping.Update(System.currentTimeMillis());
								this.coinView.flipping.Draw(canvas);
								System.out.println(numRotations);
							}
							else
							{
								if(toHeads < 12)
								{
									System.out.println("r to heads");
									coinView.flipping.Update_No_Fall(System.currentTimeMillis());
									coinView.flipping.Draw(canvas);			
								}
								else
								{
									waiting = true;
								}
							}
						}
						else
						{
							if(numRotations < 8)
							{
								this.coinView.flipping.Update(System.currentTimeMillis());
								this.coinView.flipping.Draw(canvas);
								System.out.println(numRotations);
							}
							else
							{
								waiting = true;
							}
							//done = this.coinView.Flip_Twice(canvas);
							//done = this.coinView.Value_Tails(canvas);
						}
					}
					else
					{
						this.coinView.Draw_Static(canvas);
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
	
	public boolean  Get_Running()
	{
		return running;
	}

	public void Reset()
	{
		numRotations = 0;
		toHeads = 0;
		CoinAnimation.Set_Mod(-1);
		CoinAnimation.Set_Slow(2);
	}
}



