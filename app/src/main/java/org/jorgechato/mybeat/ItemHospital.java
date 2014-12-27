package org.jorgechato.mybeat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

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
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("longitude",longitude);
        intent.putExtra("latitude",latitude);

        startActivity(intent);
    }
}