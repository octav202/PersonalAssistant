package com.example.octav.proiect.Modes;

import android.app.Activity;
import android.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Utils.Utils;
import com.example.octav.proiect.Widget.WidgetProvider;

import java.util.List;

public class ModesAdapter extends ArrayAdapter<ModeObject> {

    private DataBase db;
    Context context;
    int layoutResourceId;
    List<ModeObject> data = null;
    private boolean editMode = false;

    public ModesAdapter(Context context, int layoutResourceId, List<ModeObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        db = new DataBase(context.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NotificationHolder holder;
        final Context context = parent.getContext();
        final FragmentManager fm = ((Activity) context).getFragmentManager();

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new NotificationHolder();

            holder.nameTextView = (TextView)row.findViewById(R.id.modeNameTextView);
            holder.deleteButton = (Button)row.findViewById(R.id.deleteModeButton);
            holder.modeImage = (ImageView)row.findViewById(R.id.mode_image_view);
            holder.wifi = (ImageView)row.findViewById(R.id.mode_wifi_image);
            holder.ringtone = (ImageView)row.findViewById(R.id.mode_ringtone_image);
            holder.call = (ImageView)row.findViewById(R.id.mode_call_image);
            holder.message = (ImageView)row.findViewById(R.id.mode_message_image);
            holder.media = (ImageView)row.findViewById(R.id.mode_media_image);
            holder.brightness = (ImageView)row.findViewById(R.id.mode_br_image);
            holder.bluetooth = (ImageView)row.findViewById(R.id.mode_bluetooth_image);
            holder.lockscreen = (ImageView)row.findViewById(R.id.mode_lock_image);

            holder.editButton = (Button)row.findViewById(R.id.editModeButton);
            holder.editWrapper = (LinearLayout)row.findViewById(R.id.mode_edit_wrapper);
            row.setTag(holder);
        }
        else
        {
            holder = (NotificationHolder)row.getTag();
        }

        final ModeObject mode = data.get(position);

        //Get Selected Mode
        SharedPreferences prefs = context.getSharedPreferences("ACTIVE_MODE", context.MODE_PRIVATE);
        final Integer selectedId = prefs.getInt("id", 0);
        if (selectedId != null && selectedId == mode.id)
            row.setBackground(ContextCompat.getDrawable(context, R.drawable.mode_selected_background));
        else
            row.setBackground(ContextCompat.getDrawable(context, R.drawable.alarm_list_item));

        //Wifi

        holder.nameTextView.setText(mode.name);
        if(!mode.wifi)
            holder.wifi.setAlpha(0.4f);
        else
            holder.wifi.setAlpha(1f);

        //Ringtone


        if(mode.ringtone.equals("")) {
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ringtone));
            holder.ringtone.setAlpha(0.4f);
        }
        if(mode.ringtone.equals("Normal"))
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ringtone));
        if(mode.ringtone.equals("Vibrate"))
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vibrate));
        if(mode.ringtone.equals("Mute")) {
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ringtone));
            holder.ringtone.setAlpha(0.4f);
        }


        //Media
        if(mode.mediaVolume.equals("Min"))
            holder.media.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_media_volume_min));
        if(mode.mediaVolume.equals("Medium"))
            holder.media.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_media_volume_medium));
        if(mode.mediaVolume.equals("Max"))
            holder.media.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_media_volume_max));
        if(mode.mediaVolume.equals("")) {
            holder.media.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_media_volume_medium));
            holder.media.setAlpha(0.4f);
        }

        //Brightness

        if(mode.brightness.equals("Low"))
            holder.brightness.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_brightness_low));
        if(mode.brightness.equals("Medium"))
            holder.brightness.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_brightness_medium));
        if(mode.brightness.equals("High"))
            holder.brightness.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_brightness_high));
        if(mode.brightness.equals("")) {
            holder.brightness.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_brightness_medium));
            holder.brightness.setAlpha(0.4f);
        }

        //Bluetooth

        if(mode.bluetooth.equals("Yes"))
            holder.bluetooth.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bluetooth_yes));
        if(mode.bluetooth.equals("No"))
            holder.bluetooth.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bluetooth_no));
        if(mode.bluetooth.equals("")) {
            holder.bluetooth.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bluetooth_yes));
            holder.bluetooth.setAlpha(0.4f);
        }

        //Lockscreen

        if(mode.lockScreen.equals("Yes"))
            holder.lockscreen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock_closed));
        if(mode.lockScreen.equals("No"))
            holder.lockscreen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock_open));
        if(mode.lockScreen.equals("")) {
            holder.lockscreen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock_closed));
            holder.lockscreen.setAlpha(0.4f);
        }

        //Call

        if(mode.callMessage.equals(""))
            holder.call.setAlpha(0.4f);
        else
            holder.call.setAlpha(1f);

        //SMS

        if(mode.smsMessage.equals(""))
            holder.message.setAlpha(0.3f);
        else
            holder.message.setAlpha(1f);

        Bitmap img = Utils.loadImageFromStorage(context,mode.name);
        if(img!=null){
            holder.modeImage.setImageBitmap(Utils.loadImageFromStorage(context,mode.name));
        }

        //Listener

        if(editMode) {
           holder.editWrapper.setVisibility(View.VISIBLE);
        }
        else {
            holder.editWrapper.setVisibility(View.GONE);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedId == mode.id)
                    Utils.setMode(context,null,false);
                db.deleteMode(mode);
                data.remove(mode);
                notifyDataSetChanged();

                updateWidget();
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddModeDialog modeDialog = AddModeDialog.newInstance(mode,true);
                modeDialog.show(fm, "mode_fragment");
            }
        });

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedId == mode.id)
                    Utils.setMode(context,null,false);
                else
                    Utils.setMode(context,mode,false);

                notifyDataSetChanged();
            }
        });

        return row;
    }

    static class NotificationHolder
    {
        TextView nameTextView;
        Button deleteButton;
        Button editButton;
        ImageView modeImage;

        ImageView wifi;
        ImageView ringtone;
        ImageView call;
        ImageView message;
        ImageView media;
        ImageView brightness;
        ImageView bluetooth;
        ImageView lockscreen;

        LinearLayout editWrapper;
    }

    void enterEditMode(){
        editMode = true;
        notifyDataSetChanged();
    }

    void exitEditMode(){
        editMode = false;
        notifyDataSetChanged();
    }

    public void updateWidget(){

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        ComponentName thisWidget = new ComponentName( context, WidgetProvider.class );
        AppWidgetManager.getInstance( context).updateAppWidget( thisWidget, remoteViews );
        int[] allWidgetIds = AppWidgetManager.getInstance( context ).getAppWidgetIds(thisWidget);
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(allWidgetIds, R.id.widget_grid );
    }
}
