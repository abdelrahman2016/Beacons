package com.example.abdelrahman.beacons;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by abdelrahman on 08/05/17.
 */
public class Rec extends BroadcastReceiver {
    private  Activity activity;
    private String LOG_TAG=Rec.class.getSimpleName();
    public Rec(Activity act){
        activity=act;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG,"ahahahah");
        String action = intent.getAction();
        if(BluetoothDevice.ACTION_FOUND.equals(action)) {
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            Toast.makeText(activity, name + " => " + rssi + "dBm\n",
                    Toast.LENGTH_SHORT).show();
            Log.v(LOG_TAG,name + " => " + rssi + "dBm\n");
            // TextView rssi_msg = (TextView) findViewById(R.id.textView1);
            //  rssi_msg.setText(rssi_msg.getText() + name + " => " + rssi + "dBm\n");
        }
    }

}
