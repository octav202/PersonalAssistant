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

import static com.example.octav.proiect.Utils.Constants.ADDRESS_KEY;
import static com.example.octav.proiect.Utils.Constants.ALARMS_TABLE_NAME;
import static com.example.octav.proiect.Utils.Constants.BLUETOOTH_KEY;
import static com.example.octav.proiect.Utils.Constants.BRIGHTNESS_KEY;
import static com.example.octav.proiect.Utils.Constants.CALL_MESSAGE_KEY;
import static com.example.octav.proiect.Utils.Constants.DAYS_KEY;
import static com.example.octav.proiect.Utils.Constants.DAY_KEY;
import static com.example.octav.proiect.Utils.Constants.GEOFENCES_TABLE_NAME;
import static com.example.octav.proiect.Utils.Constants.HOUR_KEY;
import static com.example.octav.proiect.Utils.Constants.ID_KEY;
import static com.example.octav.proiect.Utils.Constants.IMAGE_KEY;
import static com.example.octav.proiect.Utils.Constants.LAT_KEY;
import static com.example.octav.proiect.Utils.Constants.LOCKSCREEN_KEY;
import static com.example.octav.proiect.Utils.Constants.LONG_KEY;
import static com.example.octav.proiect.Utils.Constants.MEDIA_KEY;
import static com.example.octav.proiect.Utils.Constants.MESSAGE_KEY;
import static com.example.octav.proiect.Utils.Constants.MINUTE_KEY;
import static com.example.octav.proiect.Utils.Constants.MODES_TABLE_NAME;
import static com.example.octav.proiect.Utils.Constants.MODE_KEY;
import static com.example.octav.proiect.Utils.Constants.MONTH_KEY;
import static com.example.octav.proiect.Utils.Constants.NAME_KEY;
import static com.example.octav.proiect.Utils.Constants.NOTIFICATIONS_TABLE_NAME;
import static com.example.octav.proiect.Utils.Constants.RADIUS_KEY;
import static com.example.octav.proiect.Utils.Constants.REMINDER_KEY;
import static com.example.octav.proiect.Utils.Constants.REPEAT_KEY;
import static com.example.octav.proiect.Utils.Constants.RINGTONE_KEY;
import static com.example.octav.proiect.Utils.Constants.SMS_MESSAGE_KEY;
import static com.example.octav.proiect.Utils.Constants.WIFI_KEY;
import static com.example.octav.proiect.Utils.Constants.YEAR_KEY;


public final class DataBase {

    private SQLiteDatabase db;

