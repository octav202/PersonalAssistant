package com.example.octav.proiect.Utils;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Octav on 4/14/2016.
 */
 public class PhoneCallListener extends PhoneStateListener {

    private Context context;
    private String incoming_nr;
    private int prev_state;

    public PhoneCallListener(Context c){
        this.context = c;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        if(incomingNumber!=null&&incomingNumber.length()>0)
            incoming_nr=incomingNumber;

        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                prev_state=state;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                prev_state=state;
                break;
            case TelephonyManager.CALL_STATE_IDLE:

                if (incoming_nr != null) {
                    //SmsManager smsManager = SmsManager.getDefault();
                    //smsManager.sendTextMessage(incoming_nr, null, "SUBJECT IS BUSY", null, null);

                    Toast.makeText(context, "LAST CALL : "+incomingNumber, Toast.LENGTH_SHORT).show();
                }

                if((prev_state==TelephonyManager.CALL_STATE_OFFHOOK)){
                    prev_state=state;
                    //Answered Call which is ended
                }
                if((prev_state==TelephonyManager.CALL_STATE_RINGING)){
                    prev_state=state;
                    //Rejected or Missed call
                }
                break;

        }

    }
}