package com.example.octav.proiect.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.octav.proiect.Modes.AddModeDialog;
import com.example.octav.proiect.Modes.ModeObject;
import com.example.octav.proiect.Modes.ModesListFragment;
import com.example.octav.proiect.Alarms.AlarmListFragment;
import com.example.octav.proiect.Alarms.AlarmObject;
import com.example.octav.proiect.Alarms.AddAlarmDialog;
import com.example.octav.proiect.Notifications.AddNotificationDialog;
import com.example.octav.proiect.Notifications.NotificationObject;
import com.example.octav.proiect.Notifications.NotificationsListFragment;
import com.example.octav.proiect.Location.MapFragment;
import com.example.octav.proiect.Location.LocationService;
import com.example.octav.proiect.R;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AddAlarmDialog.OnSetTimeInterface,
        AddNotificationDialog.OnSetDateInterface,AddModeDialog.OnSetModeInterface
{
    private Toolbar toolbar;
    private NavigationView navigationView;
    public static boolean editMode = false;
    private static String activeMode;
    MenuItem editItem;
    Menu mMenu;

    private String FRAGMENT_MODES = "modes_fragment";
    private String FRAGMENT_ALARMS = "alarms_fragment";
    private String FRAGMENT_NOTIFICATIONS= "notifications_fragment";
    private String FRAGMENT_LOCATION= "location_fragment";
    private String FRAGMENT_SETTINGS = "settings_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
            }
            public void onDrawerClosed(View view) {
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Select modes
        navigationView.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getFragmentManager();
        ModesListFragment modesListFragment = new ModesListFragment();
        fragmentManager.beginTransaction().replace(R.id.flContent,modesListFragment,"modes_fragment").commit();
        activeMode = FRAGMENT_MODES;
        toolbar.setTitle("Modes");

        //Phone Call
        PhoneCallListener phoneListener = new PhoneCallListener(MainActivity.this);
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);


        // Location Service
