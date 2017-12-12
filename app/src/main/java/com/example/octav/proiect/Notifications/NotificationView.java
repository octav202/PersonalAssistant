package com.example.octav.proiect.Notifications;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;



public class NotificationView extends AppCompatActivity {

    private TextView ui_title;
    private TextView ui_message;
    private Button ui_button;

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
    private TextView n_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_view);

        ui_title = (TextView)findViewById(R.id.notification_title);
        ui_message = (TextView)findViewById(R.id.notification_message);
        ui_button = (Button)findViewById(R.id.notification_view_button);

        //Mode

        modeWrapper = (LinearLayout)findViewById(R.id.nv_mode_wrapper);
        modeText = (TextView)findViewById(R.id.nv_mode);
        wifi = (ImageView)findViewById(R.id.nv_wifi_image);
        ringtone = (ImageView)findViewById(R.id.nv_ringtone_image);
        call = (ImageView)findViewById(R.id.nv_call_image);
        message = (ImageView)findViewById(R.id.nv_message_image);
        media = (ImageView)findViewById(R.id.nv_media_image);
        brightness = (ImageView)findViewById(R.id.nv_br_image);
        bluetooth = (ImageView)findViewById(R.id.nv_bluetooth_image);
        lockscreen = (ImageView)findViewById(R.id.nv_lock_image);

        n_message = (TextView)findViewById(R.id.nv_message);

        NotificationObject n = getIntent().getParcelableExtra("notificationInfo");
        Boolean reminder = getIntent().getExtras().getBoolean("reminder");
        ModeObject mode = getIntent().getParcelableExtra("modeInfo");

        n_message.setText(n.message);

        if(mode == null){
            modeWrapper.setVisibility(View.GONE);
        }
        else{



            modeWrapper.setVisibility(View.VISIBLE);
            if(reminder)
                modeText.setText("Will switch to "+ mode.name);
            else
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
        ui_title.setText(n.name);
        if(reminder) {
            String text = "At " + n.hour + " : " + n.minute + "\n(" + n.reminder + "min. remaining)";
            ui_message.setText(text);
            ui_message.setVisibility(View.VISIBLE);
        }
        else
            ui_message.setVisibility(View.GONE);

        ui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
