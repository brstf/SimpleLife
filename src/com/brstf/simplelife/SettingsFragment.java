package com.brstf.simplelife;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brstf.simplelife.LifeCount;

public class SettingsFragment extends Fragment implements AnimationListener {
	private TextView m_invertText;
	private Animation m_anim1;
	private Animation m_anim2;
	private CheckBox m_invert_cb;
	private CheckBox m_poison_cb;
	private CheckBox m_wake_cb;
	private CheckBox m_quick_cb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Add click listeners to the reset buttons
		ImageButton reset = (ImageButton) getView()
				.findViewById(R.id.but_reset);
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reset(getActivity()
						.getPreferences(Context.MODE_PRIVATE)
						.getInt(getActivity().getString(R.string.key_total), 20));
			}
		});

		int resetval = getActivity().getPreferences(Context.MODE_PRIVATE)
				.getInt(getActivity().getString(R.string.key_total), 20);
		changeResetVal(resetval);

		RadioGroup reset_radio = (RadioGroup) getView().findViewById(
				R.id.settings_reset_val_rg);
		switch (resetval) {
		case 20:
			reset_radio.check(R.id.settings_rg_20);
			break;
		case 30:
			reset_radio.check(R.id.settings_rg_30);
			break;
		case 40:
			reset_radio.check(R.id.settings_rg_40);
			break;
		}
		reset_radio
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.settings_rg_20:
							changeResetVal(20);
							break;
						case R.id.settings_rg_30:
							changeResetVal(30);
							break;
						case R.id.settings_rg_40:
							changeResetVal(40);
							break;
						}
					}
				});

		// Set Poison Check Box Listener
		m_poison_cb = (CheckBox) getView().findViewById(R.id.poison_check);
		boolean showPoison = getActivity().getPreferences(Context.MODE_PRIVATE)
				.getBoolean(getString(R.string.key_poison), true);
		m_poison_cb.setChecked(showPoison);
		((Button) getView().findViewById(R.id.but_poison))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						m_poison_cb.setChecked(!m_poison_cb.isChecked());
						((LifeCount) getActivity())
								.setPoisonVisible(m_poison_cb.isChecked());

					}
				});

		// Load the flip animations
		m_anim1 = AnimationUtils.loadAnimation(this.getActivity(),
				R.animator.to_middle);
		m_anim1.setAnimationListener(this);
		m_anim2 = AnimationUtils.loadAnimation(this.getActivity(),
				R.animator.from_middle);
		m_anim2.setAnimationListener(this);

		// Set the check button listener
		m_invert_cb = (CheckBox) getView().findViewById(
				R.id.settings_invert_check);
		boolean inverted = getActivity().getPreferences(Context.MODE_PRIVATE)
				.getBoolean(getString(R.string.key_invert), true);
		m_invertText = (TextView) getView().findViewById(R.id.invert_tv);
		m_invertText.setRotation(inverted ? 180.0f : 0.0f);
		m_invert_cb.setChecked(inverted);
		((Button) this.getView().findViewById(R.id.but_invert))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						m_invert_cb.setChecked(!m_invert_cb.isChecked());
						m_invertText.setAnimation(m_anim1);
						m_invertText.startAnimation(m_anim1);
						((LifeCount) getActivity())
								.setUpperInverted(m_invert_cb.isChecked());
					}
				});

		// Set wake lock change listener
		m_wake_cb = (CheckBox) getView().findViewById(R.id.settings_wake_check);
		m_wake_cb.setChecked(getActivity().getPreferences(Context.MODE_PRIVATE)
				.getBoolean(getString(R.string.key_wake), true));
		((Button) getView().findViewById(R.id.but_wake))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						m_wake_cb.setChecked(!m_wake_cb.isChecked());
						((LifeCount) getActivity()).setWakeLock(m_wake_cb
								.isChecked());
					}
				});

		// Set quick reset change listener
		m_quick_cb = (CheckBox) getView().findViewById(
				R.id.settings_quick_check);
		m_quick_cb.setChecked(getActivity()
				.getPreferences(Context.MODE_PRIVATE).getBoolean(
						getString(R.string.key_quick), false));
		((Button) getView().findViewById(R.id.but_quick))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						m_quick_cb.setChecked(!m_quick_cb.isChecked());
						((LifeCount) getActivity()).setQuickReset(m_quick_cb
								.isChecked());
						
						if (m_quick_cb.isChecked()) {
							Toast.makeText(
									getActivity(),
									"Quick-Reset enabled. Tap settings button to reset life "
											+ "totals, long-press it to show settings",
									Toast.LENGTH_LONG).show();
						}
					}
				});

		// Set entry time
		setEntryText(getActivity().getPreferences(Context.MODE_PRIVATE)
				.getFloat(getString(R.string.key_entry), 2.0f));
		((Button) getView().findViewById(R.id.entry_but_up))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						changeEntryTime(0.25f);
					}
				});
		((Button) getView().findViewById(R.id.entry_but_down))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						changeEntryTime(-0.25f);
					}
				});

		// Setup github button
		ImageButton but_github = (ImageButton) this.getView().findViewById(
				R.id.but_github);
		but_github.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.github.com"));
				startActivity(browserIntent);
			}
		});
	}

	/**
	 * Modify the time before a life modification is "locked-in" by adding the
	 * given modification to the current value. This saves the new value in the
	 * shared preferences and sets the TextView text.
	 * 
	 * @param mod
	 *            Amount to change the entry time by
	 */
	private void changeEntryTime(float mod) {
		float time = getActivity().getPreferences(Context.MODE_PRIVATE)
				.getFloat(getString(R.string.key_entry), 2.0f);
		if ((time <= 1.0f && mod < 0.0f) || (time >= 6.0f && mod > 0.0f)) {
			return;
		}

		setEntryText(time + mod);
		((LifeCount) getActivity()).setEntryInterval(time + mod);
	}

	private void setEntryText(float time) {
		((TextView) getView().findViewById(R.id.settings_entry_tv))
				.setText(String.format("%.2f", time));
	}

	protected void changeResetVal(int reset) {
		getActivity().getPreferences(Context.MODE_PRIVATE).edit()
				.putInt(getActivity().getString(R.string.key_total), reset)
				.commit();
		((TextView) this.getView().findViewById(R.id.settings_reset_text))
				.setText(String.valueOf(reset));
	}

	/**
	 * Resets the LifeCount and pops this fragment off the stack. Note: This
	 * fragment should only ever be added to the back stack when adding this
	 * fragment.
	 * 
	 * @param resetval
	 *            Integer value to reset the life totals to
	 */
	private void reset(int resetval) {
		((LifeCount) getActivity()).reset(resetval);
		SettingsFragment.this.getFragmentManager().popBackStack();
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (animation == m_anim1) {
			if (m_invert_cb.isChecked()) {
				m_invertText.setRotation(180.0f);
			} else {
				m_invertText.setRotation(0.0f);
			}
			m_invertText.setGravity(Gravity.CENTER);
			m_invertText.clearAnimation();
			m_invertText.startAnimation(m_anim2);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationStart(Animation animation) {

	}
}
