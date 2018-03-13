package com.example.octav.proiect.Notifications;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.octav.proiect.Utils.Constants.NOTIFICATION_EXTRA_PARCELABLE;
import static com.example.octav.proiect.Utils.Constants.REMINDER;

public class NotificationAdapter extends ArrayAdapter<NotificationObject> {

    private DataBase db;
    Context context;
    int layoutResourceId;
    List<NotificationObject> data = null;
    private Boolean editMode = false;

    public NotificationAdapter(Context context, int layoutResourceId, List<NotificationObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        db = new DataBase(context.openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NotificationHolder holder = null;
        final Context context = parent.getContext();
        final FragmentManager fm = ((Activity) context).getFragmentManager();

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new NotificationHolder();

            holder.notificationNameTextView = (TextView)row.findViewById(R.id.notificationNameTextView);
            holder.notificationDateTextView = (TextView) row.findViewById(R.id.notificationDateTextView);
            holder.notificationModeTextView = (TextView)row.findViewById(R.id.notificationModeTextView);
            holder.notificationTimeTextView = (TextView) row.findViewById(R.id.notificationTimeTextView);
            holder.notificiationReminderTextView = (TextView)row.findViewById(R.id.notificationReminderTextView);
            holder.deleteButton = (Button)row.findViewById(R.id.deleteNotificationButton);
            holder.editButton = (Button)row.findViewById(R.id.editNotificationButton);
            holder.editWrapper = (LinearLayout)row.findViewById(R.id.notification_edit_wrapper);
            row.setTag(holder);
        }
        else
        {
            holder = (NotificationHolder)row.getTag();
        }

        final NotificationObject notification = data.get(position);

        holder.notificationNameTextView.setText(notification.id +"."+notification.name);

        Calendar cal=Calendar.getInstance();
        cal.set(0,notification.month+1,0);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month = month_date.format(cal.getTime());

        holder.notificationDateTextView.setText(notification.day+" "+month+","+notification.year);


        String hour = String.valueOf(notification.hour);
        String minute = String.valueOf(notification.minute);
        if(hour.length()==1)
            hour = "0"+hour;
        if(minute.length()==1)
            minute = "0"+minute;

        holder.notificationTimeTextView.setText(hour+" : "+minute);
        holder.notificiationReminderTextView.setText(notification.reminder + " min.");

        String modeName = "";
        List<ModeObject> modeList = db.getModes();
        for(ModeObject m : modeList){
            if(m.id == notification.mode){
                modeName = m.name;
                break;
            }
        }
        if(modeName.equals(""))
            holder.notificationModeTextView.setText("n/a");
        else
            holder.notificationModeTextView.setText(modeName);

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
                data.remove(notification);
                db.deleteNotification(notification);
                notifyDataSetChanged();
                cancelNotification(notification);

            }
        });


        holder.editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddNotificationDialog datePicker = AddNotificationDialog.newInstance(notification,true);
                datePicker.show(fm, "date_fragment");
            }
        });


        return row;
    }

    static class NotificationHolder
    {
        TextView notificationNameTextView;

        TextView notificationDateTextView;
        TextView notificationModeTextView;

        TextView notificationTimeTextView;
        TextView notificiationReminderTextView;

        Button deleteButton;
        Button editButton;
        LinearLayout editWrapper;

    }


    public void cancelNotification(NotificationObject n){

        AlarmManager manager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);

        //Cancel Reminder
        if(n.reminder !=0) {
            Intent reminderIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            reminderIntent.addCategory("android.intent.category.DEFAULT");
            reminderIntent.putExtra(NOTIFICATION_EXTRA_PARCELABLE, n);
            reminderIntent.putExtra(REMINDER,true);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, -n.id, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            broadcast.cancel();
            manager.cancel(broadcast);
        }
        //Cancel Notification
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra(NOTIFICATION_EXTRA_PARCELABLE, n);
        notificationIntent.putExtra(REMINDER, false);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, n.id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        broadcast.cancel();
        manager.cancel(broadcast);

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
