package com.example.octav.proiect.Notifications;
import android.os.Parcel;
import android.os.Parcelable;

public class NotificationObject implements Parcelable{


    public int id;
    public String name;
    public int hour;
    public int minute;
    public int day;
    public int month;
    public int year;
    public int reminder;
    public int mode;
    public String message;


    public NotificationObject(int i,String n,int h,int m,int d,int mo,int y,int r,int mod,String msg){

        id=i;
        name = n;
        hour = h;
        minute = m;
        day = d;
        month = mo;
        year = y;
        reminder = r;
        mode = mod;
        message = msg;
    }

    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(reminder);
        dest.writeInt(mode);
        dest.writeString(message);
    }


    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public NotificationObject createFromParcel(Parcel in) {
            return new NotificationObject(in);
        }

        public NotificationObject[] newArray(int size) {
            return new NotificationObject[size];
        }
    };

    // "De-parcel object
    public NotificationObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        reminder = in.readInt();
        mode = in.readInt();
        message = in.readString();
    }
}