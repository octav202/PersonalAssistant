package com.example.octav.proiect.Widget;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.octav.proiect.R;

public class WidgetProvider extends AppWidgetProvider {
    public static String SELECTED_MODE= "SELECTED_MODE";

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i=0; i<appWidgetIds.length; i++) {
            Intent svcIntent=new Intent(ctxt, WidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews widget=new RemoteViews(ctxt.getPackageName(), R.layout.widget_layout);
            widget.setRemoteAdapter(appWidgetIds[i], R.id.widget_grid, svcIntent);


            Intent clickIntent=new Intent();
            clickIntent.setAction("widget_update_action");
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent clickPI=PendingIntent.getBroadcast(ctxt, -1234, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widget.setPendingIntentTemplate(R.id.widget_grid, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);

        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

}