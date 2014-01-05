package com.brstf.simplelife.widgets;

import java.util.List;

import com.brstf.simplelife.R;

import android.content.Context;
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

	/**
	 * Overrides default constructor and initializes this LogAdapter as
	 * non-inverted.
	 * 
	 * @param context
	 *            The current context.
	 * @param resource
	 *            The resource ID for a layout file containing a TextView to use
	 *            when instantiating views.
	 * @param objects
	 *            The objects to represent in the ListView.
	 */
	public LogAdapter(Context context, int resource, List<Integer> objects) {
		this(context, resource, objects, false);
	}

	/**
	 * Typical ArrayAdapter constructor, but also accepts whether or not this
	 * adapter should be inverted. When inverted, new items appear at the top
	 * rather than the bottom.
	 * 
	 * @param context
	 *            The current context.
	 * @param resource
	 *            The resource ID for a layout file containing a TextView to use
	 *            when instantiating views.
	 * @param objects
	 *            The objects to represent in the ListView.
	 */
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

		// If inverted get items from the end of the array first
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

	/**
	 * Given a TextView and modification amount, set the text of the TextView to
	 * be the mod amount with changes depending on positive/negative mod.
	 * 
	 * @param tv
	 *            TextView to set the text of
	 * @param mod
	 *            Integer value to use to set the text of tv
	 */
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

	/**
	 * Convenience method to get color from resource id
	 * 
	 * @param id
	 *            Id of the color to retrieve
	 * @return Color specified by id
	 */
	private int getColorFromResource(int id) {
		return getContext().getResources().getColor(id);
	}
}
