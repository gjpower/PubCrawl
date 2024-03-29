package ie.tcd.pubcrawl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
/*
 * A cube with texture. 
 * Define the vertices for only one representative face.
 * Render the cube by translating and rotating the face.
 */
public class Dice {
	private FloatBuffer vertexBuffer;  // Vertex Buffer
	private FloatBuffer texBuffer;	  // Texture Coords Buffer
	
	private int numFaces = 6;
	private int[] imageFileIDs = {  // Image file IDs
		R.drawable.dice1,
		R.drawable.dice2,
		R.drawable.dice6,
		R.drawable.dice5,
		R.drawable.dice4,
		R.drawable.dice3
	};
	private int[] textureIDs = new int[numFaces];
	private Bitmap[] bitmap = new Bitmap[numFaces];
	private float diceHalfSize = 1.0f;
		  
	// Constructor - Set up the vertex buffer
	public Dice() {
		// Allocate vertex buffer. An float has 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
  
		// Read images. Find the aspect ratio and adjust the vertices accordingly.
		for (int face = 0; face < numFaces; face++) {
			// Define the vertices for this face
			float[] vertices = {
					-1.0f, -1.0f, 0.0f,  // 0. left-bottom-front
					 1.0f, -1.0f, 0.0f,  // 1. right-bottom-front
					-1.0f,  1.0f, 0.0f,  // 2. left-top-front
					 1.0f,  1.0f, 0.0f   // 3. right-top-front
			};
			vertexBuffer.put(vertices);  // Populate
		}
		vertexBuffer.position(0);	 // Rewind
  
		// Allocate texture buffer. An float has 4 bytes. Repeat for 6 faces.
		float[] texCoords = {
			0.0f, 1.0f, // A. left-bottom
			1.0f, 1.0f, // B. right-bottom
			0.0f, 0.0f, // C. left-top
			1.0f, 0.0f	// D. right-top
		};
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4 * numFaces);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		for (int face = 0; face < numFaces; face++) {
			texBuffer.put(texCoords);
		}
		texBuffer.position(0);	// Rewind
	}
	
	// Render the shape
	public void Draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
  
		// front
		gl.glPushMatrix();
		gl.glTranslatef(0f, 0f, diceHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();
  
		// left
		gl.glPushMatrix();
		gl.glRotatef(270.0f, 0f, 1f, 0f);
		gl.glTranslatef(0f, 0f, diceHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
		gl.glPopMatrix();
  
		// back
		gl.glPushMatrix();
		gl.glRotatef(180.0f, 0f, 1f, 0f);
		gl.glTranslatef(0f, 0f, diceHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[2]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
		gl.glPopMatrix();
  
		// right
		gl.glPushMatrix();
		gl.glRotatef(90.0f, 0f, 1f, 0f);
		gl.glTranslatef(0f, 0f, diceHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
		gl.glPopMatrix();
  
		// top
		gl.glPushMatrix();
		gl.glRotatef(270.0f, 1f, 0f, 0f);
		gl.glTranslatef(0f, 0f, diceHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[4]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
		gl.glPopMatrix();
  
		// bottom
		gl.glPushMatrix();
		gl.glRotatef(90.0f, 1f, 0f, 0f);
		gl.glTranslatef(0f, 0f, diceHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[5]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
		gl.glPopMatrix();
	
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
  
	// Load images into 6 GL textures
	public void Load_Texture(GL10 gl, Context context) {
		gl.glGenTextures(6, textureIDs, 0); // Generate texture-ID array for 6 IDs
  
		// Generate OpenGL texture images
		for (int face = 0; face < numFaces; face++) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);
			// Build Texture from loaded bitmap for the currently-bind texture ID
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);
		
			InputStream istream = context.getResources().openRawResource(imageFileIDs[face]);
		      try {
		         // Read and decode input as bitmap
		         bitmap[face] = BitmapFactory.decodeStream(istream);
		      } finally {
		         try {
		            istream.close();
		         } catch(IOException e) { }
		      }
			
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap[face], 0);
				
			bitmap[face].recycle();
		}
	}
}