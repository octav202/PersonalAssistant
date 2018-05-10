package com.example.octav.proiect.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.octav.proiect.Modes.ModeObject;

import java.util.List;

import static com.example.octav.proiect.Utils.Constants.TAG;

public class StatusBarReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int modeId = intent.getIntExtra("ModeId", 0);
        Log.d(TAG, "onReceive() " + intent.getAction() + " Mode : " + modeId);
        // Select Mode
        DataBase db = new DataBase(context.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        List<ModeObject> modeList = db.getModes();
        for(ModeObject m : modeList){
            if(m.id == modeId){
                Utils.setMode(context,m,true);
                Toast.makeText(context, m.name + " mode selected.", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
