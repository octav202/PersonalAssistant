package com.example.octav.proiect.NFC;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.Modes.ModePickerDialog;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Utils.Utils;

import java.util.List;

import static com.example.octav.proiect.Utils.Constants.HIGH;
import static com.example.octav.proiect.Utils.Constants.LOW;
import static com.example.octav.proiect.Utils.Constants.MAX;
import static com.example.octav.proiect.Utils.Constants.MEDIUM;
import static com.example.octav.proiect.Utils.Constants.MIN;
import static com.example.octav.proiect.Utils.Constants.MUTE;
import static com.example.octav.proiect.Utils.Constants.NORMAL;
import static com.example.octav.proiect.Utils.Constants.OFF;
import static com.example.octav.proiect.Utils.Constants.ON;
import static com.example.octav.proiect.Utils.Constants.VIBRATE;


/**
 * Created by Octav on 5/2/2016.
 */

public class NFCActivity extends AppCompatActivity implements NFCAsyncTask.NFCListener {

    private TextView ui_title;
    private Button ui_button;
    private Button ui_edit;
    private ImageView ui_image;

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

    private NfcAdapter mNfcAdapter;
    private Tag currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_view);
        Log.e("NFC DETECTED","NFC DETECTED");
        initNFC();
    }

    private void initNFC(){

        ui_title = (TextView)findViewById(R.id.nfc_title);
        ui_button = (Button)findViewById(R.id.nfc_view_button);
        ui_image = (ImageView)findViewById(R.id.nfc_image);
        ui_edit = (Button)findViewById(R.id.nfc_edit_button);
        //Mode

        modeWrapper = (LinearLayout)findViewById(R.id.nfc_mode_wrapper);
        modeText = (TextView)findViewById(R.id.nfc_mode);
        wifi = (ImageView)findViewById(R.id.nfc_wifi_image);
        ringtone = (ImageView)findViewById(R.id.nfc_ringtone_image);
        call = (ImageView)findViewById(R.id.nfc_call_image);
        message = (ImageView)findViewById(R.id.nfc_message_image);

        media = (ImageView)findViewById(R.id.nfc_media_image);
        brightness = (ImageView)findViewById(R.id.nfc_br_image);
        bluetooth = (ImageView)findViewById(R.id.nfc_bluetooth_image);
        lockscreen = (ImageView)findViewById(R.id.nfc_lock_image);

        ui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ui_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ModePickerDialog picker = new ModePickerDialog();
                picker.setListener(new ModePickerDialog.ModePickerListener() {
                    @Override
                    public void update(String modeName,int modeId) {
                        ui_title.setText(modeId+"."+modeName);
                        if(currentTag!=null) {
                            new NFCAsyncTask(NFCActivity.this, new NFCAsyncTask.NFCListener() {
                                @Override
                                public void readSuccess(String text) {

                                }


                            }, modeId).execute(currentTag);
                        }
                    }
                });
                picker.show(getFragmentManager(), "mode_picker");

            }
        });

        //NFC

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC IS DISABLED.", Toast.LENGTH_LONG).show();
        } else {
        }

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if ("text/plain".equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                currentTag = tag;
                new NFCAsyncTask(NFCActivity.this,this,0).execute(tag);

            } else {

            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            currentTag = tag;
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NFCAsyncTask(NFCActivity.this,this,0).execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_mode) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void readSuccess(String text) {
        if(text == null){
            extractMode(0);
            ui_image.setImageDrawable(ContextCompat.getDrawable(NFCActivity.this, R.drawable.ic_nfc_add));
            ui_edit.setVisibility(View.GONE);
            ui_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ModePickerDialog picker = new ModePickerDialog();
                    picker.setListener(new ModePickerDialog.ModePickerListener() {
                        @Override
                        public void update(String modeName,int modeId) {
                           ui_title.setText(modeId+"."+modeName);
                            if(currentTag!=null) {
                                new NFCAsyncTask(NFCActivity.this, new NFCAsyncTask.NFCListener() {
                                    @Override
                                    public void readSuccess(String text) {

                                    }


                                }, modeId).execute(currentTag);
                            }
                        }
                    });
                    picker.show(getFragmentManager(), "mode_picker");

                }
            });

        }
        else{
            ui_title.setText(text);
            int mode = Integer.valueOf(text);
            extractMode(mode);
        }
    }

    private void extractMode(int mode){

        if(mode == 0){
            modeWrapper.setVisibility(View.GONE);
            ui_title.setText("This tag is empty...\nPlease select a mode.");

        }

        DataBase db = new DataBase(NFCActivity.this.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        ModeObject currentMode = null;
        List<ModeObject> modeList = db.getModes();
        for(ModeObject m : modeList){
            if(m.id == mode){
                currentMode = m;
            }
        }

        if(currentMode == null){
            modeWrapper.setVisibility(View.GONE);
            ui_title.setText("Profile not found");
        }
        else{

            Utils.setMode(NFCActivity.this,currentMode,false);

            ui_title.setText(currentMode.name);
            modeWrapper.setVisibility(View.VISIBLE);

            modeText.setText("Switched to "+ currentMode.name);
            if(!currentMode.wifi)
                wifi.setAlpha(0.2f);
            else
                wifi.setAlpha(1f);

            if(currentMode.ringtone.equals("")) {
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
                ringtone.setAlpha(0.4f);
            }
            if(currentMode.ringtone.equals(NORMAL))
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
            if(currentMode.ringtone.equals(VIBRATE))
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vibrate));
            if(currentMode.ringtone.equals(MUTE)) {
                ringtone.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ringtone));
                ringtone.setAlpha(0.4f);
            }


            if(currentMode.callMessage.equals(""))
                call.setAlpha(0.2f);
            else
                call.setAlpha(1f);

            if(currentMode.smsMessage.equals(""))
                message.setAlpha(0.2f);
            else
                message.setAlpha(1f);


            //Media
            if(currentMode.mediaVolume.equals(MIN))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_min));
            if(currentMode.mediaVolume.equals(MEDIUM))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_medium));
            if(currentMode.mediaVolume.equals(MAX))
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_max));
            if(currentMode.mediaVolume.equals("")) {
                media.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_media_volume_medium));
                media.setAlpha(0.4f);
            }

            //Brightness

            if(currentMode.brightness.equals(LOW))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_low));
            if(currentMode.brightness.equals(MEDIUM))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_medium));
            if(currentMode.brightness.equals(HIGH))
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_high));
            if(currentMode.brightness.equals("")) {
                brightness.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_brightness_medium));
                brightness.setAlpha(0.4f);
            }

            //Bluetooth

            if(currentMode.bluetooth.equals(ON))
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_yes));
            if(currentMode.bluetooth.equals(OFF))
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_no));
            if(currentMode.bluetooth.equals("")) {
                bluetooth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bluetooth_yes));
                bluetooth.setAlpha(0.4f);
            }

            //Lockscreen

            if(currentMode.lockScreen.equals(ON))
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_closed));
            if(currentMode.lockScreen.equals(OFF))
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_open));
            if(currentMode.lockScreen.equals("")) {
                lockscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_lock_closed));
                lockscreen.setAlpha(0.4f);
            }
        }
    }

}