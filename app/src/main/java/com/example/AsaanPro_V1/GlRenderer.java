package com.example.AsaanPro_V1;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import java.util.concurrent.Semaphore;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GlRenderer implements Renderer {
	
	Line line;
	final Semaphore SyncObj = new Semaphore(1, true);

		    /** Constructor */
		    public GlRenderer() {
		        this.line = new Line();
		        SyncObj.release();
		    }
		    
	@Override
	public void onDrawFrame(GL10 gl) {
		
		try {
			SyncObj.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
// clear Screen and Depth Buffer
	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	 
	        // Reset the Modelview Matrix
	        gl.glLoadIdentity();
	 
	        // Drawing
	        gl.glTranslatef(0.0f, 0.0f, 0.0f);
	        line.draw(gl);
		        
	        gl.glFlush();
		        
	        SyncObj.release();
		}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) {                       //Prevent A Divide By Zero By
			height = 1;                         //Making Height Equal One
		}
				 
        gl.glViewport(0, 0, width, height);     //Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION);    //Select The Projection Matrix
        gl.glLoadIdentity();                    //Reset The Projection Matrix
				 
        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.0f, 0.0f);
				 
	    gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix
				        
	    gl.glLoadIdentity();                    //Reset The Modelview Matrix
			
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}
}
