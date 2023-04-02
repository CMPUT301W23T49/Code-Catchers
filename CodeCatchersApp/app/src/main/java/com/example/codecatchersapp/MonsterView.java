/**
 The MonsterView class is a custom View class that displays a randomly generated monster.
 It extends the View class and overrides the onDraw method to draw the monster using the monsterFeatures class.
 @author [Josie Matalski]
 @version 1.0
 @since [Saturday March 4 2021]
 */
package com.example.codecatchersapp;

import android.content.Context;
import android.content.res.TypedArray;
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

    private String binaryHash;

    // Create a new monster
    public MonsterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MonsterView, 0, 0);
        try {
            binaryHash = a.getString(R.styleable.MonsterView_binaryHash);
        } finally {
            a.recycle();
        }
        this.binaryHash = binaryHash;
        // Set the background color to transparent
        setBackgroundColor(Color.TRANSPARENT);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
//        mCanvas = new Canvas(); // Initialize mCanvas
        monster = new monsterFeatures(context, paint);
        this.binaryHash = binaryHash;
    }

    public void setBinaryHash(String binaryHash) {
        this.binaryHash = binaryHash;
        System.out.println("binaryHashAGAIN: " + binaryHash);
        invalidate(); // redraw the view
    }

    // Called when the view is drawn on screen
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int monsterWidth = 1000;
        int monsterHeight = 1000;

        // Calculate the horizontal and vertical offsets
        float offsetX = (getWidth() - monsterWidth) / 2f;
        float offsetY = (getHeight() - monsterHeight) / 2f;

        // Apply the offsets
        canvas.save();
        canvas.translate(offsetX, offsetY);

        // Call the generateMonster method from the monsterFeatures class
        monster.generateMonster(canvas, binaryHash);

        canvas.restore();

    }

    public void setMonster(monsterFeatures monster) {
        this.monster = monster;
        invalidate(); // redraw the view
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 1000; // Set the width of MonsterView
        int desiredHeight = 1000; // Set the height of MonsterView

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }
}



