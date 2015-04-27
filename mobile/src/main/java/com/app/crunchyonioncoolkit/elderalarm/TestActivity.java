package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class TestActivity extends Activity {
    public static Context currentContext;

    private Button button;
    private EditText pathEditText;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "Starting");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        button = (Button) findViewById(R.id.transfer_button);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                transferFiles();
                return true;
            }
        });

        pathEditText = (EditText) findViewById(R.id.path_editText);

        checkBox=(CheckBox)findViewById(R.id.mobile_transfered_checkBox);
        currentContext = this;

    }

    void transferFiles() {
        String path = pathEditText.getText().toString() + ".txt";
        if (path == "") {
            showToast("NO PATH ENTERED");
            return;
        }
        DataOut.writeToFile(DataOut.readFromFile("ACC.txt"),  path+"_ACC");
        DataOut.writeToFile(DataOut.readFromFile("GYR.txt"), path+"_GYR");
        DataOut.writeToFile(DataOut.readFromFile("PUL.txt"), path+"_PUL");

        DataOut.deleteFile("ACC.txt");
        DataOut.deleteFile("GYR.txt");
        DataOut.deleteFile("PUL.txt");

        checkBox.setSelected(true);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
