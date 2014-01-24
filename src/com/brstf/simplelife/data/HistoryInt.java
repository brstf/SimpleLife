package com.brstf.simplelife.data;

import java.util.ArrayList;

public class HistoryInt {
	private ArrayList<Integer> value_history = new ArrayList<Integer>();
	private long last_time = 0L;
	private int initval;
	private long interval;

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
	 * Gets the last time (in milliseconds) that this value was updated.
	 * 
	 * @return Last time (in milliseconds) that this value was updated
	 */
	public long getLastTimeModified() {
		return this.last_time;
	}

	/**
	 * Gets the number of modifications made to this HistoryInt (how many
	 * entries in the history there are).
	 * 
	 * @return Number of modifications made to the HistoryInt
	 */
	public int getSize() {
		return this.value_history.size();
	}

	/**
	 * Check whether the most recent HistoryInt total is being modified.
	 * 
	 * @param time
	 *            Time at which this check is made
	 * @return True if time is within the interval last_time --> last_time +
	 *         interval_time, false otherwise
	 */
	public boolean isUpdating(long time) {
		return time >= last_time && time <= last_time + interval;
	}

	/**
	 * Pop the last change off of this HistoryInt.
	 * 
	 * @return Most recent total or -1 if there are no values to pop
	 */
	public int pop() {
		if (this.getSize() >= 1) {
			return this.value_history.remove(this.getSize() - 1);
		} else {
			return -1;
		}
	}

	/**
	 * Resets the value to the initial value, clearing out the history.
	 */
	public void reset() {
		this.reset(this.initval);
	}

	/**
	 * Resets the value to the given reset value, clearing out the history.
	 * 
	 * @param resetval
	 *            Value to reset the HistoryInt to
	 */
	public void reset(int resetval) {
		this.initval = resetval;
		this.value_history.clear();
		this.last_time = 0L;
		this.value_history.add(this.initval);
	}

	/**
	 * Sets the history of this HistoryInt to be the given history.
	 * 
	 * @param history
	 *            New list of integers to use as a history for this HistoryInt
	 */
	public void setHistory(ArrayList<Integer> history) {
		this.value_history = new ArrayList<Integer>(history);
	}

	/**
	 * Sets the update interval of this HistoryInt.
	 * 
	 * @param interval
	 *            New time interval (in ms)
	 */
	public void setInterval(long interval) {
		this.interval = interval;
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
			this.value_history.add(value);
			last_time = time;
		} else {
			if (value == this.value_history.get(this.getSize() - 2)) {
				this.value_history.remove(this.getSize() - 1);
				last_time = 0;
			} else {
				this.value_history.set(this.getSize() - 1, value);
				last_time = time;
			}
		}
	}
}
