package com.example.octav.proiect.NFC;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class NFCAsyncTask extends AsyncTask<Tag, Void, String> {

    public interface NFCListener{
        void readSuccess(String text);
    }

    private Context context;
    private NFCListener listener;
    private int modeID;

    public NFCAsyncTask(Context c, NFCListener l, int idToWrite){
        context = c;
        listener=l;
        modeID = idToWrite;
    }

    @Override
    protected String doInBackground(Tag... params) {

        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if(!ndef.isWritable())
            return "NOT WRITABLE";

        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        //WRITE TAG
        if(modeID!=0) {
            try {
                if(tag!=null){
                    write(modeID+"",tag);
                    return modeID+"";
                }
                else{
                    return "TAG IS NULL";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }

        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
        if(ndefMessage == null){
            return null;
        }
        else {
            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e("NFC_TAG", "Unsupported Encoding", e);
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.readSuccess(result);
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        Log.e("RECORD",new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding));
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }

}