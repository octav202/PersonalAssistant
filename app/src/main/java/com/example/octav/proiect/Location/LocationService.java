package com.example.octav.proiect.Location;

/**
 * Created by Octav on 5/11/2016.
 */

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.example.octav.proiect.Utils.DataBase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;


import java.util.ArrayList;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "PERSONAL_ASSISTANT";
    private LocationManager mLocationManager = null;
    private static int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0;

    //GEOFENCES

    protected ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private DataBase db;
    private GoogleApiClient mGoogleApiClient;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            //Log.w(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {       fetchGeofences();
            //Log.w(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            if (!mGoogleApiClient.isConnected()) {
                Toast.makeText(getApplicationContext(), "API CLIENT NOT CONNECTED" , Toast.LENGTH_SHORT).show();
                return;
            }

            if (mGeofenceList.size()==0) {
                return;
            }

            try {
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient, getGeofencingRequest(), getGeofencePendingIntent()).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
            } catch (SecurityException securityException) {

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            //Log.w(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            //Log.w(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Log.w(TAG, "onStatusChanged: " + provider);
        }
    }




    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        db = new DataBase(getApplicationContext().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        mGeofenceList = new ArrayList<Geofence>();
        mGeofencePendingIntent = null;
        fetchGeofences();
        buildGoogleApiClient();
        initializeLocationManager();
        mGoogleApiClient.connect();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String locationInterval = settings.getString("location_interval", null);
        if(locationInterval==null){
            LOCATION_INTERVAL = 1000 * 60;
        }
        else {
            if (locationInterval.equals("1 min"))
                LOCATION_INTERVAL = 1000 * 60;
            if (locationInterval.equals("3 min"))
                LOCATION_INTERVAL = 1000 * 60 * 3;
            if (locationInterval.equals("5 min"))
                LOCATION_INTERVAL = 1000 * 60 * 5;

        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }


    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
        mGoogleApiClient.disconnect();
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    //GEOFENCES

    //GEOFENCE PENDING INTENT


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }


    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getApplicationContext(), GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //GEOFENCE REQUEST
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);

        return builder.build();
    }

    //GEOFENCE FETCH FROM DB

    public void fetchGeofences() {
        mGeofenceList.clear();
        ArrayList<GeofenceObject> mGeoObjectList = db.getGeofences();
        if(mGeoObjectList.size()==0){
        }
        else{
            for(GeofenceObject geo:mGeoObjectList){

                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(geo.id+geo.address)
                        .setCircularRegion(
                                geo.latitude,
                                geo.longitude,
                                geo.radius
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());

            }
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i(TAG, "Connection suspended");

        // onConnected() will be called again automatically when the service reconnects
    }

}
