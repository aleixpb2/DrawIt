package com.aleix.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class customDrawItView extends View/* implements View.OnTouchListener */{
    //private String mExampleString;
    //private int mExampleColor = Color.RED;
    //private float mExampleDimension = 0;
    //private Drawable mExampleDrawable;
    //private TextPaint mTextPaint;
    //private float mTextWidth;
    //private float mTextHeight;

    private Paint mPaint, mCanvasPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private final String LOG_TAG = customDrawItView.class.getSimpleName();

    // Constructors needed to allow the ADT to interact with the view
    public customDrawItView(Context context) {
        super(context);
        init(null, 0);
    }
    public customDrawItView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    public customDrawItView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) { //TODO: es setupDrawing()
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10); // by default
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPaint.setColor(Color.BLACK); // Black by default
        mPaint.setTextSize(40); // Big size by default TODO: text drawing

        mPath = new Path();
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);


        //TODO: a onSizeChanged?
        mBitmap = Bitmap.createBitmap(720,720,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width; // minim
        setMeasuredDimension(size, size); // vista quadrada
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        // TODO: consider storing these as member variables to reduce allocations per draw cycle.
        int contentWidth = getWidth();
        int contentHeight = getHeight();
        //Debug:
        //Log.i(LOG_TAG, "Painting:\nWidth = " + contentWidth + "\nHeight = " + contentHeight);
        canvas.drawBitmap(mBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mPath, mPaint);
        /*
        canvas.drawRect(0, 100, contentWidth, 200, mPaint);
        canvas.drawCircle(0.0f, 0.0f, 100f, mPaint);
        canvas.drawCircle(contentWidth/2, 0.0f, 100f, mPaint);
        canvas.drawCircle(contentWidth, 0.0f, 100f, mPaint);
        canvas.drawText("Text de prova", 5, contentHeight/2, mPaint); */
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        //get the x and the y of the event:
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN: // pressed
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE: // draw the path
                mPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP: // draw it to the canvas
                mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                break;
            default: return false; // Path not drawn
        }
        invalidate();
        return true; // Path drawn correctly
    }

    public void setPaintColor(int paintColor){
        mPaint.setColor(paintColor);
        invalidate(); // TODO: no cal
    }
}