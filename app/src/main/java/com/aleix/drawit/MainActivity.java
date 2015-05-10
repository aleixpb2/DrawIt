package com.aleix.drawit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity{

    private customDrawItView mCustomDrawItView;

    private ImageButton pencilButton;
    private ImageButton geometricElemButton;
    private ImageButton colorButton;
    private ImageButton eraseButton;
    private ImageButton undoButton;
    //private ImageButton deleteButton;

    private ImageButton newImageButton;
    private ImageButton saveButton;

    String[] resolutions;
    String resolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolutions = getResources().getStringArray(R.array.resolutions);
        resolution = resolutions[1]; /*512*512*/

        // setUp();
        mCustomDrawItView = (customDrawItView) findViewById(R.id.customDrawItView);

        pencilButton = (ImageButton) findViewById(R.id.pencil);
        //pencilButton selected by default
        //pencilButton.setPressed(true);
        geometricElemButton = (ImageButton) findViewById(R.id.geometricElem);
        colorButton = (ImageButton) findViewById(R.id.color);
        eraseButton = (ImageButton) findViewById(R.id.erase);
        undoButton = (ImageButton) findViewById(R.id.undo);

        newImageButton = (ImageButton) findViewById(R.id.newImage);
        saveButton = (ImageButton) findViewById(R.id.save);

        pencilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pencilButton.setPressed(true);
                //geometricElemButton.setPressed(false);
                mCustomDrawItView.setDrawing(true);
            }
        });

        geometricElemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mCustomDrawItView.setPaintColor(Color.BLUE);
                //mCustomDrawItView.setPaintColor(Color.GREEN);
                //pencilButton.setPressed(false);
                //geometricElemButton.setPressed(true);
                mCustomDrawItView.setDrawing(true);
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDrawItView.setDrawing(false);
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDrawItView.undo();
            }
        });

        // Bottom buttons:

        newImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolution = resolutions[1]; //512*512
                Log.d("Default res", resolution);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Choose a resolution. The current drawing will be deleted")
                        .setSingleChoiceItems(resolutions, 1 /*512*512*/, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resolution = resolutions[which];
                                Log.d("Res. clicked", resolution);
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Final resolution", resolution);
                                mCustomDrawItView.newImage();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                //****   .setMessage("The current drawing will be deleted")
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Save the drawing")
                      .setMessage("Save this drawing to the gallery")
                      .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              boolean saved = saveImage(); // too much code here, better with this function
                              if (saved)
                                  Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                              else
                                  Toast.makeText(getApplicationContext(), "Error saving", Toast.LENGTH_SHORT).show();
                          }
                      })
                      .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              dialog.cancel();
                          }
                      })
                      .show();
            }
        });

        /* Fi del setUp */
    }

    private  boolean saveImage(){
        final String LOG_TAG = "saveImage";
        boolean writable = Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState());
        if(!writable) {
            Log.e(LOG_TAG, "Not writable");
            return false;
        }
        // writable:
        mCustomDrawItView.setDrawingCacheEnabled(true);
        Bitmap bitmap = mCustomDrawItView.getDrawingCache();
        // Resolution:
        String resString = "" + resolution.charAt(0) + resolution.charAt(1) + resolution.charAt(2);
        int res = Integer.parseInt(resString);
        Log.d(LOG_TAG, "Resolution: " + res + "*" + res);
        bitmap = Bitmap.createScaledBitmap(bitmap, res, res, false);
        mCustomDrawItView.destroyDrawingCache();

        File directory = new File
                (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DrawIt");
        if(!directory.exists() && !directory.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
            return false;
        }

        String name = String.valueOf(System.currentTimeMillis());
        File file = new File(directory, name + ".png");
        try{
            FileOutputStream fileOS = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
            fileOS.flush();
            fileOS.close();
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getAbsolutePath()}, null, null);
        } catch (Exception e) {
            e.getMessage();
            Log.e(LOG_TAG, "Exception");
            return false;
        }
        Log.i(LOG_TAG, "Image saved to" + file.toString());
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
