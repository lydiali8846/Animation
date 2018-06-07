package com.example.lili.animationdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author li.li
 * @Description:
 * @date 18-6-1
 * @copyright MIBC
 */
public class HandwritingView extends View {
    private Paint mLinePaint;
    private Path mPath;
    private float mControlPointX, mControlPointY;  //控制点

    public HandwritingView(Context context) {
        super(context);
        initPaint();
    }

    public HandwritingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public HandwritingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(4);
        mLinePaint.setColor(Color.BLUE);
        mPath = new Path();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                mControlPointX = event.getX();
                mControlPointY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                //画线的方式很容易看见一折一折的效果，线段的连续不婉转
                //mPath.lineTo(event.getX(),event.getY());    //画直线，但线段不平滑
                drawWithBezier(event);
                invalidate();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    //三点中将1和2的中点设成bezier的起点，2和3的中点设成bezier的终点，点2是控制点，则能画出一条曲线
    //但点1到起点的部分和3到终点的部分会消失。但touch画线的过程，两点之间的距离不会很长，因此可省略
    private void drawWithBezier(MotionEvent event) {
        float midPointX = (mControlPointX + event.getX()) / 2;
        float minPointY = (mControlPointY + event.getY()) / 2;
        mPath.quadTo(mControlPointX, mControlPointY, midPointX, minPointY);
        mControlPointX = event.getX();
        mControlPointY = event.getY();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mLinePaint);
    }

    public void reset() {
        mPath.reset();
        invalidate();
    }
}
