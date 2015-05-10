package com.aleix.drawit;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class SaveDialog extends DialogFragment {
    private String[] resolutions = getResources().getStringArray(R.array.resolutions);
    private String resolution = resolutions[1]; /*512*512*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Save")
               .setSingleChoiceItems(resolutions, 1 /*512*512*/, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       resolution = resolutions[which];
                       Log.d("Res", resolution);
                   }
               })
               .setPositiveButton("Ok", null)
               .setNegativeButton("Cancel", null);

        return builder.create();
    }
}

//Now, when you create an instance of this class and call show() on that object, the dialog appears