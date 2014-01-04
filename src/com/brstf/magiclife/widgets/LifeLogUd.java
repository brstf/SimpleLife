package com.brstf.magiclife.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import com.brstf.magiclife.controls.LifeController;
import com.example.magiclife.R;

import android.content.Context;
import android.util.AttributeSet;

public class LifeLogUd extends LifeLog {
	private ArrayList<Integer> p_list;
	private LogAdapter m_adapter;

	public LifeLogUd(Context context) {
		super(context);
	}

	public LifeLogUd(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LifeLogUd(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Inflates the layout of this LifeLog during initialization.
	 */
	@Override
	protected void initView() {
		super.initView();
		p_list = new ArrayList<Integer>();
	}

	public void updateList(ArrayList<Integer> list) {
		p_list.clear();
		p_list.addAll(list);
		Collections.reverse(p_list);
		m_adapter.notifyDataSetChanged();
	}

	@Override
	public void update(Observable observable, Object data) {
		updateList(getLifeController().getHistory());
	}

	@Override
	protected void registerLifeController(final LifeController lc) {
		getListView().setStackFromBottom(true);
		m_adapter = new LogAdapter(getContext(), R.layout.log_list_row_ud,
				p_list, true);
		getListView().setAdapter(m_adapter);
		updateList(lc.getHistory());
	}
}
