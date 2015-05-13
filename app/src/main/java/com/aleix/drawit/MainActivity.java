package com.aleix.drawit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
                          implements GeometricElementsFragment.GeometricElementsListener,
                                     BrushFragment.BrushListener,
                                     ColorsFragment.ColorsListener{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String colorDisabled = "#ffbbbbbb";

    private customDrawItView mCustomDrawItView;

    private ImageButton pencilButton;
    private ImageButton geometricElemButton;
    private ImageButton colorButton;
    private ImageButton eraseButton;
    private ImageButton undoButton;

    private ImageButton saveButton;
    private ImageButton brushButton;
    private ImageButton newImageButton;
    private ImageButton cancelButton;
    private ImageButton okButton;

    private String[] resolutions;
    private String resolution;
    private String[] formats;
    private String format;

    private GeometricElementsFragment.GeoElement chosenElement;
    private Bitmap circle, square, rectangle, triangle;
    private Bitmap brushs, brushm, brushb;
    private Bitmap colY, colG, colC, colM, colR, colB, colBlack, colBrown, colWhite;

    private customDrawItView.DrawItListener drawItListener;

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
    // BrushFragment interface implementation:
    @Override
    public void onBrushClick(char smb) {
        Log.d(LOG_TAG, "onElemClick: " + smb);
        mCustomDrawItView.setSizeSMB(smb);
        // TODO: change the images (bigger!)
        switch(smb){
            case 's':
                brushButton.setImageBitmap(brushs);
                break;
            case 'm':
                brushButton.setImageBitmap(brushm);
                break;
            case 'b':
                brushButton.setImageBitmap(brushb);
                break;
            default:
                brushButton.setImageBitmap(brushm);
        }
    }
    // ColorsFragment interface implementation
    @Override
    public void onColorsClick(ColorsFragment.DrawItColor color) {
        Log.d(LOG_TAG, "onColorsClick: " + color.name());
        int paintColor;
        switch (color){
            case Y:
                paintColor = Color.YELLOW;
                colorButton.setImageBitmap(colY);
                break;
            case G:
                paintColor = Color.GREEN;
                colorButton.setImageBitmap(colG);
                break;
            case C:
                paintColor = Color.CYAN;
                colorButton.setImageBitmap(colC);
                break;
            case M:
                paintColor = Color.MAGENTA;
                colorButton.setImageBitmap(colM);
                break;
            case R:
                paintColor = Color.RED;
                colorButton.setImageBitmap(colR);
                break;
            case B:
                paintColor = Color.BLUE;
                colorButton.setImageBitmap(colB);
                break;
            case BLACK:
                paintColor = Color.BLACK;
                colorButton.setImageBitmap(colBlack);
                break;
            case BROWN:
                paintColor = Color.parseColor("#ff9b3c15"); // RGB = 155, 60, 21
                colorButton.setImageBitmap(colBrown);
                break;
            case WHITE:
                paintColor = Color.WHITE;
                colorButton.setImageBitmap(colWhite);
                break;
            default:
                paintColor = Color.BLACK;
                colorButton.setImageBitmap(colBlack);
        }
        mCustomDrawItView.setPaintColor(paintColor);
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
        brushButton = (ImageButton) findViewById(R.id.brush);
        brushButton.setBackgroundColor(Color.parseColor(colorDisabled));
        newImageButton = (ImageButton) findViewById(R.id.newImage);
        newImageButton.setBackgroundColor(Color.parseColor(colorDisabled));
        cancelButton = (ImageButton) findViewById(R.id.cancel);
        cancelButton.setBackgroundColor(Color.parseColor(colorDisabled));
        okButton = (ImageButton) findViewById(R.id.ok);
        okButton.setBackgroundColor(Color.parseColor(colorDisabled));
        setActiveCancelOk(false);

        setUpOnTouchListeners();

        drawItListener = new customDrawItView.DrawItListener() {
            @Override
            public void onFirstClickGeo() {
                Log.d(LOG_TAG, "onFirstClickGeo");
                setActiveCancelOk(true);
            }
        };
        mCustomDrawItView.setDrawItListener(drawItListener);

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

                setActiveCancelOk(false); // make it disappear if we come from another geoElement

                GeometricElementsFragment dialog = new GeometricElementsFragment();
                dialog.show(getFragmentManager(), "dialogGeom");
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setActiveCancelOk(false);
                ColorsFragment dialog = new ColorsFragment();
                dialog.show(getFragmentManager(), "dialogColors");

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
                if(!mCustomDrawItView.getPencilActive()){ //geo
                    if(!mCustomDrawItView.getFirstClickGeo()){ // Ok and Cancel enabled
                        mCustomDrawItView.setFirstClickGeo(true);
                        setActiveCancelOk(false);
                    }
                }
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
                                setActiveCancelOk(false);
                                //mCustomDrawItView.setFirstClickGeo(true);
                                mCustomDrawItView.setGeoElemActive(mCustomDrawItView.getChosenElement());
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

        brushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setActiveCancelOk(false);
                BrushFragment dialog = new BrushFragment();
                dialog.show(getFragmentManager(), "dialogBrush");
            }
        });

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
                                setActiveCancelOk(false);
                                mCustomDrawItView.setGeoElemActive(mCustomDrawItView.getChosenElement());
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
                setActiveCancelOk(false);
                mCustomDrawItView.setFirstClickGeo(true);
                mCustomDrawItView.undo();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "okButton clicked");
                setActiveCancelOk(false);
                mCustomDrawItView.setFirstClickGeo(true);
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

    private void setUpBitmaps(){ // to load only once the bitmaps
        circle = BitmapFactory.decodeResource(getResources(), R.mipmap.circle);
        square = BitmapFactory.decodeResource(getResources(), R.mipmap.square);
        rectangle = BitmapFactory.decodeResource(getResources(), R.mipmap.rectangle);
        triangle = BitmapFactory.decodeResource(getResources(), R.mipmap.triangle);

        brushs = BitmapFactory.decodeResource(getResources(), R.mipmap.brushs);
        brushm = BitmapFactory.decodeResource(getResources(), R.mipmap.brushm);
        brushb = BitmapFactory.decodeResource(getResources(), R.mipmap.brushb);

        colY = BitmapFactory.decodeResource(getResources(), R.mipmap.coly);
        colG = BitmapFactory.decodeResource(getResources(), R.mipmap.colg);
        colC = BitmapFactory.decodeResource(getResources(), R.mipmap.colc);
        colM = BitmapFactory.decodeResource(getResources(), R.mipmap.colm);
        colR = BitmapFactory.decodeResource(getResources(), R.mipmap.colr);
        colB = BitmapFactory.decodeResource(getResources(), R.mipmap.colb);
        colBlack = BitmapFactory.decodeResource(getResources(), R.mipmap.colblack);
        colBrown = BitmapFactory.decodeResource(getResources(), R.mipmap.colbrown);
        colWhite = BitmapFactory.decodeResource(getResources(), R.mipmap.colwhite);
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
        brushButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    brushButton.setBackgroundColor(Color.GRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP)
                    brushButton.setBackgroundColor(Color.parseColor(colorDisabled));
                return false;
            }
        });
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
        if(!active) {
            cancelButton.setVisibility(View.INVISIBLE);
            okButton.setVisibility(View.INVISIBLE);
        }
        else{
            cancelButton.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        // Ask to leave
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Really exit?")
                .setMessage("The unsaved changes will be deleted")
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

        if(id == R.id.action_help){
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_about){
            HelpActivity.showAboutDialog(MainActivity.this);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }
}
