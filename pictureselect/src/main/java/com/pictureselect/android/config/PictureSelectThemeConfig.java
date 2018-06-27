package com.pictureselect.android;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;

/**
 * 创建时间： 0027/2018/6/27 下午 5:03
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择器主题配置
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：1、标题栏标题颜色
 *      2、主题颜色
 *      3、标题栏取消文字颜色
 *      4、标题栏确认文字颜色
 *      5、标题栏标题文字大小
 *      6、标题栏取消文字大小
 *      7、标题栏确认文字大小
 *      8、标题栏高度
 *      9、原图文字颜色
 *      10、原图文字大小
 *      11、原图文字大小
 *      12、预览文字颜色
 *      13、预览文字大小
 *      14、选中文字颜色
 *      15、选中文字大小
 *      16、标题栏内部控件高度
 */
public class PictureSelectThemeConfig {
    private static PictureSelectThemeConfig pictureVideoSelectConfirg;
    public static PictureSelectThemeConfig getInstance(){
        if(pictureVideoSelectConfirg == null){
            pictureVideoSelectConfirg = new PictureSelectThemeConfig();
        }
        return pictureVideoSelectConfirg;
    }

    private int acBarTitleColor = Color.WHITE;//标题栏标题颜色
    private int themeColor = Color.GRAY;//主题颜色
    private int acBarCancelColor = Color.WHITE;//标题栏取消文字颜色
    private int acBarConfirmColor = Color.WHITE;//标题栏确认文字颜色
    private int acBarTitleSize = 15;//标题栏标题文字大小
    private int acBarCancelSize = 15;//标题栏取消文字大小
    private int acBarConfirmSize = 15;//标题栏确认文字大小
    private int acBarHeight = 44;//标题栏高度
    private int originPictureTextColor = Color.WHITE;//原图文字颜色
    private int originPictureTextSize = 15;//原图文字大小
    private int previewTextColor = Color.WHITE;//预览文字颜色
    private int previewTextSize = 15;//预览文字大小
    private int selectedTextColor = Color.WHITE;//选中文字颜色
    private int selectedTextSize = 15;//选中文字大小
    private int acBarContentViewHeight = 44;//标题栏内部控件高度


    public int getAcBarTitleColor() {
        return acBarTitleColor;
    }

    public PictureSelectThemeConfig setAcBarTitleColor(@ColorRes int acBarTitleColor) {
        this.acBarTitleColor = acBarTitleColor;
        return this;
    }

    public int getThemeColor() {
        return themeColor;
    }

    public PictureSelectThemeConfig setThemeColor(@ColorInt int themeColor) {
        this.themeColor = themeColor;
        return this;
    }

    public int getAcBarCancelColor() {
        return acBarCancelColor;
    }

    public PictureSelectThemeConfig setAcBarCancelColor(@ColorRes int acBarCancelColor) {
        this.acBarCancelColor = acBarCancelColor;
        return this;
    }

    public int getAcBarConfirmColor() {
        return acBarConfirmColor;
    }

    public PictureSelectThemeConfig setAcBarConfirmColor(@ColorRes int acBarConfirmColor) {
        this.acBarConfirmColor = acBarConfirmColor;
        return this;
    }

    public int getAcBarTitleSize() {
        return acBarTitleSize;
    }

    public PictureSelectThemeConfig setAcBarTitleSize(@DimenRes int acBarTitleSize) {
        this.acBarTitleSize = acBarTitleSize;
        return this;
    }

    public int getAcBarCancelSize() {
        return acBarCancelSize;
    }

    public PictureSelectThemeConfig setAcBarCancelSize(@DimenRes int acBarCancelSize) {
        this.acBarCancelSize = acBarCancelSize;
        return this;
    }

    public int getAcBarConfirmSize() {
        return acBarConfirmSize;
    }

    public PictureSelectThemeConfig setAcBarConfirmSize(@DimenRes int acBarConfirmSize) {
        this.acBarConfirmSize = acBarConfirmSize;
        return this;
    }

    public int getAcBarHeight() {
        return acBarHeight;
    }

    public PictureSelectThemeConfig setAcBarHeight(int acBarHeight) {
        this.acBarHeight = acBarHeight;
        return this;
    }

    public int getOriginPictureTextColor() {
        return originPictureTextColor;
    }

    public PictureSelectThemeConfig setOriginPictureTextColor(@ColorRes int originPictureTextColor) {
        this.originPictureTextColor = originPictureTextColor;
        return this;
    }

    public int getOriginPictureTextSize() {
        return originPictureTextSize;
    }

    public PictureSelectThemeConfig setOriginPictureTextSize(@DimenRes int originPictureTextSize) {
        this.originPictureTextSize = originPictureTextSize;
        return this;
    }

    public int getPreviewTextColor() {
        return previewTextColor;
    }

    public PictureSelectThemeConfig setPreviewTextColor(@ColorRes int previewTextColor) {
        this.previewTextColor = previewTextColor;
        return this;
    }

    public int getPreviewTextSize() {
        return previewTextSize;
    }

    public PictureSelectThemeConfig setPreviewTextSize(@DimenRes int previewTextSize) {
        this.previewTextSize = previewTextSize;
        return this;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public PictureSelectThemeConfig setSelectedTextColor(@DimenRes int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        return this;
    }

    public int getSelectedTextSize() {
        return selectedTextSize;
    }

    public PictureSelectThemeConfig setSelectedTextSize(@DimenRes int selectedTextSize) {
        this.selectedTextSize = selectedTextSize;
        return this;
    }

    public int getAcBarContentViewHeight() {
        return acBarContentViewHeight;
    }

    public PictureSelectThemeConfig setAcBarContentViewHeight(int acBarContentViewHeight) {
        this.acBarContentViewHeight = acBarContentViewHeight;
        return this;
    }
}
