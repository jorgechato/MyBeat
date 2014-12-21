package org.jorgechato.mybeat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jorgechato.mybeat.database.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Settings extends Activity implements View.OnClickListener {
    private int RESULT_IMAGE_LOAD = 1;
    private PhotoViewAttacher mAttacher;
    private ImageView imageView;
    private File mediaFile;
    private Database database;
    private EditText txtname, txtweight, txtheight;
    private RadioGroup txtunit;
    private RadioButton mg,mmo;
    public static TextView datetext;
    public static long time;

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
        database = new Database(this);
        Cursor cursor = database.getUserData();
        cursor.moveToFirst();

        datetext = (TextView) findViewById(R.id.txtdate);
        datetext.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.editimage);
        txtname = (EditText) findViewById(R.id.editname);
        txtweight = (EditText) findViewById(R.id.editTextweight);
        txtheight = (EditText) findViewById(R.id.editTextheight);
        txtunit = (RadioGroup) findViewById(R.id.editunitsglucose);
        mg = (RadioButton) findViewById(R.id.rbglucosemg);
        mmo = (RadioButton) findViewById(R.id.rbglucosemmo);

        mAttacher = new PhotoViewAttacher(imageView);

        loadDatabase(cursor);
    }

    private void loadDatabase(Cursor cursor) {
        mediaFile = new File(cursor.getString(1));
        Picasso.with(this).load(new File(mediaFile.getAbsolutePath()))
                .error(R.drawable.ic_launcher).into(imageView);
        txtname.setText(cursor.getString(2));
        if (cursor.getString(3).equals("mg/dl")){
            mg.setChecked(true);
        }else{
            mmo.setChecked(true);
        }
        txtweight.setText(cursor.getString(5));
        txtheight.setText(cursor.getString(6));
        Date date = Date.valueOf(cursor.getString(4));
        time = date.getTime();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        datetext.setText(df.format(date));
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
            case R.id.editprofiledate:
                showDate();
                break;
            case R.id.txtdate:
                showDate();
                break;
            default:
                break;
        }

    }

    private void showDate() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), getResources().getString(R.string.date));
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            datetext.setText(day + "/" + month + "/" + year);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Settings.time = calendar.getTimeInMillis();
        }
    }

    private void beforeSave() {
        database = new Database(getApplicationContext());
        Bitmap image = mAttacher.getVisibleRectangleBitmap();
        storeImage(image);
        String unit;

        String name = txtname.getText().toString();
        float weight=50.f;
        int height=150;
        if (!txtweight.getText().toString().equals("")){
            weight = Float.parseFloat(txtweight.getText().toString());
        }
        if (!txtheight.getText().toString().equals("")) {
            height = Integer.parseInt(txtheight.getText().toString());
        }
        if (txtunit.getCheckedRadioButtonId() == R.id.rbglucosemg){
            unit = "mg/dl";
        }else{
            unit = "mmo/l";
        }

        database.changeUserData(mediaFile.getAbsolutePath(), name, unit,
                new java.sql.Date(time), weight, height);
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

        String mImageName="IMAGE_PROFILE.jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
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
            Picasso.with(this).load(new File(picturePath))
                    .error(R.drawable.ic_launcher).into(imageView);

        }
    }
}
