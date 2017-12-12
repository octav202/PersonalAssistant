package com.example.octav.proiect.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.octav.proiect.Alarms.AlarmObject;
import com.example.octav.proiect.Location.GeofenceObject;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.Notifications.NotificationObject;
import java.util.ArrayList;
import java.util.List;


public final class DataBase{

    private SQLiteDatabase db;

    public  DataBase(SQLiteDatabase database){
        db = database;

        // ALARMS
        db.execSQL("CREATE TABLE IF NOT EXISTS ALARMS(id INT,hour INT,minute INT,days VARCHAR,repeat INT,name VARCHAR,mode INT);");

        // NOTIFICATIONS
        db.execSQL("CREATE TABLE IF NOT EXISTS NOTIFICATIONS(id INT,name VARCHAR,hour INT,minute INT,day INT,month INT,year INT,reminder INT,mode INT,message VARCHAR);");

        // MODES
        db.execSQL("CREATE TABLE IF NOT EXISTS MODES(id INT,name VARCHAR,callMessage VARCHAR,smsMessage VARCHAR,wifi INT,ringtone VARCHAR,media VARCHAR,image VARCHAR,brightness VARCHAR,bluetooth VARCHAR,lockscreen VARCHAR);");

        // GEOFENCES
        db.execSQL("CREATE TABLE IF NOT EXISTS GEOFENCES(id INT,address VARCHAR,latitude DECIMAL(10,6),longitude DECIMAL(10,6),radius INT,mode INT);");
    }

    //Alarms

    public ArrayList<AlarmObject> getAlarms(){

        ArrayList<AlarmObject> alarmList = new ArrayList<AlarmObject>();

        Cursor resultSet = db.rawQuery("Select * from ALARMS", null);
        resultSet.moveToFirst();

        while(resultSet.isAfterLast() == false){


            int id = resultSet.getInt(0);
            int h = resultSet.getInt(1);
            int m = resultSet.getInt(2);
            String d = resultSet.getString(3);
            boolean repeat = resultSet.getInt(4) > 0;
            String name = resultSet.getString(5);
            int mode = resultSet.getInt(6);

            AlarmObject alarm = new AlarmObject(id,h,m,d,repeat,name,mode);
            alarmList.add(alarm);

            resultSet.moveToNext();
        }

        return alarmList;
    }

    public void deleteTableAlarms(){

        db.delete("ALARMS", null,null);
    }

