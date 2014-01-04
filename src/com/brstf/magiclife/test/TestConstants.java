package com.brstf.magiclife.test;

import java.util.ArrayList;

import com.brstf.magiclife.LifeLogSlidingMenuItem;

public final class TestConstants {

	public static ArrayList<LifeLogSlidingMenuItem> getP1List() {
		ArrayList<LifeLogSlidingMenuItem> p1list = new ArrayList<LifeLogSlidingMenuItem>();
		p1list.add(new LifeLogSlidingMenuItem(20, 0));
		p1list.add(new LifeLogSlidingMenuItem(19, -1));
		p1list.add(new LifeLogSlidingMenuItem(15, -4));
		p1list.add(new LifeLogSlidingMenuItem(17, 2));
		p1list.add(new LifeLogSlidingMenuItem(12, -5));
		p1list.add(new LifeLogSlidingMenuItem(13, 1));
		p1list.add(new LifeLogSlidingMenuItem(14, 1));
		p1list.add(new LifeLogSlidingMenuItem(15, 1));
		p1list.add(new LifeLogSlidingMenuItem(10, -5));
		p1list.add(new LifeLogSlidingMenuItem(11, 1));
		p1list.add(new LifeLogSlidingMenuItem(0, -11));
		return p1list;
	}

	public static ArrayList<LifeLogSlidingMenuItem> getP2List() {
		ArrayList<LifeLogSlidingMenuItem> p2list = new ArrayList<LifeLogSlidingMenuItem>();
		p2list.add(new LifeLogSlidingMenuItem(20, 0));
		p2list.add(new LifeLogSlidingMenuItem(19, -1));
		p2list.add(new LifeLogSlidingMenuItem(18, -1));
		p2list.add(new LifeLogSlidingMenuItem(17, -1));
		p2list.add(new LifeLogSlidingMenuItem(15, -2));
		p2list.add(new LifeLogSlidingMenuItem(14, -1));
		return p2list;
	}
}
