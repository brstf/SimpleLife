package com.brstf.simplelife;

import java.util.ArrayList;

import com.brstf.simplelife.data.OptionsItem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class SettingsFragment extends Fragment {
	public SettingsFragment() {
		// Reset to 20
		mOptions.add(new OptionsItem(R.layout.options_icon,
				R.drawable.round_arrow_20, R.id.icon));

		// Reset to 20
		mOptions.add(new OptionsItem(R.layout.options_icon,
				R.drawable.round_arrow_40, R.id.icon));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		GridView optionsGrid = (GridView) this.getView().findViewById(
				R.id.options_grid);
		optionsGrid.setAdapter(new SettingsAdapter(this.getView().getContext(),
				0, 0, mOptions));
	}

	private ArrayList<OptionsItem> mOptions = new ArrayList<OptionsItem>();
}
