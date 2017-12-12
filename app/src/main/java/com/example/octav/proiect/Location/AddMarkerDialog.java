package com.example.octav.proiect.Location;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.Modes.ModePickerDialog;
import com.example.octav.proiect.R;
import java.text.DecimalFormat;


public class AddMarkerDialog extends DialogFragment {

     public interface MarkerDialogListener{
        void setMarker(GeofenceObject geo);
    }
    private MarkerDialogListener listener;

    public void setListener(MarkerDialogListener listener) {
        this.listener = listener;
    }

    //Mode Picker

    private TextView modeTextView;
    private Button modePickerButton;
    private Button saveButton;
    private TextView titleTextView;
    private TextView radiusTextView;
    private TextView coordinatesTextView;
    private DataBase db;
    private Boolean isEditing;
    public static int mode = 0;
    public GeofenceObject geo;

    public static AddMarkerDialog newInstance(GeofenceObject g) {

        AddMarkerDialog f = new AddMarkerDialog();
        Bundle args = new Bundle();
        args.putParcelable("geofence",g);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        geo = getArguments().getParcelable("geofence");
        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_marker_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogFadeAnimation;

        //Mode Picker

        titleTextView = (TextView)view.findViewById(R.id.marker_title);
        radiusTextView = (TextView)view.findViewById(R.id.marker_radius);
        coordinatesTextView = (TextView)view.findViewById(R.id.marker_coordinates_text_view);
        modeTextView = (TextView)view.findViewById(R.id.marker_mode_text_view);
        modePickerButton = (Button)view.findViewById(R.id.marker_mode_picker_button);
        saveButton = (Button)view.findViewById(R.id.set_marker_button);

        modePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModePickerDialog picker = new ModePickerDialog();
                picker.setListener(new ModePickerDialog.ModePickerListener() {
                    @Override
                    public void update(String modeName,int modeId) {
                        modeTextView.setText(modeName);
                        mode = modeId;
                    }
                });
                picker.show(getFragmentManager(), "mode_picker");
            }
        });


        titleTextView.setText(geo.address);
        radiusTextView.setText(String.valueOf(geo.radius));

        coordinatesTextView.setText(new DecimalFormat("###.######").format(geo.latitude)+
                " , "+new DecimalFormat("###.######").format(geo.longitude));


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleTextView.getText().toString();
                int radius = Integer.valueOf(radiusTextView.getText().toString());
                GeofenceObject obj = new GeofenceObject(db.getNextGeofenceId(),title,geo.latitude,geo.longitude,radius,mode);
                listener.setMarker(obj);
                dismiss();
            }
        });

        return dialog;
    }

}
