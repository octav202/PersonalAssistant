package com.example.octav.proiect.Alarms;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class AlarmObject implements Parcelable{


    public int id;
    public int hour;
    public int minute;
    public String days;
    public boolean repeat;
    public String name;
    public int mode;

    public AlarmObject(int i ,int h,int m,String d,boolean r,String n,int mo){

        id=i;
        hour = h;
        minute = m;
        days = d;
        repeat = r;
        name = n;
        mode = mo;
    }

    public static ArrayList daysFromString(String daysString){

        ArrayList arr = new ArrayList();

        //Get alarm days and set background
        String[] days = daysString.split(",");

        if(days.length > 0)
            for(int i =0;i<days.length;i++){
                if(!days[i].isEmpty()) {
                   arr.add(Integer.valueOf(days[i]));
                }
            }
        return arr;
    }

    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeString(days);
        dest.writeByte((byte) (repeat ? 1 : 0));
        dest.writeString(name);
        dest.writeInt(mode);
    }


    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public AlarmObject createFromParcel(Parcel in) {
            return new AlarmObject(in);
        }

        public AlarmObject[] newArray(int size) {
            return new AlarmObject[size];
        }
    };

    // "De-parcel object
    public AlarmObject(Parcel in) {

        id = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        days = in.readString();
        repeat = in.readByte() != 0;
        name = in.readString();
        mode = in.readInt();
    }
}
