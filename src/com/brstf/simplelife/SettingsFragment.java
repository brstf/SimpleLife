package com.brstf.simplelife;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brstf.simplelife.LifeCount;

public class SettingsFragment extends Fragment implements AnimationListener {
	private TextView m_invertText;
	private Animation m_anim1;
	private Animation m_anim2;
	private CheckBox m_invert_cb;
	private CheckBox m_poison_cb;
	private CheckBox m_wake_cb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Add click listeners to the reset buttons
		ImageButton reset20 = (ImageButton) getView().findViewById(
				R.id.but_reset20);
		reset20.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reset(20);
			}
		});
		ImageButton reset40 = (ImageButton) getView().findViewById(
				R.id.but_reset40);
		reset40.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reset(40);
			}
		});

		// Set Poison Check Box Listener
		m_poison_cb = (CheckBox) getView().findViewById(R.id.poison_check);
		boolean showPoison = getActivity().getPreferences(Context.MODE_PRIVATE)
				.getBoolean(getString(R.string.key_poison), true);
		m_poison_cb.setChecked(showPoison);
		m_poison_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				((LifeCount) getActivity()).setPoisonVisible(isChecked);
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
		m_invert_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				m_invertText.setAnimation(m_anim1);
				m_invertText.startAnimation(m_anim1);
				((LifeCount) getActivity()).setUpperInverted(isChecked);
			}
		});

		// Set wake lock change listener
		m_wake_cb = (CheckBox) getView().findViewById(R.id.settings_wake_check);
		m_wake_cb.setChecked(getActivity().getPreferences(Context.MODE_PRIVATE)
				.getBoolean(getString(R.string.key_wake), true));
		m_wake_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				((LifeCount) getActivity()).setWakeLock(isChecked);
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
	 * Resets the LifeCount and pops this fragment off the stack. Note: This
	 * fragment should only ever be added to the back stack when adding this
	 * fragment.
	 * 
	 * @param resetval
	 *            Integer value to reset the life totals to
	 */
	private void reset(int resetval) {
		((LifeCount) getActivity()).reset(resetval);
		getActivity().getPreferences(Context.MODE_PRIVATE).edit()
				.putInt(getActivity().getString(R.string.key_total), resetval)
				.commit();
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
