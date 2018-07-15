package com.example.octav.proiect.Notifications;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.Utils.DataBase;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.octav.proiect.Modes.ModePickerDialog;
import com.example.octav.proiect.R;

import static com.example.octav.proiect.Utils.Constants.NOTIFICATION_EXTRA_ID;
import static com.example.octav.proiect.Utils.Constants.REMINDER;


public class AddNotificationDialog extends DialogFragment {

    public interface OnSetDateInterface{

        void setNotification(NotificationObject notification,Boolean isEditing);
    }

    private EditText notificationNameEditText;
    private DatePicker datePicker;
    private NumberPicker hourNumberPicker;
    private NumberPicker minuteNumberPicker;
    private NumberPicker reminderNumberPicker;
    private Button setDateButton;

    //Mode Picker
    private CheckBox modeCheckBox;
    private LinearLayout modeWrapper;
    private TextView modeTextView;
    private Button modePickerButton;

    private Switch profileSwitch;
    private Switch messageSwitch;

    //Message

    private CheckBox message_check;
    private TextView message_text;

    private TextView timeTextView;
    private TextView dateTextView;
    private Button timeButton;
    private Button dateButton;
    LinearLayout timeWrapper;

    private DataBase db;
    private Boolean isEditing;
    private NotificationObject currentNotification;
    private Context context;
    public static int mode = 0;

    public static AddNotificationDialog newInstance(NotificationObject n, Boolean isEditing){

        AddNotificationDialog f = new AddNotificationDialog();
        Bundle args = new Bundle();
        args.putParcelable("currentNotification",n);
        args.putBoolean("isNotificationEditing",isEditing);
        f.setArguments(args);
        mode = 0;
        return f;

    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        currentNotification = getArguments().getParcelable("currentNotification");
        isEditing = getArguments().getBoolean("isNotificationEditing");

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_notification_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogFadeAnimation;

        //Mode Picker

        modeCheckBox = (CheckBox)view.findViewById(R.id.setModeCheckBox);
        modeWrapper = (LinearLayout)view.findViewById(R.id.n_mode_picker_wrapper);
        modeTextView = (TextView)view.findViewById(R.id.n_selected_mode_textView);
        modePickerButton = (Button)view.findViewById(R.id.n_mode_picker_button);
        notificationNameEditText = (EditText)view.findViewById(R.id.notificationNameTextView);
        setDateButton = (Button)view.findViewById(R.id.set_date_button);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);
        timeTextView = (TextView)view.findViewById(R.id.n_time_textView);
        dateTextView = (TextView)view.findViewById(R.id.n_date_textView);
        timeButton = (Button) view.findViewById(R.id.n_time_button);
        dateButton = (Button) view.findViewById(R.id.n_date_button);
        timeWrapper = (LinearLayout) view.findViewById(R.id.n_time_wrapper);

        message_check = (CheckBox)view.findViewById(R.id.n_message_checkbox);
        message_text = (TextView)view.findViewById(R.id.n_message_text);

        profileSwitch = (Switch)view.findViewById(R.id.notificationProfileSwitch);
        messageSwitch = (Switch)view.findViewById(R.id.notificationMessageSwitch);

        message_check.setChecked(false);
        message_text.setVisibility(View.GONE);

