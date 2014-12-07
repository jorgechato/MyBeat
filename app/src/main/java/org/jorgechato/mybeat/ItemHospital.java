package org.jorgechato.mybeat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

        actionBar.setTitle(name);
        init();
    }

    private void init() {
        ImageView imageSmall = (ImageView) findViewById(R.id.imageSmall);
        ImageView imageBackgrount = (ImageView) findViewById(R.id.imageBackground);
        TextView textdescription = (TextView) findViewById(R.id.txtDescription);

        imageSmall.setImageBitmap(image);
        imageBackgrount.setImageBitmap(image);
        textdescription.setText(description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
