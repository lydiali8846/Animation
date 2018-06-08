package com.example.lili.animationdemo.DragView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

/**
 * @author li.li
 * @Description:
 * @date 18-6-8
 * @copyright MIBC
 */
public class SystemUtil {

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

}
