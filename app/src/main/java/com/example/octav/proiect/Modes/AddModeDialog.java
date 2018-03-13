package com.example.octav.proiect.Modes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Utils.Utils;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

public class AddModeDialog extends DialogFragment{

    public interface OnSetModeInterface {

        void setMode(ModeObject mode, Boolean isEditing);
    }

    private final int SELECT_PHOTO = 1;

    Button pickPhotoButton;
    ImageView imageView;
    private CheckBox call_check;
    private CheckBox sms_check;
    private CheckBox wifi_check;
    private CheckBox volume_normal_check;
    private CheckBox volume_vibrate_check;
    private CheckBox volume_mute_check;

    private CheckBox media_mute_check;
    private CheckBox media_normal_check;
    private CheckBox media_max_check;

    private CheckBox br_low_check;
    private CheckBox br_medium_check;
    private CheckBox br_high_check;

    private CheckBox bluetooth_no_check;
    private CheckBox bluetooth_yes_check;

    private CheckBox lock_yes_check;
    private CheckBox lock_no_check;

    private EditText call_message_text;
    private EditText sms_message_text;
    private EditText title_text;

    private Button save_button;
    private Bitmap modeImage;
    private DataBase db;

    private ModeObject currentMode;
    private boolean isEditing;

    public static AddModeDialog newInstance(ModeObject m,boolean isEditing) {
        AddModeDialog dialog = new AddModeDialog();
        Bundle args = new Bundle();
        args.putParcelable("currentMode",m);
        args.putBoolean("isModeEditing",isEditing);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));

        currentMode = getArguments().getParcelable("currentMode");
        isEditing = getArguments().getBoolean("isModeEditing");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_mode_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnimation;

        imageView = (ImageView)view.findViewById(R.id.activityImageView);
        pickPhotoButton = (Button)view.findViewById(R.id.pickPhotoButton);
        pickPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        call_check = (CheckBox)view.findViewById(R.id.call_checkbox);
        sms_check = (CheckBox) view.findViewById(R.id.sms_checkbox);
        wifi_check = (CheckBox) view.findViewById(R.id.wifi_checkbox);
        volume_normal_check = (CheckBox) view.findViewById(R.id.volume_normal_checkbox);
        volume_vibrate_check = (CheckBox) view.findViewById(R.id.volume_vibrate_checkbox);
        volume_mute_check = (CheckBox) view.findViewById(R.id.volume_mute_checkbox);
        save_button = (Button) view.findViewById(R.id.mode_save_button);

        call_message_text = (EditText)view.findViewById(R.id.call_message_text);
        call_message_text.setVisibility(View.GONE);
        sms_message_text = (EditText)view.findViewById(R.id.sms_message_text);
        sms_message_text.setVisibility(View.GONE);
        title_text = (EditText)view.findViewById(R.id.activity_title);

        media_mute_check = (CheckBox)view.findViewById(R.id.media_mute_check);
        media_normal_check = (CheckBox)view.findViewById(R.id.media_normal_check);
        media_max_check = (CheckBox)view.findViewById(R.id.media_max_check);

        br_low_check = (CheckBox)view.findViewById(R.id.br_low_checkbox);
        br_medium_check = (CheckBox)view.findViewById(R.id.br_medium_checkbox);
        br_high_check = (CheckBox)view.findViewById(R.id.br_high_checkbox);

        bluetooth_no_check = (CheckBox)view.findViewById(R.id.bluetooth_no_check);
        bluetooth_yes_check = (CheckBox)view.findViewById(R.id.bluetooth_yes_check);

        lock_no_check = (CheckBox)view.findViewById(R.id.lock_disabled_check);
        lock_yes_check = (CheckBox)view.findViewById(R.id.lock_enabled_check);

        call_check.setChecked(false);
        sms_check.setChecked(false);

        //Call handler

        call_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                call_check.setChecked(isChecked);
                if(isChecked)
                    call_message_text.setVisibility(View.VISIBLE);
                else
                    call_message_text.setVisibility(View.GONE);
            }
        });

        //SMS handler

        sms_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sms_check.setChecked(isChecked);
                if(isChecked)
                    sms_message_text.setVisibility(View.VISIBLE);
                else
                    sms_message_text.setVisibility(View.GONE);
            }
        });

        //Ringtone

        volume_mute_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    volume_normal_check.setChecked(false);
                    volume_vibrate_check.setChecked(false);
                }
            }
        });

        volume_vibrate_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    volume_normal_check.setChecked(false);
                    volume_mute_check.setChecked(false);
                }
            }
        });

        volume_normal_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    volume_mute_check.setChecked(false);
                    volume_vibrate_check.setChecked(false);
                }
            }
        });


        //Media Volume

        media_mute_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    media_normal_check.setChecked(false);
                    media_max_check.setChecked(false);
                }
            }
        });

        media_normal_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    media_mute_check.setChecked(false);
                    media_max_check.setChecked(false);
                }


            }
        });

        media_max_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    media_normal_check.setChecked(false);
                    media_mute_check.setChecked(false);
                }
            }
        });

        //Brightness

        br_low_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    br_medium_check.setChecked(false);
                    br_high_check.setChecked(false);
                }
            }
        });

        br_medium_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    br_low_check.setChecked(false);
                    br_high_check.setChecked(false);
                }

            }
        });

        br_high_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    br_medium_check.setChecked(false);
                    br_low_check.setChecked(false);
                }
            }
        });

        //Bluetooth

        bluetooth_no_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    bluetooth_yes_check.setChecked(false);
                }

            }
        });

        bluetooth_yes_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    bluetooth_no_check.setChecked(false);
                }

            }
        });

        //Lock Screen

        lock_no_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    lock_yes_check.setChecked(false);
            }
        });

        lock_yes_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    lock_no_check.setChecked(false);
            }
        });

        if(isEditing){
            title_text.setText(currentMode.name);
            if(currentMode.callMessage.equals("")){
                call_check.setChecked(false);
                call_message_text.setVisibility(View.GONE);
            }else{
                call_check.setChecked(true);
                call_message_text.setVisibility(View.VISIBLE);
                call_message_text.setText(currentMode.callMessage);
            }

            if(currentMode.smsMessage.equals("")){
                sms_check.setChecked(false);
                sms_message_text.setVisibility(View.GONE);
            }else{
                sms_check.setChecked(true);
                sms_message_text.setVisibility(View.VISIBLE);
                sms_message_text.setText(currentMode.smsMessage);
            }

            wifi_check.setChecked(currentMode.wifi);

            if(currentMode.ringtone.equals(MUTE))
                volume_mute_check.setChecked(true);

            if(currentMode.ringtone.equals(VIBRATE))
                volume_vibrate_check.setChecked(true);

            if(currentMode.ringtone.equals(NORMAL))
                volume_normal_check.setChecked(true);

            if(!currentMode.image.equals("")) {
                imageView.setImageBitmap(Utils.loadImageFromStorage(getActivity(), currentMode.name));
                modeImage = Utils.loadImageFromStorage(getActivity(), currentMode.name);
            }


            //Media
            if(currentMode.mediaVolume.equals(MIN))
                media_mute_check.setChecked(true);
            if(currentMode.mediaVolume.equals(MEDIUM))
                media_normal_check.setChecked(true);
            if(currentMode.mediaVolume.equals(MAX))
                media_max_check.setChecked(true);

            //Brightness

            if(currentMode.brightness.equals(LOW))
                br_low_check.setChecked(true);
            if(currentMode.brightness.equals(MEDIUM))
                br_medium_check.setChecked(true);
            if(currentMode.brightness.equals(HIGH))
                br_high_check.setChecked(true);

            //Bluetooth

            if(currentMode.bluetooth.equals(ON))
                bluetooth_yes_check.setChecked(true);
            if(currentMode.bluetooth.equals(OFF))
                bluetooth_no_check.setChecked(true);

            //Lockscreen

            if(currentMode.lockScreen.equals(ON))
                lock_yes_check.setChecked(true);
            if(currentMode.lockScreen.equals(OFF))
                lock_no_check.setChecked(true);

        }

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name="";
                name = title_text.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String volume = "Normal";
                if(volume_mute_check.isChecked())
                    volume = MUTE;
                if(volume_normal_check.isChecked())
                    volume = NORMAL;
                if(volume_vibrate_check.isChecked())
                    volume = VIBRATE;

                String media = "";
                if(media_mute_check.isChecked())
                    media = MIN;
                if(media_normal_check.isChecked())
                    media = MEDIUM;
                if(media_max_check.isChecked())
                    media = MAX;

                String brightness = "";
                if(br_low_check.isChecked())
                    brightness = LOW;
                if(br_medium_check.isChecked())
                    brightness = MEDIUM;
                if(br_high_check.isChecked())
                    brightness = HIGH;

                String callMessage = "";
                String smsMessage = "";

                if(call_check.isChecked())
                    callMessage = call_message_text.getText().toString();

                if(sms_check.isChecked())
                    smsMessage = sms_message_text.getText().toString();

                String bluetooth = "";
                if(bluetooth_yes_check.isChecked())
                    bluetooth = ON;
                if(bluetooth_no_check.isChecked())
                    bluetooth = OFF;

                String lockscreen = "";
                if(lock_yes_check.isChecked())
                    lockscreen = ON;

                if(lock_no_check.isChecked())
                    lockscreen = OFF;

                String imagePath="";
                if(modeImage!=null)
                     imagePath  = Utils.saveToInternalStorage(getActivity(),modeImage,name);

                ModeObject mode;
                if(isEditing)
                     mode = new ModeObject(currentMode.id,title_text.getText().toString(),
                        callMessage,smsMessage, wifi_check.isChecked(),volume,media,imagePath,brightness,bluetooth,lockscreen);
                else
                    mode = new ModeObject(db.getNextModeId(),title_text.getText().toString(),
                            callMessage,smsMessage, wifi_check.isChecked(),volume,media,imagePath,brightness,bluetooth,lockscreen);

                OnSetModeInterface activity = (OnSetModeInterface) getActivity();
                activity.setMode(mode,isEditing);
                dismiss();

            }
        });

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == Activity.RESULT_OK && imageReturnedIntent!=null){
                    try {

                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                         Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        if(selectedImage.getWidth() >=4096 ||selectedImage.getHeight()>=4096){
                            float width = selectedImage.getWidth();
                            float height  = selectedImage.getHeight();
                            float newWidth = 300;
                            float newHeight = newWidth*height/width;
                            selectedImage = Bitmap.createScaledBitmap(selectedImage, Math.round(newWidth), Math.round(newHeight), false);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(selectedImage , 0, 0, selectedImage .getWidth(), selectedImage .getHeight(), matrix, true);
                            imageView.setImageBitmap(rotatedBitmap);
                            modeImage = rotatedBitmap;

                        }
                        else {
                            imageView.setImageBitmap(selectedImage);
                            modeImage = selectedImage;
                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }


}

