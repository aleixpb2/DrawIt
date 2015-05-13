package com.aleix.drawit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class BrushFragment extends DialogFragment{
    ImageButton small, medium, big;

    public interface BrushListener {
        void onBrushClick(char smb);
    }

    BrushListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            mListener = (BrushListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement BrushListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_brush, null);

        small = (ImageButton) dialogView.findViewById(R.id.dialogSmall);
        medium = (ImageButton) dialogView.findViewById(R.id.dialogMedium);
        big = (ImageButton) dialogView.findViewById(R.id.dialogBig);

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "small clicked");
                mListener.onBrushClick('s');
                dismiss();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "medium clicked");
                mListener.onBrushClick('m');
                dismiss();
            }
        });
        big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "big clicked");
                mListener.onBrushClick('b');
                dismiss();
            }
        });

        builder.setView(dialogView)
                .setTitle("Select a brush size");
        return builder.create();
    }

}
