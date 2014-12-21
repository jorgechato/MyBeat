package org.jorgechato.mybeat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jorgechato.mybeat.util.BlurTransform;

public class ItemHospital extends Activity implements View.OnClickListener{
    private String name,timetable,phone,description,email,direction,imageURL;
    private float longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_hospital);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        imageURL = intent.getStringExtra("image");
        description = intent.getStringExtra("description");
        timetable = intent.getStringExtra("timetable");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        direction = intent.getStringExtra("direction");
        longitude = intent.getFloatExtra("longitude", 0);
        latitude = intent.getFloatExtra("latitude", 0);

        actionBar.setTitle(name);

        ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToScrollView((com.melnykov.fab.ObservableScrollView) scroll);

        init();
    }

    private void init() {
        CircularImageView imageSmall = (CircularImageView) findViewById(R.id.imageSmall);
        imageSmall.setBorderColor(getResources().getColor(R.color.GrayLight));
        imageSmall.setBorderWidth(8);
        imageSmall.addShadow();

        ImageView imageBackgrount = (ImageView) findViewById(R.id.imageBackground);

        Picasso.with(this).setDebugging(true);
        Picasso.with(this).load(imageURL).transform(new BlurTransform(this,10)).into(imageBackgrount);
        Picasso.with(this).load(imageURL).placeholder(R.drawable.ic_launcher).into(imageSmall);

        TextView textdescription = (TextView) findViewById(R.id.txtDescription);
        TextView texttimetable = (TextView) findViewById(R.id.txttimetable);
        TextView textphone = (TextView) findViewById(R.id.txtphone);
        TextView textdirection = (TextView) findViewById(R.id.txtdirection);
        TextView textemail = (TextView) findViewById(R.id.txtemail);

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
    }/*
    public class BlurTransform implements Transformation {
        RenderScript rs;

        public BlurTransform(Context context) {
            super();
            rs = RenderScript.create(context);
        }

        @Override
        public Bitmap transform(Bitmap bitmap) {
            // Create another bitmap that will hold the results of the filter.
            Bitmap blurredBitmap = Bitmap.createBitmap(bitmap);

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(10);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap);

            return blurredBitmap;
        }

        @Override
        public String key() {
            return "blur";
        }
    }*/

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("longitude",longitude);
        intent.putExtra("latitude",latitude);

        startActivity(intent);
    }
}

/*
 * method recicled by Mario Viviani in https://plus.google.com/+MarioViviani/posts/fhuzYkji9zz
 * @param bitmap
 * @return
 */
/*
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
*/
