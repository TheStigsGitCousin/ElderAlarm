package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView listView;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.scenarioListView);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MainActivity", "text: " + ((TextView) view).getText());
                if (((TextView) view).getText().equals("StartActivity")) {
                    startActivity(new Intent(getApplicationContext(), StartActivity.class));
                } else if (((TextView) view).getText().equals("TestActivity")) {
                    startActivity(new Intent(getApplicationContext(), TestActivity.class));
                } else if (((TextView) view).getText().equals("AlarmActivity")) {
                    serviceIntent = new Intent(getApplicationContext(), BackgroundService.class);
                    startService(serviceIntent);

                    Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                    MyParcelable data = new MyParcelable(10, "cardiac arrest");
                    intent.putExtra("message", data);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stopService(new Intent(this, BackgroundService.class));

        ArrayList<String> list = new ArrayList<>();
        list.add("TestActivity");
        list.add("StartActivity");
        list.add("AlarmActivity");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
