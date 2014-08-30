package com.brstf.simplelife.widgets;

import java.util.Observer;

import com.brstf.simplelife.controls.LifeController;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * ObserverLayout is a simple extension of RelativeLayout that implements
 * Observer to listen to various Observable objects. In our case, that object is
 * the LifeController.
 * 
 */
public abstract class ObserverLayout extends RelativeLayout implements Observer {
	private LifeController m_lc;

	public ObserverLayout(Context context) {
		super(context);
		init();
	}

	public ObserverLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ObserverLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * Initializes necessary components of this ObserverLayout.
	 */
	protected void init() {
		initView();
	}

	/**
	 * Inflates the layout of this LifeLog during initialization.
	 */
	protected abstract void initView();

	/**
	 * Sets the LifeController of this ObserverLayout.
	 * 
	 * @param lc
	 *            New LifeController for this ObserverLayout
	 */
	public void setLifeController(final LifeController lc) {
		m_lc = lc;
		lc.addObserver(this);
		registerLifeController(lc);
	}

	/**
	 * Get the LifeController associated with this ObserverLayout if any.
	 * 
	 * @return LifeController associated with this ObserverLayout
	 */
	public LifeController getLifeController() {
		return m_lc;
	}

	/**
	 * Called after LifeController is changed, perform any action that needs to
	 * be performed after a new LifeController is set.
	 * 
	 * @param lc
	 *            New LifeController for the layout.
	 */
	protected abstract void registerLifeController(final LifeController lc);
}
