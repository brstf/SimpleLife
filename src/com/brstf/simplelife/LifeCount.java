package com.brstf.simplelife;

import java.util.ArrayList;

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
		p1.setPoisonVisible(mPrefs.getBoolean(getString(R.string.key_poison),
				true));
		p2.setPoisonVisible(mPrefs.getBoolean(getString(R.string.key_poison),
				true));

		setBehindContentView(R.layout.sliding_menu_frame);
		getSlidingMenu().setSecondaryMenu(R.layout.sliding_menu_frame2);
		createSlidingMenus(savedInstanceState);
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
			edit.commit();
		}

		// Register this activity as a listener for preference changes
		mPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	/**
	 * Create the life HistoryInt's, restoring their histories from the saved
	 * bundle if any.
	 * 
	 * @param savedInstanceState
	 *            Bundle with histories saved
	 */
	private void initializeLife(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			// If there's no saved instance state, restore from database
			mDbHelper.open();
			int p1count = mDbHelper.getRowCount(mDbHelper.getP1Table());
			int p2count = mDbHelper.getRowCount(mDbHelper.getP2Table());
			if (p1count != 0 && p2count != 0) {
				p1Life.setHistory(mDbHelper.getAllFrom(mDbHelper.getP1Table()));
				p2Life.setHistory(mDbHelper.getAllFrom(mDbHelper.getP2Table()));
			}
			mDbHelper.close();
			return;
		}

		// Get the player keys
		ArrayList<String> players = savedInstanceState
				.getStringArrayList(getResources().getString(
						R.string.tag_players_life));
		if (players != null) {
			p1Life.setHistory(savedInstanceState.getIntegerArrayList(players
					.get(0)));
			p2Life.setHistory(savedInstanceState.getIntegerArrayList(players
					.get(1)));
		}

		// Get the poison amounts
		ArrayList<String> players_poison = savedInstanceState
				.getStringArrayList(getResources().getString(
						R.string.tag_players_poison));
		if (players_poison != null) {
			p1Controller.setPoison(savedInstanceState.getInt(players_poison
					.get(0)));
			p2Controller.setPoison(savedInstanceState.getInt(players_poison
					.get(1)));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Construct a list of player tags
		ArrayList<String> lh_tags = new ArrayList<String>();
		ArrayList<String> p_tags = new ArrayList<String>();
		String lh_tag = getResources().getString(R.string.tag_lh);
		String p_tag = getResources().getString(R.string.tag_poison);

		// Add a tag for each player we need to restore
		lh_tags.add(lh_tag + "1");
		lh_tags.add(lh_tag + "2");
		outState.putStringArrayList(
				getResources().getString(R.string.tag_players_life), lh_tags);
		outState.putIntegerArrayList(lh_tags.get(0), p1Life.getHistory());
		outState.putIntegerArrayList(lh_tags.get(1), p2Life.getHistory());

		p_tags.add(p_tag + "1");
		p_tags.add(p_tag + "2");
		outState.putStringArrayList(
				getResources().getString(R.string.tag_players_poison), p_tags);

		// For each tag, save the history
		outState.putInt(p_tags.get(0), p1Controller.getCurrentPoison());
		outState.putInt(p_tags.get(1), p2Controller.getCurrentPoison());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// On activity destruction, write the life totals to the database
		mDbHelper.open();
		mDbHelper.clear();
		mDbHelper.addEntries(mDbHelper.getP1Table(), p1Life.getHistory());
		mDbHelper.addEntries(mDbHelper.getP2Table(), p2Life.getHistory());
		mDbHelper.close();
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
	private void createSlidingMenus(Bundle savedInstanceState) {
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

		if (savedInstanceState == null) {
			mLogFragRight = new SlidingMenuLogListFragment();
			mLogFragRight.setControllers(p1Controller, p2Controller);
			this.getFragmentManager().beginTransaction()
					.replace(R.id.sliding_menu_frame, mLogFragRight).commit();
			mLogFragLeft = new SlidingMenuLogListFragment();
			mLogFragLeft.setControllers(p1Controller, p2Controller);
			this.getFragmentManager().beginTransaction()
					.replace(R.id.sliding_menu_frame2, mLogFragLeft).commit();
		} else {
			// TODO: Fix the bug here
			mLogFragRight = (SlidingMenuLogListFragment) this
					.getFragmentManager()
					.findFragmentById(R.id.sliding_menu_frame)
					.getFragmentManager().getBackStackEntryAt(0);
			mLogFragRight.setControllers(p1Controller, p2Controller);
			mLogFragLeft = (SlidingMenuLogListFragment) this
					.getFragmentManager()
					.findFragmentById(R.id.sliding_menu_frame2)
					.getFragmentManager().getBackStackEntryAt(0);
			mLogFragLeft.setControllers(p1Controller, p2Controller);
		}
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
		p1Controller.reset(resetval);
		p2Controller.reset(resetval);
	}

	/**
	 * Sets whether or not the upper display is inverted. Typically called from
	 * the settings fragment.
	 * 
	 * @param invert
	 *            True if the upper display should be inverted, false otherwise.
	 */
	public void setUpperInverted(boolean invert) {
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
	public void setPoisonVisible(boolean visible) {
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
	public void setEntryInterval(float interval) {
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
	public void setQuickReset(boolean quick) {
		mLogFragRight.setQuickReset(quick);
		mLogFragLeft.setQuickReset(quick);
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
		}
	}
}