/*
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled() && isLocationEnabled()){
            startService(new Intent(this, LocationService.class));
        }
        else{
          stopService(new Intent(this, LocationService.class));
            //Toast.makeText(this,"Please enable internet connection & location to use this feature",Toast.LENGTH_LONG).show();
        }
*/

       // initTesting();
    }

    //Select Tabs

    private void initTesting(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectModesTab();
            }
        }, 1500);
    }


    private void selectModesTab(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigationView.getMenu().getItem(0).setChecked(true);
                FragmentManager fragmentManager = getFragmentManager();
                ModesListFragment modesListFragment = new ModesListFragment();
                fragmentManager.beginTransaction().replace(R.id.flContent,modesListFragment,"modes_fragment").commit();
                activeMode = FRAGMENT_MODES;
                toolbar.setTitle("Modes");
                selectAlarmsTab();
            }
        }, 1000);
    }

    private void selectAlarmsTab(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigationView.getMenu().getItem(1).setChecked(true);
                FragmentManager fragmentManager = getFragmentManager();
                AlarmListFragment alarmsListFragment = new AlarmListFragment();
                fragmentManager.beginTransaction().replace(R.id.flContent,alarmsListFragment,"alarms_fragment").commit();
                activeMode = FRAGMENT_ALARMS;
                toolbar.setTitle("Alarms");

                selectNotificationsTab();
            }
        }, 1000);

    }


    private void selectNotificationsTab(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigationView.getMenu().getItem(3).setChecked(true);
                FragmentManager fragmentManager = getFragmentManager();
                NotificationsListFragment notListFragment = new NotificationsListFragment();
                fragmentManager.beginTransaction().replace(R.id.flContent,notListFragment,"notifications_fragment").commit();
                activeMode = FRAGMENT_NOTIFICATIONS;
                toolbar.setTitle("Notifications");

                selectSettingsTab();
            }
        }, 1000);

    }

    private void selectSettingsTab(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigationView.getMenu().getItem(4).setChecked(true);
                FragmentManager fragmentManager = getFragmentManager();
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.flContent,settingsFragment,"settings_fragment").commit();
                activeMode = FRAGMENT_SETTINGS;
                toolbar.setTitle("Settings");

                selectModesTab();
            }
        }, 1000);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.action_edit_mode) {

            editItem = item;
            editMode =!editMode;
            FragmentManager fm = getFragmentManager();

            if(activeMode.equals(FRAGMENT_MODES)){
                ModesListFragment frag= (ModesListFragment) fm.findFragmentByTag(FRAGMENT_MODES);
                if(editMode)
                    frag.enterEditMode();
                else
                    frag.exitEditMode();
            }
            if(activeMode.equals(FRAGMENT_ALARMS)){
                AlarmListFragment frag = (AlarmListFragment) fm.findFragmentByTag(FRAGMENT_ALARMS);
                if(editMode)
                    frag.enterEditMode();
                else
                    frag.exitEditMode();
            }
            if(activeMode.equals(FRAGMENT_NOTIFICATIONS)){
                NotificationsListFragment frag = (NotificationsListFragment) fm.findFragmentByTag(FRAGMENT_NOTIFICATIONS);
                if(editMode)
                    frag.enterEditMode();
                else
                    frag.exitEditMode();
            }

            if(editMode)
                item.setIcon(R.drawable.ic_edit);
            else
                item.setIcon(R.drawable.ic_edit_white);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();


        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
        if(f!=null) {
            fTransaction.remove(f);
            fTransaction.commit();
        }

       if (id == R.id.nav_alarms) {

           AlarmListFragment listFragment = new AlarmListFragment();
           fragmentManager.beginTransaction().replace(R.id.flContent, listFragment,FRAGMENT_ALARMS).commit();
           toolbar.setTitle("Alarms");
           activeMode = FRAGMENT_ALARMS;

        } else if (id == R.id.nav_notifications) {
           NotificationsListFragment notificationsListFragment = new NotificationsListFragment();
           fragmentManager.beginTransaction().replace(R.id.flContent, notificationsListFragment,FRAGMENT_NOTIFICATIONS).commit();
           toolbar.setTitle("Notifications");
           activeMode = FRAGMENT_NOTIFICATIONS;

        } else if (id == R.id.nav_settings) {
           SettingsFragment settingsFragment = new SettingsFragment();
           fragmentManager.beginTransaction().replace(R.id.flContent, settingsFragment,FRAGMENT_SETTINGS).commit();
           toolbar.setTitle("Settings");
           activeMode = FRAGMENT_SETTINGS;
        }
         else if(id == R.id.nav_modes){
           ModesListFragment modesListFragment = new ModesListFragment();
           fragmentManager.beginTransaction().replace(R.id.flContent,modesListFragment,FRAGMENT_MODES).commit();
           toolbar.setTitle("Modes");
           activeMode = FRAGMENT_MODES;
       }
        else if(id == R.id.nav_location){

           toolbar.setTitle("Location");
           activeMode = FRAGMENT_LOCATION;

           WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
           if(!wifiManager.isWifiEnabled() && !isMobileDataEnabled()){
               Toast.makeText(this,"Please enable internet connection to use this feature",Toast.LENGTH_LONG).show();
               return false;
           }

           if(!isLocationEnabled()){
               Toast.makeText(this,"Please enable GPS to use this feature",Toast.LENGTH_LONG).show();
               return false;
           }


           final Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                   MapFragment mapFragment = new MapFragment();
                   fragmentTransaction.replace(R.id.flContent, mapFragment, FRAGMENT_LOCATION);
                   fragmentTransaction.commit();
               }
           },300);




       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        resetEditMode();
        return true;
    }

    void resetEditMode(){
        editMode = false;
        if(editItem!=null)
            editItem.setIcon(R.drawable.ic_edit_white);
    }

    @Override
    public void setTime(AlarmObject alarm, Boolean isEditing) {
        FragmentManager fm = getFragmentManager();
        AlarmListFragment frag = (AlarmListFragment) fm.findFragmentByTag(FRAGMENT_ALARMS);
        frag.addAlarm(alarm, isEditing);
    }

    @Override
    public void setNotification(NotificationObject notification,Boolean isEditing) {
        FragmentManager fm = getFragmentManager();
        NotificationsListFragment frag = (NotificationsListFragment) fm.findFragmentByTag(FRAGMENT_NOTIFICATIONS);
        frag.addNotification(notification, isEditing);
    }

    @Override
    public void setMode(ModeObject mode, Boolean isEditing) {
        FragmentManager fm = getFragmentManager();
        ModesListFragment frag= (ModesListFragment) fm.findFragmentByTag(FRAGMENT_MODES);
        frag.addMode(mode, isEditing);
    }

    private  boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public Boolean isMobileDataEnabled(){
        Object connectivityService = getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
