package com.example.octav.proiect.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Octav on 4/15/2016.
 */
public class SMSListener extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    //SmsManager smsManager = SmsManager.getDefault();
                    //smsManager.sendTextMessage(incoming_nr, null, "SUBJECT IS BUSY", null, null);
                    Toast.makeText(context, "senderNum: "+ phoneNumber + ", message: " + message, Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}