package com.example.AsaanPro_V1;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Line {
	     
	volatile FloatBuffer vertexBuffer;   // buffer holding the vertices
	private volatile ByteBuffer vertexByteBuffer;
	volatile int RefreshCount;
	volatile int iColorIndex;

	public Line() {
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		//(250 samples per second * 2.5 second graph)/2 * 3 co-ordinates * 4 byte size_of_float * 12 ECG leads = 45000
		//LEAD_XSPAN * 3 co-ordinates * 4 byte size_of_float * 12 ECG leads
		vertexByteBuffer = ByteBuffer.allocateDirect(425 * 3 * 4 * 12);

		vertexByteBuffer.order(ByteOrder.nativeOrder());

		// allocates the memory from the byte buffer
		vertexBuffer = vertexByteBuffer.asFloatBuffer();

		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);

		RefreshCount = 0;
	}
	    
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		// set the colour
		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);

		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		// Draw the vertices as line strips
		for(int i=0; i<12; i++) {
			gl.glDrawArrays(GL10.GL_LINE_STRIP, 425*i, 425);
		}
//		for(int i=0; i<12; i++) {
//			if(i == iColorIndex)
//				gl.glColor4f(1.0f, 0.0f, 1.0f, 1.0f);
//			else
//				gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
//			gl.glDrawArrays(GL10.GL_LINE_STRIP, 425*i, 425);
//		}
		RefreshCount = 0;

		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
