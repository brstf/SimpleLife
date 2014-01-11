package com.brstf.simplelife;

import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class TextUtils {

	/**
	 * Constructs a SpannableString that underlines ambiguous numbers (positive
	 * numbers constructed with only 6, 8, 9, and 0) so that upside down numbers
	 * are unique in their appearance.
	 * 
	 * @param num
	 *            Number to check for ambiguity with
	 * @return SpannableString representing the number, underlined if it is an
	 *         ambiguous number.
	 */
	public static SpannableString getUnambiguousText(int num) {
		SpannableString st = new SpannableString(String.valueOf(num));

		int copy = num;
		boolean ambiguous = (num >= 0);
		while (copy > 0) {
			int digit = copy % 10;
			if (!(digit == 6 || digit == 8 || digit == 9 || digit == 0)) {
				ambiguous = false;
			}
			copy = (int) copy / 10;
		}

		if (ambiguous) {
			st.setSpan(new UnderlineSpan(), 0, st.length(), 0);
		}
		return st;
	}

	/**
	 * Set the text of a given TextView to represent a given modification
	 * including proper coloring.
	 * 
	 * @param tv
	 *            TextView to update the text of
	 * @param mod
	 *            Integer modification to set the text with
	 */
	public static void modTextView(TextView tv, int mod, Resources res) {
		String sMod = modValueOf(mod);

		tv.setText(sMod);
		tv.setTextColor(res.getColor(R.color.black));
		if (mod > 0) {
			tv.setTextColor(res.getColor(R.color.green));
		} else if (mod < 0) {
			tv.setTextColor(res.getColor(R.color.red));
		}
	}

	/**
	 * Given a mod value, create a String representation of the mod : -x -->
	 * "-x", 0 --> "", x --> "+x".
	 * 
	 * @param mod
	 *            Integer mod to create the string for
	 * @return String representation of the integer mod value
	 */
	public static String modValueOf(int mod) {
		if (mod == 0) {
			return "";
		}

		return (mod > 0 ? "+" : "") + String.valueOf(mod);
	}
}
