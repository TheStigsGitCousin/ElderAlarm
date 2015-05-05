package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    public static Context currentContext;
    private EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "Starting");


        //startService(new Intent(this, ListenerService.class));

        currentContext = this;
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.main_editText);

        button = (Button) findViewById(R.id.run_button);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("MainActivity", "LongClick");
                RunTestRes Res = new RunTestRes();
                Res.readRes("davidtest/" + editText.getText().toString());
                return true;
            }
        });

//        Intent serviceIntent = new Intent(this, BackgroundService.class);
//        startService(serviceIntent);
//        startActivity(new Intent(this, TestActivity.class));
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
