package org.jorgechato.mybeat.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jorgechato.mybeat.AddControl;
import org.jorgechato.mybeat.MainActivity;
import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.database.Database;
import org.jorgechato.mybeat.util.BlurTransform;

import java.io.File;

public class ProfileFragment extends Fragment{
    private CircularImageView profileimage;
    private ImageView bigprofile;
    private TextView txtname,txtday,txtweek,txtmonth;

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
        loadAVG();
    }

    private void init(View view) {
        profileimage = (CircularImageView) view.findViewById(R.id.profileimage);
        profileimage.setBorderColor(getResources().getColor(R.color.GrayLight));
        profileimage.setBorderWidth(8);
        profileimage.addShadow();

        bigprofile = (ImageView) view.findViewById(R.id.profilebackground);
        txtname = (TextView) view.findViewById(R.id.profilename);
        txtday = (TextView) view.findViewById(R.id.txtday);
        txtweek = (TextView) view.findViewById(R.id.txtweek);
        txtmonth = (TextView) view.findViewById(R.id.txtmonth);

        loadData();
        loadAVG();
    }

    private void loadAVG() {
        Database database = new Database(getActivity());
        int avg;

        Cursor cursor = database.dayAverage();
        if (cursor.getCount() == 0) {
            txtday.setText("-");
        } else {
            cursor.moveToFirst();
            avg = (int) cursor.getFloat(0);
            txtday.setText(String.valueOf(avg));
        }

        cursor = database.weekAverage();
        if (cursor.getCount() == 0) {
            txtweek.setText("-");
        } else {
            cursor.moveToFirst();
            avg = (int) cursor.getFloat(0);
            txtweek.setText(String.valueOf(avg));
        }

        cursor = database.monthAverage();
        if (cursor.getCount() == 0) {
            txtmonth.setText("-");
        } else {
            cursor.moveToFirst();
            avg = (int) cursor.getFloat(0);
            txtmonth.setText(String.valueOf(avg));
        }
    }
    private void loadData() {
        Database database = new Database(getActivity());
        Cursor cursor = database.getUserData();
        cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return;

        Picasso.with(getActivity()).load(new File(cursor.getString(1)))
                .error(R.drawable.default_blur).into(profileimage);

        Picasso.with(getActivity()).load(new File(cursor.getString(1))).error(R.drawable.default_blur)
                .transform(new BlurTransform(getActivity(),25)).into(bigprofile);

        txtname.setText(cursor.getString(2));
    }

    public void onClick(View view){
        Intent intent = new Intent(getActivity(), AddControl.class);
        startActivity(intent);
    }
}
