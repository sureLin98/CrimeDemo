package com.demo.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private DatePicker mDatePicker;
    private static final String DATE="date";
    public static final String DATE_KEY="com.demo.criminalintent.date";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Date date=(Date) getArguments().getSerializable(DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        View v=LayoutInflater.from(getActivity()).inflate(R.layout.date_picker,null);

        mDatePicker=v.findViewById(R.id.dialog_date_picker);

        mDatePicker.init(year,month,day,null);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year=mDatePicker.getYear();
                        int month=mDatePicker.getMonth();
                        int day=mDatePicker.getDayOfMonth();
                        Date date=new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    public void sendResult(int resultCode,Date date){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(DATE_KEY,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle bundle=new Bundle();
        bundle.putSerializable(DATE,date);
        DatePickerFragment datePickerFragment=new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

}
