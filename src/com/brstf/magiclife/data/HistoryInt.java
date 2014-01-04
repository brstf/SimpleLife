package com.brstf.magiclife.data;

import java.util.ArrayList;

public class HistoryInt {
	private ArrayList<Integer> value_history = new ArrayList<Integer>();
	private ArrayList<Integer> value_mods = new ArrayList<Integer>();
	private long last_time = 0L;
	private int initval;
	private final long interval;

	/**
	 * Default constructor, initializes the starting value to 0.
	 */
	public HistoryInt() {
		this(0);
	}

	/**
	 * Constructor that initializes the starting value to the given initial
	 * value.
	 * 
	 * @param initial_value
	 *            Starting value for the HistoryInt
	 */
	public HistoryInt(int initial_value) {
		this(initial_value, 0L);
	}

	/**
	 * Constructor that initializes the starting value to the given initial
	 * value and also sets the update interval (the maximum time in between
	 * updates before a new history entry is added).
	 * 
	 * @param initial_value
	 *            Starting value for the HistoryInt
	 * @param update_interval
	 *            Maximum time in between updates before a new history entry
	 *            will be added
	 */
	public HistoryInt(int initial_value, long update_interval) {
		this.initval = initial_value;
		this.interval = update_interval;
		this.reset();
	}

	/**
	 * Gets the most recent value for this HistoryArray object.
	 * 
	 * @return Current value of the array.
	 */
	public int getCurrentValue() {
		return value_history.get(value_history.size() - 1);
	}

	/**
	 * Gets the history of values for this HistoryInt.
	 * 
	 * @return ArrayList of integers in this object's history
	 */
	public ArrayList<Integer> getHistory() {
		return value_history;
	}

	/**
	 * Gets the most recent modification to the HistoryInt.
	 * 
	 * @return Most recent modification
	 */
	public int getLastMod() {
		return value_mods.get(value_mods.size() - 1);
	}

	/**
	 * Gets the last time (in milliseconds) that this value was updated.
	 * 
	 * @return Last time (in milliseconds) that this value was updated
	 */
	public long getLastTimeModified() {
		return this.last_time;
	}

	/**
	 * Resets the value to the initial value, clearing out all the logs.
	 */
	public void reset() {
		this.value_history.clear();
		this.value_mods.clear();
		this.last_time = 0L;
		this.value_history.add(this.initval);
		this.value_mods.add(0);
	}

	/**
	 * Sets the current value to the given value, taking into account the time
	 * this modification is being made and the last time the value was updated.
	 * This also appropriately updates the value modification.
	 * 
	 * @param value
	 *            New integer value
	 * @param time
	 *            Time (in milliseconds) that this modification is being made
	 */
	public void setValue(int value, long time) {
		if (time - this.last_time > this.interval) {
			this.value_mods.add(value - getCurrentValue());
			this.value_history.add(value);
		} else {
			this.value_mods.set(this.value_history.size() - 1, value
					- this.value_history.get(this.value_history.size() - 2));
			this.value_history.set(this.value_history.size() - 1, value);
		}
		last_time = time;
	}
}
