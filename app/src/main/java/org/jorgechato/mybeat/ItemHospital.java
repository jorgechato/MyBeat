package org.jorgechato.mybeat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemHospital extends Activity {
    private String name,timetable,phone,description,email,direction;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_hospital);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        image = (Bitmap) intent.getParcelableExtra("image");
        description = intent.getStringExtra("description");
        timetable = intent.getStringExtra("timetable");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        direction = intent.getStringExtra("direction");

        actionBar.setTitle(name);
        init();
    }

    private void init() {
        ImageView imageSmall = (ImageView) findViewById(R.id.imageSmall);
        ImageView imageBackgrount = (ImageView) findViewById(R.id.imageBackground);
        TextView textdescription = (TextView) findViewById(R.id.txtDescription);
        TextView texttimetable = (TextView) findViewById(R.id.txttimetable);
        TextView textphone = (TextView) findViewById(R.id.txtphone);
        TextView textdirection = (TextView) findViewById(R.id.txtdirection);
        TextView textemail = (TextView) findViewById(R.id.txtemail);

        imageSmall.setImageBitmap(getRoundedCornerBitmap(image));
        imageBackgrount.setImageBitmap(blurBitmap(image));
        textdescription.setText(Html.fromHtml(description));
        textdirection.setText(direction);
        texttimetable.setText(timetable);
        textphone.setText(phone);
        textemail.setText(email);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * method recicled by Mario Viviani in https://plus.google.com/+MarioViviani/posts/fhuzYkji9zz
     * @param bitmap
     * @return
     */
    public Bitmap blurBitmap(Bitmap bitmap){
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(getApplicationContext());
        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        //Set the radius of the blur
        blurScript.setRadius(8.f);
        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
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
