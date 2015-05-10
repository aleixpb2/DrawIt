package com.aleix.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class customDrawItView extends View/* implements View.OnTouchListener */{

    private Paint mPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private boolean drawing;
    private int color;
    private ArrayList<Pair<Path, Paint>> pathsDrawn;

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

    private void init(AttributeSet attrs, int defStyle) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10); // by default
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        color = Color.BLACK;
        mPaint.setColor(Color.BLACK); // Black by default
        mPaint.setTextSize(40); // Big size by default TODO: text drawing

        mPath = new Path();

        pathsDrawn = new ArrayList<>();

        //TODO: a onSizeChanged?
        // 720 is the maximum resolution, but 512 the default (when saving the bitmap)
        mBitmap = Bitmap.createBitmap(720,720,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        drawing = true; // by default

        int contentWidth = getWidth();
        int contentHeight = getHeight();
        //Debug:
        Log.d(LOG_TAG, "Painting:\nWidth = " + contentWidth + "\nHeight = " + contentHeight);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width; // minim
        setMeasuredDimension(size, size); // square view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Pair p : pathsDrawn)
            canvas.drawPath( (Path) p.first, (Paint) p.second);
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
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP: // draw it to the canvas
                /*if(mPath.isEmpty()){
                    Log.d(LOG_TAG, "empty path");
                }*/
                mPath.lineTo(x+0.5f,y+0.5f); // DOT
                mCanvas.drawPath(mPath, mPaint);
                pathsDrawn.add(
                       new Pair<>(new Path(mPath), new Paint(mPaint)) );
                mPath.reset();
                break;
            default: return false; // Path not drawn
        }
        invalidate();
        return true; // Path drawn correctly
    }

    public void setPaintColor(int paintColor){
        invalidate(); // TODO: no cal?
        color = paintColor;
        mPaint.setColor(paintColor);
    }

    public void newImage(){
        mCanvas.drawColor(Color.WHITE);
        drawing = true; // we don't want to start a drawing erasing!
        mPaint.setColor(color);
        pathsDrawn.clear();
        invalidate();
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
        if(!drawing)
            mPaint.setColor(Color.WHITE);
        else mPaint.setColor(color);
    }

    public void undo(){
        int last = pathsDrawn.size() - 1;
        if(last == -1) return;
        Path pathToBeUndone = pathsDrawn.get(last).first;
        pathsDrawn.remove(last);
        boolean wasDrawing = drawing;
        setDrawing(false);
        mPaint.setStrokeWidth(mPaint.getStrokeWidth() + 1);
        mCanvas.drawPath(pathToBeUndone, mPaint);
        if(wasDrawing) setDrawing(true);
        mPaint.setStrokeWidth(mPaint.getStrokeWidth() - 1);
        mPath.reset();
        invalidate();
    }
}