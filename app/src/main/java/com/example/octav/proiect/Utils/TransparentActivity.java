package com.example.octav.proiect.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.octav.proiect.R;

import static com.example.octav.proiect.Utils.Constants.EXTRA_BRIGHTNESS;
import static com.example.octav.proiect.Utils.Constants.HIGH;
import static com.example.octav.proiect.Utils.Constants.LOW;
import static com.example.octav.proiect.Utils.Constants.MEDIUM;


public class TransparentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tansparent_layout);

        if(getIntent().getExtras()!=null){
            String brightness = getIntent().getExtras().getString(EXTRA_BRIGHTNESS);
            if(brightness.equals(LOW)){
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = 0f;
            }
            if(brightness.equals(MEDIUM)){
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = 0.5f;
            }
            if(brightness.equals(HIGH)){
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = 1f;
            }}
        finish();
    }
}
