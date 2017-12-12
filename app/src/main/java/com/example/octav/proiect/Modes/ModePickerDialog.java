package com.example.octav.proiect.Modes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.octav.proiect.Utils.DataBase;
import com.example.octav.proiect.R;


import java.util.List;

public class ModePickerDialog extends DialogFragment{

    private DataBase db;
    private GridView gridView;
    private TextView textView;

    public interface ModePickerListener{
        void update(String modeName,int modeId);
    }
    private ModePickerListener listener;

    public void setListener(ModePickerListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.mode_picker_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnimation;

        gridView = (GridView) view.findViewById(R.id.pick_mode_list);
        textView = (TextView)view.findViewById(R.id.picker_no_modes);

        db = new DataBase(getActivity().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        List<ModeObject> modes = db.getModes();
        ModePickerAdapter adapter = new ModePickerAdapter(getActivity(),R.layout.mode_picker_row, modes);
        gridView.setAdapter(adapter);
        adapter.setListener(new ModePickerAdapter.ModePickerAdapterListener() {
            @Override
            public void dismissDialog(String modeName,int modeId) {
                if(listener!=null)
                    listener.update(modeName,modeId);
                dismiss();
            }
        });

        if(modes.size()==0) {
            gridView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            gridView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }

        return dialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
       params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = 900;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

}

