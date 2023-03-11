package com.example.codecatchersapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MonsterView extends View {
    private Paint paint;
    private monsterFeatures monster;
    private Canvas mCanvas;

    public MonsterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        mCanvas = new Canvas(); // Initialize mCanvas
        monster = new monsterFeatures(context, mCanvas, paint);
    }

    public MonsterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        mCanvas = new Canvas(); // Initialize mCanvas
        monster = new monsterFeatures(context, mCanvas, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("MonsterView", "onDraw called");
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        monster.generateRandomMonster(canvas, paint);
    }

    public Canvas getCanvas() {
        return this.mCanvas;
    }
}