    public  int getNextAlarmId(){

        Cursor resultSet = db.rawQuery("Select * from ALARMS", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while(resultSet.isAfterLast() == false){
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertAlarm(AlarmObject alarm){

        ContentValues values = new ContentValues();
        values.put("id", alarm.id);
        values.put("hour", alarm.hour);
        values.put("minute", alarm.minute);
        values.put("days", alarm.days);
        values.put("repeat", alarm.repeat);
        values.put("name", alarm.name);
        db.insert("ALARMS", null, values);
    }

    public void deleteAlarm(AlarmObject alarm){
        db.delete("ALARMS", "id" + "=" + alarm.id, null);
    }

    public void updateAlarm(AlarmObject oldAlarm,AlarmObject newAlarm){
        ContentValues values = new ContentValues();
        values.put("hour", newAlarm.hour);
        values.put("minute",newAlarm.minute);
        values.put("days",newAlarm.days);
        values.put("name",newAlarm.name);
        values.put("repeat", newAlarm.repeat);
        db.update("ALARMS", values, "id="+oldAlarm.id, null);
    }

    //Notifications

    public List<NotificationObject> getNotifications(){

        List<NotificationObject> notificationList = new ArrayList<NotificationObject>();

        Cursor resultSet = db.rawQuery("Select * from NOTIFICATIONS", null);
        resultSet.moveToFirst();

        while(resultSet.isAfterLast() == false){

            int id = resultSet.getInt(0);
            String name = resultSet.getString(1);
            int hour = resultSet.getInt(2);
            int minute = resultSet.getInt(3);
            int day = resultSet.getInt(4);
            int month = resultSet.getInt(5);
            int year = resultSet.getInt(6);
            int reminder = resultSet.getInt(7);
            int mode = resultSet.getInt(8);
            String message = resultSet.getString(9);

            NotificationObject notification = new NotificationObject(id,name,hour,minute,day,month,year,reminder,mode,message);
            notificationList.add(notification);

            resultSet.moveToNext();
        }

        return notificationList;
    }

    public void deleteTableNotifications(){

        db.delete("NOTIFICATIONS", null,null);
    }

    public  int getNextNotificationId(){

        Cursor resultSet = db.rawQuery("Select * from NOTIFICATIONS", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while(resultSet.isAfterLast() == false){
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertNotification(NotificationObject notification){

        ContentValues values = new ContentValues();
        values.put("id", notification.id);
        values.put("name", notification.name);
        values.put("hour", notification.hour);
        values.put("minute", notification.minute);
        values.put("day", notification.day);
        values.put("month", notification.month);
        values.put("year", notification.year);
        values.put("reminder", notification.reminder);
        values.put("message", notification.message);

        db.insert("NOTIFICATIONS", null, values);

    }

    public void deleteNotification(NotificationObject notificationObject){
        db.delete("NOTIFICATIONS", "id" + "=" + notificationObject.id, null);
    }

    public void updateNotification(NotificationObject oldNotification,NotificationObject newNotification){
        ContentValues values = new ContentValues();
        values.put("id", newNotification.id);
        values.put("name", newNotification.name);
        values.put("hour", newNotification.hour);
        values.put("minute", newNotification.minute);
        values.put("day", newNotification.day);
        values.put("month", newNotification.month);
        values.put("year", newNotification.year);
        values.put("reminder", newNotification.reminder);
        values.put("message", newNotification.message);

        db.update("NOTIFICATIONS", values, "id="+oldNotification.id, null);
    }

    //Modes

    public List<ModeObject> getModes(){

        List<ModeObject> modeList = new ArrayList<ModeObject>();

        Cursor resultSet = db.rawQuery("Select * from MODES", null);
        resultSet.moveToFirst();

        while(resultSet.isAfterLast() == false){

            int id = resultSet.getInt(0);
            String name = resultSet.getString(1);
            String callMessage = resultSet.getString(2);
            String smsMessage = resultSet.getString(3);
            boolean wifi = resultSet.getInt(4) > 0;
            String ringtone = resultSet.getString(5);
            String media = resultSet.getString(6);
            String image = resultSet.getString(7);
            String brightness = resultSet.getString(8);
            String bluetooth = resultSet.getString(9);
            String lockscreen = resultSet.getString(10);

            ModeObject m = new ModeObject(id,name,callMessage,smsMessage,wifi,ringtone,media,image,brightness,bluetooth,lockscreen);
            modeList.add(m);

            resultSet.moveToNext();
        }

        return modeList;
    }

    public void deleteTableModes(){

        db.delete("MODES", null,null);
    }

    public  int getNextModeId(){

        Cursor resultSet = db.rawQuery("Select * from MODES", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while(resultSet.isAfterLast() == false){
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertMode(ModeObject mode){

        ContentValues values = new ContentValues();
        values.put("id", mode.id);
        values.put("name", mode.name);
        values.put("callMessage", mode.callMessage);
        values.put("smsMessage", mode.smsMessage);
        values.put("wifi", mode.wifi);
        values.put("ringtone", mode.ringtone);
        values.put("media", mode.mediaVolume);
        values.put("image", mode.image);
        values.put("brightness", mode.brightness);
        values.put("bluetooth", mode.bluetooth);
        values.put("lockscreen", mode.lockScreen);

        db.insert("MODES", null, values);

    }

    public void deleteMode(ModeObject mode){
        db.delete("MODES", "id" + "=" + mode.id, null);
    }

    public void updateMode(ModeObject oldMode,ModeObject newMode){

        ContentValues values = new ContentValues();
        values.put("id", newMode.id);
        values.put("name", newMode.name);
        values.put("callMessage", newMode.callMessage);
        values.put("smsMessage", newMode.smsMessage);
        values.put("wifi", newMode.wifi);
        values.put("ringtone", newMode.ringtone);
        values.put("media", newMode.mediaVolume);
        values.put("image", newMode.image);
        values.put("brightness", newMode.brightness);
        values.put("bluetooth", newMode.bluetooth);
        values.put("lockscreen", newMode.lockScreen);

        db.update("MODES", values, "id="+oldMode.id, null);

    }

    //GEOFENCES

    public ArrayList<GeofenceObject> getGeofences(){

        ArrayList<GeofenceObject> geoList = new ArrayList<GeofenceObject>();

        Cursor resultSet = db.rawQuery("Select * from GEOFENCES", null);
        resultSet.moveToFirst();

        while(resultSet.isAfterLast() == false){


            int id = resultSet.getInt(0);
            String address = resultSet.getString(1);
            Double lat = resultSet.getDouble(2);
            Double lon = resultSet.getDouble(3);
            int radius = resultSet.getInt(4);
            int mode = resultSet.getInt(5);

            GeofenceObject geo = new GeofenceObject(id,address,lat,lon,radius,mode);
            geoList.add(geo);

            resultSet.moveToNext();
        }

        return geoList;
    }

    public void deleteTableGeofences(){

        db.delete("GEOFENCES", null,null);
    }

    public  int getNextGeofenceId(){

        Cursor resultSet = db.rawQuery("Select * from GEOFENCES", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while(resultSet.isAfterLast() == false){
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertGeofence(GeofenceObject geo){

        ContentValues values = new ContentValues();
        values.put("id", geo.id);
        values.put("address", geo.address);
        values.put("latitude", geo.latitude);
        values.put("longitude", geo.longitude);
        values.put("radius", geo.radius);
        values.put("mode", geo.mode);

        db.insert("GEOFENCES", null, values);
    }

    public void deleteGeofence(GeofenceObject geo){
        db.delete("GEOFENCES", "id" + "=" + geo.id, null);
    }


}
