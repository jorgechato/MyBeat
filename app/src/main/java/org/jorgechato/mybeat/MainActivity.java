package org.jorgechato.mybeat;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jorgechato.mybeat.base.Control;
import org.jorgechato.mybeat.base.Hospital;
import org.jorgechato.mybeat.database.Database;
import org.jorgechato.mybeat.fragments.ControlFragment;
import org.jorgechato.mybeat.fragments.HospitalListFragment;
import org.jorgechato.mybeat.fragments.ProfileFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private static final String JSONURL = "http://datos.gijon.es/doc/salud/centros-sanitarios.json";
    private ArrayList<Hospital> arrayListHospital;
    private String NOTIMETABLE;
    private String NOPHONE;
    private String ERRORLOADJSON;
    private String JSONLOADED;
    private HospitalListFragment hospitalListFragment;
    private ProgressBar progressBar;
    private static Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        arrayListHospital = new ArrayList<Hospital>();

        NOTIMETABLE = getResources().getString(R.string.no_timetable);
        NOPHONE = getResources().getString(R.string.no_phone);
        ERRORLOADJSON = getResources().getString(R.string.error_load_json);
        JSONLOADED = getResources().getString(R.string.json_loaded);

        database = new Database(this);

        loadHospital();

        startFragment();
    }

    public static Database getDatabase() {
        return database;
    }

    private void tabSwipe(ActionBar actionBar) {
        final ActionBar mViewPager = actionBar;

    }

    public void startFragment(){
        Resources resources = getResources();
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tabProfile= actionBar.newTab().setText(resources.getString(R.string.tab_profile));
        ActionBar.Tab tabControl= actionBar.newTab().setText(resources.getString(R.string.tab_control));
        ActionBar.Tab tabHospital= actionBar.newTab().setText(resources.getString(R.string.tab_hospital));
//        ActionBar.Tab tabComments= actionBar.newTab().setText(resources.getString(R.string.tab_comments));

        hospitalListFragment = new HospitalListFragment();

        Fragment fragmentProfile = new ProfileFragment();
        Fragment fragmentControl = new ControlFragment();
        Fragment fragmentHospital = hospitalListFragment;
//        Fragment fragmentComments = new CommentsFragment();

        tabProfile.setTabListener(new TabsListener(fragmentProfile));
        tabControl.setTabListener(new TabsListener(fragmentControl));
        tabHospital.setTabListener(new TabsListener(fragmentHospital));
//        tabComments.setTabListener(new TabsListener(fragmentComments));

        actionBar.addTab(tabProfile,false);
        actionBar.addTab(tabControl,true);
        actionBar.addTab(tabHospital,false);
//        actionBar.addTab(tabComments,false);

        tabSwipe(actionBar);
        hospitalListFragment.setArrayListHospital(arrayListHospital);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_acercade) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadHospital(){
        ThreadDownloadData threadData = new ThreadDownloadData();
        threadData.execute(JSONURL);
    }

    public class ThreadDownloadData extends AsyncTask<String,Void,Void> {
        private boolean error = false;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            InputStream inputStream = null;
            String results = null;
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null)
                    builder.append(line + "\n");

                inputStream.close();
                results = builder.toString();

                jsonObject = new JSONObject(results);
                jsonArray = jsonObject.getJSONObject("directorios").getJSONArray("directorio");

                String name = null,timetable = null,phone = null,description = null,email = null,
                        map = null,direction = null,imageURL = null;
                float longitude = -1,latitude = -1;
                Hospital hospital = null;

                for (int i = 0; i<jsonArray.length() ; i++){
                    email = jsonArray.getJSONObject(i).getString("correo-electronico");
                    if (!jsonArray.getJSONObject(i).getJSONObject("descripcion").isNull("content"))
                        description = jsonArray.getJSONObject(i).getJSONObject("descripcion").getString("content");
                    direction = jsonArray.getJSONObject(i).getJSONArray("direccion").getString(0);
                    timetable = jsonArray.getJSONObject(i).getString("horario");
                    if (timetable.equals("{}"))
                        timetable = NOTIMETABLE;
                    if (!jsonArray.getJSONObject(i).getJSONObject("localizacion").isNull("content")){
                        map = jsonArray.getJSONObject(i).getJSONObject("localizacion").getString("content");
                        String mapLocate[] = map.split(" ");
                        longitude = Float.parseFloat(mapLocate[0]);
                        latitude = Float.parseFloat(mapLocate[1]);
                    }
                    name = jsonArray.getJSONObject(i).getJSONObject("nombre").getString("content");
                    if (!jsonArray.getJSONObject(i).isNull("foto")){
                        imageURL = jsonArray.getJSONObject(i).getJSONObject("foto").optString("content");
                    }else {
                        imageURL = null;
                    }
                    if (!jsonArray.getJSONObject(i).getJSONObject("telefono").isNull("content")){
                        phone = jsonArray.getJSONObject(i).getJSONObject("telefono").getString("content");
                    }else {
                        phone = NOPHONE;
                    }

                    hospital = new Hospital(name,timetable,phone,description,direction, URLDecoder.decode(email, "UTF-8"),imageURL,longitude,latitude);
                    arrayListHospital.add(hospital);
                }

            }catch (ClientProtocolException e) {
                e.printStackTrace();
                error = true;
            } catch (IOException e) {
                e.printStackTrace();
                error = true;
            } catch (JSONException e) {
                e.printStackTrace();
                error = true;
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (hospitalListFragment.ADAPTER != null)
                hospitalListFragment.ADAPTER.clear();
            arrayListHospital = new ArrayList<Hospital>();
            setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            if (hospitalListFragment.ADAPTER != null)
                hospitalListFragment.ADAPTER.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.INVISIBLE);
            if (error) {
                Toast.makeText(MainActivity.this, ERRORLOADJSON, Toast.LENGTH_SHORT).show();
                return;
            }
            if (hospitalListFragment.ADAPTER != null)
                hospitalListFragment.ADAPTER.notifyDataSetChanged();
        }
    }

    public void onClick(View view){
        Intent intent = new Intent(this, AddControl.class);
        startActivity(intent);
    }
}
