package com.example.lili.animationdemo.PointAnimation;

import android.view.animation.Interpolator;

/**
 * @author li.li
 * @Description:动画变化的速度
 * @date 18-6-5
 * @copyright MIBC
 */
public class PointInterpolator implements Interpolator {
    /**
     * @param input 根据设定的动画时长匀速增加,范围在0和1之间
     * @return 返回值就是fraction，一般在0-1之间，不过，这个返回值可能存在小于0和大于1的情况，这是为了实现越界反弹等动画效果
     */
    @Override
    public float getInterpolation(float input) {
        float value;
        value = (float) Math.sin(Math.PI * input / 2);
        /*if (input <0.5){
            value = (float) (Math.sin(Math.PI * input)/2);
        }else{
            value = (float) (1-(Math.sin(Math.PI * input)/2));
        }*/
        return value;
    }
}
