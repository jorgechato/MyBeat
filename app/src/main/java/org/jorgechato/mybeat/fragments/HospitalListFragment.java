package org.jorgechato.mybeat.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jorgechato.mybeat.Adapter.HospitalAdapter;
import org.jorgechato.mybeat.ItemHospital;
import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.base.Hospital;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HospitalListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView listView;
    private ArrayList<Hospital> arrayListHospital;
    private HospitalAdapter adapter;

    private static final String JSONURL = "http://datos.gijon.es/doc/salud/centros-sanitarios.json";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_hospital_list,group,false);

        listView = (ListView) view.findViewById(R.id.listViewHospital);
        listView.setOnItemClickListener(this);
        arrayListHospital = new ArrayList<Hospital>();
        adapter = new HospitalAdapter(getActivity(),R.layout.activity_hospital_adapter,arrayListHospital);
        listView.setAdapter(adapter);

        loadHospital();

        return view;
    }

    public void loadHospital(){
        ThreadDownloadData threadData = new ThreadDownloadData();
        threadData.execute(JSONURL);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), arrayListHospital.get(position).getName(), Toast.LENGTH_SHORT).show();
        if (position == ListView.INVALID_POSITION)
            return;

        Hospital hospital = arrayListHospital.get(position);

        Intent intent = new Intent(getActivity(), ItemHospital.class);
        intent.putExtra("image",hospital.getImage());
        intent.putExtra("name",hospital.getName());
        intent.putExtra("description",hospital.getDescription());
        startActivity(intent);
    }

    public class ThreadDownloadData extends AsyncTask<String,Void,Void>{
        private boolean error = false;

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
                        map = null,direction = null;
                float longitude = -1,latitude = -1;
                Bitmap image = null;
                Hospital hospital = null;

                for (int i = 0; i<jsonArray.length() ; i++){
                    email = jsonArray.getJSONObject(i).getString("correo-electronico");
                    if (!jsonArray.getJSONObject(i).getJSONObject("descripcion").isNull("content"))
                        description = jsonArray.getJSONObject(i).getJSONObject("descripcion").getString("content");
                    direction = jsonArray.getJSONObject(i).getJSONArray("direccion").getString(0);
                    timetable = jsonArray.getJSONObject(i).getString("horario");
                    if (!jsonArray.getJSONObject(i).getJSONObject("localizacion").isNull("content")){
                        map = jsonArray.getJSONObject(i).getJSONObject("localizacion").getString("content");
                        String mapLocate[] = map.split(" ");
                        longitude = Float.parseFloat(mapLocate[0]);
                        latitude = Float.parseFloat(mapLocate[1]);
                    }
                    name = jsonArray.getJSONObject(i).getJSONObject("nombre").getString("content");
                    if (!jsonArray.getJSONObject(i).isNull("foto")){
                        String imageURL = jsonArray.getJSONObject(i).getJSONObject("foto").optString("content");
                        URL url = new URL(imageURL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        image = BitmapFactory.decodeStream(input);
                    }
                    if (!jsonArray.getJSONObject(i).getJSONObject("telefono").isNull("content"))
                        phone = jsonArray.getJSONObject(i).getJSONObject("telefono").getString("content");

                    hospital = new Hospital(name,timetable,phone,description,direction,email,image,longitude,latitude);
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
            adapter.clear();
            //arrayListHospital = new ArrayList<>();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_load_json), Toast.LENGTH_SHORT).show();
                return;
            }

            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), getResources().getString(R.string.json_loaded), Toast.LENGTH_SHORT).show();
        }
    }
}
