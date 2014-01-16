package com.brstf.simplelife;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SettingsFragment extends Fragment {

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
}
