package com.example.octav.proiect.Modes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Octav on 4/15/2016.
 */
public class ModeObject implements Parcelable{

    public int id;
    public String name;
    public String callMessage;
    public String smsMessage;
    public boolean wifi;
    public String ringtone;
    public String mediaVolume;
    public String brightness;
    public String bluetooth;
    public String lockScreen;
    public String image;

    public ModeObject(int id,String name,String callMessage,String smsMessage,boolean wifi,
                      String ringtone,String media,String image,String brightness,String bluetooth,String lockScreen){
        this.id = id;
        this.name = name;
        this.callMessage = callMessage;
        this.smsMessage = smsMessage;
        this.wifi = wifi;
        this.ringtone = ringtone;
        this.mediaVolume = media;
        this.image = image;
        this.brightness = brightness;
        this.bluetooth = bluetooth;
        this.lockScreen = lockScreen;
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
        dest.writeString(callMessage);
        dest.writeString(smsMessage);
        dest.writeByte((byte) (wifi ? 1 : 0));
        dest.writeString(ringtone);
        dest.writeString(mediaVolume);
        dest.writeString(image);
        dest.writeString(brightness);
        dest.writeString(bluetooth);
        dest.writeString(lockScreen);
    }


    // Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ModeObject createFromParcel(Parcel in) {
            return new ModeObject(in);
        }

        public ModeObject[] newArray(int size) {
            return new ModeObject[size];
        }
    };

    // "De-parcel object
    public ModeObject(Parcel in) {

        id = in.readInt();
        name = in.readString();
        callMessage = in.readString();
        smsMessage = in.readString();
        wifi = in.readByte() != 0;
        ringtone = in.readString();
        mediaVolume = in.readString();
        image = in.readString();
        brightness = in.readString();
        bluetooth = in.readString();
        lockScreen = in.readString();
    }

    @Override
    public String toString() {
        return "ModeObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", callMessage='" + callMessage + '\'' +
                ", smsMessage='" + smsMessage + '\'' +
                ", wifi=" + wifi +
                ", ringtone='" + ringtone + '\'' +
                ", mediaVolume='" + mediaVolume + '\'' +
                ", brightness='" + brightness + '\'' +
                ", bluetooth='" + bluetooth + '\'' +
                ", lockScreen='" + lockScreen + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
