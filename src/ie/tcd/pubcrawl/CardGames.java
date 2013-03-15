package ie.tcd.pubcrawl;

import java.util.Random;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.support.v4.app.NavUtils;

public class CardGames extends Activity implements OnTouchListener{
	draw ourSurfaceView;
	int x, y, next;
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        next=0;
    	ourSurfaceView = new draw(this);
    	ourSurfaceView.setOnTouchListener(this);
        setContentView(R.layout.activity_card_games);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_card_games, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void Draw(View v){
    	System.out.println('d');
    	setContentView(ourSurfaceView);
    }
    
	protected void onPause() {
		super.onPause();
		ourSurfaceView.pause();
	}
	protected void onResume() {
		super.onResume();
		ourSurfaceView.resume();
	}
	public void onBackPressed()
	{
		onPause();
		super.onBackPressed();
	}
	public boolean onTouch(View v, MotionEvent event) {
		if(next==1){
			next=0;
		}
		return false;
	}
	public class draw extends SurfaceView implements Runnable{
		
		SurfaceHolder ourHolder;
		Thread ourThread;
		boolean IsOk = false;
		Random rc;
		String Rules[]= new String[13];
		int crds[] = new int[52];
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		Paint rule = new Paint();
		Rect src = new Rect();
		Rect dst = new Rect();
		int w,h, count;
		
		public draw(Context context) {
			super(context);
			ourHolder = getHolder();
			rc = new Random(System.currentTimeMillis());
			x=0;y=0;
			for(int i=0;i<52;i++){
				crds[i]=0;
			}
			Rules[0]="Waterfall";
			Rules[1]="You";
			Rules[2]="Me";
			Rules[3]="Floor";
			Rules[4]="Guys";
			Rules[5]="Chicks";
			Rules[6]="Heaven";
			Rules[7]="Mate";
			Rules[8]="Bust a rhyme";
			Rules[9]="Catagories";
			Rules[10]="Make a Rule";
			Rules[11]="Quiz master";
			Rules[12]="Everyone";
			rule.setTextSize(25);
			rule.setColor(Color.RED);
			rule.setTextAlign(Align.CENTER);
			h = (bmp.getHeight()/5);
			w = (bmp.getWidth()/13);
			count=0;
			
		}
		public void resume() {
			IsOk=true;
			ourThread = new Thread(this);
			ourThread.start();
		}
		public void pause(){
			IsOk=false;
			while(true){
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			ourThread=null;
		}
		public void run() {
			while(IsOk){
				if(!ourHolder.getSurface().isValid()){
					continue;
				}
				if(next==0){
					x=rc.nextInt(13);
					y=rc.nextInt(4);
					while(crds[(12*y)+x]==1){
						x=rc.nextInt(13);
						y=rc.nextInt(4);
					}
					crds[(12*y)+x]=1;
					x=w*x;
					y=h*y;
					src.set(x, y, x+w, y+h);
					dst.set(50, 50, 150+w, 150+h);
					Canvas c = ourHolder.lockCanvas();
					c.drawColor(Color.GREEN);
					c.drawBitmap(bmp, src, dst, null);
					c.drawText(Rules[x/w], 100, 600, rule);
					ourHolder.unlockCanvasAndPost(c);
					next=1;count++;
				}
				//if(count==52){
				//	IsOk=false;
				//}
				
			}
		}
	}
}