package com.light.niepaper.Customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.light.niepaper.R;

import java.util.ArrayList;

/**
 * Created by sayo on 5/31/2017.
 */

public class DrawingClassView extends View{
    private Path drawPath;

    private Paint canvasPaint;
    private String newColor;
    private Paint drawPaint;
    private Paint _paintBlur;
    private int paintColor = 0xFF660000 ;
    private Canvas drawCanvas;
    private float mX, mY;
    private Bitmap canvasBitmap;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    private boolean erase=false;
    //List<Pair<Path, Integer>> path_color_list = new ArrayList<Pair<Path,Integer>>();
    private float currentBrushSize, lastBrushSize;
    private static final float TOUCH_TOLERANCE = 4;

    private void init(){
        currentBrushSize = getResources().getInteger(R.integer.medium_size);

        lastBrushSize = currentBrushSize;

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(currentBrushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }
    public DrawingClassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(drawPath, drawPaint);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void eraseAll() {
        drawPath = new Path();
        paths.clear();
        drawCanvas.drawColor(Color.WHITE);
        invalidate();
    }
    private void touch_start(float x, float y) {
        undonePaths.clear();
        drawPath.reset();
        drawPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            drawPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            drawPath.lineTo(mX, mY);
            // commit the path to our offscreen
            drawCanvas.drawPath(drawPath, drawPaint);
            drawPath.reset();
            drawPath.moveTo(mX, mY);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
          drawPath.reset();
    }

    public void onClickUndo () {
        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }

    }

    public void onClickRedo (){
        if (undonePaths.size()>0)
        {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }

    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        currentBrushSize = pixelAmount;
        drawPaint.setStrokeWidth(currentBrushSize);
    }

    public void setLastBrushSize(float lastBrushSize){
        this.lastBrushSize= lastBrushSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }
    public void setColorx(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setErase(boolean isErase) {
        erase = isErase;
        if (erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint.setXfermode(null);
        }
        invalidate();
    }
    public void redraw(Bitmap bitmap){
        setBackgroundDrawable(new BitmapDrawable(bitmap));
    }
}

