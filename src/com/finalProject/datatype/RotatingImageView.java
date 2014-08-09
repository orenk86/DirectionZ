package com.finalProject.datatype;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RotatingImageView extends ImageView {

	int direction = 0;
	
	public RotatingImageView(Context context) {
		super(context);
	}
	public RotatingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public RotatingImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	 // Called when component is to be drawn
	  @Override
	  public void onDraw(Canvas canvas) { // 
	    int height = this.getHeight();  // 
	    int width = this.getWidth();

	    canvas.rotate(direction, width / 2, height / 2); // 
	    super.onDraw(canvas); // 
	  }

	  // Called by Compass to update the orientation
	  public void setDirection(int direction) { // 
	    this.direction = direction;
	    this.invalidate(); // request to be redrawn 
	  }
}
