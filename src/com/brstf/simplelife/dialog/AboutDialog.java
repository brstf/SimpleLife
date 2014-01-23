package com.brstf.simplelife.dialog;

import com.brstf.simplelife.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class AboutDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.about_title);
		builder.setMessage(R.string.about_message);

		builder.setNegativeButton(R.string.about_button, null);

		return builder.create();
	}
}
