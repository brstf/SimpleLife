package com.brstf.magiclife.widgets;

import java.util.List;

import com.example.magiclife.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LogAdapter extends ArrayAdapter<Integer> {
	private class ViewHolder {
		public TextView life_total;
		public TextView life_mod;
	}

	private int row_id;
	private boolean m_invert;

	public LogAdapter(Context context, int resource, List<Integer> objects) {
		this(context, resource, objects, false);
	}

	public LogAdapter(Context context, int resource, List<Integer> objects,
			boolean invert) {
		super(context, resource, objects);
		row_id = resource;
		m_invert = invert;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder h;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(row_id, null);

			// Store View objects in the ViewHolder
			h = new ViewHolder();
			h.life_total = (TextView) v.findViewById(R.id.row_total);
			h.life_mod = (TextView) v.findViewById(R.id.row_lifemod);

			// Store the holder in the View's tag for later retrieval
			v.setTag(h);
		} else {
			// View already exists, get the corresponding holder
			h = (ViewHolder) v.getTag();
		}

		h.life_total.setText(String.valueOf(getItem(position)));

		int bound = m_invert ? getCount() - 1 : 0;
		int mod = m_invert ? 1 : -1;
		if (position == bound) {
			mod = 0;
		} else {
			mod = getItem(position) - getItem(position + mod);
		}

		setModText(h.life_mod, mod);

		return v;
	}

	private void setModText(TextView tv, int mod) {
		tv.setTextColor(getColorFromResource(R.color.black));
		if (mod > 0) {
			tv.setTextColor(getColorFromResource(R.color.green));
		} else if (mod < 0) {
			tv.setTextColor(getColorFromResource(R.color.red));
		}
		String text = (mod > 0 ? "+" : "") + String.valueOf(mod);
		tv.setText(text);
	}

	private int getColorFromResource(int id) {
		return getContext().getResources().getColor(id);
	}
}
