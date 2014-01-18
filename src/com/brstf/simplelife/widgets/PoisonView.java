package com.brstf.simplelife.widgets;

import java.util.Observable;

import com.brstf.simplelife.R;
import com.brstf.simplelife.controls.LifeController;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class PoisonView extends ObserverLayout {
	private boolean m_poison_mode = false;
	private AlphaAnimation m_PoisonAnim;

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
				((ImageView) this.getChildAt(i))
						.setImageResource(R.drawable.black_drop);
				this.getChildAt(i).setAlpha(0.5f);
			} else {
				((ImageView) this.getChildAt(i))
						.setImageResource(R.drawable.gray_drop);
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
				this.getChildAt(i).setAlpha(0.5f);
				this.getChildAt(i).startAnimation(m_PoisonAnim);
			}
		}
	}

}
