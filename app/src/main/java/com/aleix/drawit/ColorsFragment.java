package com.aleix.drawit;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class ColorsFragment extends DialogFragment {
    ImageButton colY, colG, colC;
    ImageButton colM, colR, colB;
    ImageButton colBlack, colBrown, colWhite;

    public enum DrawItColor{Y, G, C, M, R, B, BLACK, BROWN, WHITE}

    public interface ColorsListener {
        void onColorsClick(DrawItColor color);
    }

    ColorsListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            mListener = (ColorsListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ColorsListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_colors, null);

        colY = (ImageButton) dialogView.findViewById(R.id.colY);
        colG = (ImageButton) dialogView.findViewById(R.id.colG);
        colC = (ImageButton) dialogView.findViewById(R.id.colC);

        colM = (ImageButton) dialogView.findViewById(R.id.colM);
        colR = (ImageButton) dialogView.findViewById(R.id.colR);
        colB = (ImageButton) dialogView.findViewById(R.id.colB);

        colBlack = (ImageButton) dialogView.findViewById(R.id.colBlack);
        colBrown = (ImageButton) dialogView.findViewById(R.id.colBrown);
        colWhite = (ImageButton) dialogView.findViewById(R.id.colWhite);

        colY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.Y);
                dismiss();
            }
        });
        colG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.G);
                dismiss();
            }
        });
        colC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.C);
                dismiss();
            }
        });

        colM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.M);
                dismiss();
            }
        });
        colR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.R);
                dismiss();
            }
        });
        colB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.B);
                dismiss();
            }
        });

        colBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.BLACK);
                dismiss();
            }
        });
        colBrown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.BROWN);
                dismiss();
            }
        });
        colWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorsClick(DrawItColor.WHITE);
                dismiss();
            }
        });

        builder.setView(dialogView)
                .setTitle("Select a color");
        return builder.create();
    }
}
