package com.example.octav.proiect.Location;

/**
 * Created by Octav on 5/10/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.octav.proiect.Utils.Constants.MAP_TYPE;
import static com.example.octav.proiect.Utils.Constants.TYPE_HYBRID;
import static com.example.octav.proiect.Utils.Constants.TYPE_NORMAL;
import static com.example.octav.proiect.Utils.Constants.TYPE_SATELLITE;

public class MapFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {


    private DataBase db;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,};
    private int curMapTypeIndex = 2;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        String map_type = settings.getString(MAP_TYPE, null);
        if(map_type==null){
            curMapTypeIndex = 1;
        }else {
            if (map_type.equals(TYPE_SATELLITE))
                curMapTypeIndex = 0;
            if (map_type.equals(TYPE_NORMAL))
                curMapTypeIndex = 1;
            if (map_type.equals(TYPE_HYBRID))
                curMapTypeIndex = 2;
        }


        fetchGeofences();
        setHasOptionsMenu(false);

        mGoogleApiClient = new GoogleApiClient.Builder( getActivity() )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();
        mGoogleApiClient.connect();
        initListeners();

    }


    private void initListeners() {
        getMap().setOnMarkerClickListener(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener( this );
        getMap().setOnMapClickListener(this);
    }

    private void removeListeners() {
        if( getMap() != null ) {
            getMap().setOnMarkerClickListener( null );
            getMap().setOnMapLongClickListener(null);
            getMap().setOnInfoWindowClickListener(null);
            getMap().setOnMapClickListener(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeListeners();
    }

    private void initCamera( Location location ) {
        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( location.getLatitude(), location.getLongitude() ) )
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();

        getMap().animateCamera( CameraUpdateFactory.newCameraPosition( position ), null );

        getMap().setMapType( MAP_TYPES[curMapTypeIndex] );
        getMap().setTrafficEnabled( true );
        getMap().setMyLocationEnabled( true );
        getMap().getUiSettings().setZoomControlsEnabled( true );
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mGoogleApiClient !=null)
         mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
        initCamera( mCurrentLocation );


    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Create a default location if the Google API Client fails. Placing location at Googleplex
        mCurrentLocation = new Location( "" );
        mCurrentLocation.setLatitude( 37.422535 );
        mCurrentLocation.setLongitude( -122.084804 );
        initCamera(mCurrentLocation);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText( getActivity(), "Clicked on marker", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(final LatLng latLng) {

        GeofenceObject geo = new GeofenceObject(db.getNextGeofenceId(),getAddressFromLatLng(latLng),latLng.latitude,latLng.longitude,100,0);
        AddMarkerDialog addMarkerDialog = AddMarkerDialog.newInstance(geo);
        addMarkerDialog.setListener(new AddMarkerDialog.MarkerDialogListener() {
            @Override
            public void setMarker(GeofenceObject geo) {
                MarkerOptions options = new MarkerOptions().position( latLng );
                options.title( geo.address );
                options.icon( BitmapDescriptorFactory.defaultMarker( ) );
                getMap().addMarker(options);
                drawRadius(geo);

                db.insertGeofence(geo);
            }
        });
        addMarkerDialog.show(getActivity().getFragmentManager(), "marker_fragment");
    }

    //GEOFENCE FETCH FROM DB

    public void fetchGeofences() {
        ArrayList<GeofenceObject> mGeoObjectList = db.getGeofences();
        if(mGeoObjectList.size()==0){

        }
        else{
            for(GeofenceObject geo:mGeoObjectList){
                addMarker(geo);
                drawRadius(geo);
            }
        }

    }

    public void addMarker(GeofenceObject geo){
        LatLng coord = new LatLng(geo.latitude,geo.longitude);
        MarkerOptions options = new MarkerOptions().position( coord );
        options.title( getAddressFromLatLng(coord) );
        options.icon( BitmapDescriptorFactory.defaultMarker( ) );
        getMap().addMarker(options).showInfoWindow();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //handle play services disconnecting if location is being constantly used
    }



    private String getAddressFromLatLng( LatLng latLng ) {
        Geocoder geocoder = new Geocoder( getActivity() );

        String address = "";
        try {
            address = geocoder.getFromLocation( latLng.latitude, latLng.longitude, 1 ).get( 0 ).getAddressLine( 0 );
        } catch (IOException e ) {
        }

        return address;
    }

    private void drawRadius( GeofenceObject geo ) {
        CircleOptions options = new CircleOptions();
        LatLng location = new LatLng(geo.latitude,geo.longitude);
        options.center( location );
        options.radius(geo.radius);
        options.fillColor( getResources().getColor( R.color.colorAccentTransparent ) );
        options.strokeColor( getResources().getColor( R.color.colorAccentTransparent ) );
        options.strokeWidth( 1 );
        getMap().addCircle(options);
    }

}