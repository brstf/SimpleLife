package com.brstf.simplelife;

import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.widgets.LifeLog;
import com.brstf.simplelife.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class SlidingMenuLogListFragment extends Fragment {
	private LifeLog m_log1;
	private LifeLog m_log2;
	private LifeController m_lc1;
	private LifeController m_lc2;
	private boolean mOptionsShowing = false;
	private boolean mUpperInverted = true;
	private TextView m_p2tv;
	private boolean m_quick;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.sliding_menu_log, null);
	}

	/**
	 * Sets the life controllers for the two associated life logs in this menu.
	 * Should be called before the fragment is created.
	 * 
	 * @param lc1
	 *            LifeController for the first life total
	 * @param lc2
	 *            LifeController for the second life total
	 */
	public void setControllers(LifeController lc1, LifeController lc2) {
		m_lc1 = lc1;
		m_lc2 = lc2;
	}

	@Override
	public void onResume() {
		super.onResume();

		// We pass our taken list to the adapter. LifeLogSlidingMenuAdapter
		m_log1 = (LifeLog) this.getView().findViewById(R.id.log1);
		m_log2 = (LifeLog) this.getView().findViewById(R.id.log2);

		m_log1.setLifeController(m_lc1);
		m_log2.setLifeController(m_lc2);
		m_log2.setInverse(mUpperInverted);

		m_p2tv = (TextView) this.getView().findViewById(R.id.tv_p2);
		flipText(mUpperInverted);

		// Add listener for the reset button
		ImageButton reset = (ImageButton) this.getView().findViewById(
				R.id.but_reset);
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// If quick reset is disabled, show options, otherwise reset
				if (!getActivity().getPreferences(Context.MODE_PRIVATE)
						.getBoolean(
								getActivity().getString(R.string.key_quick),
								false)) {
					showOptions();
				} else {
					((LifeCount) getActivity()).reset();
				}
			}
		});

		reset.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showOptions();
				return true;
			}
		});

		m_quick = this.getActivity().getPreferences(Context.MODE_PRIVATE)
				.getBoolean(getString(R.string.key_quick), false);
		int id = m_quick ? R.drawable.reset_gear
				: R.drawable.ic_action_settings;
		if (this.getView() != null) {
			((ImageButton) this.getView().findViewById(R.id.but_reset))
					.setImageResource(id);
		}
	}

	/**
	 * Properly flip the "Player 2" text view when the upper display is
	 * inverted.
	 * 
	 * @param invert
	 *            True if the upper display is inverted, false otherwise
	 */
	private void flipText(boolean invert) {
		m_p2tv.setRotation(invert ? 180.0f : 0.0f);
		float pleft = invert ? getResources().getDimension(
				R.dimen.sliding_text_padding) : 0.0f;
		float pright = invert ? 0.0f : getResources().getDimension(
				R.dimen.sliding_text_padding);
		m_p2tv.setPadding((int) pleft, 0, (int) pright, 0);
	}

	/**
	 * Sets whether or not the upper display is inverted. Typically called from
	 * the settings fragment.
	 * 
	 * @param invert
	 *            True if the upper display should be inverted, false otherwise.
	 */
	public void setUpperInverted(boolean invert) {
		mUpperInverted = invert;
		if (m_log2 != null) {
			m_log2.setInverse(invert);
			this.flipText(invert);
		}
	}

	/**
	 * Flip the sliding menu to show the options menu, or flip back if the
	 * options is already showing.
	 */
	public void showOptions() {
		if (mOptionsShowing) {
			getFragmentManager().popBackStack();
			mOptionsShowing = false;
		}

		mOptionsShowing = true;
		getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.animator.flip_right_in,
						R.animator.flip_right_out, R.animator.flip_left_in,
						R.animator.flip_left_out)
				.replace(this.getId(), new SettingsFragment(),
						this.getTag() + "_OPTIONS").addToBackStack(null)
				.commit();
	}

	/**
	 * Get whether or not quick reset is enabled.
	 * 
	 * @return True if quick reset is enabled, false otherwise
	 */
	public boolean getQuickReset() {
		return m_quick;
	}

	/**
	 * Sets whether or not quick reset is enabled.
	 * 
	 * @param quick
	 *            True if quick reset should be enabled, false otherwise
	 */
	public void setQuickReset(boolean quick) {
		m_quick = quick;
		int id = quick ? R.drawable.reset_gear : R.drawable.ic_action_settings;
		if (this.getView() != null) {
			((ImageButton) this.getView().findViewById(R.id.but_reset))
					.setImageResource(id);
		}
	}
}
