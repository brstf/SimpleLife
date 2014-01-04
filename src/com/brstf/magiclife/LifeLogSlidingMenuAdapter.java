package com.brstf.magiclife;

import java.util.ArrayList;
import java.util.List;

import com.example.magiclife.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LifeLogSlidingMenuAdapter extends
		ArrayAdapter<LifeLogSlidingMenuItem> {
	private int rowViewResourceId;
	private List<LifeLogSlidingMenuItem> slidingMenuItemsList = new ArrayList<LifeLogSlidingMenuItem>();

	/**
	 * Overrides the superclass ArrayAdapter constructor and initializes this
	 * adapter with the given parameters.
	 * 
	 * @param context
	 *            Context that the adapter is being created for
	 * @param textViewResourceId
	 *            Resource id for the text main text view
	 * @param objects
	 *            List of menu items to put in the list
	 */
	public LifeLogSlidingMenuAdapter(Context context, int textViewResourceId,
			List<LifeLogSlidingMenuItem> objects) {
		super(context, textViewResourceId, objects);

		this.rowViewResourceId = textViewResourceId;
		this.slidingMenuItemsList = objects;
	}

	/**
	 * Retrieves the count of items in the list.
	 * 
	 * @return Number of items in the list
	 */
	public int getCount() {
		return this.slidingMenuItemsList.size();
	}

	/**
	 * Retrieves the list item at a specified location.
	 * 
	 * @param index
	 *            Index of the item to retrieve
	 * @return Item at the given index
	 */
	public LifeLogSlidingMenuItem getItem(int index) {
		return this.slidingMenuItemsList.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(this.rowViewResourceId, parent, false);
		}
		LifeLogSlidingMenuItem slidingMenuListItem = getItem(position);

		TextView slidingMenuItemTotal = (TextView) row
				.findViewById(R.id.row_total);
		TextView slidingMenuItemMod = (TextView) row
				.findViewById(R.id.row_lifemod);

		slidingMenuItemTotal.setText(String
				.valueOf(slidingMenuListItem.lifeTotal));
		slidingMenuItemMod.setText(String.valueOf(slidingMenuListItem.lifeMod));

		return row;
	}
}
