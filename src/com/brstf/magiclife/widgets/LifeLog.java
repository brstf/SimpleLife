package com.brstf.magiclife.widgets;

import java.util.Observable;
import java.util.Observer;

import com.brstf.magiclife.controls.LifeController;
import com.example.magiclife.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class LifeLog extends ObserverLayout implements Observer {
	private ListView m_lv;

	public LifeLog(Context context) {
		super(context);
	}

	public LifeLog(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LifeLog(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void init() {
		super.init();

		m_lv = (ListView) findViewById(R.id.life_log);
	}

	/**
	 * Inflates the layout of this LifeLog during initialization.
	 */
	@Override
	protected void initView() {
		View.inflate(getContext(), R.layout.life_log, this);
	}

	/**
	 * Gets the ListView for the life history.
	 * 
	 * @return ListView for the life history.
	 */
	protected ListView getListView() {
		return m_lv;
	}

	@Override
	public void update(Observable observable, Object data) {
		((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
	}

	@Override
	protected void registerLifeController(final LifeController lc) {
		getListView().setAdapter(
				new LogAdapter(getContext(), R.layout.log_list_row, lc
						.getHistory()));
	}
}
