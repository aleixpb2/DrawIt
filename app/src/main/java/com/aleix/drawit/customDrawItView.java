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

    private boolean drawing; // !drawing = erasing
    private boolean pencilActive; // !pencilActive = geometric elements active
    private boolean firstClickGeo; // first click when geometric elements is active (!pencilActive)

    private GeometricElementsFragment.GeoElement chosenElement;

    private int color;
    private ArrayList<Pair<Path, Paint>> pathsDrawn;

    private final String LOG_TAG = customDrawItView.class.getSimpleName();

    private DrawItListener mListener;

    public interface DrawItListener {
        void onFirstClickGeo();
    }

    public void setDrawItListener(DrawItListener l) {
        mListener = l;
    }


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
        mPaint.setTextSize(40); // not needed...

        mPath = new Path();

        pathsDrawn = new ArrayList<>();

        // 720 is the maximum resolution, but 512 the default (when saving the bitmap)
        Bitmap mBitmap = Bitmap.createBitmap(720,720,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        drawing = true; // by default
        pencilActive = true;
        //firstClickGeo = true;

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
                if(!pencilActive) { // geo
                    drawGeo(x, y);
                    if(firstClickGeo){
                        firstClickGeo = false;
                        mListener.onFirstClickGeo();
                    }
                    else{
                        undo();
                        drawGeo(x,y);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE: // draw the path
                if(pencilActive) mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP: // draw it to the canvas
                if(pencilActive) mPath.lineTo(x + 0.5f, y + 0.5f); // DOT
                    //mCanvas.drawPath(mPath, mPaint);
                pathsDrawn.add(new Pair<>(new Path(mPath), new Paint(mPaint)));
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

    public boolean getDrawing(){
        return drawing;
    }

    public void setPencilActive(){
        pencilActive = true;
        mPaint.setStyle(Paint.Style.STROKE);
        setDrawing(true);
    }

    public boolean getPencilActive(){
        return pencilActive;
    }

    public void setGeoElemActive(GeometricElementsFragment.GeoElement elem){
        pencilActive = false;
        firstClickGeo = true;
        mPaint.setStyle(Paint.Style.FILL);
        setDrawing(true);
        chosenElement = elem;
    }

    public void setFirstClickGeo(boolean firstClickGeo){
        this.firstClickGeo = firstClickGeo;
    }

    public void undo(){
        int last = pathsDrawn.size() - 1;
        if(last == -1) return;
        pathsDrawn.remove(last);
        mPath.reset();
        invalidate();
    }

    private void drawGeo(float x, float y){
        switch (chosenElement){
            case Circle:
                mPath.addCircle(x,y, 25f,Path.Direction.CW);
                break;
            case Square:
                mPath.addRect(x -25f, y - 25f, x + 25f, y + 25f, Path.Direction.CW);
                break;
            case Rectangle:
                mPath.addRect(x -25f, y - 15f, x + 25f, y + 15f, Path.Direction.CW);
                break;
            case Triangle:
                // lets review a bit of geometry (High School/Baccalaureat):
                // in an equilateral triangle, h = (sqrt(3)*x)/2 where x is the side
                // and the center is at h/3 from the bottom side
                // (circumcenter, incenter, orthocenter and centroid at the same point)
                float side = 50f;
                float h = (float) ((Math.sqrt(3)*side)/2f);
                float leftbottomx = x - 0.5f*side;
                float leftbottomy = y + h/3f;
                float rightbottomx = x + 0.5f*side;
                float rightbottomy = y+ h/3f;
                float topx = x;
                float topy = y - (2f*h)/3f;
                mPath.moveTo(leftbottomx, leftbottomy);
                mPath.lineTo(rightbottomx, rightbottomy);
                mPath.lineTo(topx, topy);
                mPath.lineTo(leftbottomx, leftbottomy);
                break;
        }
    }
}