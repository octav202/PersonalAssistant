package com.example.octav.proiect.Modes;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RemoteViews;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.R;
import com.example.octav.proiect.Widget.WidgetProvider;

import java.util.ArrayList;
import java.util.List;


public class ModesListFragment extends Fragment {

    private GridView gridView;
    private FloatingActionButton fab;
    private ModesAdapter adapter;
    List<ModeObject> modeList = new ArrayList<>();
    private DataBase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modes_list_fragment, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.activities_fab);
        gridView = (GridView) view.findViewById(R.id.modesGridView);

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        modeList = db.getModes();

        adapter = new ModesAdapter(getActivity(), R.layout.mode_grid_item, modeList);
        gridView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddModeDialog addMode = AddModeDialog.newInstance(null, false);
                addMode.show(getFragmentManager(), "add_activity_fragment");
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.colorPrimary)));

        return view;
    }

    public void addMode(ModeObject mode, boolean isEditing) {

        if (isEditing) {
            for (int i = 0; i < modeList.size(); i++) {
                ModeObject m = modeList.get(i);
                if (mode.id == m.id) {
                    modeList.set(i, mode);
                    db.updateMode(m, mode);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            modeList.add(mode);
            adapter.notifyDataSetChanged();
            db.insertMode(mode);
        }

        updateWidget();
    }

    //Update Widget

    public void updateWidget() {

        RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.widget_layout);
        ComponentName thisWidget = new ComponentName(getActivity(), WidgetProvider.class);
        AppWidgetManager.getInstance(getActivity()).updateAppWidget(thisWidget, remoteViews);
        int[] allWidgetIds = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(thisWidget);
        AppWidgetManager.getInstance(getActivity()).notifyAppWidgetViewDataChanged(allWidgetIds, R.id.widget_grid);
    }

    // Edit Mode

    public void enterEditMode() {
        adapter.enterEditMode();
    }

    public void exitEditMode() {
        adapter.exitEditMode();
    }
}


