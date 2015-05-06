package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SMSActivity extends ActionBarActivity {
    private String TAG = "SMSActivity";
    private String SETTINGS = "settings";
    private String PHONE_NUMBER = "phonenumber";
    private String CONTACT_NAME = "contactname";
    private String contactID;
    private Uri uriContact;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    TextView nameTextView;
    TextView numberTextView;
    ListView listView;
    int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
//
//        nameTextView = (TextView) findViewById(R.id.nameTextView);
//        numberTextView = (TextView) findViewById(R.id.numberTextView);

        listView = (ListView) findViewById(R.id.contactListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
            }
        });

        Button button = (Button) findViewById(R.id.contactButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
            }
        });

        Button removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected != -1) {
                    remove();
                }
            }
        });

        if (this.getIntent().hasExtra("alarm")) {
            sendAlarmSMS();
        }
        setContactData();

    }

    void remove() {
        HashSet<String> phoneNumbers = new HashSet(getContactData(PHONE_NUMBER));
        HashSet<String> contactName = new HashSet(getContactData(CONTACT_NAME));
        Iterator phoneIterator = phoneNumbers.iterator();
        Iterator nameIterator = contactName.iterator();
        int i = 0;
        while (phoneIterator.hasNext() && nameIterator.hasNext()) {
            Object name = nameIterator.next();
            Object number = phoneIterator.next();
            if (i == selected) {
                phoneNumbers.remove(number);
                contactName.remove(name);
                break;
            }
            i++;
        }

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PHONE_NUMBER, phoneNumbers);
        editor.putStringSet(CONTACT_NAME, contactName);

        // Commit the edits!
        editor.commit();

        setContactData();
    }

    void setContactData() {
//        Set<String> phoneNumber = getContactData(PHONE_NUMBER);
        HashSet<String> phoneNumbers = new HashSet(getContactData(PHONE_NUMBER));
        HashSet<String> contactName = new HashSet(getContactData(CONTACT_NAME));
        List<Map<String, String>> data = new ArrayList<>();

        Iterator phoneIterator = phoneNumbers.iterator();
        Iterator nameIterator = contactName.iterator();
        while (phoneIterator.hasNext() && nameIterator.hasNext()) {
            String name = (String) nameIterator.next();
            String number = (String) phoneIterator.next();
            Log.d(TAG, name + " - " + number);
            Map<String, String> datum = new HashMap<>(2);
            datum.put("name", name);
            datum.put("number", number);
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "number"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        listView.setAdapter(adapter);

//        nameTextView.setText(contactNames);
//        numberTextView.setText(phoneNumber);
    }

    void sendAlarmSMS() {
        HashSet<String> phoneNumbers = new HashSet(getContactData(PHONE_NUMBER));
        if (phoneNumbers.size() == 0)
            phoneNumbers.add("911");

        for (String s : phoneNumbers) {
            String number = new String(s);

            String smsBody = "ALARM. I'm GOING TO DIE! ;)";
            Log.d(TAG, "Send SMS to '" + number + "' with following body '" + smsBody + "'");

            // Get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            // Send a text based SMS
            smsManager.sendTextMessage(number, null, smsBody, null, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactNumber();
            retrieveContactName();
        }
    }

    Set<String> getContactData(String type) {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        return settings.getStringSet(type, new HashSet<String>());
    }

    void setContactData(String type, String data) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> set = settings.getStringSet(type, new HashSet<String>());
        set.add(data);
        editor.putStringSet(type, set);

        // Commit the edits!
        editor.commit();
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);
//        nameTextView.setText(contactName);
        setContactData(CONTACT_NAME, contactName);
        setContactData();
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
//        numberTextView.setText(contactNumber);
        setContactData(PHONE_NUMBER, contactNumber);
        setContactData();
    }
}
