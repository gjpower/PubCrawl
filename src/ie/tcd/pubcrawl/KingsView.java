package ie.tcd.pubcrawl;

import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class KingsView extends SurfaceView implements Runnable{
	int kingrule;
	boolean next=true;
	int count;
	SurfaceHolder ourHolder;
	Thread ourThread;
	boolean IsOk = false;
	Random rc;
	String Rules[]= new String[13];
	int crds[] = new int[52];
	Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.background_main_small);
	Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.packcards);
	Paint rule = new Paint();
	Rect src = new Rect();
	Rect dst = new Rect();
	int x,y,w,h;
	
	public KingsView(Context context) {
		super(context);
		ourHolder = getHolder();
		rc = new Random(System.currentTimeMillis());
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
		Rules[12]="Add to the King cup";
		rule.setTextSize(25);
		rule.setColor(Color.YELLOW);
		rule.setTextAlign(Align.LEFT);
		h = (bmp2.getHeight()/5);
		w = (bmp2.getWidth()/13);
		count=0;kingrule=0;
		x=0;y=0;
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
				e.printStackTrace();
			}
			break;
		}
		ourThread=null;
	}
	public void run() {
		while((IsOk)&&(count<54)){
			if(!ourHolder.getSurface().isValid()){
				continue;
			}
			if(next){
				if(kingrule==4){
					Canvas c = ourHolder.lockCanvas();
					src.set(0, 0, bmp1.getWidth(), bmp1.getHeight());
					dst.set(0,0, c.getWidth(), c.getHeight());
					c.drawBitmap(bmp1, src, dst, null);
					src.set(x, y, x+w, y+h);
					dst.set((c.getWidth()/5), (c.getHeight()/10), ((c.getWidth()*4)/5), ((c.getHeight()*7)/10));
					c.drawBitmap(bmp2, src, dst, null);
					c.drawText("All king have been drawn", (c.getWidth()/5), ((c.getHeight()*8)/10), rule);
					c.drawText("Down the King cup", (c.getWidth()/5), ((c.getHeight()*8)/10)+25, rule);
					c.drawText("Kings! Game Over", (c.getWidth()/5), ((c.getHeight()*8)/10)+50, rule);
					ourHolder.unlockCanvasAndPost(c);
					kingrule++;
				}
				else{
					x=rc.nextInt(13);
					y=rc.nextInt(4);
					while(crds[(13*y)+x]==1){
						x=rc.nextInt(13);
						System.out.println(count);
						y=rc.nextInt(4);
					}
					if(x==12){
						kingrule++;
					}
					crds[(13*y)+x]=1;
					x=w*x;
					y=h*y;
					Canvas c = ourHolder.lockCanvas();
					src.set(0, 0, bmp1.getWidth(), bmp1.getHeight());
					dst.set(0,0, c.getWidth(), c.getHeight());
					c.drawBitmap(bmp1, src, dst, null);
					src.set(x, y, x+w, y+h);
					dst.set((c.getWidth()/5), (c.getHeight()/10), ((c.getWidth()*4)/5), ((c.getHeight()*7)/10));
					c.drawBitmap(bmp2, src, dst, null);
					c.drawText(Rules[x/w], (c.getWidth()/5), ((c.getHeight()*8)/10), rule);
					ourHolder.unlockCanvasAndPost(c);
				}
				next=false;count++;
				System.out.println(count);
			}
		}
	}
}
