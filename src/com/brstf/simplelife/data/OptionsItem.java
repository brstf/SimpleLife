package com.brstf.simplelife.data;

public class OptionsItem {
	private int mLayoutId;
	private String mText;
	private int mTextViewId;
	private int mIconId;
	private int mImageButtonId;
	private String mSharedPreferenceName;

	public OptionsItem(int layoutid) {
		this(layoutid, "", -1);
	}

	public OptionsItem(int layoutid, String text, int textid) {
		this(layoutid, text, textid, "");
	}

	public OptionsItem(int layoutid, String text, int textid,
			String preferenceName) {
		this(layoutid, text, textid, -1, -1, preferenceName);
	}

	public OptionsItem(int layoutid, int iconid, int buttonid) {
		this(layoutid, iconid, buttonid, "");
	}

	public OptionsItem(int layoutid, int iconid, int buttonid,
			String preferenceName) {
		this(layoutid, "", -1, iconid, buttonid, preferenceName);
	}

	public OptionsItem(int layoutid, String text, int textid, int iconid,
			int buttonid, String preferenceName) {
		mLayoutId = layoutid;
		mText = text;
		mTextViewId = textid;
		mIconId = iconid;
		mImageButtonId = buttonid;
		mSharedPreferenceName = preferenceName;
	}

	/**
	 * Gets the id of the layout associated with this OptionsItem.
	 * 
	 * @return Id of the layout associated with this OptionsItem
	 */
	public int getLayoutId() {
		return mLayoutId;
	}

	/**
	 * Gets the name of the shared preference associated with this OptionsItem
	 * (if any).
	 * 
	 * @return Shared preference name if it exists, "" if none are associated
	 *         with this item
	 */
	public String getPreferenceName() {
		return mSharedPreferenceName;
	}

	/**
	 * Gets the id of the icon associated with this OptionsItem (if any).
	 * 
	 * @return Id of the icon associated with this OptionsItem if it exists, -1
	 *         if it doesn't
	 */
	public int getIconId() {
		return mIconId;
	}

	/**
	 * Gets the id of the ImageButton where the icon should be displayed in the
	 * layout of this OptionsItem.
	 * 
	 * @return Id of the ImageButton in this item's layout where the icon should
	 *         go, -1 if no such ImageButton exists
	 */
	public int getImageButtonId() {
		return mImageButtonId;
	}

	/**
	 * Get the text associated with this OptionsItem
	 * 
	 * @return Text associated with the OptionsItem
	 */
	public String getText() {
		return mText;
	}

	/**
	 * Gets the id of the TextView where text should be displayed in the layout
	 * of this OptionsItem.
	 * 
	 * @return Id of the TextView in this item's layout where text should go, -1
	 *         if no such text view exists
	 */
	public int getTextViewId() {
		return mTextViewId;
	}

	/**
	 * Checks whether or not this OptionsItem makes use of an Icon.
	 * 
	 * @return True if this OptionsItem has an icon, false otherwise
	 */
	public boolean hasIcon() {
		return mIconId != -1 && mImageButtonId != -1;
	}

	/**
	 * Checks whether or not this OptionsItem makes use of text.
	 * 
	 * @return True if this OptionsItem has text, false otherwise
	 */
	public boolean hasText() {
		return mText != "" && mTextViewId != -1;
	}
}
