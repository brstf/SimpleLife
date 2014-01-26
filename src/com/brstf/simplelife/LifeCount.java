package com.brstf.simplelife;

import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.data.HistoryInt;
import com.brstf.simplelife.data.LifeDbAdapter;
import com.brstf.simplelife.widgets.LifeView;
import com.brstf.simplelife.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.WindowManager;

public class LifeCount extends SlidingFragmentActivity implements
		OnSharedPreferenceChangeListener {
	private LifeController p1Controller;
	private LifeController p2Controller;
	private HistoryInt p1Life;
	private HistoryInt p2Life;
	private SlidingMenuLogListFragment mLogFragRight;
	private SlidingMenuLogListFragment mLogFragLeft;
	private SharedPreferences mPrefs;
	private LifeDbAdapter mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupPreferences();
		mDbHelper = new LifeDbAdapter(this);

		// Restore life histories
		int start_total = mPrefs.getInt(getString(R.string.key_total),
				getResources().getInteger(R.integer.default_total));
		long interval = getResources().getInteger(R.integer.update_interval);
		p1Life = new HistoryInt(start_total, interval);
		p2Life = new HistoryInt(start_total, interval);
		p1Controller = new LifeController(p1Life);
		p2Controller = new LifeController(p2Life);
		initializeLife(savedInstanceState);

		// Don't turn screen off
		setWakeFlag(mPrefs.getBoolean(getString(R.string.key_wake), true));
		setContentView(R.layout.life_count);

		// Initialize Player 1:
		LifeView p1 = (LifeView) findViewById(R.id.player1_lv);
		p1.setLifeController(p1Controller);
		p1.setInversed(false);

		// Initialize Player 2:
		LifeView p2 = (LifeView) findViewById(R.id.player2_lv);
		p2.setInversed(mPrefs.getBoolean(getString(R.string.key_invert), true));
		p2.setLifeController(p2Controller);

		// Set poison visibility
		setPoisonVisible(mPrefs.getBoolean(getString(R.string.key_poison),
				false));
		setBigmodChanged(mPrefs
				.getBoolean(getString(R.string.key_bigmod), true));

		setBehindContentView(R.layout.sliding_menu_frame);
		getSlidingMenu().setSecondaryMenu(R.layout.sliding_menu_frame2);
		createSlidingMenus();
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register this activity as a listener for preference changes
		mPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	/**
	 * Setup a SharedPreferences with default values if it doesn't exist, then
	 * read preferences used for setup.
	 */
	private void setupPreferences() {
		mPrefs = getPreferences(Context.MODE_PRIVATE);

		// Fill SharedPreferences with default information if they don't exist
		if (mPrefs.getAll().size() == 0) {
			SharedPreferences.Editor edit = mPrefs.edit();
			edit.putBoolean(getString(R.string.key_invert), true);
			edit.putInt(getString(R.string.key_total), getResources()
					.getInteger(R.integer.default_total));
			edit.putInt(getString(R.string.key_uppernum), getResources()
					.getInteger(R.integer.default_upper));
			edit.putInt(getString(R.string.key_changes), getResources()
					.getInteger(R.integer.default_changes));
			edit.putBoolean(getString(R.string.key_poison), false);
			edit.putBoolean(getString(R.string.key_wake), true);
			edit.putBoolean(getString(R.string.key_quick), false);
			edit.putFloat(getString(R.string.key_entry), 2.0f);
			edit.putBoolean(getString(R.string.key_bigmod), true);
			edit.putInt(getString(R.string.key_dice_sides), 6);
			edit.putInt(getString(R.string.key_dice_num), 2);
			edit.commit();
		}
	}

	/**
	 * Create the life HistoryInt's, restoring their histories from the saved
	 * bundle if any.
	 * 
	 * @param savedInstanceState
	 *            Bundle with histories saved
	 */
	private void initializeLife(Bundle savedInstanceState) {
		// If there's no saved instance state, restore from database
		mDbHelper.open();
		int p1count = mDbHelper.getRowCount(LifeDbAdapter.getP1Table());
		int p2count = mDbHelper.getRowCount(LifeDbAdapter.getP2Table());
		if (p1count != 0 && p2count != 0) {
			mDbHelper.restoreLife(LifeDbAdapter.getP1Table(), p1Controller);
			mDbHelper.restoreLife(LifeDbAdapter.getP2Table(), p2Controller);
		}
		mDbHelper.close();

		if (p1Controller.getHistory().size() == 0
				|| p2Controller.getHistory().size() == 0) {
			reset();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// On activity destruction, write the life totals to the database
		mDbHelper.open();
		mDbHelper.clear();
		mDbHelper.addLife(LifeDbAdapter.getP1Table(), p1Controller);
		mDbHelper.addLife(LifeDbAdapter.getP2Table(), p2Controller);
		mDbHelper.close();

		// Unregister preference listener
		mPrefs.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.life_count, menu);
		return true;
	}

	/**
	 * Create the sliding menus for this activity. If savedInstanceState is not
	 * null, the menu fragment can simply be retrieved from the fragment
	 * manager.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). Note: Otherwise it is
	 *            null.
	 */
	private void createSlidingMenus() {
		SlidingMenu menu = getSlidingMenu();
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);
		menu.setMenu(R.layout.sliding_menu_frame);
		menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setSecondaryShadowDrawable(R.drawable.sliding_menu_shadow_right);
		menu.setOnClosedListener(new OnClosedListener() {
			@Override
			public void onClosed() {
				closeOptions();
			}
		});

		mLogFragRight = new SlidingMenuLogListFragment();
		mLogFragRight.setControllers(p1Controller, p2Controller);
		mLogFragLeft = new SlidingMenuLogListFragment();
		mLogFragLeft.setControllers(p1Controller, p2Controller);
		this.getFragmentManager().beginTransaction()
				.replace(R.id.sliding_menu_frame, mLogFragRight, "RIGHT")
				.replace(R.id.sliding_menu_frame2, mLogFragLeft, "LEFT")
				.commit();
		mLogFragRight.setUpperInverted(mPrefs.getBoolean(
				getString(R.string.key_invert), true));
		mLogFragLeft.setUpperInverted(mPrefs.getBoolean(
				getString(R.string.key_invert), true));
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& getSlidingMenu().isMenuShowing()) {
			// Try to close options before the sliding menu
			if (!closeOptions()) {
				// Otherwise, close the sliding menu as normal
				showContent();
			}
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	private boolean closeOptions() {
		// Check if settings are showing, if they are just pop it off
		if (mLogFragRight.getFragmentManager().getBackStackEntryCount() > 0) {
			mLogFragRight.getFragmentManager().popBackStack();
			return true;
		}
		if (mLogFragLeft.getFragmentManager().getBackStackEntryCount() > 0) {
			mLogFragLeft.getFragmentManager().popBackStack();
			return true;
		}
		return false;
	}

	/**
	 * Get the SharedPreferences of this Activity.
	 * 
	 * @return SharedPreferences object associated with this Activtiy.
	 */
	public SharedPreferences getPrefs() {
		return mPrefs;
	}

	/**
	 * Resets both life totals to their starting values.
	 */
	public void reset() {
		saveStats();
		p1Controller.reset();
		p2Controller.reset();
	}

	/**
	 * Resets both life totals to the given value.
	 * 
	 * @param resetval
	 *            Value to reset the life totals to
	 */
	public void reset(int resetval) {
		saveStats();
		p1Controller.reset(resetval);
		p2Controller.reset(resetval);
	}

	/**
	 * Save the stats of the current life controllers to their respective
	 * tables.
	 */
	private void saveStats() {
		mDbHelper.open();
		mDbHelper.addStatsFromTo(p1Controller, LifeDbAdapter.getP1StatsTable());
		mDbHelper.addStatsFromTo(p2Controller, LifeDbAdapter.getP2StatsTable());
		mDbHelper.close();
	}

	/**
	 * Sets whether or not the upper display is inverted. Typically called from
	 * the settings fragment.
	 * 
	 * @param invert
	 *            True if the upper display should be inverted, false otherwise.
	 */
	private void setUpperInverted(boolean invert) {
		((LifeView) findViewById(R.id.player2_lv)).setInversed(invert);
		mLogFragRight.setUpperInverted(invert);
		mLogFragLeft.setUpperInverted(invert);
	}

	/**
	 * Sets whether or not the poison counters / toggle button are visible.
	 * 
	 * @param visible
	 *            True if the poison items would be visible, false otherwise
	 */
	private void setPoisonVisible(boolean visible) {
		((LifeView) findViewById(R.id.player2_lv)).setPoisonVisible(visible);
		((LifeView) findViewById(R.id.player1_lv)).setPoisonVisible(visible);
	}

	/**
	 * Sets or clears the wake flag to keep screen on or let it turn off.
	 * 
	 * @param wake
	 *            True if screen should stay on, false otherwise
	 */
	private void setWakeFlag(boolean wake) {
		if (wake) {
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	/**
	 * Set the entry interval time (in seconds), the time until a modification
	 * to a life total is "locked-in".
	 * 
	 * @param interval
	 *            Entry interval time (in seconds)
	 */
	private void setEntryInterval(float interval) {
		long entrytime = (long) (interval * 1000.0);
		p1Life.setInterval(entrytime);
		p2Life.setInterval(entrytime);
	}

	/**
	 * Set whether or not the "Quick-Reset" option is enabled.
	 * 
	 * @param quick
	 *            True if quick-reset should be enabled, false otherwise
	 */
	private void setQuickReset(boolean quick) {
		mLogFragRight.setQuickReset(quick);
		mLogFragLeft.setQuickReset(quick);
	}

	/**
	 * Set whether or not the "+5/-5" buttons are enabled.
	 * 
	 * @param bigmod
	 *            True if +5/-5 buttons should be enabled, false otherwise
	 */
	private void setBigmodChanged(boolean bigmod) {
		((LifeView) findViewById(R.id.player2_lv)).setBigmodEnabled(bigmod);
		((LifeView) findViewById(R.id.player1_lv)).setBigmodEnabled(bigmod);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// Properly respond depending on which preference was changed
		if (key == getString(R.string.key_poison)) {
			// Poison Changed
			setPoisonVisible(mPrefs.getBoolean(key, false));
		} else if (key == getString(R.string.key_invert)) {
			// Inverted upper life view changed
			setUpperInverted(mPrefs.getBoolean(key, true));
		} else if (key == getString(R.string.key_wake)) {
			// Wake lock setting changed
			setWakeFlag(mPrefs.getBoolean(key, true));
		} else if (key == getString(R.string.key_quick)) {
			// Quick reset setting changed
			setQuickReset(mPrefs.getBoolean(key, false));
		} else if (key == getString(R.string.key_entry)) {
			// Entry time changed
			setEntryInterval(mPrefs.getFloat(key, 2.0f));
		} else if (key == getString(R.string.key_bigmod)) {
			// Bigmod preference changed
			setBigmodChanged(mPrefs.getBoolean(key, true));
		}
	}
}