    public DataBase(SQLiteDatabase database) {
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

    public ArrayList<AlarmObject> getAlarms() {

        ArrayList<AlarmObject> alarmList = new ArrayList<AlarmObject>();

        Cursor resultSet = db.rawQuery("Select * from ALARMS", null);
        resultSet.moveToFirst();

        while (resultSet.isAfterLast() == false) {


            int id = resultSet.getInt(0);
            int h = resultSet.getInt(1);
            int m = resultSet.getInt(2);
            String d = resultSet.getString(3);
            boolean repeat = resultSet.getInt(4) > 0;
            String name = resultSet.getString(5);
            int mode = resultSet.getInt(6);

            AlarmObject alarm = new AlarmObject(id, h, m, d, repeat, name, mode);
            alarmList.add(alarm);

            resultSet.moveToNext();
        }

        return alarmList;
    }

    public void deleteTableAlarms() {

        db.delete("ALARMS", null, null);
    }

    public int getNextAlarmId() {

        Cursor resultSet = db.rawQuery("Select * from ALARMS", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while (resultSet.isAfterLast() == false) {
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertAlarm(AlarmObject alarm) {

        ContentValues values = new ContentValues();
        values.put(ID_KEY, alarm.id);
        values.put(HOUR_KEY, alarm.hour);
        values.put(MINUTE_KEY, alarm.minute);
        values.put(DAYS_KEY, alarm.days);
        values.put(REPEAT_KEY, alarm.repeat);
        values.put(NAME_KEY, alarm.name);
        values.put(MODE_KEY, alarm.mode);
        db.insert(ALARMS_TABLE_NAME, null, values);
    }

    public void deleteAlarm(AlarmObject alarm) {
        db.delete(ALARMS_TABLE_NAME, "id" + "=" + alarm.id, null);
    }

    public void updateAlarm(AlarmObject oldAlarm, AlarmObject newAlarm) {
        ContentValues values = new ContentValues();
        values.put(HOUR_KEY, newAlarm.hour);
        values.put(MINUTE_KEY, newAlarm.minute);
        values.put(DAYS_KEY, newAlarm.days);
        values.put(NAME_KEY, newAlarm.name);
        values.put(REPEAT_KEY, newAlarm.repeat);
        values.put(MODE_KEY, newAlarm.mode);
        db.update(ALARMS_TABLE_NAME, values, "id=" + oldAlarm.id, null);
    }

    //Notifications

    public List<NotificationObject> getNotifications() {

        List<NotificationObject> notificationList = new ArrayList<NotificationObject>();

        Cursor resultSet = db.rawQuery("Select * from NOTIFICATIONS", null);
        resultSet.moveToFirst();

        while (resultSet.isAfterLast() == false) {

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

            NotificationObject notification = new NotificationObject(id, name, hour, minute, day, month, year, reminder, mode, message);
            notificationList.add(notification);

            resultSet.moveToNext();
        }

        return notificationList;
    }

    public void deleteTableNotifications() {
        db.delete(NOTIFICATIONS_TABLE_NAME, null, null);
    }

    public int getNextNotificationId() {

        Cursor resultSet = db.rawQuery("Select * from NOTIFICATIONS", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while (resultSet.isAfterLast() == false) {
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertNotification(NotificationObject notification) {

        ContentValues values = new ContentValues();
        values.put(ID_KEY, notification.id);
        values.put(NAME_KEY, notification.name);
        values.put(HOUR_KEY, notification.hour);
        values.put(MINUTE_KEY, notification.minute);
        values.put(DAY_KEY, notification.day);
        values.put(MONTH_KEY, notification.month);
        values.put(YEAR_KEY, notification.year);
        values.put(REMINDER_KEY, notification.reminder);
        values.put(MODE_KEY, notification.mode);
        values.put(MESSAGE_KEY, notification.message);

        db.insert(NOTIFICATIONS_TABLE_NAME, null, values);

    }

    public void deleteNotification(NotificationObject notificationObject) {
        db.delete(NOTIFICATIONS_TABLE_NAME, "id" + "=" + notificationObject.id, null);
    }

    public void updateNotification(NotificationObject oldNotification, NotificationObject newNotification) {
        ContentValues values = new ContentValues();
        values.put(ID_KEY, newNotification.id);
        values.put(NAME_KEY, newNotification.name);
        values.put(HOUR_KEY, newNotification.hour);
        values.put(MINUTE_KEY, newNotification.minute);
        values.put(DAY_KEY, newNotification.day);
        values.put(MONTH_KEY, newNotification.month);
        values.put(YEAR_KEY, newNotification.year);
        values.put(REMINDER_KEY, newNotification.reminder);
        values.put(MESSAGE_KEY, newNotification.message);

        db.update(NOTIFICATIONS_TABLE_NAME, values, "id=" + oldNotification.id, null);
    }

    //Modes

    public List<ModeObject> getModes() {

        List<ModeObject> modeList = new ArrayList<ModeObject>();

        Cursor resultSet = db.rawQuery("Select * from MODES", null);
        resultSet.moveToFirst();

        while (resultSet.isAfterLast() == false) {

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

            ModeObject m = new ModeObject(id, name, callMessage, smsMessage, wifi, ringtone, media, image, brightness, bluetooth, lockscreen);
            modeList.add(m);

            resultSet.moveToNext();
        }

        return modeList;
    }

    public Cursor getAllModes() {
        Cursor cursor = db.rawQuery("Select * from MODES", null);
        return cursor;
    }

    public void deleteTableModes() {
        db.delete(MODES_TABLE_NAME, null, null);
    }

    public int getNextModeId() {

        Cursor resultSet = db.rawQuery("Select * from MODES", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while (resultSet.isAfterLast() == false) {
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertMode(ModeObject mode) {

        ContentValues values = new ContentValues();
        values.put(ID_KEY, mode.id);
        values.put(NAME_KEY, mode.name);
        values.put(CALL_MESSAGE_KEY, mode.callMessage);
        values.put(SMS_MESSAGE_KEY, mode.smsMessage);
        values.put(WIFI_KEY, mode.wifi);
        values.put(RINGTONE_KEY, mode.ringtone);
        values.put(MEDIA_KEY, mode.mediaVolume);
        values.put(IMAGE_KEY, mode.image);
        values.put(BRIGHTNESS_KEY, mode.brightness);
        values.put(BLUETOOTH_KEY, mode.bluetooth);
        values.put(LOCKSCREEN_KEY, mode.lockScreen);

        db.insert(MODES_TABLE_NAME, null, values);

    }

    public void deleteMode(ModeObject mode) {
        db.delete(MODES_TABLE_NAME, "id" + "=" + mode.id, null);
    }

    public void updateMode(ModeObject oldMode, ModeObject newMode) {

        ContentValues values = new ContentValues();
        values.put(ID_KEY, newMode.id);
        values.put(NAME_KEY, newMode.name);
        values.put(CALL_MESSAGE_KEY, newMode.callMessage);
        values.put(SMS_MESSAGE_KEY, newMode.smsMessage);
        values.put(WIFI_KEY, newMode.wifi);
        values.put(RINGTONE_KEY, newMode.ringtone);
        values.put(MEDIA_KEY, newMode.mediaVolume);
        values.put(IMAGE_KEY, newMode.image);
        values.put(BRIGHTNESS_KEY, newMode.brightness);
        values.put(BLUETOOTH_KEY, newMode.bluetooth);
        values.put(LOCKSCREEN_KEY, newMode.lockScreen);

        db.update(MODES_TABLE_NAME, values, "id=" + oldMode.id, null);

    }

    //GEOFENCES

    public ArrayList<GeofenceObject> getGeofences() {

        ArrayList<GeofenceObject> geoList = new ArrayList<GeofenceObject>();

        Cursor resultSet = db.rawQuery("Select * from GEOFENCES", null);
        resultSet.moveToFirst();

        while (resultSet.isAfterLast() == false) {


            int id = resultSet.getInt(0);
            String address = resultSet.getString(1);
            Double lat = resultSet.getDouble(2);
            Double lon = resultSet.getDouble(3);
            int radius = resultSet.getInt(4);
            int mode = resultSet.getInt(5);

            GeofenceObject geo = new GeofenceObject(id, address, lat, lon, radius, mode);
            geoList.add(geo);

            resultSet.moveToNext();
        }

        return geoList;
    }

    public void deleteTableGeofences() {
        db.delete(GEOFENCES_TABLE_NAME, null, null);
    }

    public int getNextGeofenceId() {

        Cursor resultSet = db.rawQuery("Select * from GEOFENCES", null);
        resultSet.moveToFirst();
        int lastId = 0;

        while (resultSet.isAfterLast() == false) {
            lastId = resultSet.getInt(0);
            resultSet.moveToNext();
        }
        return lastId + 1;
    }

    public void insertGeofence(GeofenceObject geo) {

        ContentValues values = new ContentValues();
        values.put(ID_KEY, geo.id);
        values.put(ADDRESS_KEY, geo.address);
        values.put(LAT_KEY, geo.latitude);
        values.put(LONG_KEY, geo.longitude);
        values.put(RADIUS_KEY, geo.radius);
        values.put(MODE_KEY, geo.mode);

        db.insert(GEOFENCES_TABLE_NAME, null, values);
    }

    public void deleteGeofence(GeofenceObject geo) {
        db.delete(GEOFENCES_TABLE_NAME, "id" + "=" + geo.id, null);
    }


}
