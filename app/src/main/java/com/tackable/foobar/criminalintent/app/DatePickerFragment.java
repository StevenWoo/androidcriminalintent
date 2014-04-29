package com.tackable.foobar.criminalintent.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by stevenwoo on 4/28/14.
 */
public class DatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok, null).create();
    }
}
