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
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        numberTextView = (TextView) findViewById(R.id.numberTextView);

        Button button = (Button) findViewById(R.id.contactButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
            }
        });

        if (this.getIntent().hasExtra("alarm")) {
            sendAlarmSMS();
        }
        setContactData();

    }

    void setContactData() {
        String phoneNumber = getContactData(PHONE_NUMBER);
        String contactName = getContactData(CONTACT_NAME);
        nameTextView.setText(contactName);
        numberTextView.setText(phoneNumber);
    }

    void sendAlarmSMS() {
        String phoneNumber = getContactData(PHONE_NUMBER);
        if (phoneNumber.equals("")) {
            phoneNumber = "911";
        }

        String smsBody = "ALARM. I'm GOING TO DIE! ;)";
        Log.d(TAG, "Send SMS to '" + phoneNumber + "' with following body '" + smsBody + "'");

        // Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
        // Send a text based SMS
        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
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

    String getContactData(String type) {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        return settings.getString(type, "");
    }

    void setContactData(String type, String data) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(type, data);

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
        nameTextView.setText(contactName);
        setContactData(CONTACT_NAME, contactName);
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
        numberTextView.setText(contactNumber);
        setContactData(PHONE_NUMBER, contactNumber);
    }
}
