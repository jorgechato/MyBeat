package org.jorgechato.mybeat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Settings extends Activity implements View.OnClickListener {
    private int RESULT_IMAGE_LOAD = 1;
    private PhotoViewAttacher mAttacher;
    private Bitmap image;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getResources().getString(R.string.action_settings));

        ImageView editprofileimage =(ImageView) findViewById(R.id.editprofileimage);
        editprofileimage.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.editprofileimage:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_IMAGE_LOAD);
                break;
            case R.id.saveprofile:
                image = mAttacher.getVisibleRectangleBitmap();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == RESULT_IMAGE_LOAD) && (resultCode == RESULT_OK) && (data != null)) {
            // Obtiene el Uri de la imagen seleccionada por el usuario
            Uri selectedimage = data.getData();
            String[] path = {MediaStore.Images.Media.DATA };

            // Realiza una consulta a la galería de imágenes solicitando la imagen seleccionada
            Cursor cursor = getContentResolver().query(selectedimage, path, null, null, null);
            cursor.moveToFirst();

            // Obtiene la path a la imagen
            int index = cursor.getColumnIndex(path[0]);
            String picturePath = cursor.getString(index);
            cursor.close();

            // Carga la imagen en la vista ImageView que hay encima del botón
            imageView = (ImageView) findViewById(R.id.editimage);
            Picasso.with(this).setDebugging(true);
            Picasso.with(this).load(new File(picturePath)).into(imageView);

            mAttacher = new PhotoViewAttacher(imageView);
        }
    }
}
