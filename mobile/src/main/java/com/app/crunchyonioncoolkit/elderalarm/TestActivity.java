package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

public class TestActivity extends Activity {

    private static final String TAG = "TestActivity";
    public static Context currentContext;

    private Button button;
    private EditText pathEditText;
    private TextView transferedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "Starting");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        Log.d("TestActivity", "Starting");


        IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver=new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,filter);

        button = (Button) findViewById(R.id.transfer_button);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                transferFiles();
                return true;

            }
        });

        pathEditText = (EditText) findViewById(R.id.path_editText);

        transferedTextView = (TextView) findViewById(R.id.transferred_textView);
        currentContext = this;

    }

    void transferFiles() {
        String path = pathEditText.getText().toString();
        if (path == "") {
            showToast("NO PATH ENTERED");
            return;
        }
        DataOut.writeToFile(DataOut.readFromFile("ACC.txt"), path + "_ACC.txt");
        DataOut.writeToFile(DataOut.readFromFile("GYR.txt"), path + "_GYR.txt");
        DataOut.writeToFile(DataOut.readFromFile("PUL.txt"), path + "_PUL.txt");

        DataOut.deleteFile("ACC.txt");
        DataOut.deleteFile("GYR.txt");
        DataOut.deleteFile("PUL.txt");

        transferedTextView.setText("Files transferred");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public class MessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            if(message.equals("done")){
                transferedTextView.setText("DONE");
            }else if (message.equals("receive")){

                transferedTextView.setText("Receiving data");
            }
        }
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
