package com.brstf.magiclife.widgets;

import com.example.magiclife.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class LifeViewUd extends LifeView {

	public LifeViewUd(Context context) {
		super(context);
	}

	public LifeViewUd(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LifeViewUd(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void initView() {
		View.inflate(getContext(), R.layout.life_total_ud, this);
	}
}
