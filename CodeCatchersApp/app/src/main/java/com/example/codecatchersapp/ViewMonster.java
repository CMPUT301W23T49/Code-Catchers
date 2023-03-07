package com.example.codecatchersapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.graphics.Color;

public class ViewMonster extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    public ViewMonster(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    public void drawMonster() {
        MonsterDB.generateRandomMonster(mCanvas, mPaint);
        invalidate();
    }

    public Canvas getCanvas() {
        return mCanvas;
    }
    public void setmCanvas(Canvas mCanvas) {
        this.mCanvas = mCanvas;
        invalidate();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        invalidate();
    }
}


