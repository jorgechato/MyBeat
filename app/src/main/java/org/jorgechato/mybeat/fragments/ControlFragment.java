package org.jorgechato.mybeat.fragments;

import static android.provider.BaseColumns._ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import org.jorgechato.mybeat.AddControl;
import org.jorgechato.mybeat.ItemHospital;
import org.jorgechato.mybeat.MainActivity;
import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.adapter.ControlCursorAdapter;
import org.jorgechato.mybeat.database.Database;

public class ControlFragment extends Fragment implements ListView.OnItemLongClickListener{
    private Cursor cursor;
    private Database database;
    private ListView lv;
    private ControlCursorAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_control,group,false);

        ListView listView = (ListView) view.findViewById(R.id.controlLW);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_control);
        fab.attachToListView(listView);

        loadControlData(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadControlData(view);
    }

    private void loadControlData(View view) {
        database = MainActivity.getDatabase();
        cursor = database.getControl();
        if (cursor.getCount()<0)
            return;

        adapter = new ControlCursorAdapter(getActivity(),cursor,false);

        lv = (ListView) view.findViewById(R.id.controlLW);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {
        TextView id = (TextView) view.findViewById(R.id.idcontrol);
        final int ids = Integer.parseInt(id.getText().toString());
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        //ad.setTitle("Notice");
        ad.setMessage(getString(R.string.delete_control));
        ad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteControl(ids);
                cursor.requery();
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);
            }
        });
        ad.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
        return false;
    }
}
