package com.brstf.simplelife;

import java.util.ArrayList;

import com.brstf.simplelife.data.OptionsItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

public class SettingsAdapter extends ArrayAdapter<OptionsItem> {
	private ArrayList<OptionsItem> mOptions;

	public SettingsAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<OptionsItem> objects) {
		super(context, resource, textViewResourceId, objects);

		mOptions = objects;
	}

	@Override
	public int getCount() {
		return mOptions.size();
	}

	@Override
	public OptionsItem getItem(int position) {
		return mOptions.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		OptionsItem item = getItem(position);
		View v = vi.inflate(item.getLayoutId(), null);

		// Add icon if any
		if (item.hasIcon()) {
			ImageButton button = (ImageButton) v.findViewById(item
					.getImageButtonId());
			button.setImageResource(item.getIconId());
		}

		return v;
	}

}
