package com.brstf.simplelife.widgets;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;

import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.R;

public class LifeView extends ObserverLayout {
	private Button m_button_up;
	private Button m_button_down;
	private TextView m_life;
	private TextView m_mod;
	private AlphaAnimation m_anim;

	public LifeView(Context context) {
		super(context);
	}

	public LifeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LifeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void init() {
		super.init();

		m_button_up = (Button) findViewById(R.id.but_up);
		m_button_down = (Button) findViewById(R.id.but_down);
		m_life = (TextView) findViewById(R.id.life_text);
		m_mod = (TextView) findViewById(R.id.life_mod);
	}

	/**
	 * Inflates the layout of this LifeView during initialization.
	 */
	protected void initView() {
		View.inflate(getContext(), R.layout.life_total, this);
	}

	/**
	 * Sets the LifeController object that this LifeView will observe and
	 * interact with the life total through.
	 * 
	 * @param lc
	 *            LifeController to "attach" to.
	 */
	@Override
	protected void registerLifeController(final LifeController lc) {
		addListeners(lc);
		m_life.setText(String.valueOf(getLifeController().getCurrentValue()));
	}

	/**
	 * Creates an OnClickListener to modify the given LifeController by the
	 * given amount.
	 * 
	 * @param lc
	 *            LifeController to modify
	 * @param modification
	 *            Amount to modify by
	 * @return OnClickListener to modify the LifeController as described
	 */
	private OnClickListener createListener(final LifeController lc,
			final int modification) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				lc.incrementBy(modification);
			}
		};
	}

	/**
	 * Convenience method to add click listeners to the buttons of this view.
	 * 
	 * @param lc
	 *            LifeController associated with this view
	 */
	private void addListeners(LifeController lc) {
		m_button_up.setOnClickListener(createListener(lc, 1));
		m_button_down.setOnClickListener(createListener(lc, -1));
	}

	@Override
	public void update(Observable observable, Object data) {
		int lifeTotal = getLifeController().getCurrentValue();
		SpannableString st = new SpannableString(String.valueOf(lifeTotal));
		if (lifeTotal == 6 || lifeTotal == 9) {
			st.setSpan(new UnderlineSpan(), 0, st.length(), 0);
		}
		m_life.setText(st);

		// On a life reset, we don't have a mod to set
		if (this.getLifeController().getHistory().size() != 1) {
			int mod = this.getLifeController().getLastMod();
			m_mod.setText((mod > 0 ? "+" : "") + String.valueOf(mod));
			m_mod.setTextColor(getColorFromResource(R.color.black));
			if (mod > 0) {
				m_mod.setTextColor(getColorFromResource(R.color.green));
			} else if (mod < 0) {
				m_mod.setTextColor(getColorFromResource(R.color.red));
			}

			// Setup alpha animation
			m_anim = new AlphaAnimation(1.0f, 0.0f);
			m_anim.setDuration(getResources().getInteger(
					R.integer.update_interval));
			m_anim.setStartOffset(0L);
			m_anim.setFillAfter(true);
			m_mod.startAnimation(m_anim);
		}

		if (!getLifeController().isUpdating()) {
			m_mod.setText("");
		}
	}

	private int getColorFromResource(int id) {
		return getContext().getResources().getColor(id);
	}
}
