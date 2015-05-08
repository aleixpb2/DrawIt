package com.aleix.drawit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


public class customDrawItView extends View {
    //private String mExampleString;
    //private int mExampleColor = Color.RED;
    //private float mExampleDimension = 0;
    //private Drawable mExampleDrawable;

    //private TextPaint mTextPaint;
    //private float mTextWidth;
    //private float mTextHeight;

    private Paint mPaint;

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
        // Load attributes
        /*
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.customDrawItView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.customDrawItView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.customDrawItView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.customDrawItView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.customDrawItView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(R.styleable.customDrawItView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        */
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK); // Black by default
        mPaint.setTextSize(40); // Big size by default

        invalidateTextPaintAndMeasurements();
    }


    private void invalidateTextPaintAndMeasurements() {
        // TODO: és com invalidate(); ? Cal també requestLayout(); ?
        /*
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom; */
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce allocations per draw cycle.
        //int paddingLeft = getPaddingLeft();
        //int paddingTop = getPaddingTop();
        //int paddingRight = getPaddingRight();
        //int paddingBottom = getPaddingBottom();
        int contentWidth = getWidth();// - paddingLeft - paddingRight;
        int contentHeight = getHeight();// - paddingTop - paddingBottom;

        canvas.drawRect(1, 100, contentWidth-1, 200, mPaint);

        canvas.drawCircle(0.0f, 0.0f, 100f, mPaint);
        canvas.drawCircle(contentWidth/2, 0.0f, 100f, mPaint);
        canvas.drawCircle(contentWidth, 0.0f, 100f, mPaint);

        canvas.drawText("Text de prova", 5, contentHeight/2, mPaint);

        // Draw the text.
        /*
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);
                */

        // Draw the example drawable on top of the text.
        /*
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }*/
    }

    public void setPaintColor(int paintColor){
        mPaint.setColor(paintColor);
        invalidate();
    }

    /*
    public String getExampleString() {
        return mExampleString;
    }
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }
    public int getExampleColor() {
        return mExampleColor;
    }
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }
    public float getExampleDimension() {
        return mExampleDimension;
    }
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }*/
}
