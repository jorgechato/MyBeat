package org.jorgechato.mybeat;

import org.jorgechato.mybeat.R;
import org.jorgechato.mybeat.base.Hospital;
import org.jorgechato.mybeat.fragments.CommentsFragment;
import org.jorgechato.mybeat.fragments.HospitalListFragment;
import org.jorgechato.mybeat.fragments.MapFragment;
import org.jorgechato.mybeat.fragments.ProfileFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFragment();
    }

    public void startFragment(){
        Resources resources = getResources();
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tabMap= actionBar.newTab().setText(resources.getString(R.string.tab_map));
        ActionBar.Tab tabProfile= actionBar.newTab().setText(resources.getString(R.string.tab_profile));
        ActionBar.Tab tabHospital= actionBar.newTab().setText(resources.getString(R.string.tab_hospital));
        ActionBar.Tab tabComments= actionBar.newTab().setText(resources.getString(R.string.tab_comments));


        Fragment fragmentMap = new MapFragment();
        Fragment fragmentProfile = new ProfileFragment();
        Fragment fragmentHospital = new HospitalListFragment();
        Fragment fragmentComments = new CommentsFragment();

        tabMap.setTabListener(new TabsListener(fragmentMap));
        tabProfile.setTabListener(new TabsListener(fragmentProfile));
        tabHospital.setTabListener(new TabsListener(fragmentHospital));
        tabComments.setTabListener(new TabsListener(fragmentComments));

        actionBar.addTab(tabMap,false);
        actionBar.addTab(tabProfile,true);
        actionBar.addTab(tabHospital,false);
        actionBar.addTab(tabComments,false);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
