package com.example.octav.proiect.Utils;

public class Constants {

    public static final String TAG = "PA_";

    // Mode
    public static final String SHARED_NAME = "ACTIVE_MODE";
    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String CALL_MESSAGE_KEY = "callMessage";
    public static final String SMS_MESSAGE_KEY = "smsMessage";
    public static final String WIFI_KEY = "wifi";
    public static final String RINGTONE_KEY = "ringtone";
    public static final String IMAGE_KEY = "image";
    public static final String ACTIVE_KEY = "modeActive";
    public static final String MEDIA_KEY = "media";
    public static final String BRIGHTNESS_KEY = "brightness";
    public static final String BLUETOOTH_KEY = "bluetooth";
    public static final String LOCKSCREEN_KEY = "lockscreen";
    public static final String MODES_TABLE_NAME = "MODES";


    // Alarm
    public static final String HOUR_KEY = "hour";
    public static final String MINUTE_KEY = "minute";
    public static final String DAYS_KEY = "days";
    public static final String REPEAT_KEY = "repeat";
    public static final String ALARMS_TABLE_NAME = "ALARMS";

    public static final String CURRENT_ALARM = "currentAlarm";
    public static final String IS_ALARM_EDIT = "isAlarmEditing";

    // Notification

    public static final String DAY_KEY = "day";
    public static final String MONTH_KEY = "month";
    public static final String YEAR_KEY = "year";
    public static final String REMINDER_KEY = "reminder";
    public static final String MESSAGE_KEY = "message";
    public static final String NOTIFICATIONS_TABLE_NAME = "NOTIFICATIONS";

    // Geofences

    public static final String ADDRESS_KEY = "address";
    public static final String LAT_KEY = "latitude";
    public static final String LONG_KEY = "longitude";
    public static final String RADIUS_KEY = "radius";
    public static final String MODE_KEY = "mode";
    public static final String GEOFENCES_TABLE_NAME = "GEOFENCES";

    // Intents

    public static final String EXTRA_BRIGHTNESS = "brightness";
    public static final String KEY_GUARD_LOCK = "MyKeyguardLock";
    public static final String ALARM_EXTRA_PARCELABLE = "alarmInfo";
    public static final String NOTIFICATION_EXTRA_PARCELABLE = "notificationInfo";
    public static final String MODE_EXTRA_PARCELABLE = "modeInfo";
    public static final String ALARM_ACTION = "alarm_view";
    public static final String WIDGET_MODE = "widget_mode";
    public static final String REMINDER = "reminder";

    // Ringtone values
    public static final String MUTE = "Mute";
    public static final String NORMAL = "Normal";
    public static final String VIBRATE = "Vibrate";

    // Media Volumes
    public static final String MIN = "Min";
    public static final String MEDIUM = "Medium";
    public static final String MAX = "Max";

    // Brightness Values
    public static final String LOW = "Low";
    public static final String HIGH = "High";

    // Bluetooth, Lockscreen
    public static final String ON = "On";
    public static final String OFF = "Off";

    // Settings Alarm
    public static final String ALARM_SOUND = "AlarmSound";
    public static final String ALARM_VOLUME = "AlarmVolume";
    public static final String ALARM_TYPE_RINGTONE = "Ringtone";
    public static final String ALARM_TYPE_ALARM = "Alarm";
    public static final String ALARM_TYPE_NOTIFICATION = "Notification";

    // Settings Notification
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String NOT_TYPE_SOUND = "Sound";
    public static final String NOT_TYPE_VIBRATE = "Vibrate";
    public static final String NOT_TYPE_SILENT = "Silent";

    // Settings Location
    public static final String LOCATION_INTERVAL = "location_interval";
    public static final String INTERVAL_1_MIN = "1 min";
    public static final String INTERVAL_3_MIN = "3 min";
    public static final String INTERVAL_5_MIN = "5 min";

    public static final String MAP_TYPE = "map_type";
    public static final String TYPE_NORMAL = "Normal";
    public static final String TYPE_HYBRID = "Hybrid";
    public static final String TYPE_SATELLITE = "Satellite";
}
