package com.example.jessie.stormyjoints;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Jessie on 8/3/2017.
 * Dialog Fragment class to config a dialog box for issues getting a response from our
 * forcast api
 */

public class AlertDialogFragment extends DialogFragment {
    private Context mContext = getActivity();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message).setPositiveButton(R.string.dialog_positive_button,null);
        AlertDialog dialog = builder.create();
        return dialog;

    }
}
