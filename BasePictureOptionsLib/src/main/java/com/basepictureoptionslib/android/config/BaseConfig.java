package com.basepictureoptionslib.android.config;

import android.os.Parcelable;

/**
 * 创建时间： 0026/2018/2/26 下午 2:23
 * 创建人：王亮（Loren wang）
 * 功能作用：基础配置类，是一个抽象类，需要各个功能模块去实现该类，其主要作用是在调用相册选择、自定义相机、图片裁剪的时
 *         候传入相应参数，例如图片选择的最大张数，主题等等
 * 功能方法：1、设置标题栏高度
 *         2、设置标题栏颜色
 *         3、标题颜色
 *         4、标题文字
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public abstract class BaseConfig implements Parcelable{
    protected int aBarHeight = 0;//标题栏高度
    protected int aBarColor = 0;//标题栏颜色
    protected int titleColor = 0;//标题颜色
    protected String titleText;//标题文字


    public int getaBarHeight() {
        return aBarHeight;
    }

    public BaseConfig setaBarHeight(int aBarHeight) {
        this.aBarHeight = aBarHeight;
        return this;
    }

    public int getaBarColor() {
        return aBarColor;
    }

    public BaseConfig setaBarColor(int aBarColor) {
        this.aBarColor = aBarColor;
        return this;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public BaseConfig setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public String getTitleText() {
        return titleText;
    }

    public BaseConfig setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }
}
