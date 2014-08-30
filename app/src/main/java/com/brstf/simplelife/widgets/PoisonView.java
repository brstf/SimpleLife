package com.brstf.simplelife.widgets;

import java.util.Observable;

import com.brstf.simplelife.R;
import com.brstf.simplelife.controls.LifeController;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class PoisonView extends ObserverLayout {
	private boolean m_poison_mode = false;
	private AlphaAnimation m_PoisonAnim;
	private int darkGrayId;
	private int lightGrayId;

	public PoisonView(Context context) {
		super(context);
	}

	public PoisonView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PoisonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void update(Observable observable, Object data) {
		updatePoisonView();
	}

	@Override
	protected void initView() {
		View.inflate(getContext(), R.layout.poison_view, this);

		for (int i = 0; i < this.getChildCount(); ++i) {
			this.getChildAt(i).setAlpha(0.0f);
		}

		// Check if we're using a dark theme or a light theme and switch the
		// poison drawable resource appropriately
		boolean darkTheme = false;
		int style = ((Activity) getContext()).getPreferences(
				Context.MODE_PRIVATE).getInt(
				getContext().getString(R.string.key_theme),
				R.style.AppBaseThemeLight);
		switch (style) {
		case R.style.AppBaseThemeLight:
			darkTheme = false;
			break;
		case R.style.AppBaseThemeDark:
		case R.style.AppBaseThemeBlack:
			darkTheme = true;
			break;
		}

		if (!darkTheme) {
			darkGrayId = R.drawable.gray_drop_darker;
			lightGrayId = R.drawable.gray_drop;
		} else {
			darkGrayId = R.drawable.gray_drop;
			lightGrayId = R.drawable.gray_drop_darker;
		}
	}

	/**
	 * Gets whether or not the PoisonView is opaque (i.e. gray drops), or
	 * transparent (only black drops showing).
	 * 
	 * @return True if all drops show, false otherwise
	 */
	public boolean isOpaque() {
		return m_poison_mode;
	}

	@Override
	protected void registerLifeController(LifeController lc) {
		updatePoisonView();
	}

	public void updatePoisonView() {
		// Update the poison views
		for (int i = 0; i < this.getChildCount(); ++i) {
			if (i < getLifeController().getCurrentPoison()) {
				((ImageView) this.getChildAt(i)).setImageResource(darkGrayId);
				this.getChildAt(i).setAlpha(1.0f);
			} else {
				((ImageView) this.getChildAt(i)).setImageResource(lightGrayId);
				if (!m_poison_mode) {
					this.getChildAt(i).setAlpha(0.0f);
				}
			}
		}
	}

	/**
	 * Method to toggle the view of the poison counters. Animates the poison
	 * counters and properly changes the mode.
	 */
	public void togglePoison() {
		float start = m_poison_mode ? 1.0f : 0.0f;
		float end = m_poison_mode ? 0.0f : 1.0f;
		m_PoisonAnim = new AlphaAnimation(start, end);
		m_PoisonAnim.setDuration(getResources().getInteger(
				R.integer.poison_reveal_time));
		m_PoisonAnim.setStartOffset(0L);
		m_PoisonAnim.setFillAfter(true);
		m_poison_mode = !m_poison_mode;
		for (int i = 0; i < this.getChildCount(); ++i) {
			if (i >= this.getLifeController().getCurrentPoison()) {
				this.getChildAt(i).setAlpha(1.0f);
				this.getChildAt(i).startAnimation(m_PoisonAnim);
			}
		}
	}
}
