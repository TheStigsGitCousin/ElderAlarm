package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class BluetoothActivity extends Activity {

    private final String TAG = "BluetoothActivity";

    private final int REQUEST_ENABLE_BT = 1337;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        tv = (TextView) findViewById(R.id.textView);

        mHandler = new Handler();

        initializeAdapter();
        scanLeDevice(true);
    }

    public void initializeAdapter() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();
    private static String BLE_DEVICE_NAME = "devicename";
    BluetoothGatt bluetoothGatt;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDevices.add(device);
                        }
                    });
                }
            };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    for (BluetoothDevice device : mLeDevices) {
                        String s = tv.getText().toString() + "\n";
                        s += "device name: " + device.getName() + ", device address: " + device.getAddress();
                        tv.setText(s);
                        Log.d(TAG, "device name: " + device.getName() + ", device address: " + device.getAddress());
                        if (device.getName().equals(BLE_DEVICE_NAME)) {
                            bluetoothGatt = device.connectGatt(getApplicationContext(), false, btleGattCallback);
                            if (bluetoothGatt != null) {
                                bluetoothGatt.discoverServices();
                                break;
                            }
                        }
                    }
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation
            //read the characteristic data
            byte[] data = characteristic.getValue();
            Log.d(TAG, "data: " + new String(data));
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            // this will get called when a device connects or disconnects
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> services = bluetoothGatt.getServices();
                for (BluetoothGattService service : services) {

                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {

                        Log.d(TAG, "characteristic: " + new String(characteristic.getValue()));
                        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                            //find descriptor UUID that matches Client Characteristic Configuration (0x2902)
                            // and then call setValue on that descriptor
                            Log.d(TAG, "descriptor: " + new String(descriptor.getValue()));
                            String s = tv.getText().toString() + "\n";
                            s += new String(descriptor.getValue());
                            tv.setText(s);
                        }
                    }
                }
            }
        }
    };


}
