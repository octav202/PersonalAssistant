package com.example.octav.proiect.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Utils.Utils;

import java.util.Calendar;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationObject n = intent.getParcelableExtra("notificationInfo");
        Boolean reminder = intent.getExtras().getBoolean("reminder");

        //Delete Notification from database
        DataBase db = new DataBase(context.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        db.deleteNotification(n);

        //Extract set mode

        String modeName = "";
        ModeObject currentMode = null;

        List<ModeObject> modeList = db.getModes();
        for(ModeObject m : modeList){
            if(m.id == n.mode){
                if(!reminder)
                    Utils.setMode(context,m,true);
                modeName = m.name;
                currentMode = m;
            }
        }

        Intent notificationIntent = new Intent(context, NotificationView.class);
        notificationIntent.putExtra("notificationInfo",n);
        if(currentMode!=null)
            notificationIntent.putExtra("modeInfo",currentMode);
        notificationIntent.putExtra("reminder",reminder);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationView.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(n.id, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Sound

        Notification notification;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String notificationSound = settings.getString("notification_type", null);
        int sound =0;
        if(notificationSound==null)
            notificationSound="Sound";
        switch(notificationSound) {
            case "Sound":
                sound = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
                break;
            case "Vibrate":
                sound = Notification.DEFAULT_VIBRATE;
                break;
            case "Silent":
                sound = Notification.DEFAULT_VIBRATE;
                break;
            default:
                break;
        }

        // Light

        String hexCode = settings.getString("notification_light", "");
        final SharedPreferences.Editor  editor = settings.edit();
        int notificationLight = 0;
        if(!hexCode.equals("")) {
            notificationLight = Color.parseColor(hexCode);
        }
        else{
            notificationLight = Notification.DEFAULT_LIGHTS;
        }

        if(reminder) {

            String text = "At "+ n.hour + ":"+n.minute;

            if(!modeName.equals(""))
                text += "Will switch "+modeName+"  in "+ n.reminder+" min.";


            notification = builder.setContentTitle(n.name)
                    .setWhen(Calendar.getInstance().getTimeInMillis())
                    .setShowWhen(true)
                    .setContentText(text)
                    .setTicker(text)
                    .setAutoCancel(true).setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(sound | Notification.COLOR_DEFAULT).setLights(notificationLight,500,1000)
                    .setSmallIcon(R.mipmap.ic_launcher_pa).setLights(Color.YELLOW,500,500)
                    .setContentIntent(pendingIntent).build();
        }
        else{

            String text = "Now";
            if(!modeName.equals(""))
                text = "Switched to "+modeName;

            notification = builder.setContentTitle(n.name)
                    .setWhen(Calendar.getInstance().getTimeInMillis())
                    .setShowWhen(true)
                    .setContentText(text)
                    .setTicker(text)
                    .setAutoCancel(true).setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(sound | Notification.COLOR_DEFAULT)
                    .setSmallIcon(R.mipmap.ic_launcher_pa).setLights(notificationLight,500,1000)
                    .setContentIntent(pendingIntent).build();

        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(n.id, notification);

    }
}