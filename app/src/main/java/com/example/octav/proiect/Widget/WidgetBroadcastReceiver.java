package com.example.octav.proiect.Widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.Utils.Utils;

import java.util.List;

/**
 * Created by Octav on 5/5/2016.
 */
public class WidgetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int mode = intent.getExtras().getInt("widget_mode");
        DataBase db = new DataBase(context.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        List<ModeObject> modeList = db.getModes();
        for(ModeObject m : modeList){
            if(m.id == mode){
                Utils.setMode(context,m,true);
                break;
            }
        }
    }

}
