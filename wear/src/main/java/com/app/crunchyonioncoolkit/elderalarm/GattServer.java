package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import java.util.UUID;

/**
 * Created by David on 2015-04-24.
 */
public class GattServer {

    private static final String TAG = "GattServer";

    private static final UUID SERVICE_UUID = UUID.fromString("f0001802-0451-4000-b000-000000000000");

    private static BluetoothGattServer gattServer;
    private static BluetoothManager bluetoothManager;

    public static void startServer(Activity activity) {
        createGattServer(activity);
    }

    public static void stopServer() {
        if (gattServer != null)
            gattServer.close();
    }

    private static void createGattServer(Activity activity) {
        if (gattServer == null) {
            bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
            gattServer = bluetoothManager.openGattServer(activity, gattServerCallback);
        }

        gattServer.addService(getService());
        Log.d(TAG, "GattServerCreated");
    }

    private static BluetoothGattService getService() {
        BluetoothGattService service = new BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);
        service.addCharacteristic(getCharacteristic());
        return service;
    }

    private static BluetoothGattCharacteristic getCharacteristic() {
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(SERVICE_UUID, BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        for (BluetoothGattDescriptor descriptor : getDescriptors()) {
            characteristic.addDescriptor(descriptor);
        }
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        return characteristic;
    }

    private static BluetoothGattDescriptor[] getDescriptors() {
        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(SERVICE_UUID, BluetoothGattDescriptor.PERMISSION_WRITE);
        byte[] data = new byte[6];
        data[0] = 0x4F;
        data[1] = 0x48;
        data[2] = 0x43;
        data[3] = 0x4F;
        data[4] = 0x43;
        data[5] = 0x4B;
        descriptor.setValue(data);
        BluetoothGattDescriptor[] descriptors = new BluetoothGattDescriptor[1];
        descriptors[0] = descriptor;

        return descriptors;
    }

    private static final BluetoothGattServerCallback gattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.d(TAG, "onConnectionStateChange");

        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
            Log.d(TAG, "onServiceAdded");
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            Log.d(TAG, "onCharacteristicReadRequest");
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            Log.d(TAG, "onCharacteristicWriteRequest");
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            Log.d(TAG, "onDescriptorReadRequest");
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            Log.d(TAG, "onDescriptorWriteRequest");
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
            super.onExecuteWrite(device, requestId, execute);
            Log.d(TAG, "onExecuteWrite");
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
            Log.d(TAG, "onNotificationSent");
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
            super.onMtuChanged(device, mtu);
            Log.d(TAG, "onMtuChanged");
        }
    };
}
