package com.example.octav.proiect.Alarms;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmAdapter extends ArrayAdapter<AlarmObject> {

    private DataBase db;
    Context context;
    int layoutResourceId;
    List<AlarmObject> data = null;
    ArrayList<AlarmObject> filteredData = null;
    private int selectedDay;
    private boolean editMode = false;

    public AlarmAdapter(Context context, int layoutResourceId, List<AlarmObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        filteredData = new ArrayList<AlarmObject>();
        filterAlarms(0);
        notifyDataSetChanged();
        db = new DataBase(context.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
    }


    @Override
    public int getCount() {
        return filteredData.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AlarmHolder holder = null;
        final Context context = parent.getContext();
        final FragmentManager fm = ((Activity) context).getFragmentManager();
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AlarmHolder();

            holder.alarmNameTextView = (TextView)row.findViewById(R.id.alarmNameTextView);
            holder.alarmTimeTextView = (TextView) row.findViewById(R.id.alarmTimeTextView);
            holder.alarmRepeatSwitch = (Switch) row.findViewById(R.id.alarmRepeatSwitch);
            holder.alarmIdTextView = (TextView)row.findViewById(R.id.alarmIdTextView);
            holder.modeTextView = (TextView)row.findViewById(R.id.alarm_mode_text_view);

            holder.deleteButton = (Button)row.findViewById(R.id.deleteAlarmButton);
            holder.editAlarmButton = (Button)row.findViewById(R.id.editAlarmButton);

            holder.mondayButton = (Button)row.findViewById(R.id.monday_button);
            holder.tuesdayButton = (Button)row.findViewById(R.id.tuesday_button);
            holder.wednesdayButton = (Button)row.findViewById(R.id.wednesday_button);
            holder.thursdayButton = (Button)row.findViewById(R.id.thursday_button);
            holder.fridayButton = (Button)row.findViewById(R.id.friday_button);
            holder.saturdayButton = (Button)row.findViewById(R.id.saturday_button);
            holder.sundayButton = (Button)row.findViewById(R.id.sunday_button);

            holder.dayButtons[0] = holder.sundayButton;
            holder.dayButtons[1] = holder.mondayButton;
            holder.dayButtons[2] = holder.tuesdayButton;
            holder.dayButtons[3] = holder.wednesdayButton;
            holder.dayButtons[4] = holder.thursdayButton;
            holder.dayButtons[5] = holder.fridayButton;
            holder.dayButtons[6] = holder.saturdayButton;

            holder.editWrapper = (LinearLayout)row.findViewById(R.id.alarm_edit_wrapper);

            row.setTag(holder);
        }
        else
        {
            holder = (AlarmHolder)row.getTag();
        }

        final AlarmObject alarm = filteredData.get(position);

        holder.alarmIdTextView.setText(alarm.id+".");
        holder.alarmNameTextView.setText(alarm.name);

        String hour = String.valueOf(alarm.hour);
        String minute = String.valueOf(alarm.minute);
        if(hour.length()==1)
            hour = "0"+hour;
        if(minute.length()==1)
            minute = "0"+minute;

        holder.alarmRepeatSwitch.setChecked(alarm.repeat);

        String modeName = "";
        List<ModeObject> modeList = db.getModes();
        for(ModeObject m : modeList){
            if(m.id == alarm.mode){
                modeName = m.name;
                break;
            }
        }

        holder.alarmTimeTextView.setText(hour+":"+minute);

        if(modeName.equals(""))
            holder.modeTextView.setText("n/a");
        else
            holder.modeTextView.setText(modeName);

        //Get alarm days and set background
        String[] days = alarm.days.split(",");

        if(days.length > 0)
            for(int i =0;i<days.length;i++){
                if(!days[i].isEmpty()) {
                    int index = Integer.valueOf(days[i]);
                    Button b = holder.dayButtons[index - 1];
                    b.setSelected(true);
                }
            }

        for(int i=0;i<holder.dayButtons.length;i++){

            Button b = holder.dayButtons[i];
            if(!b.isSelected()) {
                b.setBackground(ContextCompat.getDrawable(context, R.drawable.day_button));
                b.setTextColor(Color.GRAY);
            }
            else {
                b.setBackground(ContextCompat.getDrawable(context, R.drawable.day_button_selected));
                b.setTextColor(Color.WHITE);
            }
        }

        //Listener

        if(editMode) {
            holder.editWrapper.setVisibility(View.VISIBLE);
        }
        else{
            holder.editWrapper.setVisibility(View.GONE);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteAlarm(alarm);
                data.remove(alarm);
                filterAlarms(selectedDay);
                notifyDataSetChanged();
                cancelAlarm(alarm);
            }
        });

        holder.editAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddAlarmDialog timeFragment = AddAlarmDialog.newInstance(alarm,true);
               // timeFragment.isEditing = true;
                //timeFragment.currentAlarm = alarm;
                timeFragment.show(fm, "time_fragment");
            }
        });

        holder.alarmRepeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                AlarmObject newAlarm = alarm;
                alarm.repeat = isChecked;
               // data.set(position,newAlarm);

                for(int i=0;i<data.size();i++){
                    if(data.get(i).id == newAlarm.id)
                        data.set(i,newAlarm);
                }
                filterAlarms(selectedDay);
                notifyDataSetChanged();

                db.updateAlarm(alarm, newAlarm);
                //Update pending intents
                updateAlarm(alarm);
            }
        });

        final Button[] arr = holder.dayButtons;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button b = (Button) v;
                int dayForButton = 0;
                for(int i =0;i<arr.length;i++){
                    Button btn = arr[i];
                    if(b==btn)
                        dayForButton = i+1;
                }

                b.setSelected(!b.isSelected());
                if (b.isSelected()){
                    addAlarmForDay(alarm,dayForButton);
                    b.setBackground(ContextCompat.getDrawable(context, R.drawable.day_button_selected));
                    b.setTextColor(Color.GRAY);
                }
                else {
                    cancelAlarmForDay(alarm,dayForButton);
                    b.setBackground(ContextCompat.getDrawable(context, R.drawable.day_button));
                    b.setTextColor(Color.WHITE);
                }

                String daysSelected="";

                for(int i =0;i<arr.length;i++){
                    Button btn = arr[i];
                    if(btn.isSelected())
                        if(daysSelected!="")
                            daysSelected+=","+(i+1);
                        else
                            daysSelected+=(i+1);

                }


                AlarmObject newAlarm = alarm;
                newAlarm.days = daysSelected;
                for(int i=0;i<data.size();i++){
                    if(data.get(i).id == newAlarm.id)
                        data.set(i,newAlarm);
                }
               // data.set(position,newAlarm);
                filterAlarms(selectedDay);
                notifyDataSetChanged();
                db.updateAlarm(alarm,newAlarm);


                Log.w("days", "DAYS : " + daysSelected);
            }
        };

        holder.mondayButton.setOnClickListener(clickListener);
        holder.tuesdayButton.setOnClickListener(clickListener);
        holder.wednesdayButton.setOnClickListener(clickListener);
        holder.thursdayButton.setOnClickListener(clickListener);
        holder.fridayButton.setOnClickListener(clickListener);
        holder.saturdayButton.setOnClickListener(clickListener);
        holder.sundayButton.setOnClickListener(clickListener);

        return row;
    }



    static class AlarmHolder
    {
        TextView alarmNameTextView;
        TextView alarmTimeTextView;
        Switch alarmRepeatSwitch;
        TextView alarmIdTextView;
        TextView modeTextView;

        Button deleteButton;
        Button editAlarmButton;

        Button mondayButton;
        Button tuesdayButton;
        Button wednesdayButton;
        Button thursdayButton;
        Button fridayButton;
        Button saturdayButton;
        Button sundayButton;

        Button[] dayButtons = new Button[7];
        LinearLayout editWrapper;

    }

    public void cancelAlarm(AlarmObject alarm) {

        ArrayList<Integer> days = AlarmObject.daysFromString(alarm.days);
        for (Integer day : days) {

            int id = alarm.id * 1000 + day;
            Log.w("CANCEL", "CANCELED ALARM:" + id+" ("+alarm.hour+":"+alarm.minute+")");
            AlarmManager manager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent("alarm_view");
            i.putExtra("alarmInfo",alarm);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),
                   id, i, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            manager.cancel(pendingIntent);

        }
    }

    public void cancelAlarmForDay(AlarmObject alarm,int day) {

        int id = alarm.id * 1000 + day;
        Log.w("CANCEL", "CANCELED ALARM:" + id+" ("+alarm.hour+":"+alarm.minute+")");
        AlarmManager manager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent("alarm_view");
        i.putExtra("alarmInfo",alarm);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),
                id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        manager.cancel(pendingIntent);
    }

    public void addAlarmForDay(AlarmObject alarm,int day){

        int id = alarm.id * 1000 + day;
        Log.w("ADD", "ADDED ALARM:" + id+" ("+alarm.hour+":"+alarm.minute+")");

        AlarmManager manager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Calendar calSet = Calendar.getInstance();
        calSet.set(Calendar.DAY_OF_WEEK, day);
        calSet.set(Calendar.HOUR_OF_DAY,alarm.hour);
        calSet.set(Calendar.MINUTE, alarm.minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        Intent i = new Intent("alarm_view");
        i.putExtra("alarmInfo", alarm);
        PendingIntent operation = PendingIntent.getActivity(getContext(), id, i, Intent.FLAG_ACTIVITY_NEW_TASK);

        if(calSet.getTimeInMillis() < System.currentTimeMillis()) {
            calSet.add(Calendar.MILLISECOND,1000 * 3600 * 24 * 7);//Add a week
        }

        if(alarm.repeat){
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), 2 * 60 * 1000, operation);
            Log.w("ADD", "ADDED REPEAT ALARM:" + id+" ("+alarm.hour+":"+alarm.minute+")");
        }
        else{
            manager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), operation);
            Log.w("ADD", "ADDED NORMAL ALARM:" + id + " (" + alarm.hour + ":" + alarm.minute + ")");
        }


    }

    public void updateAlarm(AlarmObject alarm){

        Log.v("ARR", AlarmObject.daysFromString(alarm.days).toString());

        ArrayList<Integer> days = AlarmObject.daysFromString(alarm.days);
        for (Integer day: days) {

            int id = alarm.id * 1000 + day;

            //Cancel old alarm
            Log.w("CANCELED", "CANCELED ALARM:" +id+" ("+alarm.hour+":"+alarm.minute+")");

            AlarmManager manager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
            Intent oldIntent = new Intent("alarm_view");
            oldIntent.putExtra("alarmInfo", alarm);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),
                    id, oldIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            manager.cancel(pendingIntent);

            //Add new alarm

            Calendar calSet = Calendar.getInstance();
            calSet.set(Calendar.DAY_OF_WEEK, day);
            calSet.set(Calendar.HOUR_OF_DAY,alarm.hour);
            calSet.set(Calendar.MINUTE, alarm.minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            Intent i = new Intent("alarm_view");
            i.putExtra("alarmInfo", alarm);
            PendingIntent operation = PendingIntent.getActivity(getContext(), id, i, Intent.FLAG_ACTIVITY_NEW_TASK);

            if(calSet.getTimeInMillis() < System.currentTimeMillis()) {
                calSet.add(Calendar.MILLISECOND,1000 * 3600 * 24 * 7);//Add a week
            }

            if(alarm.repeat){
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), 2 * 60 * 1000, operation);
                Log.w("ADD", "ADDED REPEAT ALARM:" + id+" ("+alarm.hour+":"+alarm.minute+")");
            }
            else{
                manager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), operation);
                Log.w("ADD", "ADDED NORMAL ALARM:" + id + " (" + alarm.hour + ":" + alarm.minute + ")");
            }


        }

    }

    public void filterAlarms(int day){
        selectedDay = day;
        filteredData.clear();
        for(AlarmObject alarm:data){
            ArrayList<Integer> days = AlarmObject.daysFromString(alarm.days);
            for(int d:days){
                if(day == d){
                    filteredData.add(alarm);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void reloadAdapter(ArrayList<AlarmObject> alarms){
        data = alarms;
        filterAlarms(selectedDay);
    }

    void enterEditMode(){
        editMode = true;
        notifyDataSetChanged();
    }

    void exitEditMode(){
        editMode = false;
        notifyDataSetChanged();
    }
}
