package org.jorgechato.mybeat.fragments;

import static org.jorgechato.mybeat.util.Constant.DATEC;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.jorgechato.mybeat.MainActivity;
import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.base.Control;
import org.jorgechato.mybeat.database.Database;

import java.sql.Date;
import java.sql.Time;

public class ControlFragment extends Fragment implements AdapterView.OnItemLongClickListener {
    private Control control;
    private Database database;
    private static String[] FROM_SHOW = {DATEC};
    private static int[] TO = {R.id.prueba};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_control,group,false);

//        addDataTest();

        loadControlData(view);

        return view;
    }

    private void addDataTest() {
        database = new Database(getActivity());

        control = new Control("","almuerzo",new Date(500124),new Time(54789547),57,12);

        database.newControl(control);
    }

    private void loadControlData(View view) {
        database = MainActivity.getDatabase();
        Cursor cursor = database.getControl();
        if (cursor.getCount()<0)
            return;

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.control_adapter, cursor, FROM_SHOW, TO, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ((ListView) view.findViewById(R.id.controlLW)).setAdapter(adaptador);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("funca");
        Toast.makeText(getActivity(), "funca", Toast.LENGTH_SHORT).show();

        Control control = (Control) parent.getItemAtPosition(position);
        database.deleteControl(control);
        return false;
    }
}
