package com.brstf.simplelife.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TextView object that displays its contents upside down.
 */
public class UpsideDownText extends TextView {

	public UpsideDownText(Context context) {
		super(context);
	}

	public UpsideDownText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onDraw(Canvas canvas) {
		// save the current matrix
		canvas.save();

		// Flip the text around the center
		float cx = this.getWidth() / 2.0f;
		float cy = this.getHeight() / 2.0f;
		canvas.rotate(180, cx, cy);
		super.onDraw(canvas);

		// restore the old matrix
		canvas.restore();
	}
}
