package org.jorgechato.mybeat.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jorgechato.mybeat.MainActivity;
import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.database.Database;
import org.jorgechato.mybeat.util.BlurTransform;

import java.io.File;

public class ProfileFragment extends Fragment{
    private CircularImageView profileimage;
    private ImageView bigprofile;
    private TextView txtname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile,group,false);

        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void init(View view) {
        profileimage = (CircularImageView) view.findViewById(R.id.profileimage);
        profileimage.setBorderColor(getResources().getColor(R.color.GrayLight));
        profileimage.setBorderWidth(8);
        profileimage.addShadow();

        bigprofile = (ImageView) view.findViewById(R.id.profilebackground);
        txtname = (TextView) view.findViewById(R.id.profilename);

        loadData();
    }

    private void loadData() {
        Database database = new Database(getActivity());
        Cursor cursor = database.getUserData();
        cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return;

        Picasso.with(getActivity()).load(new File(cursor.getString(1)))
                .error(R.drawable.ic_launcher).into(profileimage);

        Picasso.with(getActivity()).load(new File(cursor.getString(1)))
                .transform(new BlurTransform(getActivity(),25)).into(bigprofile);

        txtname.setText(cursor.getString(2));
    }

    public void onClick(View view){

    }
}
