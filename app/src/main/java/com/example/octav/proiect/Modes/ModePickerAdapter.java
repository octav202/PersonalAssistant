package com.example.octav.proiect.Modes;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.octav.proiect.R;
import com.example.octav.proiect.Utils.Utils;

import java.util.List;

public class ModePickerAdapter extends ArrayAdapter<ModeObject> {

    Context context;
    int layoutResourceId;
    List<ModeObject> data = null;

    public interface ModePickerAdapterListener{
        void dismissDialog(String modeName,int modeId);
    }
    private ModePickerAdapterListener listener;

    public void setListener(ModePickerAdapterListener listener) {
        this.listener = listener;
    }

    public ModePickerAdapter(Context context, int layoutResourceId, List<ModeObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NotificationHolder holder;
        final Context context = parent.getContext();

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new NotificationHolder();

            holder.nameTextView = (TextView)row.findViewById(R.id.mode_picker_title);
            holder.modeImage = (ImageView)row.findViewById(R.id.spinner_row_image);
            holder.wifi = (ImageView)row.findViewById(R.id.picker_wifi_image);
            holder.ringtone = (ImageView)row.findViewById(R.id.picker_ringtone_image);
            holder.call = (ImageView)row.findViewById(R.id.picker_call_image);
            holder.message = (ImageView)row.findViewById(R.id.picker_message_image);
            row.setTag(holder);
        }
        else
        {
            holder = (NotificationHolder)row.getTag();
        }

        final ModeObject mode = data.get(position);

        holder.nameTextView.setText(mode.name);
        if(!mode.wifi)
            holder.wifi.setAlpha(0.2f);
        else
            holder.wifi.setAlpha(1f);

        if(mode.ringtone.equals("")) {
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ringtone));
            holder.ringtone.setAlpha(0.4f);
        }
        if(mode.ringtone.equals("Normal"))
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ringtone));
        if(mode.ringtone.equals("Vibrate"))
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vibrate));
        if(mode.ringtone.equals("Mute")) {
            holder.ringtone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ringtone));
            holder.ringtone.setAlpha(0.4f);
        }


        if(mode.callMessage.equals(""))
            holder.call.setAlpha(0.2f);
        else
            holder.call.setAlpha(1f);

        if(mode.smsMessage.equals(""))
            holder.message.setAlpha(0.2f);
        else
            holder.message.setAlpha(1f);


        holder.modeImage.setImageBitmap(Utils.loadImageFromStorage(context,mode.name));

        //Listener

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dismissDialog(mode.name,mode.id);
            }
        });

        return row;
    }

    static class NotificationHolder
    {
        TextView nameTextView;
        ImageView modeImage;
        ImageView wifi;
        ImageView ringtone;
        ImageView call;
        ImageView message;
    }



}
