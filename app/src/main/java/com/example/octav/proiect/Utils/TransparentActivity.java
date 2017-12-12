package com.example.octav.proiect.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.octav.proiect.R;


/**
 * Created by Octav on 5/8/2016.
 */
public class TransparentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tansparent_layout);

        if(getIntent().getExtras()!=null){
            String brightness = getIntent().getExtras().getString("brightness");
            if(brightness.equals("Low")){
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = 0f;
            }
            if(brightness.equals("Medium")){
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = 0.5f;
            }
            if(brightness.equals("High")){
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = 1f;
            }}
        finish();
    }
}
