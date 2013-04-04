package ie.tcd.pubcrawl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

class DiceRenderer implements GLSurfaceView.Renderer{
	
	private Context context;
	private Dice dice;

	// For controlling dice's z-position, x and y angles and speeds 
	float angleX = 0;
	float angleY = 0;
	float speedX = 0;
	float speedY = 0;
	float z = -6.0f;
	float decelX = 0.1f;
	float decelY = 0.1f;
	
	boolean nearEnd = false;
	int endCount = 10;
	float angleOffX = 0.0f;
	float angleOffY = 0.0f;
	
	private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
	private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};
	
	public DiceRenderer(Context context) {
		this.context = context;
		dice = new Dice();
	}
	
	// Call back when the surface is first created or re-created.
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
		gl.glClearDepthf(1.0f);				// Set depth's clear-value to farthest
		gl.glEnable(GL10.GL_DEPTH_TEST);	// Enables depth-buffer for hidden surface removal
		gl.glDepthFunc(GL10.GL_LEQUAL);	 // The type of depth testing to do
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
		gl.glShadeModel(GL10.GL_SMOOTH);	// Enable smooth shading of colour
		gl.glDisable(GL10.GL_DITHER);		// Disable dithering for better performance
	 
		// Setup Texture, each time the surface is created
		dice.Load_Texture(gl, context);		// Load images into textures
		gl.glEnable(GL10.GL_TEXTURE_2D);	// Enable texture
		
		// Setup lighting GL_LIGHT1 with ambient and diffuse lights 
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient, 0);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse, 0);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition, 0);
		gl.glEnable(GL10.GL_LIGHT1);   // Enable Light 1 
		gl.glEnable(GL10.GL_LIGHT0);   // Enable the default Light 0 
	}

	
	// Call back after onSurfaceCreated() or whenever the window's size changes
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) height = 1;	// To prevent divide by zero
		float aspect = (float)width / height;
	
		// Set the viewport (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);
  
		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
		gl.glLoadIdentity();					  // Reset projection matrix
		// Use perspective projection
		GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);
  
		gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
		gl.glLoadIdentity();					  // Reset
	}
	
	// Call back to draw the current frame.
	public void onDrawFrame(GL10 gl) {
		// Clear colour and depth buffers
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// Enable lighting 
		gl.glEnable(GL10.GL_LIGHTING);
  
		// ----- Render the Dice -----
		gl.glLoadIdentity();					// Reset the model-view matrix
		gl.glTranslatef(0.0f, 0.0f, -6.0f);		// Translate into the screen
		gl.glTranslatef(0.0f, 0.0f, z);   		// Translate into the screen 
		gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // Rotate 
		gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // Rotate 
		dice.Draw(gl);
	      
		// Update the rotational angle after each refresh 
		angleX += speedX;
		angleY += speedY;
		
		angleX = angleX%360;
		angleY = angleY%360;
		
		
		
		if(!nearEnd)
		{	
			System.out.println("Angle X is " + angleX);
			System.out.println("Angle Y is " + angleY);
			
			speedX -= decelX*speedX;
			speedY -= decelY*speedY;
			
			System.out.println("Speed X is " + speedX);
			System.out.println("Speed Y is " + speedY);
			
			if(((speedX*speedX)+(speedY*speedY))<0.01)
			{
				nearEnd = true;
				endCount = 15;
				angleOffX = angleX%90;
				angleOffY = angleY%90;
				
				if(angleOffX > 45)
					speedX = (90-angleOffX)/15;
				else
					if(angleOffX > -45)
						speedX = (-angleOffX)/15;
					else
						speedX = (-90-angleOffX)/15;
				
				if(angleOffY > 45)
					speedY = (90-angleOffY)/15;
				else
					if(angleOffY > -45)
						speedY = (-angleOffY)/15;
					else
						speedY = (-90-angleOffY)/15;
				
				System.out.println("angleOffX is " + angleOffX);
				System.out.println("speedX is " + speedX);
				System.out.println("angleOffY is " + angleOffY);
				System.out.println("speedY is " + speedY);
			}
		}
		
		if(nearEnd)
		{
			if(endCount > 0)
			{
				endCount--;
			}
			else
			{
				speedX = 0;
				speedY = 0;
				nearEnd = false;
			}
			
		}
	}
}
