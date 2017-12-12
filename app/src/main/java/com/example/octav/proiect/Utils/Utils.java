package com.example.octav.proiect.Utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Widget.WidgetProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Octav on 4/15/2016.
 */
public class Utils {

    private static KeyguardManager.KeyguardLock mKeyguardLock;

    public static String saveToInternalStorage(Context context,Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,name+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
           // Toast.makeText(context,fos.toString(),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        } finally {
            try {
                fos.close();
            }catch (Exception e){
            e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadImageFromStorage(Context context,String name) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f=new File(directory, name+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return  b;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void setMode(Context context, ModeObject mode,boolean fromWidget){
        SharedPreferences.Editor editor = context.getSharedPreferences("ACTIVE_MODE", Context.MODE_PRIVATE).edit();

        if(mode!=null) {
            editor.putInt("id", mode.id);
            editor.putString("name", mode.name);
            editor.putString("callMessage", mode.callMessage);
            editor.putString("smsMessage", mode.smsMessage);
            editor.putBoolean("wifi", mode.wifi);
            editor.putString("ringtone", mode.ringtone);
            editor.putString("image", mode.image);
            editor.putBoolean("modeActive",true);

            //Ringtone
            AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

            if(mode.ringtone.equals("Normal"))
                manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            if(mode.ringtone.equals("Vibrate"))
                manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if(mode.ringtone.equals("Mute"))
                manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            //Media

            if(mode.mediaVolume.equals("Min")){
                AudioManager audio = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float percent = 0.0f;
                int mVolume = (int) (maxVolume*percent);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
            }
            if(mode.mediaVolume.equals("Medium")){
                AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float percent = 0.5f;
                int mVolume = (int) (maxVolume*percent);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
            }
            if(mode.mediaVolume.equals("Max")){
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            }


            //Wifi

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (mode.wifi)
                wifiManager.setWifiEnabled(true);
            else
                wifiManager.setWifiEnabled(false);


            //Brightness

            if(mode.brightness.equals("Low")){
                try{
                    android.provider.Settings.System.putInt(context.getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);

                    if(!fromWidget) {
                        WindowManager.LayoutParams lp;
                        lp = ((Activity) context).getWindow().getAttributes();
                        lp.screenBrightness = 0f;
                        ((Activity) context).getWindow().setAttributes(lp);
                    }
                }
                catch (Exception e){

                }

            }
            if(mode.brightness.equals("Medium")){
                try{
                    android.provider.Settings.System.putInt(context.getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS, 126);
                    if(!fromWidget) {
                        WindowManager.LayoutParams lp;
                        lp = ((Activity) context).getWindow().getAttributes();
                        lp.screenBrightness = 0.5f;
                        ((Activity) context).getWindow().setAttributes(lp);
                    }
                }
                catch (Exception e){

                }

            }
            if(mode.brightness.equals("High")){
                try{
                    android.provider.Settings.System.putInt(context.getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);
                    if(!fromWidget) {
                        WindowManager.LayoutParams lp;
                        lp = ((Activity) context).getWindow().getAttributes();
                        lp.screenBrightness = 1f;
                        ((Activity) context).getWindow().setAttributes(lp);
                    }
                }
                catch (Exception e){

                }

            }

            if(fromWidget)
                setBrightness(context,mode.brightness);

            //Bluetooth

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mode.bluetooth.equals("No"))
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }

            if(mode.bluetooth.equals("Yes")){
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }
            }

            if(mode.lockScreen.equals("No"))
                unlockScreen(context);
            if(mode.lockScreen.equals("Yes"))
                lockScreen(context);

            //Update Widget

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            ComponentName thisWidget = new ComponentName( context, WidgetProvider.class );
            AppWidgetManager.getInstance( context ).updateAppWidget( thisWidget, remoteViews );
            int[] allWidgetIds = AppWidgetManager.getInstance( context ).getAppWidgetIds(thisWidget);
            AppWidgetManager.getInstance( context ).notifyAppWidgetViewDataChanged(allWidgetIds, R.id.widget_grid );
        }
        else{
            editor.putInt("id", -1);
            editor.putBoolean("modeActive",false);
        }
        editor.commit();
    }

    public static void setBrightness(Context context,String b){
        Intent i = new Intent(context,TransparentActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if(!b.equals(""))
            i.putExtra("brightness",b);
        context.startActivity(i);
    }

    public static void unlockScreen(Context context){


        if (mKeyguardLock == null) {
            KeyguardManager km = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = km.newKeyguardLock("MyKeyguardLock");
        }
        try{
            mKeyguardLock.reenableKeyguard();
        }
        catch(Exception e){
        }
        mKeyguardLock.disableKeyguard();
    }

    public static void lockScreen(Context context){
        if (mKeyguardLock == null) {
            KeyguardManager km = (KeyguardManager)context. getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = km.newKeyguardLock("MyKeyguardLock");
        }
        try{
            mKeyguardLock.reenableKeyguard();
        }
        catch(Exception e){
        }
    }

}
