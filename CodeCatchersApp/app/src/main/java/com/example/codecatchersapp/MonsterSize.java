package com.example.codecatchersapp;

import android.content.Context;
import android.util.AttributeSet;

public class MonsterSize extends ViewMonster {

    public MonsterSize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set the size of the view
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
    }
}
