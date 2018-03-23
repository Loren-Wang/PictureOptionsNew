package com.basepictureoptionslib.android.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static  int dip2px(Context context,float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取当前时间的毫秒值
     * @return
     */
    public static Long getMillisecond(){
        return new Date().getTime();
    }
    /**
     * 获取当前时间的秒值
     * @return
     */
    public static Long getSecond(){
        return new Date().getTime() / 1000;
    }
}
