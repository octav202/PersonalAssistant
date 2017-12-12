package com.example.octav.proiect.Alarms;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.R;

import java.util.ArrayList;

/**
 * Created by Octav on 3/15/2016.
 */
public class AlarmListFragment extends Fragment {

    ArrayList<AlarmObject> alarmList = new ArrayList<AlarmObject>();
    AlarmAdapter adapter;

    private FloatingActionButton fab;
    private DataBase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alarms_fragment, container, false);

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        alarmList = db.getAlarms();

        adapter = new AlarmAdapter(getActivity(),R.layout.alarm_list_item, alarmList);
        //adapter.db = db;
        ListView listView = (ListView)view.findViewById(R.id.alarmList);
        listView.setAdapter(adapter);

        fab = (FloatingActionButton) view.findViewById(R.id.alarms_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlarmDialog timeFragment = AddAlarmDialog.newInstance(null, false);
                timeFragment.show(getFragmentManager(), "time_fragment");
            }
        });

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Su"));
        tabLayout.addTab(tabLayout.newTab().setText("Mo"));
        tabLayout.addTab(tabLayout.newTab().setText("Tu"));
        tabLayout.addTab(tabLayout.newTab().setText("We"));
        tabLayout.addTab(tabLayout.newTab().setText("Th"));
        tabLayout.addTab(tabLayout.newTab().setText("Fr"));
        tabLayout.addTab(tabLayout.newTab().setText("Sa"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.filterAlarms(tab.getPosition() + 1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }


    public void addAlarm(AlarmObject alarm,Boolean isEditing){

        if(isEditing) {
            for (int i =0;i<alarmList.size();i++) {
                AlarmObject a = alarmList.get(i);
                if(alarm.id == a.id){
                    alarmList.set(i, alarm);
                    db.updateAlarm(a, alarm);
                    adapter.reloadAdapter(alarmList);
                    adapter.notifyDataSetChanged();

                }
            }
        }
        else{
            alarmList.add(alarm);
            adapter.notifyDataSetChanged();
            adapter.reloadAdapter(alarmList);
            db.insertAlarm(alarm);
        }
    }

    public void deleteAlarmsTable(){
        db.deleteTableAlarms();
        alarmList.clear();
        adapter.notifyDataSetChanged();
    }

    public void enterEditMode(){
        adapter.enterEditMode();
    }

    public void exitEditMode(){
        adapter.exitEditMode();
    }
}

