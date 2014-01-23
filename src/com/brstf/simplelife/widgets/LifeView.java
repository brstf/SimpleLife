package com.brstf.simplelife.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Observable;

import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.R;
import com.brstf.simplelife.TextUtils;

public class LifeView extends ObserverLayout {
	private Button m_button_up;
	private Button m_button_down;
	private Button m_button_5up;
	private Button m_button_5down;
	private ImageButton m_button_poison;
	private PoisonView m_poison;
	private TextView m_life;
	private TextView m_mod;
	private AlphaAnimation m_mod_anim;
	private AlphaAnimation m_text_anim;
	private boolean m_inverse = false;

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
		m_button_5up = (Button) findViewById(R.id.but_5up);
		m_button_5down = (Button) findViewById(R.id.but_5down);
		m_button_poison = (ImageButton) findViewById(R.id.poison_button);
		m_poison = (PoisonView) findViewById(R.id.poison_layout);
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
		m_poison.setLifeController(lc);
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
				if (m_poison.isOpaque()) {
					lc.incrementPoisonBy(modification);
				} else {
					lc.incrementBy(modification);
				}
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
		m_button_5up.setOnClickListener(createListener(lc, 5));
		m_button_5down.setOnClickListener(createListener(lc, -5));
		m_button_poison.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LifeView.this.togglePoison();
			}
		});
	}

	/**
	 * Toggles editing of poison counters
	 */
	public void togglePoison() {
		if (m_poison.isOpaque()) {
			m_text_anim = new AlphaAnimation(0.15f, 1.0f);
		} else {
			m_text_anim = new AlphaAnimation(1.0f, 0.15f);
		}
		m_text_anim.setDuration(getResources().getInteger(
				R.integer.poison_reveal_time));
		m_text_anim.setStartOffset(0L);
		m_text_anim.setFillAfter(true);
		m_life.startAnimation(m_text_anim);

		this.m_poison.togglePoison();
	}

	/**
	 * Sets whether or not this life view should be inversed or not.
	 * 
	 * @param inverse
	 *            True if this LifeView should be inversed, which flips the
	 *            text, poison counters, mod, etc. False otherwise.
	 */
	public void setInversed(boolean inverse) {
		this.m_inverse = inverse;
		if (this.m_inverse) {
			this.setRotation(180.0f);
		} else {
			this.setRotation(0.0f);
		}
	}

	/**
	 * Sets whether or not the poison controls / counters are visible.
	 * 
	 * @param visible
	 *            True if they should be visible, false otherwise
	 */
	public void setPoisonVisible(boolean visible) {
		int viscode = visible ? View.VISIBLE : View.INVISIBLE;
		this.m_poison.setVisibility(viscode);
		this.m_button_poison.setVisibility(viscode);
		if (this.m_poison.isOpaque()) {
			this.togglePoison();
		}
	}

	/**
	 * Set whether or not the "+5/-5" buttons are enabled.
	 * 
	 * @param bigmod
	 *            True if +5/-5 buttons should be enabled, false otherwise
	 */
	public void setBigmodEnabled(boolean bigmod) {
		int viscode = bigmod ? View.VISIBLE : View.INVISIBLE;
		m_button_5up.setVisibility(viscode);
		m_button_5down.setVisibility(viscode);
	}

	@Override
	public void update(Observable observable, Object data) {
		m_life.setText(TextUtils.getUnambiguousText(getLifeController()
				.getCurrentValue()));

		// On a life reset, we don't have a mod to set
		if (this.getLifeController().getHistory().size() != 1) {
			int mod = this.getLifeController().getLastMod();
			TextUtils.modTextView(m_mod, mod, getResources());

			// Setup alpha animation
			m_mod_anim = new AlphaAnimation(1.0f, 0.0f);
			m_mod_anim.setDuration((long) (1000.0f * ((Activity) getContext())
					.getPreferences(Context.MODE_PRIVATE).getFloat(
							((Activity) getContext())
									.getString(R.string.key_entry), 2.0f)));
			m_mod_anim.setStartOffset(0L);
			m_mod_anim.setFillAfter(true);
			m_mod.startAnimation(m_mod_anim);
		}

		if (!getLifeController().isUpdating()) {
			m_mod.setText("");
		}
	}
}
