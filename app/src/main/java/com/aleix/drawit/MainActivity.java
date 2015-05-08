package com.aleix.drawit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity{

    private customDrawItView mCustomDrawItView;
    private ImageButton button1;
    private ImageButton button2;

    private Paint mPaint;
    //private Canvas c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setUp();
        mCustomDrawItView = (customDrawItView) findViewById(R.id.customDrawItView);
        button1 = (ImageButton) findViewById(R.id.button1);
        button2 = (ImageButton) findViewById(R.id.button2);

        //mCustomDrawItView = new customDrawItView(getApplicationContext());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK); // black by default

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPaint.setColor(Color.GREEN); // green
                mCustomDrawItView.setPaintColor(Color.GREEN);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPaint.setColor(Color.BLUE); // blue
                mCustomDrawItView.setPaintColor(Color.BLUE);
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
