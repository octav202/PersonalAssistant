package com.example.octav.proiect.Utils;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;


import com.example.octav.proiect.R;

public class SettingsFragment extends Fragment {

    //Notifications

    private RadioGroup  n_type_group;
    private RadioButton n_type_mute;
    private RadioButton n_type_sound;
    private RadioButton n_type_vibrate;

    //Light

    private Button l_primary;
    private Button l_primary_dark;
    private Button l_primary_accent;
    private Button l_primary_blue;
    private Button l_primary_red;

    private FrameLayout l_primary_wrapper;
    private FrameLayout l_primary_dark_wrapper;
    private FrameLayout l_primary_accent_wrapper;
    private FrameLayout l_primary_blue_wrapper;
    private FrameLayout l_primary_red_wrapper;

    //Location

    private RadioGroup  l_interval_group;
    private RadioButton l_interval_1;
    private RadioButton l_interval_3;
    private RadioButton l_interval_5;

    private RadioGroup  l_type_group;
    private RadioButton l_type_normal;
    private RadioButton l_type_hybrid;
    private RadioButton l_type_satellite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.settings_fragment, container, false);

        //Alarm

        RadioGroup group = (RadioGroup)fragmentView.findViewById(R.id.radioGroup);
        RadioButton btn1 = (RadioButton)fragmentView.findViewById(R.id.radioButton);
        RadioButton btn2 = (RadioButton)fragmentView.findViewById(R.id.radioButton2);
        RadioButton btn3 = (RadioButton)fragmentView.findViewById(R.id.radioButton3);

        SeekBar volumeSeekBar = (SeekBar)fragmentView.findViewById(R.id.volumeSeekBar);

        //Notifications
        n_type_group = (RadioGroup)fragmentView.findViewById(R.id.n_type_radio_group);
        n_type_mute  = (RadioButton)fragmentView.findViewById(R.id.n_mute_radio);
        n_type_sound = (RadioButton)fragmentView.findViewById(R.id.n_sound_radio);
        n_type_vibrate = (RadioButton)fragmentView.findViewById(R.id.n_vibrate_radio);


        l_primary = (Button)fragmentView.findViewById(R.id.light_primary);
        l_primary_dark = (Button)fragmentView.findViewById(R.id.light_primaryDark);
        l_primary_accent = (Button)fragmentView.findViewById(R.id.light_accent);
        l_primary_blue = (Button)fragmentView.findViewById(R.id.light_blue);
        l_primary_red = (Button)fragmentView.findViewById(R.id.light_red);


        l_primary_wrapper = (FrameLayout) fragmentView.findViewById(R.id.light_primary_wrapper);
        l_primary_dark_wrapper = (FrameLayout)fragmentView.findViewById(R.id.light_primaryDark_wrapper);
        l_primary_accent_wrapper = (FrameLayout)fragmentView.findViewById(R.id.light_accent_wrapper);
        l_primary_blue_wrapper = (FrameLayout)fragmentView.findViewById(R.id.light_blue_wrapper);
        l_primary_red_wrapper = (FrameLayout)fragmentView.findViewById(R.id.light_red_wrapper);

        //Location

        l_interval_group = (RadioGroup)fragmentView.findViewById(R.id.l_interval_radio_group);
        l_interval_1 = (RadioButton)fragmentView.findViewById(R.id.l_interaval_1_radio);
        l_interval_3 = (RadioButton)fragmentView.findViewById(R.id.l_interaval_3_radio);
        l_interval_5 = (RadioButton)fragmentView.findViewById(R.id.l_interaval_5_radio);

        l_type_group = (RadioGroup)fragmentView.findViewById(R.id.l_map_radio_group);
        l_type_normal = (RadioButton)fragmentView.findViewById(R.id.l_map_normal_radio);
        l_type_hybrid = (RadioButton)fragmentView.findViewById(R.id.l_map_hybrid_radio);
        l_type_satellite = (RadioButton)fragmentView.findViewById(R.id.l_map_satellite_radio);


        // ALARM

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String alarmSound = settings.getString("AlarmSound", null);
        if(alarmSound==null)
            alarmSound="Ringtone";
        switch(alarmSound){
            case "Ringtone":btn1.setChecked(true);
                break;
            case "Alarm":btn2.setChecked(true);
                break;
            case "Notification":btn3.setChecked(true);
                break;
            default:break;

        }
        Float alarmVolume = settings.getFloat("AlarmVolume", 1);

        if(alarmVolume==null)
            alarmVolume = (float)0.5;

        volumeSeekBar.setProgress(Math.round(alarmVolume * 100));

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                String alarmSound = button.getText().toString();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.putString("AlarmSound", alarmSound);
                editor.apply();
            }
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float) progress / 100;

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.putFloat("AlarmVolume", volume);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // NOTIFICATIONS

        String notificationSound = settings.getString("notification_type", null);
        if(notificationSound==null)
            notificationSound="Sound";
        switch(notificationSound) {
            case "Sound":
                n_type_sound.setChecked(true);
                break;
            case "Vibrate":
                n_type_vibrate.setChecked(true);
                break;
            case "Silent":
                n_type_mute.setChecked(true);
                break;
            default:
                break;
        }

        n_type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                String nType = button.getText().toString();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.putString("notification_type", nType);
                editor.apply();
            }
        });


        //Light

        final String hexCode = settings.getString("notification_light", "");
        final SharedPreferences.Editor  editor = settings.edit();

        if(hexCode != null && !hexCode.equals("")){
            if(hexCode.equals("#009688"))
                l_primary_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
            if(hexCode.equals("#00796B"))
                l_primary_dark_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
            if(hexCode.equals("#FF5722"))
                l_primary_accent_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
            if(hexCode.equals("#0000FF"))
                l_primary_blue_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
            if(hexCode.equals("#FF0000"))
                l_primary_red_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
        }

        View.OnClickListener lightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String colorHex="color";
                Button b = (Button)v;
                if(b == l_primary)
                    colorHex = "#009688";
                if(b == l_primary_dark)
                    colorHex = "#00796B";
                if(b == l_primary_accent)
                    colorHex = "#FF5722";
                if(b == l_primary_blue)
                    colorHex = "#0000FF";
                if(b == l_primary_red)
                    colorHex = "#FF0000";

                editor.putString("notification_light",colorHex);
                editor.apply();

                // Deselect all colors

                l_primary_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_unselected));
                l_primary_dark_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_unselected));
                l_primary_accent_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_unselected));
                l_primary_blue_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_unselected));
                l_primary_red_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_unselected));

                //Select Color
                if(colorHex.equals("#009688"))
                    l_primary_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
                if(colorHex.equals("#00796B"))
                    l_primary_dark_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
                if(colorHex.equals("#FF5722"))
                    l_primary_accent_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
                if(colorHex.equals("#0000FF"))
                    l_primary_blue_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
                if(colorHex.equals("#FF0000"))
                    l_primary_red_wrapper.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.color_selected));
            }

        };

        l_primary_dark.setOnClickListener(lightListener);
        l_primary.setOnClickListener(lightListener);
        l_primary_accent.setOnClickListener(lightListener);
        l_primary_blue.setOnClickListener(lightListener);
        l_primary_red.setOnClickListener(lightListener);

        // LOCATION


        //Interval

        String locationInterval = settings.getString("location_interval", null);
        if(locationInterval==null)
            locationInterval="1 min";
        switch(locationInterval) {
            case "1 min":
                l_interval_1.setChecked(true);
                break;
            case "3 min":
                l_interval_3.setChecked(true);
                break;
            case "5 min":
                l_interval_5.setChecked(true);
                break;
            default:
                break;
        }

        l_interval_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                String locType = button.getText().toString();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.putString("location_interval", locType);
                editor.apply();
            }
        });

        //Map Type

        String map_type = settings.getString("map_type", null);
        if(map_type==null)
            map_type="Normal";
        switch(map_type) {
            case "Normal":
                l_type_normal.setChecked(true);
                break;
            case "Hybrid":
                l_type_hybrid.setChecked(true);
                break;
            case "Satellite":
                l_type_satellite.setChecked(true);
                break;
            default:
                break;
        }

        l_type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                String mapType = button.getText().toString();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.putString("map_type", mapType);
                editor.apply();
            }
        });

        return fragmentView;
    }
}
