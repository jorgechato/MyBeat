package org.jorgechato.mybeat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.base.Hospital;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class HospitalAdapter extends ArrayAdapter<Hospital> {
    private int layoutId;
    private Context context;
    private List<Hospital> objects;

    public HospitalAdapter(Context context, int layoutId, List<Hospital> objects) {
        super(context, layoutId, objects);

        this.layoutId = layoutId;
        this.context = context;
        this.objects = objects;
    }

    static class ResourceHospital{
        ImageView imageView;
        TextView name;
        TextView direction;
    }

    @Override
    public View getView(int position, View view, ViewGroup group){
        View row = view;
        ResourceHospital res = null;

        if (res == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutId,group,false);

            res = new ResourceHospital();
            res.imageView = (ImageView) row.findViewById(R.id.imageView);
            res.name = (TextView) row.findViewById(R.id.hospitalName);
            res.direction = (TextView) row.findViewById(R.id.hospitalDirection);

            row.setTag(res);
        }else{
            res = (ResourceHospital) row.getTag();
        }

        Hospital hospital = objects.get(position);
        //res.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
        res.imageView.setImageBitmap(objects.get(position).getImage());
        res.name.setText(hospital.getName());
        res.direction.setText(hospital.getDirection());

        return row;
    }
}
