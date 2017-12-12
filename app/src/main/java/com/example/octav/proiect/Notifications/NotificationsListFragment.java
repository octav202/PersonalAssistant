package com.example.octav.proiect.Notifications;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.R;
import java.util.ArrayList;
import java.util.List;


public class NotificationsListFragment extends Fragment {

    GridView gridView;;
    FloatingActionButton fab;
    List<NotificationObject> notificationList = new ArrayList<NotificationObject>();
    NotificationAdapter adapter;
    private DataBase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications_fragment,container,false);

        fab = (FloatingActionButton) view.findViewById(R.id.notifications_fab);

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        notificationList = db.getNotifications();

        adapter = new NotificationAdapter(getActivity(),R.layout.notification_grid_item, notificationList);
        gridView = (GridView)view.findViewById(R.id.notificationsGridView);
        gridView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNotificationDialog datePicker = AddNotificationDialog.newInstance(null,false);
                datePicker.show(getFragmentManager(), "date_fragment");
            }
        });


        return view;
    }

    public void addNotification(NotificationObject notification,Boolean isEditing){

        if(isEditing) {
            for (int i =0;i<notificationList.size();i++) {
                NotificationObject n = notificationList.get(i);
                if(n.id == notification.id){
                    notificationList.set(i, notification);
                    adapter.notifyDataSetChanged();
                    db.updateNotification(n, notification);
                }
            }
        }
        else{
            notificationList.add(notification);
            adapter.notifyDataSetChanged();
            db.insertNotification(notification);
        }

    }

    public void deleteNotificationsTable(){
        db.deleteTableNotifications();
        adapter.notifyDataSetChanged();
    }

    public void enterEditMode(){
        adapter.enterEditMode();
    }

    public void exitEditMode(){
        adapter.exitEditMode();
    }
}
