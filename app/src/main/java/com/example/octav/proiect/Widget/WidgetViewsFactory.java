package com.example.octav.proiect.Widget;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;

import java.util.List;

import static com.example.octav.proiect.Utils.Constants.HIGH;
import static com.example.octav.proiect.Utils.Constants.ID_KEY;
import static com.example.octav.proiect.Utils.Constants.LOW;
import static com.example.octav.proiect.Utils.Constants.MAX;
import static com.example.octav.proiect.Utils.Constants.MEDIUM;
import static com.example.octav.proiect.Utils.Constants.MIN;
import static com.example.octav.proiect.Utils.Constants.MUTE;
import static com.example.octav.proiect.Utils.Constants.NORMAL;
import static com.example.octav.proiect.Utils.Constants.OFF;
import static com.example.octav.proiect.Utils.Constants.ON;
import static com.example.octav.proiect.Utils.Constants.SHARED_NAME;
import static com.example.octav.proiect.Utils.Constants.VIBRATE;
import static com.example.octav.proiect.Utils.Constants.WIDGET_MODE;


public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context ctxt=null;
    private int appWidgetId;

    private DataBase db;
    List<ModeObject> modeList = null;

    public WidgetViewsFactory(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        db = new DataBase(ctxt.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        modeList = db.getModes();
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(modeList.size());
    }



    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_item);

        ModeObject mode = modeList.get(position);


        SharedPreferences prefs = ctxt.getSharedPreferences(SHARED_NAME, ctxt.MODE_PRIVATE);
        final Integer selectedId = prefs.getInt(ID_KEY, 0);

        if (selectedId != null && selectedId == mode.id) {
            row.setInt(R.id.widget_row, "setBackgroundResource", R.drawable.widget_row_background_selected);

        }
        else {
            row.setInt(R.id.widget_row, "setBackgroundResource", R.drawable.widget_row_background);
        }


        row.setTextViewText(R.id.widget_row_name, mode.name);


        if(!mode.wifi)
            row.setImageViewResource(R.id.widget_wifi_image,R.drawable.w_ic_wifi_faded);
        else
            row.setImageViewResource(R.id.widget_wifi_image,R.drawable.w_ic_wifi);



        if(mode.ringtone.equals(NORMAL))
            row.setImageViewResource(R.id.widget_ringtone_image,R.drawable.w_ic_volume_up);
        else
            row.setImageViewResource(R.id.widget_ringtone_image,R.drawable.w_ic_volume_up_faded);


        if(mode.ringtone.equals("")) {
            row.setImageViewResource(R.id.widget_ringtone_image,R.drawable.w_ic_ringtone_faded);
        }
        if(mode.ringtone.equals(NORMAL))
            row.setImageViewResource(R.id.widget_ringtone_image,R.drawable.w_ic_ringtone);
        if(mode.ringtone.equals(VIBRATE))
            row.setImageViewResource(R.id.widget_ringtone_image,R.drawable.w_ic_vibrate);
        if(mode.ringtone.equals(MUTE)) {
            row.setImageViewResource(R.id.widget_ringtone_image,R.drawable.w_ic_ringtone_faded);
        }

        if(mode.callMessage.equals(""))
            row.setImageViewResource(R.id.widget_call_image,R.drawable.w_ic_call_faded);
        else
            row.setImageViewResource(R.id.widget_call_image,R.drawable.w_ic_call);

        if(mode.smsMessage.equals(""))
            row.setImageViewResource(R.id.widget_message_image,R.drawable.w_ic_email_faded);
        else
            row.setImageViewResource(R.id.widget_message_image,R.drawable.w_ic_email);


        //Media
        if(mode.mediaVolume.equals(MIN))
            row.setImageViewResource(R.id.widget_media_image,R.drawable.w_ic_media_volume_min);
        if(mode.mediaVolume.equals(MEDIUM))
            row.setImageViewResource(R.id.widget_media_image,R.drawable.w_ic_media_volume_medium);
        if(mode.mediaVolume.equals(MAX))
            row.setImageViewResource(R.id.widget_media_image,R.drawable.w_ic_media_volume_max);
        if(mode.mediaVolume.equals("")) {
            row.setImageViewResource(R.id.widget_media_image,R.drawable.w_ic_media_volume_max_faded);
        }

        //Brightness

        if(mode.brightness.equals(LOW))
            row.setImageViewResource(R.id.widget_br_image,R.drawable.w_ic_brightness_low);
        if(mode.brightness.equals(MEDIUM))
            row.setImageViewResource(R.id.widget_br_image,R.drawable.w_ic_brightness_medium);
        if(mode.brightness.equals(HIGH))
            row.setImageViewResource(R.id.widget_br_image,R.drawable.w_ic_brightness_high);
        if(mode.brightness.equals("")) {
            row.setImageViewResource(R.id.widget_br_image,R.drawable.w_ic_brightness_medium_faded);
        }

        //Bluetooth

        if(mode.bluetooth.equals(ON))
            row.setImageViewResource(R.id.widget_bluetooth_image,R.drawable.w_ic_bluetooth_yes);
        if(mode.bluetooth.equals(OFF))
            row.setImageViewResource(R.id.widget_bluetooth_image,R.drawable.w_ic_bluetooth_no);
        if(mode.bluetooth.equals("")) {
            row.setImageViewResource(R.id.widget_bluetooth_image,R.drawable.w_ic_bluetooth_yes_faded);
        }

        //Lockscreen

        if(mode.lockScreen.equals(ON))
            row.setImageViewResource(R.id.widget_lock_image,R.drawable.w_ic_lock_closed);
        if(mode.lockScreen.equals(OFF))
            row.setImageViewResource(R.id.widget_lock_image,R.drawable.w_ic_lock_open);
        if(mode.lockScreen.equals("")) {
            row.setImageViewResource(R.id.widget_lock_image,R.drawable.w_ic_lock_closed_faded);
        }

        Intent i=new Intent();
        i.putExtra(WIDGET_MODE,mode.id);
        row.setOnClickFillInIntent(R.id.widget_row, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {
        modeList.clear();
        modeList = db.getModes();
    }
}