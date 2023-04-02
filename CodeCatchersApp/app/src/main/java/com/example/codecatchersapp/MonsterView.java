/**
 The MonsterView class is a custom View class that displays a randomly generated monster.
 It extends the View class and overrides the onDraw method to draw the monster using the monsterFeatures class.
 @author [Josie Matalski]
 @version 1.0
 @since [Saturday March 4 2021]
 */

package com.example.codecatchersapp;                                     // Package name

import android.content.Context;                                          // Import the Context class
import android.content.res.TypedArray;                                   // Import the TypedArray class
import android.graphics.Canvas;                                          // Import the Canvas class
import android.graphics.Color;                                           // Import the Color class
import android.graphics.Paint;                                           // Import the Paint class
import android.util.AttributeSet;                                        // Import the AttributeSet class
import android.view.View;                                                // Import the View class

public class MonsterView extends View {                                  // MonsterView class
    /**
     * The paint object is used to draw the monster.
     * The monster object is used to generate the monster.
     * The binaryHash is used to generate the monster.
     */
    private Paint paint;                                                 // Paint object
    private monsterFeatures monster;                                     // Monster object
    private String binaryHash;                                           // Binary hash value
    /**
     * The MonsterView constructor is used to initialize the MonsterView object.
     * @param context Context of the views creation
     * @param attrs Attributes of the view
     */

    public MonsterView(Context context, AttributeSet attrs) {             // Constructor
        super(context, attrs);                                            // Call the super class constructor

                                                                          // Get the binaryHash value from the attributes

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MonsterView, 0, 0);
        try {
            binaryHash = a.getString(R.styleable.MonsterView_binaryHash); // Get the binaryHash value
        } finally {                                                       // Ensure that the TypedArray object is recycled
            a.recycle();                                                  // Recycle the TypedArray, to be re-used by a later caller
        }
        setBackgroundColor(Color.TRANSPARENT);                            // Set the background color to be transparent
        paint = new Paint();                                              // Create a new paint object
        paint.setStyle(Paint.Style.FILL);                                 // Set the paint style to be fill

        monster = new monsterFeatures(context, paint);                    // Create a new monsterFeatures object

        this.binaryHash = binaryHash;                                     // Set the binaryHash value
    }
    /**
     * Sets a new binary hash value and redraws the view.
     * @param binaryHash The new binaryHash value is set.
     */
    public void setBinaryHash(String binaryHash) {
        this.binaryHash = binaryHash;                                     // Set the binaryHash value
        System.out.println("binaryHashAGAIN: " + binaryHash);             //TODO: remove
        invalidate();                                                     // redraw the view
    }
    /**
     * The onDraw method is used to draw the monster on the canvas. Using the monsterFeatures class.
     * @param canvas the canvas on which the background will be drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);                                // Call the super class onDraw method

        int monsterWidth = 1000;                             // Set the width of the monster
        int monsterHeight = 1000;                            // Set the height of the monster

        float scaleX = (float) getWidth() / monsterWidth;    // Calculate the horizontal scale
        float scaleY = (float) getHeight() / monsterHeight;  // Calculate the vertical scale

        canvas.save();                                       // Save the canvas state
        canvas.scale(scaleX, scaleY);                        // Scale the canvas

        monster.generateMonster(canvas, binaryHash);         // Generate the monster

        canvas.restore();                                    // Restore the canvas state
    }
    /**
     * Sets a new monster (monsterFeatures) object and redraws the view.
     * @param monster The new monster (monsterFeatures) object is set.
     */
    public void setMonster(monsterFeatures monster) {
        this.monster = monster;                             // Set the monster object
        invalidate();                                       // redraw the view
    }

    /**
     * The onMeasure method is used to set the size of the MonsterView.
     * It Measures the desired size of the view and determines the actual size based on the MeasureSpec parameters.
     *
     * @param widthMeasureSpec The width of the MonsterView, horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec The height of the MonsterView, vertical space requirements as imposed by the parent.
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 1000;                                 // Set the width of MonsterView
        int desiredHeight = 1000;                                // Set the height of MonsterView

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   // Get the width mode
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   // Get the width size
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); // Get the height mode
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); // Get the height size

        int width; // Set the width
        int height; // Set the height
        if (widthMode == MeasureSpec.EXACTLY) {         // If the width mode is exactly
            width = widthSize;                          // Set the width to the width size
        } else if (widthMode == MeasureSpec.AT_MOST) {  // If the width mode is at most
            width = Math.min(desiredWidth, widthSize);  // Set the width to the minimum of the desired width and the width size
        } else {                                        // If the width mode is unspecified
            width = desiredWidth;                       // Set the width to the desired width
        }

        if (heightMode == MeasureSpec.EXACTLY) {          // If the height mode is exactly
            height = heightSize;                          // Set the height to the height size
        } else if (heightMode == MeasureSpec.AT_MOST) {   // If the height mode is at most
            height = Math.min(desiredHeight, heightSize); // Set the height to the minimum of the desired height and the height size
        } else {                                          // If the height mode is unspecified
            height = desiredHeight;                       // Set the height to the desired height
        }

        float aspectRatio = (float) desiredWidth / desiredHeight; // Get the aspect ratio for the MonsterView
        if (width < height * aspectRatio) {                       // If the width is less than the height with the aspect ratio taken into account
            height = (int) (width / aspectRatio);                 // Set the height to the width divided by the aspect ratio
        } else {
            width = (int) (height * aspectRatio);                 // Otherwise set the width to the height multiplied by the aspect ratio
        }
        setMeasuredDimension(width, height);              // Set the measured dimension
    }
}



