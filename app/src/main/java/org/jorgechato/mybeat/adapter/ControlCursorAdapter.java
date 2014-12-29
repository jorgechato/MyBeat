package org.jorgechato.mybeat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.database.Database;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by jorge on 28/12/14.
 */
public class ControlCursorAdapter extends CursorAdapter {

    private Cursor cursor;
    private Context context;
    private LayoutInflater inflater;

    public ControlCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.control_adapter, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        TextView id = (TextView) view.findViewById(R.id.idcontrol);
        TextView glucose = (TextView) view.findViewById(R.id.glucosecontrol);
        TextView insuline = (TextView) view.findViewById(R.id.insulinecontrol);
        TextView time = (TextView) view.findViewById(R.id.timecontrol);
        TextView daytime = (TextView) view.findViewById(R.id.daytimecontrol);
        TextView date = (TextView) view.findViewById(R.id.datecontrol);
        TextView separator = (TextView) view.findViewById(R.id.separator);

        Date datadate = Date.valueOf(cursor.getString(1));
        date.setText(df.format(datadate.getTime()));

        id.setText(cursor.getString(0));
        glucose.setText(cursor.getString(3));
        insuline.setText(cursor.getString(5));
        time.setText(cursor.getString(2));
        daytime.setText(cursor.getString(6));

        String thisDate = cursor.getString(1);
        String prevDate = null;

        if (cursor.getPosition() > 0 && cursor.moveToPrevious()) {
            prevDate = cursor.getString(1);
            cursor.moveToNext();
        }

        if (prevDate == null || !prevDate.equals(thisDate)) {
            date.setVisibility(View.VISIBLE);
            separator.setVisibility(View.VISIBLE);
        } else {
            date.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }
    }
}
