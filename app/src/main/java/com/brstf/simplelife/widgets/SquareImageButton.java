package com.brstf.simplelife.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class SquareImageButton extends ImageButton {

	public SquareImageButton(Context context) {
		super(context);
	}

	public SquareImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);

		super.onMeasure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
	}
}
