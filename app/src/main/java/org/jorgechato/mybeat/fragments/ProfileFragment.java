package org.jorgechato.mybeat.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jorgechato.mybeat.R;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile,group,false);
        return view;
    }

    public void onClick(View view){

    }
}
