package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    public static Context currentContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "Starting");
        super.onCreate(savedInstanceState);

        //startService(new Intent(this, ListenerService.class));

        currentContext = this;
        setContentView(R.layout.activity_main);

//        Intent serviceIntent = new Intent(this, BackgroundService.class);
//        startService(serviceIntent);
        startActivity(new Intent(this, BluetoothActivity.class));
//        GattServer.startServer(this);
//        DecisionMakerSimple.TestAlgorithm();
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
