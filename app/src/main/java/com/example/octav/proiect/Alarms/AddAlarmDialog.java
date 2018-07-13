package com.example.octav.proiect.Alarms;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModePickerDialog;
import com.example.octav.proiect.R;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.octav.proiect.Utils.Constants.ALARM_ACTION;
import static com.example.octav.proiect.Utils.Constants.ALARM_EXTRA_PARCELABLE;
import static com.example.octav.proiect.Utils.Constants.CURRENT_ALARM;
import static com.example.octav.proiect.Utils.Constants.IS_ALARM_EDIT;

public class AddAlarmDialog extends DialogFragment {

    public interface OnSetTimeInterface {

        void setTime(AlarmObject alarm, Boolean isEditing);
    }

    private DataBase db;
    private Boolean isEditing;
    private AlarmObject currentAlarm;
    private Context context;

    private Button setTime;
    private TimePicker tPicker;
    private Switch repeatSwitch;
    private EditText alarmNameEditText;

    //Mode picker
    private CheckBox modeCheckBox;
    private LinearLayout modeWrapper;
    private TextView modeTextView;
    private Button modePickerButton;
    private Switch profileSwitch;

    //Days

    private Button mondayButton;
    private Button tuesdayButton;
    private Button wednesdayButton;
    private Button thursdayButton;
    private Button fridayButton;
    private Button saturdayButton;
    private Button sundayButton;
    private Button[] dayButtons;

    public static int mode = 0;

    //Next ID
    int nextId;


