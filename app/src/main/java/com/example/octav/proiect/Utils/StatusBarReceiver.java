package com.example.octav.proiect.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.example.octav.proiect.Utils.Constants.TAG;

public class StatusBarReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() " + intent.getAction());
    }
}
