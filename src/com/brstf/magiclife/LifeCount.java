package com.brstf.magiclife;

import com.brstf.magiclife.controls.LifeController;
import com.brstf.magiclife.data.HistoryInt;
import com.brstf.magiclife.menu.LifeLogMenuBuilder;
import com.brstf.magiclife.widgets.LifeView;
import com.example.magiclife.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.WindowManager;

import java.util.Observable;
import java.util.Observer;

public class LifeCount extends Activity implements Observer {
	private LifeController p1Controller;
	private LifeController p2Controller;
	private HistoryInt p1Life;
	private HistoryInt p2Life;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Don't turn screen off
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// TODO: Restore life totals / histories from savedInstanceState
		// onResume
		setContentView(R.layout.life_count);
		getActionBar().hide();

		// Initialize Player 1:
		long interval = getResources().getInteger(R.integer.update_interval);
		p1Life = new HistoryInt(20, interval);
		p1Controller = new LifeController(p1Life);
		((LifeView) findViewById(R.id.player1_lv))
				.setLifeController(p1Controller);

		// Initialize Player 2:
		p2Life = new HistoryInt(20, interval);
		p2Controller = new LifeController(p2Life);
		((LifeView) findViewById(R.id.player2_lv))
				.setLifeController(p2Controller);

		createSlidingMenus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.life_count, menu);
		return true;
	}

	private void createSlidingMenus() {
		LifeLogMenuBuilder.buildMenu(this, p1Controller, p2Controller);
	}

	@Override
	public void update(Observable observable, Object data) {

	}
}
