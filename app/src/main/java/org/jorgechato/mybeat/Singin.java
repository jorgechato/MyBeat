package org.jorgechato.mybeat;

import android.app.Activity;
import android.os.Bundle;


public class Singin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        getActionBar().hide();
    }


}
