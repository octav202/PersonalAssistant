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
import android.util.Log;
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

import static com.example.octav.proiect.Utils.Constants.ACTIVE_KEY;
import static com.example.octav.proiect.Utils.Constants.CALL_MESSAGE_KEY;
import static com.example.octav.proiect.Utils.Constants.EXTRA_BRIGHTNESS;
import static com.example.octav.proiect.Utils.Constants.HIGH;
import static com.example.octav.proiect.Utils.Constants.ID_KEY;
import static com.example.octav.proiect.Utils.Constants.IMAGE_KEY;
import static com.example.octav.proiect.Utils.Constants.KEY_GUARD_LOCK;
import static com.example.octav.proiect.Utils.Constants.LOW;
import static com.example.octav.proiect.Utils.Constants.MAX;
import static com.example.octav.proiect.Utils.Constants.MEDIUM;
import static com.example.octav.proiect.Utils.Constants.MIN;
import static com.example.octav.proiect.Utils.Constants.MUTE;
import static com.example.octav.proiect.Utils.Constants.NAME_KEY;
import static com.example.octav.proiect.Utils.Constants.NORMAL;
import static com.example.octav.proiect.Utils.Constants.OFF;
import static com.example.octav.proiect.Utils.Constants.ON;
import static com.example.octav.proiect.Utils.Constants.RINGTONE_KEY;
import static com.example.octav.proiect.Utils.Constants.SHARED_NAME;
import static com.example.octav.proiect.Utils.Constants.SMS_MESSAGE_KEY;
import static com.example.octav.proiect.Utils.Constants.TAG;
import static com.example.octav.proiect.Utils.Constants.VIBRATE;
import static com.example.octav.proiect.Utils.Constants.WIFI_KEY;

/**
 * Created by Octav on 4/15/2016.
 */
public class Utils {

    private static KeyguardManager.KeyguardLock mKeyguardLock;

    public static String saveToInternalStorage(Context context, Bitmap bitmapImage, String name) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, name + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // Toast.makeText(context,fos.toString(),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadImageFromStorage(Context context, String name) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, name + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void setMode(Context context, ModeObject mode, boolean fromWidget) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE).edit();

        if (mode != null) {
            editor.putInt(ID_KEY, mode.id);
            editor.putString(NAME_KEY, mode.name);
            editor.putString(CALL_MESSAGE_KEY, mode.callMessage);
            editor.putString(SMS_MESSAGE_KEY, mode.smsMessage);
            editor.putBoolean(WIFI_KEY, mode.wifi);
            editor.putString(RINGTONE_KEY, mode.ringtone);
            editor.putString(IMAGE_KEY, mode.image);
            editor.putBoolean(ACTIVE_KEY, true);

            //Ringtone
            AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (mode.ringtone.equals(MUTE))
                manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            if (mode.ringtone.equals(VIBRATE))
                manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if (mode.ringtone.equals(NORMAL))
                manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            //Media

            if (mode.mediaVolume.equals(MIN)) {
                AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float percent = 0.0f;
                int mVolume = (int) (maxVolume * percent);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
            }
            if (mode.mediaVolume.equals(MEDIUM)) {
                AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float percent = 0.5f;
                int mVolume = (int) (maxVolume * percent);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
            }
            if (mode.mediaVolume.equals(MAX)) {
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

            if (mode.brightness.equals(LOW)) {
                try {
                    android.provider.Settings.System.putInt(context.getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);

                    if (!fromWidget) {
                        WindowManager.LayoutParams lp;
                        lp = ((Activity) context).getWindow().getAttributes();
                        lp.screenBrightness = 0f;
                        ((Activity) context).getWindow().setAttributes(lp);
                    }
                } catch (Exception e) {

                }

            }
            if (mode.brightness.equals(MEDIUM)) {
                try {
                    android.provider.Settings.System.putInt(context.getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS, 126);
                    if (!fromWidget) {
                        WindowManager.LayoutParams lp;
                        lp = ((Activity) context).getWindow().getAttributes();
                        lp.screenBrightness = 0.5f;
                        ((Activity) context).getWindow().setAttributes(lp);
                    }
                } catch (Exception e) {

                }

            }
            if (mode.brightness.equals(HIGH)) {
                try {
                    android.provider.Settings.System.putInt(context.getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);
                    if (!fromWidget) {
                        WindowManager.LayoutParams lp;
                        lp = ((Activity) context).getWindow().getAttributes();
                        lp.screenBrightness = 1f;
                        ((Activity) context).getWindow().setAttributes(lp);
                    }
                } catch (Exception e) {

                }

            }

            if (fromWidget)
                setBrightness(context, mode.brightness);

            //Bluetooth

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter != null) {
                if (mode.bluetooth.equals(OFF))
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }

                if (mode.bluetooth.equals(ON)) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }
                }

            } else {
                Log.d(TAG, "BluetoothAdapter.getDefaultAdapter is NULL");
            }

            if (mode.lockScreen.equals(OFF))
                unlockScreen(context);
            if (mode.lockScreen.equals(ON))
                lockScreen(context);

            //Update Widget

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);
            int[] allWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget);
            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(allWidgetIds, R.id.widget_grid);
        } else {
            editor.putInt(ID_KEY, -1);
            editor.putBoolean(ACTIVE_KEY, false);
        }
        editor.commit();
    }

    public static void setBrightness(Context context, String b) {
        Intent i = new Intent(context, TransparentActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (!b.equals("")) {
            i.putExtra(EXTRA_BRIGHTNESS, b);
        }
        context.startActivity(i);
    }

    public static void unlockScreen(Context context) {


        if (mKeyguardLock == null) {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = km.newKeyguardLock(KEY_GUARD_LOCK);
        }
        try {
            mKeyguardLock.reenableKeyguard();
        } catch (Exception e) {
        }
        mKeyguardLock.disableKeyguard();
    }

    public static void lockScreen(Context context) {
        if (mKeyguardLock == null) {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = km.newKeyguardLock(KEY_GUARD_LOCK);
        }
        try {
            mKeyguardLock.reenableKeyguard();
        } catch (Exception e) {
        }
    }

}
