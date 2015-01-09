package org.jorgechato.mybeat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.jorgechato.mybeat.base.Control;
import org.jorgechato.mybeat.database.Database;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddControl extends Activity {
    private Database database;
    private Spinner spinner;
    private EditText note,glucose,insulin;
    private TextView unit;
    private static TextView date,time;
    private static long calendarDate,calendarTime;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_control);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.new_control);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mp = MediaPlayer.create(getBaseContext(),uri);

        init();
        initSpinner();
        loadunit();
    }

    private void loadunit() {
        database = new Database(this);
        Cursor cursor = database.getUserData();
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            unit.setText("mg/dl");
        }else {
            unit.setText(cursor.getString(3));
        }
    }

    private void init() {
        Calendar c = Calendar.getInstance();
        calendarDate = c.getTimeInMillis();
        calendarTime = c.getTimeInMillis();

        unit = (TextView) findViewById(R.id.addunit);
        spinner = (Spinner) findViewById(R.id.spinner);
        note = (EditText) findViewById(R.id.addnote);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setFocusable(true);
                note.setFocusableInTouchMode(true);
                note.requestFocus();
            }
        });
        glucose = (EditText) findViewById(R.id.addglucose);
        insulin = (EditText) findViewById(R.id.addinsulin);
        date = (TextView) findViewById(R.id.adddate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
        time = (TextView) findViewById(R.id.addtime);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(df.format(new java.util.Date(calendarDate)));
        df = new SimpleDateFormat("kk:mm:ss");
        time.setText(df.format(new Time(calendarTime)));
    }

    private void initSpinner() {
        String [] arraySpinner = new String[]{
                getString(R.string.breakfast),getString(R.string.lunch),
                getString(R.string.dinner),getString(R.string.other)
        };

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view){
        database = new Database(this);

        if (glucose.getText().toString().equals("") ||
                insulin.getText().toString().equals("")){
            glucose.setHint(R.string.need);
            insulin.setHint(R.string.need);
            insulin.setHintTextColor(getResources().getColor(R.color.accent));
            glucose.setHintTextColor(getResources().getColor(R.color.accent));
        }else {
            Control control = new Control(note.getText().toString(), spinner.getSelectedItem().toString(),
                    new Date(calendarDate), new Time(calendarTime),
                    Integer.parseInt(String.valueOf(glucose.getText())), Integer.parseInt(String.valueOf(insulin.getText())));
            database.newControl(control);

            mp.setLooping(false);
            mp.start();

            this.finish();
        }
    }

    private void showDate() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), getResources().getString(R.string.date));
    }

    private void showTime() {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), getResources().getString(R.string.date));
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            date.setText(day + "/" + month + "/" + year);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            calendarDate = calendar.getTimeInMillis();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(),this,hour,min,true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            DateFormat df = new SimpleDateFormat("kk:mm:ss");

            time.setText(df.format(new Time(calendar.getTimeInMillis())));
            calendarTime = calendar.getTimeInMillis();
        }
    }
}
