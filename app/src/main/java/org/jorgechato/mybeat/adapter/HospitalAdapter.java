package org.jorgechato.mybeat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.base.Hospital;

import java.util.List;

public class HospitalAdapter extends ArrayAdapter<Hospital> {
    private int layoutId;
    private Context context;
    private List<Hospital> objects;

    public HospitalAdapter(){
        super(null,0);
    }

    public HospitalAdapter(Context context, int layoutId, List<Hospital> objects) {
        super(context, layoutId, objects);

        this.layoutId = layoutId;
        this.context = context;
        this.objects = objects;
    }

    static class ResourceHospital{
        CircularImageView imageView;
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

            res.imageView = (CircularImageView) row.findViewById(R.id.imageView);
            res.name = (TextView) row.findViewById(R.id.hospitalName);
            res.direction = (TextView) row.findViewById(R.id.hospitalDirection);

            row.setTag(res);
        }else{
            res = (ResourceHospital) row.getTag();
        }

        Hospital hospital = objects.get(position);

        Picasso.with(context).load(objects.get(position).getImageURL()).placeholder(R.drawable.default_blur)
                .error(R.drawable.default_blur).into(res.imageView);

        res.name.setText(hospital.getName());
        res.direction.setText(hospital.getDirection());

        return row;
    }
}