        //Message Switch
        message_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    message_text.setVisibility(View.VISIBLE);
                else
                    message_text.setVisibility(View.GONE);
            }
        });

        messageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    message_text.setVisibility(View.VISIBLE);
                else
                    message_text.setVisibility(View.GONE);
            }
        });

        //Profile Switch

        modeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    modeWrapper.setVisibility(View.VISIBLE);
                }
                else {
                    modeWrapper.setVisibility(View.GONE);
                    mode = 0;
                }
            }
        });

        profileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    modeWrapper.setVisibility(View.VISIBLE);
                }
                else {
                    modeWrapper.setVisibility(View.GONE);
                    mode = 0;
                }
            }
        });

        modePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModePickerDialog picker = new ModePickerDialog();
                picker.setListener(new ModePickerDialog.ModePickerListener() {
                    @Override
                    public void update(String modeName,int modeId) {
                        modeTextView.setText(modeName);
                        mode = modeId;
                    }
                });
                picker.show(getFragmentManager(), "mode_picker");
            }
        });

        //Date & Time

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeWrapper.getVisibility() == View.VISIBLE) {
                    timeWrapper.setVisibility(View.GONE);
                    updateTime();
                }
                else
                    timeWrapper.setVisibility(View.VISIBLE);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datePicker.getVisibility() == View.VISIBLE) {
                    datePicker.setVisibility(View.GONE);
                    updateDate();
                }
                else
                    datePicker.setVisibility(View.VISIBLE);
            }
        });

        hourNumberPicker = (NumberPicker)view.findViewById(R.id.hourNumberPicker);
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(23);
        hourNumberPicker.setWrapSelectorWheel(false);
        hourNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


            }
        });

        minuteNumberPicker = (NumberPicker)view.findViewById(R.id.minuteNumberPicker);
        minuteNumberPicker.setMinValue(0);
        minuteNumberPicker.setMaxValue(60);
        minuteNumberPicker.setWrapSelectorWheel(false);
        minuteNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        reminderNumberPicker = (NumberPicker)view.findViewById(R.id.reminderNumberPicker);
        reminderNumberPicker.setMinValue(0);
        reminderNumberPicker.setMaxValue(300);
        reminderNumberPicker.setWrapSelectorWheel(false);
        reminderNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        int hours = new Time(System.currentTimeMillis()).getHours();
        int minutes = new Time(System.currentTimeMillis()).getMinutes();
        hourNumberPicker.setValue(hours);
        minuteNumberPicker.setValue(minutes);

        String curHour = String.valueOf(hours);
        if(curHour.length() == 1)
            curHour = "0"+curHour;
        String curMin = String.valueOf(minutes);
        if(curMin.length() == 1)
            curMin = "0"+curMin;
        timeTextView.setText(curHour+":"+curMin);

        //DATE

        updateDate();


        if(isEditing){

            notificationNameEditText.setText(currentNotification.name);
            hourNumberPicker.setValue(currentNotification.hour);
            minuteNumberPicker.setValue(currentNotification.minute);
            reminderNumberPicker.setValue(currentNotification.reminder);
            datePicker.updateDate(currentNotification.year,currentNotification.month,currentNotification.day);

            //Profile Switch

            if(currentNotification.mode!=0){
                profileSwitch.setChecked(true);

                for(ModeObject mode:db.getModes()){
                    if(mode.id == currentNotification.mode){
                        modeTextView.setText(mode.name);
                        break;
                    }

                }
            }

            //Message Switch
            if(currentNotification.message.equals("")){
                messageSwitch.setChecked(false);
            }
            else{
                messageSwitch.setChecked(true);
                message_text.setText(currentNotification.message);
                message_text.setVisibility(View.VISIBLE);
            }

        }

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnSetDateInterface activity = (OnSetDateInterface) getActivity();

                String name = notificationNameEditText.getText().toString();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                int hour = hourNumberPicker.getValue();
                int minute = minuteNumberPicker.getValue();
                int reminder = reminderNumberPicker.getValue();

                Calendar calSet = Calendar.getInstance();
                calSet.set(Calendar.YEAR, year);
                calSet.set(Calendar.MONTH, month);
                calSet.set(Calendar.DAY_OF_MONTH, day);
                calSet.set(Calendar.HOUR_OF_DAY, hour);
                calSet.set(Calendar.MINUTE, minute);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                if(calSet.getTimeInMillis() < System.currentTimeMillis()) {
                    Toast.makeText(getActivity(),"Invalid Date or Time",Toast.LENGTH_LONG).show();
                    return;
                }

                String message;
                if(messageSwitch.isChecked()){
                    message = message_text.getText().toString();
                }
                else{
                    message = "";
                }

                if (name == null || name.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a name",
                            Toast.LENGTH_SHORT).show();

                } else {

                    NotificationObject notification;
                    if(isEditing){
                        notification = new NotificationObject(currentNotification.id,name,hour,minute,day,month,year,reminder,mode,message);
                        activity.setNotification(notification,isEditing);
                        scheduleNotification(notification);
                    }
                    else{
                        notification = new NotificationObject(db.getNextNotificationId(),name,hour,minute,day,month,year,reminder,mode,message);
                        activity.setNotification(notification,isEditing);
                        scheduleNotification(notification);
                    }
                    dismiss();
                }

            }
        });

        return dialog;
    }

    void updateTime(){
        String curHour = String.valueOf(hourNumberPicker.getValue());
        if(curHour.length() == 1)
            curHour = "0"+curHour;
        String curMin = String.valueOf(minuteNumberPicker.getValue());
        if(curMin.length() == 1)
            curMin = "0"+curMin;
        timeTextView.setText(curHour+":"+curMin);
    }

    void updateDate(){
        Calendar cal=Calendar.getInstance();
        cal.set(0,datePicker.getMonth()+1,0);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month = month_date.format(cal.getTime());
        dateTextView.setText(datePicker.getDayOfMonth()+" "+month+","+datePicker.getYear());
    }

    void scheduleNotification(NotificationObject notification){

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Reminder Notification

        if(notification.reminder !=0) {
            Intent reminderIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            reminderIntent.addCategory("android.intent.category.DEFAULT");
            reminderIntent.putExtra(NOTIFICATION_EXTRA_ID, notification.id);
            reminderIntent.putExtra(REMINDER,true);
            PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), -notification.id, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calSet = Calendar.getInstance();
            calSet.set(Calendar.YEAR, notification.year);
            calSet.set(Calendar.MONTH, notification.month);
            calSet.set(Calendar.DAY_OF_MONTH, notification.day);
            calSet.set(Calendar.HOUR_OF_DAY, notification.hour);
            calSet.set(Calendar.MINUTE, notification.minute - notification.reminder);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), broadcast);
        }

        //Notification

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra(NOTIFICATION_EXTRA_ID, notification.id);
        notificationIntent.putExtra(REMINDER,false);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), notification.id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calSet = Calendar.getInstance();
        calSet.set(Calendar.YEAR,notification.year);
        calSet.set(Calendar.MONTH,notification.month);
        calSet.set(Calendar.DAY_OF_MONTH, notification.day);
        calSet.set(Calendar.HOUR_OF_DAY,notification.hour);
        calSet.set(Calendar.MINUTE, notification.minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), broadcast);

    }
}
