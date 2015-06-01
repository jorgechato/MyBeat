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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jorgechato.mybeat.base.Control;
import org.jorgechato.mybeat.database.Database;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddControl extends Activity {
    private Database database;
    private Spinner spinner,spinnertype;
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
        spinnertype = (Spinner) findViewById(R.id.spinnertype);
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
        String [] arraySpinnerType = new String[]{
                getString(R.string.quickly),getString(R.string.slow)
        };

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        spinner.setAdapter(adapter);
        ArrayAdapter adaptertype = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinnerType);
        spinnertype.setAdapter(adaptertype);
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
                    spinnertype.getSelectedItem().toString(),new Date(calendarDate), new Time(calendarTime),
                    Integer.parseInt(String.valueOf(glucose.getText())), Integer.parseInt(String.valueOf(insulin.getText())));
            database.newControl(control);
            postToAPI(control);
            mp.setLooping(false);
            mp.start();

            this.finish();
        }
    }

    /**
     * {
         "control" : {
             "date" : "2015-05-23T07:23:03.593Z",
             "time" : "2015-05-23T07:23:03.593Z",
             "glucose" : 140,
             "insulin" : 12,
             "type" : "quickly",
             "daytime" : "breakfast",
             "note" : "something"
             }
         }
     * @param control
     * @return
     */
    private void postToAPI(Control control) {
        try {
            JSONObject controlsJson = new JSONObject();
            JSONObject controlJson = new JSONObject();
            controlJson.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(control.getDate().getTime()));
            controlJson.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(control.getTime().getTime()));
            controlJson.put("glucose", control.getGlucose());
            controlJson.put("insulin", control.getInsulin());
            controlJson.put("type", control.getType());
            controlJson.put("daytime", control.getDaytime());
            controlJson.put("note", control.getNote());

            controlsJson.put("control", controlJson);
            System.out.println(controlsJson.toString());
            /**
             *
             */
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.144:5000/api/controls");
            StringEntity entity = new StringEntity(controlsJson.toString());
            entity.setContentType("application/json;charset=UTF-8");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            //Setting the content type is very important
//            entity.setContentEncoding(HTTP.UTF_8);
//            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            //Execute and get the response.
            HttpResponse response = httpClient.execute(httpPost);
            /**
             *
             */

//            HttpPost post = new HttpPost("http://192.168.1.144:5000/api/controls/");
//            post.setHeader("Accept", "application/json");
//            post.setHeader("Content-type", "application/json");
////            StringEntity se = new StringEntity(controlsJson.toString());
//            StringEntity entity = new StringEntity(controlsJson.toString(), HTTP.UTF_8);
//            entity.setContentType("application/json");
//
//            post.setEntity(entity);
//            post.addHeader("Accept", "application/json");
//            post.addHeader("Content-type", "application/json");
////            System.out.println(post.getEntity().toString());
//
//            HttpClient client = new DefaultHttpClient();
//            HttpResponse response = client.execute(post);
//            Log.d("HTTP", "HTTP: OK");

//            String responseText = EntityUtils.toString(response.getEntity());
//            JSONObject json = new JSONObject(responseText);
//            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        control.setId(45);

//        return control;
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