    public static AddAlarmDialog newInstance(AlarmObject a, boolean isEditing) {
        AddAlarmDialog f = new AddAlarmDialog();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_ALARM, a);
        args.putBoolean(IS_ALARM_EDIT, isEditing);
        f.setArguments(args);
        mode = 0;
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));

        currentAlarm = getArguments().getParcelable(CURRENT_ALARM);
        isEditing = getArguments().getBoolean(IS_ALARM_EDIT);

        context = getActivity().getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_alarm_dialog, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnimation;

        setTime = (Button) view.findViewById(R.id.set_time_button);
        tPicker = (TimePicker) view.findViewById(R.id.timePicker);
        tPicker.setIs24HourView(true);

        repeatSwitch = (Switch) view.findViewById(R.id.repeatSwitch);
        alarmNameEditText = (EditText) view.findViewById(R.id.nameEditText);

        //Days

        mondayButton = (Button) view.findViewById(R.id.monday_button);
        tuesdayButton = (Button) view.findViewById(R.id.tuesday_button);
        wednesdayButton = (Button) view.findViewById(R.id.wednesday_button);
        thursdayButton = (Button) view.findViewById(R.id.thursday_button);
        fridayButton = (Button) view.findViewById(R.id.friday_button);
        saturdayButton = (Button) view.findViewById(R.id.saturday_button);
        sundayButton = (Button) view.findViewById(R.id.sunday_button);

        dayButtons = new Button[7];
        dayButtons[0] = sundayButton;
        dayButtons[1] = mondayButton;
        dayButtons[2] = tuesdayButton;
        dayButtons[3] = wednesdayButton;
        dayButtons[4] = thursdayButton;
        dayButtons[5] = fridayButton;
        dayButtons[6] = saturdayButton;

        sundayButton.setTextColor(Color.GRAY);
        mondayButton.setTextColor(Color.GRAY);
        tuesdayButton.setTextColor(Color.GRAY);
        wednesdayButton.setTextColor(Color.GRAY);
        thursdayButton.setTextColor(Color.GRAY);
        fridayButton.setTextColor(Color.GRAY);
        saturdayButton.setTextColor(Color.GRAY);

        //Mode Picker

        modeCheckBox = (CheckBox) view.findViewById(R.id.setModeCheckBox);
        modeWrapper = (LinearLayout) view.findViewById(R.id.alarm_mode_picker_wrapper);
        modeTextView = (TextView) view.findViewById(R.id.selected_mode_textView);
        modePickerButton = (Button) view.findViewById(R.id.mode_picker_button);

        profileSwitch = (Switch) view.findViewById(R.id.alarmProfileSwitch);

        modePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModePickerDialog picker = new ModePickerDialog();
                picker.setListener(new ModePickerDialog.ModePickerListener() {
                    @Override
                    public void update(String modeName, int modeId) {
                        modeTextView.setText(modeName);
                        mode = modeId;
                    }
                });
                picker.show(getFragmentManager(), "mode_picker");
            }
        });

        modeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    modeWrapper.setVisibility(View.VISIBLE);
                } else {
                    modeWrapper.setVisibility(View.GONE);
                    mode = 0;
                }
            }
        });

        profileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    modeWrapper.setVisibility(View.VISIBLE);
                } else {
                    modeWrapper.setVisibility(View.GONE);
                    mode = 0;
                }
            }
        });

        //Get Next Id
        nextId = db.getNextAlarmId();

        //If isEditing

        if (currentAlarm != null) {

            alarmNameEditText.setText(currentAlarm.name);
            tPicker.setCurrentHour(currentAlarm.hour);
            tPicker.setCurrentMinute(currentAlarm.minute);
            repeatSwitch.setChecked(currentAlarm.repeat);

            profileSwitch.setChecked(currentAlarm.repeat);

            //Remove days buttons and switch for edit mode

            for (int i = 0; i < dayButtons.length; i++) {
                Button b = dayButtons[i];
                ((ViewManager) b.getParent()).removeView(b);
            }
            TextView repeatLabel = (TextView) view.findViewById(R.id.repeatLabel);
            ((ViewManager) repeatLabel.getParent()).removeView(repeatLabel);
            ((ViewManager) repeatSwitch.getParent()).removeView(repeatSwitch);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button b = (Button) v;
                b.setSelected(!b.isSelected());
                if (b.isSelected()) {
                    b.setBackground(ContextCompat.getDrawable(context, R.drawable.day_button_selected));
                    b.setTextColor(Color.WHITE);
                } else {
                    b.setBackground(ContextCompat.getDrawable(context, R.drawable.day_button));
                    b.setTextColor(Color.GRAY);
                }

                switch (b.getId()) {

                    case R.id.monday_button:
                        System.out.println("Monday");
                        break;
                    case R.id.tuesday_button:
                        System.out.println("Tuesday");
                        break;
                    case R.id.wednesday_button:
                        System.out.println("Wednesday");
                        break;
                    case R.id.thursday_button:
                        System.out.println("Thursday");
                        break;
                    case R.id.friday_button:
                        System.out.println("Friday");
                        break;
                    case R.id.saturday_button:
                        System.out.println("Saturday");
                        break;
                    case R.id.sunday_button:
                        System.out.println("Sunday");
                        break;

                    default:
                        break;
                }

            }

        };

        mondayButton.setOnClickListener(clickListener);
        tuesdayButton.setOnClickListener(clickListener);
        wednesdayButton.setOnClickListener(clickListener);
        thursdayButton.setOnClickListener(clickListener);
        fridayButton.setOnClickListener(clickListener);
        saturdayButton.setOnClickListener(clickListener);
        sundayButton.setOnClickListener(clickListener);


        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = tPicker.getCurrentHour();
                int minute = tPicker.getCurrentMinute();
                boolean repeat = repeatSwitch.isChecked();
                String name = alarmNameEditText.getText().toString();

                String days = "";
                for (int i = 0; i < 7; i++) {

                    Button b = dayButtons[i];
                    if (b.isSelected())
                        if (!days.equals(""))
                            days += "," + (i + 1);
                        else
                            days += (i + 1);
                }

                Boolean valid = true;

                if (days.equals("") && !isEditing) {
                    Toast.makeText(getActivity(), "Please select at least one day.",
                            Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if (name.equals("") || name.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a name",
                            Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if (valid) {
                    AlarmObject alarm;
                    if (!isEditing)
                        alarm = new AlarmObject(nextId, hour, minute, days, repeat, name, mode);
                    else
                        alarm = new AlarmObject(currentAlarm.id, hour, minute, currentAlarm.days, currentAlarm.repeat, name, mode);
                    setAlarm(alarm, isEditing);
                    dismiss();

                }

            }
        });

        return dialog;
    }

    public void setAlarm(AlarmObject alarm, Boolean isEditing) {

        Log.v("ARR", AlarmObject.daysFromString(alarm.days).toString());

        ArrayList<Integer> days = AlarmObject.daysFromString(alarm.days);
        for (Integer day : days) {

            int id = alarm.id * 1000 + day;

            if (isEditing) {

                Log.w("CANCELED", "CANCELED ALARM:" + id + " (" + alarm.hour + ":" + alarm.minute + ")");

                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(ALARM_ACTION);
                i.putExtra(ALARM_EXTRA_PARCELABLE, alarm);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, id, i, PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent.cancel();
                manager.cancel(pendingIntent);

            }

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calSet = Calendar.getInstance();
            calSet.set(Calendar.DAY_OF_WEEK, day);
            calSet.set(Calendar.HOUR_OF_DAY, alarm.hour);
            calSet.set(Calendar.MINUTE, alarm.minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            Intent i = new Intent(ALARM_ACTION);
            i.putExtra(ALARM_EXTRA_PARCELABLE, alarm);
            PendingIntent operation = PendingIntent.getActivity(context, id, i, Intent.FLAG_ACTIVITY_NEW_TASK);

            if (calSet.getTimeInMillis() < System.currentTimeMillis()) {
                calSet.add(Calendar.MILLISECOND, 1000 * 3600 * 24 * 7);//Add a week
            }

            int week = 1000 * 3600 * 24 * 7;
            if (alarm.repeat) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), week, operation);
                Log.w("ADD", "ADDED REPEAT ALARM:" + id + " (" + alarm.hour + ":" + alarm.minute + ")");
            } else {
                manager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), operation);
                Log.w("ADD", "ADDED NORMAL ALARM:" + id + " (" + alarm.hour + ":" + alarm.minute + ")");
            }


        }

        OnSetTimeInterface activity = (OnSetTimeInterface) getActivity();
        activity.setTime(alarm, isEditing);
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
