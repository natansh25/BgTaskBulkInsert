package com.example.natan.backgroundtasks.BackGroundTasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadCastReciverReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        switch (intentAction) {
            case Intent.ACTION_BATTERY_LOW:

                Toast.makeText(context, "Low Battery !!", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                Toast.makeText(context, "Discharging !!", Toast.LENGTH_SHORT).show();

                break;
        }
        // an Intent broadcast.

    }
}
