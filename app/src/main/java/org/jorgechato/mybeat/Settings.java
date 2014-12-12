package org.jorgechato.mybeat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jorgechato.mybeat.util.Units;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Settings extends Activity implements View.OnClickListener {
    private int RESULT_IMAGE_LOAD = 1;
    private PhotoViewAttacher mAttacher;
    private ImageView imageView;
    private File mediaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getResources().getString(R.string.action_settings));

        ImageView editprofileimage =(ImageView) findViewById(R.id.editprofileimage);
        editprofileimage.setOnClickListener(this);

        init();
    }

    private void init() {
        /*Picasso.with(this).load(new File(path)).into(imageView);
        EditText name = (EditText) findViewById(R.id.editname);
        DatePicker date = (DatePicker) findViewById(R.id.datePicker);
        EditText weight = (EditText) findViewById(R.id.editTextweight);
        EditText height = (EditText) findViewById(R.id.editTextheight);*/
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
                beforeSave();
                Toast.makeText(this, getResources().getString(R.string.data_saved), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    private void beforeSave() {
        Bitmap image = mAttacher.getVisibleRectangleBitmap();
        storeImage(image);
        Units unit;

        EditText txtname = (EditText) findViewById(R.id.editname);
        DatePicker txtdate = (DatePicker) findViewById(R.id.datePicker);
        EditText txtweight = (EditText) findViewById(R.id.editTextweight);
        EditText txtheight = (EditText) findViewById(R.id.editTextheight);
        RadioGroup txtunit = (RadioGroup) findViewById(R.id.editunitsglucose);

        String name = txtname.getText().toString();
        float weight = Float.parseFloat(txtweight.getText().toString());
        float height = Float.parseFloat(txtheight.getText().toString());

        if (txtunit.getCheckedRadioButtonId() == R.id.rbglucosemg){
            unit = Units.MGDL;
        }else{
            unit = Units.MMOL;
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private  File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        //String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String mImageName="IMAGE_PROFILE.jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        System.out.println(mediaFile);
        return mediaFile;
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
            Picasso.with(this).load(new File(picturePath))
                    .error(R.drawable.ic_launcher).into(imageView);

            mAttacher = new PhotoViewAttacher(imageView);
        }
    }
}
