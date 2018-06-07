package com.example.lili.animationdemo.PointAnimation;

import android.animation.TypeEvaluator;

/**
 * @author li.li
 * @Description:位置估值器
 * @date 18-6-4
 * @copyright MIBC
 */
public class PositionEvaluator implements TypeEvaluator {
    private Point currentPoint;

    /**
     * @return start+fraction(系数)×(end-start)
     * 加减速的依据是fraction的变化速率，当前一个fraction比后一个fraction大时，运动方向反向
     */
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point pointStart = (Point) startValue;
        Point pointEnd = (Point) endValue;
        float currentX = pointStart.pointX + fraction * (pointEnd.pointX - pointStart.pointX);
        float currentY = pointStart.pointY + fraction * (pointEnd.pointY - pointStart.pointY);
        if (currentPoint == null) {
            currentPoint = new Point(currentX, currentY);
        } else {
            currentPoint.pointX = currentX;
            currentPoint.pointY = currentY;
        }
        return currentPoint;
    }
}
