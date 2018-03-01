package com.basepictureoptionslib.android.utils;

import com.basepictureoptionslib.android.AppCommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParamsAndJudgeUtils {
    /**
     * 数组转集合
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> List<T> paramesArrayToList(T[] arrays){
        List<T> list = new ArrayList<>();
        if(arrays != null) {
            list.addAll(Arrays.asList(arrays));
        }
        return list;
    }
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static  int px2dip(float pxValue) {
        final float scale = AppCommon.APPLICATION_CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static  int dip2px(float dipValue) {
        final float scale = AppCommon.APPLICATION_CONTEXT.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static  int px2sp(float pxValue) {
        final float fontScale = AppCommon.APPLICATION_CONTEXT.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static  int sp2px(float spValue) {
        final float fontScale = AppCommon.APPLICATION_CONTEXT.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
