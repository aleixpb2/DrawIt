package com.aleix.drawit;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HelpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Declared here because it's related to the "Help"
    public static void showAboutDialog(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        TextView message = new TextView(context);
        message.setText("DrawIt version 1.0\nCreated by Aleix Paris\nIDI Project, spring 2015");
        message.setGravity(Gravity.CENTER);

        dialog.setTitle("About")
                .setView(message)
                .setPositiveButton("Ok", null)
                .show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_about){
            showAboutDialog(HelpActivity.this);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }
}
