package com.aleix.drawit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity
                          implements GeometricElementsFragment.GeometricElementsListener{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String colorDisabled = "#ffbbbbbb";

    private customDrawItView mCustomDrawItView;

    private ImageButton pencilButton;
    private ImageButton geometricElemButton;
    private ImageButton colorButton;
    private ImageButton eraseButton;
    private ImageButton undoButton;

    private ImageButton saveButton;
    //private ImageButton brushButton;
    private ImageButton newImageButton;
    private ImageButton cancelButton;
    private ImageButton okButton;

    private String[] resolutions;
    private String resolution;
    private String[] formats;
    private String format;

    private GeometricElementsFragment.GeoElement chosenElement;
    private Bitmap circle, square, rectangle, triangle;

    // GeometricElementsFragment interface implementation:
    @Override
    public void onElemClick(GeometricElementsFragment.GeoElement element) {
        Log.d(LOG_TAG, "onElemClick: " + element.toString());
        chosenElement = element;
        mCustomDrawItView.setGeoElemActive(chosenElement);
        switch(chosenElement){
            case Circle:
                geometricElemButton.setImageBitmap(circle);
                break;
            case Square:
                geometricElemButton.setImageBitmap(square);
                break;
            case Rectangle:
                geometricElemButton.setImageBitmap(rectangle);
                break;
            case Triangle:
                geometricElemButton.setImageBitmap(triangle);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolutions = getResources().getStringArray(R.array.resolutions);
        resolution = resolutions[1]; /*512*512*/ // Not needed...
        formats = getResources().getStringArray(R.array.formats);
        format = formats[0]; /*png*/  // Not needed...

        chosenElement = GeometricElementsFragment.GeoElement.Circle;

        setUpBitmaps();

        mCustomDrawItView = (customDrawItView) findViewById(R.id.customDrawItView);

        pencilButton = (ImageButton) findViewById(R.id.pencil);
        pencilButton.setBackgroundColor(Color.GRAY);    //pencilButton selected by default
        geometricElemButton = (ImageButton) findViewById(R.id.geometricElem);
        geometricElemButton.setBackgroundColor(Color.parseColor(colorDisabled));
        colorButton = (ImageButton) findViewById(R.id.color);
        colorButton.setBackgroundColor(Color.parseColor(colorDisabled));
        eraseButton = (ImageButton) findViewById(R.id.erase);
        eraseButton.setBackgroundColor(Color.parseColor(colorDisabled));
        undoButton = (ImageButton) findViewById(R.id.undo);
        undoButton.setBackgroundColor(Color.parseColor(colorDisabled));


        saveButton = (ImageButton) findViewById(R.id.save);
        saveButton.setBackgroundColor(Color.parseColor(colorDisabled));
        //brushButton = (ImageButton) findViewById();
        //brushButton.setBackgroundColor(Color.parseColor(colorDisabled));
        newImageButton = (ImageButton) findViewById(R.id.newImage);
        newImageButton.setBackgroundColor(Color.parseColor(colorDisabled));
        cancelButton = (ImageButton) findViewById(R.id.cancel);
        cancelButton.setBackgroundColor(Color.parseColor(colorDisabled));
        cancelButton.setVisibility(View.INVISIBLE); // TODO
        //cancelButton.setVisibility(View.GONE); // TODO
        okButton = (ImageButton) findViewById(R.id.ok);
        //okButton.setBackgroundColor(Color.parseColor(colorDisabled)); TODO: avaluar diferencia
        //okButton.setVisibility(View.?);
        // TODO: usar funcio setActiveCancelOk

        setUpOnTouchListeners();

        pencilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDrawItView.setPencilActive();
                geometricElemButton.setBackgroundColor(Color.parseColor(colorDisabled));
                eraseButton.setBackgroundColor(Color.parseColor(colorDisabled));
                pencilButton.setBackgroundColor(Color.GRAY);
                setActiveCancelOk(false);
            }
        });

        geometricElemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencilButton.setBackgroundColor(Color.parseColor(colorDisabled));
                eraseButton.setBackgroundColor(Color.parseColor(colorDisabled));
                geometricElemButton.setBackgroundColor(Color.GRAY);
                setActiveCancelOk(true);

                GeometricElementsFragment dialog = new GeometricElementsFragment();
                dialog.show(getFragmentManager(), "dialogGeom");
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
                mCustomDrawItView.setPencilActive(); // erase in pencil mode
                mCustomDrawItView.setDrawing(false);
                pencilButton.setBackgroundColor(Color.parseColor(colorDisabled));
                geometricElemButton.setBackgroundColor(Color.parseColor(colorDisabled));
                eraseButton.setBackgroundColor(Color.GRAY);
                setActiveCancelOk(false);
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDrawItView.undo();
            }
        });

        // Bottom buttons:

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = formats[0]; /*png*/
                Log.d("Default format", format);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Save this drawing to the gallery. Choose a format")
                        .setSingleChoiceItems(formats, 0 /*png*/, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                format = formats[which];
                                Log.d("Format clicked", format);
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean saved = saveImage(format); // too much code here, better with this function
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

        //brushButton.setOnClickListener...

        newImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolution = resolutions[1]; //512*512
                Log.d("Default res", resolution);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Choose the new resolution. This drawing will be deleted")
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "cancelButton clicked");
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "okButton clicked");
            }
        });

    }


    private  boolean saveImage(String format){ // png o jpg
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
        File file = new File(directory, name + "." + format);
        try{
            FileOutputStream fileOS = new FileOutputStream(file);
            Bitmap.CompressFormat compressFormat = format.equals("png")?
                    Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
            bitmap.compress(compressFormat, 100, fileOS);
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

    private void setUpBitmaps(){
        circle = BitmapFactory.decodeResource(getResources(), R.mipmap.circle);
        square = BitmapFactory.decodeResource(getResources(), R.mipmap.square);
        rectangle = BitmapFactory.decodeResource(getResources(), R.mipmap.rectangle);
        triangle = BitmapFactory.decodeResource(getResources(), R.mipmap.triangle);
    }

    private void setUpOnTouchListeners() {
        pencilButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pencilButton.setBackgroundColor(Color.GRAY);
                }
                return false;
            }
        });
        geometricElemButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    geometricElemButton.setBackgroundColor(Color.GRAY);
                }
                return false;
            }
        });
        colorButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    colorButton.setBackgroundColor(Color.GRAY);
                } else if(event.getAction() == MotionEvent.ACTION_UP)
                    colorButton.setBackgroundColor(Color.parseColor(colorDisabled));
                return false;
            }
        });
        eraseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    eraseButton.setBackgroundColor(Color.GRAY);
                }
                return false;
            }
        });
        undoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    undoButton.setBackgroundColor(Color.GRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP)
                    undoButton.setBackgroundColor(Color.parseColor(colorDisabled));
                return false;
            }
        });


        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    saveButton.setBackgroundColor(Color.GRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP)
                    saveButton.setBackgroundColor(Color.parseColor(colorDisabled));
                return false;
            }
        });
        //brushButton.setOnTouchListener(new OnTouch...
        newImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    newImageButton.setBackgroundColor(Color.GRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP)
                    newImageButton.setBackgroundColor(Color.parseColor(colorDisabled));
                return false;
            }
        });
        cancelButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cancelButton.setBackgroundColor(Color.GRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP)
                    cancelButton.setBackgroundColor(Color.parseColor(colorDisabled));
                return false;
            }
        });
        okButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    okButton.setBackgroundColor(Color.GRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP)
                    okButton.setBackgroundColor(Color.parseColor(colorDisabled));
                return false;
            }
        });
    }

    private void setActiveCancelOk(boolean active){
        // TODO: implementar
    }

    @Override
    public void onBackPressed() {
        // Ask to leave
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Really exit?")
                .setMessage("The drawing will be deleted")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
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
