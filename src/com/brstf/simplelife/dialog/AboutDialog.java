package com.brstf.simplelife.dialog;

import com.brstf.simplelife.R;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.widget.TextView;

public class AboutDialog extends DialogFragment {

	public static AlertDialog create(Context context) {
		final TextView tv = new TextView(context);

		final SpannableString s = new SpannableString(Html.fromHtml(context
				.getString(R.string.about_message)));
		tv.setText(s);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
		tv.setPadding(30, 30, 30, 30);
		tv.setMovementMethod(LinkMovementMethod.getInstance());

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.about_title);
		builder.setView(tv);

		builder.setNegativeButton(R.string.about_button, null);

		return builder.create();
	}
}
