package com.brstf.simplelife.menu;

import com.brstf.simplelife.SlidingMenuLogListFragment;
import com.brstf.simplelife.controls.LifeController;
import com.brstf.simplelife.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;

public class LifeLogMenuBuilder {
	public static SlidingMenu buildMenu(Activity activity, LifeController lc1,
			LifeController lc2) {
		SlidingMenu menu = new SlidingMenu(activity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.sliding_menu_frame);
		menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
		menu.setMode(SlidingMenu.RIGHT);

		SlidingMenuLogListFragment logFrag = new SlidingMenuLogListFragment();
		logFrag.setControllers(lc1, lc2);
		activity.getFragmentManager().beginTransaction()
				.replace(R.id.sliding_menu_frame, logFrag).commit();

		return menu;
	}
}
