package com.light.niepaper.Customviews;

/**
 * Created by sayo on 4/26/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;



public class LinedEditTextView extends android.support.v7.widget.AppCompatEditText{

    private Rect rect;
    private Paint paint;


    public LinedEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#e5e500"));
    }

    public void setLineColor(int color) {
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = height / lineHeight;
        if (getLineCount() > count) {
            count = getLineCount();
        }
        int baseline = getLineBounds(0, rect);
        for (int i = 0; i < count; i++) {
            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}
