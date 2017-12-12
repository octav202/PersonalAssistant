package com.example.octav.proiect.Alarms;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Utils.Utils;

import java.util.List;

/**
 * Created by Octav on 3/14/2016.
 */
public class AlarmView extends Activity {

    TextView titleTextView;
    TextView timeTextView;
    TextView soundTextView;
    TextView volumeTextView;
    TextView messageTextView;
    ImageView image;
    Button okButton;

    //Mode

    private LinearLayout modeWrapper;
    private TextView modeText;
    private ImageView wifi;
    private ImageView ringtone;
    private ImageView call;
    private ImageView message;
    private ImageView media;
    private ImageView brightness;
    private ImageView bluetooth;
    private ImageView lockscreen;

    private KeyguardManager.KeyguardLock mKeyguardLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_view);
        titleTextView = (TextView)findViewById(R.id.titleTextView);
        timeTextView = (TextView)findViewById(R.id.timeTextView);
        messageTextView = (TextView)findViewById(R.id.messageTextView);
        soundTextView = (TextView)findViewById(R.id.alarmSoundTextView);
        volumeTextView = (TextView)findViewById(R.id.alarmVolumeTextView);
        image = (ImageView)findViewById(R.id.alarm_view_image);
        okButton = (Button)findViewById(R.id.okButton);

        //Mode

        modeWrapper = (LinearLayout)findViewById(R.id.av_mode_wrapper);
        modeText = (TextView)findViewById(R.id.av_mode);
        wifi = (ImageView)findViewById(R.id.av_wifi_image);
        ringtone = (ImageView)findViewById(R.id.av_ringtone_image);
        call = (ImageView)findViewById(R.id.av_call_image);
        message = (ImageView)findViewById(R.id.av_message_image);

        media = (ImageView)findViewById(R.id.av_media_image);
        brightness = (ImageView)findViewById(R.id.av_br_image);
        bluetooth = (ImageView)findViewById(R.id.av_bluetooth_image);
        lockscreen = (ImageView)findViewById(R.id.av_lock_image);

        AlarmObject alarm =(AlarmObject) getIntent().getExtras().getParcelable("alarmInfo");

        titleTextView.setText(alarm.id+". " + alarm.name);
        timeTextView.setText(alarm.hour + ":" + alarm.minute);

        //Extract set mode
        DataBase db = new DataBase(AlarmView.this.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        db.deleteAlarm(alarm);

        String modeName = "";
        ModeObject mode = null;

        List<ModeObject> modeList = db.getModes();
        for(ModeObject m : modeList){
            if(m.id == alarm.mode){
                Utils.setMode(AlarmView.this,m,false);
                modeName = m.name;
                mode = m;
                break;
            }
        }

        if(mode == null){
            modeWrapper.setVisibility(View.GONE);
        }
        else{
            modeWrapper.setVisibility(View.VISIBLE);

            modeText.setText("Switched to "+ mode.name);
            if(!mode.wifi)
                wifi.setAlpha(0.2f);
            else
                wifi.setAlpha(1f);

            if(mode.ringtone.equals("")) {
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
                ringtone.setAlpha(0.4f);
            }
            if(mode.ringtone.equals("Normal"))
               ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
            if(mode.ringtone.equals("Vibrate"))
               ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vibrate));
            if(mode.ringtone.equals("Mute")) {
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
                ringtone.setAlpha(0.4f);
            }


            if(mode.callMessage.equals(""))
                call.setAlpha(0.2f);
            else
                call.setAlpha(1f);

            if(mode.smsMessage.equals(""))
                message.setAlpha(0.2f);
            else
                message.setAlpha(1f);


            //Media

            if(mode.mediaVolume.equals("Min"))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_min));
            if(mode.mediaVolume.equals("Medium"))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_medium));
            if(mode.mediaVolume.equals("Max"))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_max));
            if(mode.mediaVolume.equals("")) {
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_medium));
                media.setAlpha(0.4f);
            }

            //Brightness

            if(mode.brightness.equals("Low"))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_low));
            if(mode.brightness.equals("Medium"))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_medium));
            if(mode.brightness.equals("High"))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_high));
            if(mode.brightness.equals("")) {
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_medium));
                brightness.setAlpha(0.4f);
            }

            //Bluetooth

            if(mode.bluetooth.equals("Yes"))
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_yes));
            if(mode.bluetooth.equals("No"))
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_no));
            if(mode.bluetooth.equals("")) {
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_yes));
                bluetooth.setAlpha(0.4f);
            }

            //Lockscreen

            if(mode.lockScreen.equals("Yes"))
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_closed));
            if(mode.lockScreen.equals("No"))
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_open));
            if(mode.lockScreen.equals("")) {
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_closed));
                lockscreen.setAlpha(0.4f);
            }
        }

        if(modeName.equals("")){

        }
        else{
            messageTextView.setText("Switched to "+modeName+" mode");
        }
        //Get Settings

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String alarmSound = settings.getString("AlarmSound", null);
        Float alarmVolume = settings.getFloat("AlarmVolume", 1);
        if(alarmVolume==null)
            alarmVolume = (float)0.5;

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if(alarmSound==null)
            alarmSound="Ringtone";

        switch(alarmSound){

            case "Ringtone":uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                break;
            case "Alarm":uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                break;
            case "Notification":uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                break;
            default:break;

        }


        soundTextView.setText("Sound :"+alarmSound);
        volumeTextView.setText("Volume :"+alarmVolume);

        final MediaPlayer mp = MediaPlayer.create(AlarmView.this,uri);
        mp.setVolume(alarmVolume,alarmVolume);
        mp.start();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                finish();
                lockScreen();
            }
        });

        unlockScreen();
        animateImage();
    }

    void animateImage() {
        ObjectAnimator animX = ObjectAnimator.ofFloat(image, "scaleX", 1.5f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(image, "scaleY", 1.5f);
        animX.setRepeatCount(ValueAnimator.INFINITE);
        animY.setRepeatCount(ValueAnimator.INFINITE);
        animX.setRepeatMode(ObjectAnimator.REVERSE);
        animY.setRepeatMode(ObjectAnimator.REVERSE);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.setDuration(600);
        animSetXY.playTogether(animX, animY);
        animSetXY.start();

    }

    public void unlockScreen(){

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();

        if (mKeyguardLock == null) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = km.newKeyguardLock("MyKeyguardLock");
        }
        try{
            mKeyguardLock.reenableKeyguard();
        }
        catch(Exception e){
        }
        mKeyguardLock.disableKeyguard();
    }

    public void lockScreen(){
        if (mKeyguardLock == null) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = km.newKeyguardLock("MyKeyguardLock");
        }
        try{
            mKeyguardLock.reenableKeyguard();
        }
        catch(Exception e){
        }

    }
}
