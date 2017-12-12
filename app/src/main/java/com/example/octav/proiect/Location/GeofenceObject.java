package com.example.octav.proiect.Location;

/**
 * Created by Octav on 5/11/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;


public class GeofenceObject implements Parcelable {


    public int id;
    public String address;
    public double latitude;
    public double longitude;
    public int radius;
    public int mode;

    public GeofenceObject(int i ,String add,double lat,double lon,int r,int m){
        id=i;
        address = add;
        latitude = lat;
        longitude  = lon;
        radius = r;
        mode = m;
    }

    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(radius);
        dest.writeInt(mode);
    }


    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public GeofenceObject createFromParcel(Parcel in) {
            return new GeofenceObject(in);
        }

        public GeofenceObject[] newArray(int size) {
            return new GeofenceObject[size];
        }
    };

    // "De-parcel object
    public GeofenceObject(Parcel in) {

        id = in.readInt();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        radius = in.readInt();
        mode = in.readInt();
    }
}
