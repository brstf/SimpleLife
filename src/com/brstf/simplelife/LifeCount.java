package com.brstf.simplelife;

import java.util.ArrayList;

import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.data.HistoryInt;
import com.brstf.simplelife.widgets.LifeView;
import com.brstf.simplelife.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.WindowManager;

public class LifeCount extends SlidingFragmentActivity {
	private LifeController p1Controller;
	private LifeController p2Controller;
	private HistoryInt p1Life;
	private HistoryInt p2Life;
	private SlidingMenuLogListFragment mLogFrag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Restore life histories
		long interval = getResources().getInteger(R.integer.update_interval);
		p1Life = new HistoryInt(20, interval);
		p2Life = new HistoryInt(20, interval);
		initializeLife(savedInstanceState);

		// Don't turn screen off
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.life_count);

		// Initialize Player 1:
		p1Controller = new LifeController(p1Life);
		((LifeView) findViewById(R.id.player1_lv))
				.setLifeController(p1Controller);

		// Initialize Player 2:
		p2Controller = new LifeController(p2Life);
		((LifeView) findViewById(R.id.player2_lv))
				.setLifeController(p2Controller);

		Log.d("LifeCount", "onCreateActivity");
		setBehindContentView(R.layout.sliding_menu_frame);
		createSlidingMenus(savedInstanceState);
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
			return;
		}

		// Get the player keys
		ArrayList<String> players = savedInstanceState
				.getStringArrayList(getResources().getString(
						R.string.tag_players));
		if (players != null) {
			p1Life.setHistory(savedInstanceState.getIntegerArrayList(players
					.get(0)));
			p2Life.setHistory(savedInstanceState.getIntegerArrayList(players
					.get(1)));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Construct a list of player tags
		ArrayList<String> tags = new ArrayList<String>();
		String lh_tag = getResources().getString(R.string.tag_lh);

		// Add a tag for each player we need to restore
		tags.add(lh_tag + "1");
		tags.add(lh_tag + "2");
		outState.putStringArrayList(
				getResources().getString(R.string.tag_players), tags);

		// For each tag, save the history
		outState.putIntegerArrayList(tags.get(0), p1Life.getHistory());
		outState.putIntegerArrayList(tags.get(1), p2Life.getHistory());
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
		menu.setMode(SlidingMenu.RIGHT);

		if (savedInstanceState == null) {
			mLogFrag = new SlidingMenuLogListFragment();
			mLogFrag.setControllers(p1Controller, p2Controller);
			this.getFragmentManager().beginTransaction()
					.replace(R.id.sliding_menu_frame, mLogFrag).commit();
		} else {
			mLogFrag = (SlidingMenuLogListFragment) this.getFragmentManager()
					.findFragmentById(R.id.sliding_menu_frame);
			mLogFrag.setControllers(p1Controller, p2Controller);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& getSlidingMenu().isMenuShowing()) {
			// Check if settings are showing, if they are just pop it off
			if (mLogFrag.getFragmentManager().getBackStackEntryCount() > 0) {
				mLogFrag.getFragmentManager().popBackStack();
			} else {
				// Otherwise, close the sliding menu as normal
				showContent();
			}
			return true;
		}
		return false;
	}
}
