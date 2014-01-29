package com.brstf.simplelife.dialog;

import com.brstf.simplelife.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutDialog extends DialogFragment {

	public static AlertDialog create(final Activity activity) {
		final View view = activity.getLayoutInflater().inflate(
				R.layout.dialog_about, null);
		final TextView tv = (TextView) view.findViewById(R.id.about_tv);

		final SpannableString s = new SpannableString(Html.fromHtml(activity
				.getBaseContext().getString(R.string.about_message)));
		tv.setText(s);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
		tv.setMovementMethod(LinkMovementMethod.getInstance());

		ImageButton paypal = (ImageButton) view.findViewById(R.id.but_donate);
		paypal.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent myIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=FPNN2J9U7HXTJ&lc=US&item_name=Simple%20Life&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted"));
				activity.startActivity(myIntent);
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(R.string.about_title);
		builder.setView(view);

		builder.setNegativeButton(R.string.about_button, null);

		return builder.create();
	}
}
