package com.aleix.drawit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity{

    private customDrawItView mCustomDrawItView;

    private ImageButton pencilButton;
    private ImageButton geometricElemButton;
    private ImageButton colorButton;
    private ImageButton eraseButton;
    private ImageButton undoButton;
    //private ImageButton deleteButton;

    private ImageButton newButton;
    private ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setUp();
        mCustomDrawItView = (customDrawItView) findViewById(R.id.customDrawItView);

        pencilButton = (ImageButton) findViewById(R.id.pencil);
        //pencilButton selected by default
        //pencilButton.setPressed(true);
        geometricElemButton = (ImageButton) findViewById(R.id.geometricElem);
        colorButton = (ImageButton) findViewById(R.id.color);
        eraseButton = (ImageButton) findViewById(R.id.erase);
        undoButton = (ImageButton) findViewById(R.id.undo);

        newButton = (ImageButton) findViewById(R.id.newImage);
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
                //
            }
        });



        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Draw a new drawing")
                      .setMessage("The current drawing will be deleted")
                      .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              mCustomDrawItView.newImage();
                              dialog.dismiss();
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Save the drawing")
                      .setMessage("Save this drawing to the gallery")
                      .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              // TODO: implementar save. Mostrar toast segons si guardat correctament o no
                              try {
                                  mCustomDrawItView.setDrawingCacheEnabled(true);
                                  Bitmap bitmap = mCustomDrawItView.getDrawingCache();
                                  //File file = new File(getFilesDir(), "prova1");
                                  //file.createNewFile();

                                  //FileOutputStream fileOS = new FileOutputStream(file);
                                  //bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
                                  //fileOS.close();
                                  String s = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "prova1", "aaa");
                                  //MediaScannerConnection.scanFile(getApplicationContext(), new String[]{getFilesDir() + "prova1"}, null, null);
                                  if (s != null)
                                      Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                  else Toast.makeText(getApplicationContext(), "Error saving", Toast.LENGTH_SHORT).show();
                                  mCustomDrawItView.destroyDrawingCache();

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                /* Quan tens el file:
                FileOutputStream fos = new FileOutputStream(pictureFile);
                i.bitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);//png, 100% de calidad y se guarda en el fos
                fos.close();

                MediaScannerConnection.scanFile(Context context, String[] path, null, null);
                // context -> "getApplicationContext()" y el path es la ruta del fichero con el nombre incluido
                */
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

        /* FI del setUp */
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
