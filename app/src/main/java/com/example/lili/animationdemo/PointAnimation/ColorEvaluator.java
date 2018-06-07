package com.example.lili.animationdemo.PointAnimation;

import android.animation.TypeEvaluator;

/**
 * @author li.li
 * @Description:
 * @date 18-6-4
 * @copyright MIBC
 */
public class ColorEvaluator implements TypeEvaluator {
    private int mCurrentRed = -1;
    private int mCurrentGreen = -1;
    private int mCurrentBlue = -1;

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        String colorStart = (String) startValue;
        String colorEnd = (String) endValue;
        int colorStartRed = Integer.parseInt(colorStart.substring(1, 3), 16);
        int colorStartGreen = Integer.parseInt(colorStart.substring(3, 5), 16);
        int colorStartBlue = Integer.parseInt(colorStart.substring(5, 7), 16);
        int colorEndRed = Integer.parseInt(colorEnd.substring(1, 3), 16);
        int colorEndGreen = Integer.parseInt(colorEnd.substring(3, 5), 16);
        int colorEndBlue = Integer.parseInt(colorEnd.substring(5, 7), 16);

        mCurrentRed = getCurrentColor(fraction, colorStartRed, colorEndRed);
        mCurrentGreen = getCurrentColor(fraction, colorStartGreen, colorEndGreen);
        mCurrentBlue = getCurrentColor(fraction, colorStartBlue, colorEndBlue);

        String currentColor = "#" + getHexString(mCurrentRed)
                + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);

        return currentColor;
    }

    private int getCurrentColor(float fraction, int startValue, int endValue) {
        int currentColor = (int) (startValue + fraction * (endValue - startValue));
        return currentColor;
    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    private String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
