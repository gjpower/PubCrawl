package ie.tcd.pubcrawl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class DiceView extends GLSurfaceView{
	DiceRenderer renderer;
	
	// For touch event
	private final float TOUCH_SCALE_FACTOR = 40.0f / 320.0f;
	private float previousX;
	private float previousY;

	// Constructor - Allocate and set the renderer
	public DiceView(Context context) {
		super(context);
		renderer = new DiceRenderer(context);
		this.setRenderer(renderer);
		// Request focus, otherwise key/button won't react
		this.requestFocus();  
		this.setFocusableInTouchMode(true);
	}
	
	// Handler for touch event
	@Override
	public boolean onTouchEvent(final MotionEvent evt) {
		float currentX = evt.getX();
		float currentY = evt.getY();
		float deltaX, deltaY;
		switch (evt.getAction()) {
			case MotionEvent.ACTION_MOVE:
				// Modify linear velocities according to movement
				deltaX = currentX - previousX;
				deltaY = currentY - previousY;
				renderer.speedX += deltaY * TOUCH_SCALE_FACTOR;
				renderer.speedY += deltaX * TOUCH_SCALE_FACTOR;
		}
		// Save current x, y
		previousX = currentX;
		previousY = currentY;
		return true;  // Event handled
	}
}
