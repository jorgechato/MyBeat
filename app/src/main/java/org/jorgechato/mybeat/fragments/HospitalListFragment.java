package org.jorgechato.mybeat.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jorgechato.mybeat.MainActivity;
import org.jorgechato.mybeat.adapter.HospitalAdapter;
import org.jorgechato.mybeat.ItemHospital;
import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.base.Hospital;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;

public class HospitalListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView listView;
    private ArrayList<Hospital> arrayListHospital;
    public static HospitalAdapter ADAPTER;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_hospital_list,group,false);

        listView = (ListView) view.findViewById(R.id.listViewHospital);
        listView.setOnItemClickListener(this);
        ADAPTER = new HospitalAdapter(getActivity(),R.layout.activity_hospital_adapter,arrayListHospital);
        listView.setAdapter(ADAPTER);

        return view;
    }

    public void setArrayListHospital(ArrayList<Hospital> arrayListHospital) {
        this.arrayListHospital = arrayListHospital;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == ListView.INVALID_POSITION)
            return;

        Hospital hospital = arrayListHospital.get(position);

        Intent intent = new Intent(getActivity(), ItemHospital.class);
        intent.putExtra("image",hospital.getImageURL());
        intent.putExtra("name",hospital.getName());
        intent.putExtra("timetable",hospital.getTimetable());
        intent.putExtra("phone",hospital.getPhone());
        intent.putExtra("email",hospital.getEmail());
        intent.putExtra("direction",hospital.getDirection());
        intent.putExtra("description",hospital.getDescription());

        intent.putExtra("longitude",hospital.getLongitude());
        intent.putExtra("latitude",hospital.getLatitude());

        startActivity(intent);
    }
}
