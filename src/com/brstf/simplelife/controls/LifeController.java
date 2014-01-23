package com.brstf.simplelife.controls;

import java.util.ArrayList;
import java.util.Observable;

import com.brstf.simplelife.data.HistoryInt;

public class LifeController extends Observable {
	private HistoryInt life;
	private int m_poison = 0;

	/**
	 * Constructor that takes in a HistoryInt object for this controller to
	 * control.
	 * 
	 * @param life_history
	 *            HistoryInt for this to control
	 */
	public LifeController(HistoryInt life_history) {
		this.life = life_history;
	}

	/**
	 * Increments the life total and updates accordingly.
	 */
	public void increment() {
		this.incrementBy(1);
	}

	/**
	 * Decrements the life total and updates accordingly.
	 */
	public void decrement() {
		this.decrementBy(1);
	}

	/**
	 * Gets the current value of the HistoryInt.
	 * 
	 * @return Current value of the HistoryInt
	 */
	public int getCurrentValue() {
		return this.life.getCurrentValue();
	}

	/**
	 * Gets the most recent modification of the HistoryInt.
	 * 
	 * @return Difference between most recent value and second most recent value
	 */
	public int getLastMod() {
		return this.life.getCurrentValue()
				- this.life.getHistory().get(this.life.getHistory().size() - 2);
	}

	/**
	 * Gets the history of values for this LifeController.
	 * 
	 * @return ArrayList of integers in this object's history
	 */
	public ArrayList<Integer> getHistory() {
		return this.life.getHistory();
	}

	/**
	 * Checks whether or not the underlying HistoryInt is updating right now.
	 * 
	 * @return True if at the current time, the most recent HistoryInt total is
	 *         still being modified, false otherwise
	 */
	public boolean isUpdating() {
		return this.life.isUpdating(System.currentTimeMillis());
	}

	/**
	 * Increments the life total by the given amount and updates accordingly.
	 */
	public void incrementBy(int i) {
		this.life.setValue(this.life.getCurrentValue() + i,
				System.currentTimeMillis());
		triggerObservers();
	}

	/**
	 * Decrements the life total by the given amount and updates accordingly.
	 */
	public void decrementBy(int i) {
		this.incrementBy(-i);
	}

	/**
	 * Increments poison by 1.
	 */
	public void incrementPoison() {
		this.incrementPoisonBy(1);
	}

	/**
	 * Decrements poison by 1.
	 */
	public void decrementPoison() {
		this.incrementPoisonBy(-1);
	}

	/**
	 * Increments poison by the given amount.
	 * 
	 * @param mod
	 *            Amount to modify the poison total by
	 */
	public void incrementPoisonBy(int mod) {
		this.setPoison(this.m_poison + mod);
	}

	/**
	 * Wrapper method to set the history of the underlying HistoryInt object to
	 * the given history.
	 * 
	 * @param history
	 *            Integer list history to set the life to
	 */
	public void setHistory(ArrayList<Integer> history) {
		life.setHistory(history);
	}

	/**
	 * Sets the poison amount to the given value.
	 * 
	 * @param poison
	 *            New poison amount
	 */
	public void setPoison(int poison) {
		this.m_poison = poison;
		this.m_poison = this.m_poison < 0 ? 0 : this.m_poison;
		this.m_poison = this.m_poison > 10 ? 10 : this.m_poison;
		triggerObservers();
	}

	/**
	 * Decrements poison by the given amount.
	 * 
	 * @param mod
	 *            Amount to modify the poison total by
	 */
	public void decrementPoisonBy(int mod) {
		this.incrementPoisonBy(-mod);
	}

	/**
	 * Get the current poison amount.
	 * 
	 * @return Current poison total
	 */
	public int getCurrentPoison() {
		return this.m_poison;
	}

	/**
	 * Resets the life total.
	 */
	public void reset() {
		this.life.reset();
		this.m_poison = 0;
		triggerObservers();
	}

	/**
	 * Resets the life total to the given value.
	 * 
	 * @param resetval
	 *            Value to reset the Life totals to
	 */
	public void reset(int resetval) {
		this.life.reset(resetval);
		this.setPoison(0);
		triggerObservers();
	}

	/**
	 * Private method to update any observers that are watching this object.
	 */
	private void triggerObservers() {
		setChanged();
		notifyObservers();
	}
}
