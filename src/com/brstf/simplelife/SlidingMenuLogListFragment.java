package com.brstf.simplelife;

import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.widgets.LifeLog;
import com.brstf.simplelife.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SlidingMenuLogListFragment extends Fragment {
	private LifeLog m_log1;
	private LifeLog m_log2;
	private LifeController m_lc1;
	private LifeController m_lc2;
	private boolean mOptionsShowing = false;
	private boolean mUpperInverted = true;

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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// We pass our taken list to the adapter. LifeLogSlidingMenuAdapter
		m_log1 = (LifeLog) this.getView().findViewById(R.id.log1);
		m_log2 = (LifeLog) this.getView().findViewById(R.id.log2);
		m_log1.setLifeController(m_lc1);
		m_log2.setLifeController(m_lc2);
		m_log2.setInverse(mUpperInverted);

		// Add listener for the reset button
		ImageButton reset = (ImageButton) this.getView().findViewById(
				R.id.but_reset);
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOptions();
			}
		});
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
		}
	}

	/**
	 * Flip the sliding menu to show the options menu, or flip back if the
	 * options is already showing.
	 */
	protected void showOptions() {
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
				.replace(R.id.sliding_menu_frame, new SettingsFragment())
				.addToBackStack(null).commit();
	}
}
