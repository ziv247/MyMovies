package com.example.ziv24.canvasex2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    public class GraphicsView extends View{

        private Paint mPaint;
        private Path mPath;
        private Canvas mCanvas;


        public GraphicsView(Context context) {
            super(context);
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5);
            mPaint.setColor(Color.BLUE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mCanvas = canvas;
            mCanvas.drawPath(mPath,mPaint);
        }
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
    }

}
