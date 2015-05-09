package com.aleix.drawit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity{

    private customDrawItView mCustomDrawItView;
    private ImageButton button1;
    private ImageButton button2;
    private ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setUp();
        mCustomDrawItView = (customDrawItView) findViewById(R.id.customDrawItView);
        button1 = (ImageButton) findViewById(R.id.button1);
        button2 = (ImageButton) findViewById(R.id.button2);
        saveButton = (ImageButton) findViewById(R.id.save);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDrawItView.setPaintColor(Color.GREEN);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDrawItView.setPaintColor(Color.BLUE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: implementar save. Mostrar toast segons si guardat correctament o no
                /* Quan tens el file:
                FileOutputStream fos = new FileOutputStream(pictureFile);
                i.bitmap().compress(Bitmap.CompressFormat.PNG, 100, fos); //Comprimimos a png, con 100% de calidad y se guarda en el fos
                fos.close();

                MediaScannerConnection.scanFile(Context context, String[] path, null, null);
                // context -> "getApplicationContext()" y el path es la ruta del fichero con el nombre incluido
                */

            }
        });

        /* FI del setUp */

        //Bitmap bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888); // TODO: 512*512 default
        //c = new Canvas(bitmap);
        //c.drawRGB(255, 0, 0);
        //c.drawPaint(mPaint);

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
