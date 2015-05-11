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

public class GeometricElementsFragment extends DialogFragment{
    ImageButton circle;
    ImageButton square;
    ImageButton rectangle;
    ImageButton triangle;

    public enum GeoElement{Circle, Square, Rectangle, Triangle}

    public interface GeometricElementsListener {
        void onElemClick(GeoElement element);
    }

    GeometricElementsListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the GeometricElementsListener so we can send events to the host
            mListener = (GeometricElementsListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement GeometricElementsListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_geometric_elements, null);

        circle = (ImageButton) dialogView.findViewById(R.id.dialogCircle);
        square = (ImageButton) dialogView.findViewById(R.id.dialogSquare);
        rectangle = (ImageButton) dialogView.findViewById(R.id.dialogRectangle);
        triangle = (ImageButton) dialogView.findViewById(R.id.dialogTriangle);

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "Circle clicked");
                mListener.onElemClick(GeoElement.Circle);
                dismiss();
            }
        });

        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "Square clicked");
                mListener.onElemClick(GeoElement.Square);
                dismiss();
            }
        });

        rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "Rectangle clicked");
                mListener.onElemClick(GeoElement.Rectangle);
                dismiss();
            }
        });

        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "Triangle clicked");
                mListener.onElemClick(GeoElement.Triangle);
                dismiss();
            }
        });

        builder.setView(dialogView)
                /*
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Dialog", "Selected");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Dialog", "Cancel");
                        GeometricElementsFragment.this.getDialog().cancel();
                    }
                })*/
                .setTitle("Select a Geometric Element");
        return builder.create();
    }
}
