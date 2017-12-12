package com.example.octav.proiect.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Octav on 5/4/2016.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new WidgetViewsFactory(this.getApplicationContext(),
                intent));
    }
}