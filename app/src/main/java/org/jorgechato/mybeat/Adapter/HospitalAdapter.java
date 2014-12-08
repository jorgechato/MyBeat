package org.jorgechato.mybeat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
        res.imageView.setImageBitmap(getRoundedCornerBitmap(objects.get(position).getImage()));
        res.name.setText(hospital.getName());
        res.direction.setText(hospital.getDirection());

        return row;
    }

    /**
     * methon from http://ruibm.com/2009/06/16/rounded-corner-bitmaps-on-android/
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 1000;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
