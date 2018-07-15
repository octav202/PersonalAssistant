package com.example.octav.proiect.Notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Utils.Utils;

import java.util.List;

import static com.example.octav.proiect.Utils.Constants.HIGH;
import static com.example.octav.proiect.Utils.Constants.LOW;
import static com.example.octav.proiect.Utils.Constants.MAX;
import static com.example.octav.proiect.Utils.Constants.MEDIUM;
import static com.example.octav.proiect.Utils.Constants.MIN;
import static com.example.octav.proiect.Utils.Constants.MODE_EXTRA_PARCELABLE;
import static com.example.octav.proiect.Utils.Constants.MUTE;
import static com.example.octav.proiect.Utils.Constants.NORMAL;
import static com.example.octav.proiect.Utils.Constants.NOTIFICATION_EXTRA_ID;
import static com.example.octav.proiect.Utils.Constants.OFF;
import static com.example.octav.proiect.Utils.Constants.ON;
import static com.example.octav.proiect.Utils.Constants.REMINDER;
import static com.example.octav.proiect.Utils.Constants.VIBRATE;
import static com.example.octav.proiect.Utils.Constants.TAG;


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

        ui_title = (TextView) findViewById(R.id.notification_title);
        ui_message = (TextView) findViewById(R.id.notification_message);
        ui_button = (Button) findViewById(R.id.notification_view_button);

        //Mode

        modeWrapper = (LinearLayout) findViewById(R.id.nv_mode_wrapper);
        modeText = (TextView) findViewById(R.id.nv_mode);
        wifi = (ImageView) findViewById(R.id.nv_wifi_image);
        ringtone = (ImageView) findViewById(R.id.nv_ringtone_image);
        call = (ImageView) findViewById(R.id.nv_call_image);
        message = (ImageView) findViewById(R.id.nv_message_image);
        media = (ImageView) findViewById(R.id.nv_media_image);
        brightness = (ImageView) findViewById(R.id.nv_br_image);
        bluetooth = (ImageView) findViewById(R.id.nv_bluetooth_image);
        lockscreen = (ImageView) findViewById(R.id.nv_lock_image);

        n_message = (TextView) findViewById(R.id.nv_message);

        ui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NotificationObject n = null;
        int notificationId = getIntent().getIntExtra(NOTIFICATION_EXTRA_ID ,0);
        DataBase db = new DataBase(NotificationView.this.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));

        for (NotificationObject not : db.getNotifications()) {
            if (not.id == notificationId) {
                n = not;
                break;
            }
        }

        Boolean reminder = getIntent().getExtras().getBoolean(REMINDER);

        if (n == null) {
            Log.d(TAG, " Null Notification Object");
            return;
        }

        // Get Mode
        ModeObject mode = null;
        List<ModeObject> modeList = db.getModes();
        for (ModeObject m : modeList) {
            if (m.id == n.mode) {
                Utils.setMode(NotificationView.this, m, false);
                mode = m;
                break;
            }
        }

        n_message.setText(n.message);

        if (mode == null) {
            modeWrapper.setVisibility(View.GONE);
        } else {


            modeWrapper.setVisibility(View.VISIBLE);
            if (reminder)
                modeText.setText("Will switch to " + mode.name);
            else
                modeText.setText("Switched to " + mode.name);
            if (!mode.wifi)
                wifi.setAlpha(0.2f);
            else
                wifi.setAlpha(1f);

            if (mode.ringtone.equals("")) {
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
                ringtone.setAlpha(0.4f);
            }
            if (mode.ringtone.equals(NORMAL))
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
            if (mode.ringtone.equals(VIBRATE))
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vibrate));
            if (mode.ringtone.equals(MUTE)) {
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
                ringtone.setAlpha(0.4f);
            }


            if (mode.callMessage.equals(""))
                call.setAlpha(0.2f);
            else
                call.setAlpha(1f);

            if (mode.smsMessage.equals(""))
                message.setAlpha(0.2f);
            else
                message.setAlpha(1f);

            //Media

            if (mode.mediaVolume.equals(MIN))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_min));
            if (mode.mediaVolume.equals(MEDIUM))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_medium));
            if (mode.mediaVolume.equals(MAX))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_max));
            if (mode.mediaVolume.equals("")) {
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_medium));
                media.setAlpha(0.4f);
            }

            //Brightness

            if (mode.brightness.equals(LOW))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_low));
            if (mode.brightness.equals(MEDIUM))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_medium));
            if (mode.brightness.equals(HIGH))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_high));
            if (mode.brightness.equals("")) {
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_medium));
                brightness.setAlpha(0.4f);
            }

            //Bluetooth

            if (mode.bluetooth.equals(ON))
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_yes));
            if (mode.bluetooth.equals(OFF))
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_no));
            if (mode.bluetooth.equals("")) {
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_yes));
                bluetooth.setAlpha(0.4f);
            }

            //Lockscreen

            if (mode.lockScreen.equals(ON))
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_closed));
            if (mode.lockScreen.equals(OFF))
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_open));
            if (mode.lockScreen.equals("")) {
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_closed));
                lockscreen.setAlpha(0.4f);
            }

        }
        ui_title.setText(n.name);
        if (reminder) {
            String text = "At " + n.hour + " : " + n.minute + "\n(" + n.reminder + "min. remaining)";
            ui_message.setText(text);
            ui_message.setVisibility(View.VISIBLE);
        } else
            ui_message.setVisibility(View.GONE);

        //Delete Notification from database
        db.deleteNotification(n);
    }
}
