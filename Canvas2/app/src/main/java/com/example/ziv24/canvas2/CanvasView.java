package com.example.ziv24.canvas2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class CanvasView extends View {

    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(5f);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                invalidate();

                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPaint);
        }
        canvas.drawPath(mPath, mPaint);
    }

    public void clearCanvas(){

        mPath.reset();
        invalidate();
    }

    public void setPaintWidth(int paintWidth) {

        mPaint.setStrokeWidth(paintWidth);
    }
    public void setPaintColor(@ColorInt int paintColor){
        mPaint.setColor(paintColor);
    }
    public void setEraser(){
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(15f);
    }
    public void setBitmapString(String bitmapString){

    }

}
