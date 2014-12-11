package org.jorgechato.mybeat.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import org.jorgechato.mybeat.R;

public class ProfileFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile,group,false);

        init(view);

        return view;
    }

    private void init(View view) {
        CircularImageView profileimage = (CircularImageView) view.findViewById(R.id.profileimage);
        profileimage.setBorderColor(getResources().getColor(R.color.GrayLight));
        profileimage.setBorderWidth(8);
        profileimage.addShadow();
    }

    public void onClick(View view){

    }
}
