package com.example.lili.animationdemo.PointAnimation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * @author li.li
 * @Description:
 * @date 18-6-4
 * @copyright MIBC
 */
public class PointMovement extends View {
    private Paint mPaint;
    private Point mCurrentPoint;
    private String color;
    private final float RADIUS = 80.0f;
    private float width, height;

    public PointMovement(Context context, float width, float height) {
        super(context);
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
    }

    private void drawCirclePoint(Canvas canvas) {
        canvas.drawCircle(mCurrentPoint.pointX, mCurrentPoint.pointY, RADIUS, mPaint);
    }

    public void startAnim() {
        ValueAnimator valueAnimator = movementAnim();
        valueAnimator.setDuration(3000);
        valueAnimator.start();
    }

    private ValueAnimator movementAnim() {
        Point pointStart = new Point(width / 2, RADIUS);
        Point pointEnd = new Point(width / 2, height - RADIUS);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PositionEvaluator(), pointStart, pointEnd);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPoint = (Point) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        return valueAnimator;
    }

    private ValueAnimator colorAnim() {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ColorEvaluator(), "#00FFFF", "#FF00FF");
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                color = (String) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        return colorAnimator;
    }

    public void startSet() {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator moveAnimator = movementAnim();
        ValueAnimator colorAnimator = colorAnim();
        set.playTogether(moveAnimator, colorAnimator);
        set.setDuration(3000);
        set.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor(color));
        drawCirclePoint(canvas);
    }
}
