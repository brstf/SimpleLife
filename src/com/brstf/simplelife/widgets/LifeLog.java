package com.brstf.simplelife.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class LifeLog extends ObserverLayout implements Observer {
	private ListView m_lv;
	private boolean m_inverse;
	private ArrayList<Integer> m_list;
	private LogAdapter m_adapter;

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
		m_list = new ArrayList<Integer>();
		setInverse(false);
	}

	/**
	 * Sets whether or not this log is inversed or not.
	 * 
	 * @param inverse
	 *            True if this list should be flipped upside down, false
	 *            otherwise
	 */
	public void setInverse(boolean inverse) {
		m_inverse = inverse;
		int rowid = R.layout.log_list_row;
		getListView().setStackFromBottom(false);
		if (m_inverse) {
			getListView().setStackFromBottom(true);
			rowid = R.layout.log_list_row_ud;
		}
		m_adapter = new LogAdapter(getContext(), rowid, m_list, m_inverse);
		getListView().setAdapter(m_adapter);
		if (getLifeController() != null) {
			m_list.clear();
			m_list.addAll(getLifeController().getHistory());
			if (m_inverse) {
				Collections.reverse(m_list);
			}
		}
		updateList();
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

	public void updateList() {
		m_adapter.notifyDataSetChanged();
		if (!m_inverse) {
			getListView().setSelection(m_adapter.getCount() - 1);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		m_list.clear();
		m_list.addAll(getLifeController().getHistory());
		if (m_inverse) {
			Collections.reverse(m_list);
		}
		updateList();
	}

	@Override
	protected void registerLifeController(final LifeController lc) {
		setInverse(m_inverse);
	}
}
