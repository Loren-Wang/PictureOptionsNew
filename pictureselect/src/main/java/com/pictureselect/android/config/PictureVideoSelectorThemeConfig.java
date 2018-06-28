package com.pictureselect.android.config;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;

import com.pictureselect.android.R;

import java.util.ArrayList;

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
 *      R.dimen.base_text_size、选中文字大小
 *      16、标题栏内部控件高度
 *      17、底部操作栏高度
 *      18、图片视频选中状态图标
 *      19、图片视频非选中状态图标
 *      20、图片视频选择的时候显示的列数
 *      21、指定扫描文件夹（将扫描做到配置当中）
 *      22、主题id
 */
public class PictureVideoSelectorThemeConfig {
    private static PictureVideoSelectorThemeConfig pictureVideoSelectConfirg;
    public static PictureVideoSelectorThemeConfig getInstance(){
        if(pictureVideoSelectConfirg == null){
            pictureVideoSelectConfirg = new PictureVideoSelectorThemeConfig();
        }
        return pictureVideoSelectConfirg;
    }

    @ColorRes
    private int acBarTitleColor = R.color.white;//标题栏标题颜色
    @ColorRes
    private int themeColor = R.color.gary;//主题颜色
    @ColorRes
    private int acBarCancelColor = R.color.white;//标题栏取消文字颜色
    @ColorRes
    private int acBarConfirmColor = R.color.white;//标题栏确认文字颜色
    @DimenRes
    private int acBarTitleSize = R.dimen.base_text_size;//标题栏标题文字大小
    @DimenRes
    private int acBarCancelSize = R.dimen.base_text_size;//标题栏取消文字大小
    @DimenRes
    private int acBarConfirmSize = R.dimen.base_text_size;//标题栏确认文字大小
    @DimenRes
    private int acBarHeight = R.dimen.base_height;//标题栏高度
    @ColorRes
    private int originPictureTextColor = R.color.white;//原图文字颜色
    @DimenRes
    private int originPictureTextSize = R.dimen.base_text_size;//原图文字大小
    @ColorRes
    private int previewTextColor = R.color.white;//预览文字颜色
    @DimenRes
    private int previewTextSize = R.dimen.base_text_size;//预览文字大小
    @ColorRes
    private int selectedTextColor = R.color.white;//选中文字颜色
    @DimenRes
    private int selectedTextSize = R.dimen.base_text_size;//选中文字大小
    @DimenRes
    private int acBarContentViewHeight = R.dimen.base_height;//标题栏内部控件高度
    @DimenRes
    private int bottomOptionsHeight = R.dimen.base_height;//底部操作栏高度
    @DrawableRes
    private Integer selectStateY = null;//选中状态图标
    @DrawableRes
    private Integer selectStateN = null;//非选中状态图标
    @StyleRes
    private int themeId = R.style.AppTheme_NoActionBar;//主题id
    private int showRowCount = 3;//显示的列数
    private ArrayList<String> scanDirList = new ArrayList<>();//  指定扫描文件夹（将扫描做到配置当中）

    public int getAcBarTitleColor() {
        return acBarTitleColor;
    }

    public PictureVideoSelectorThemeConfig setAcBarTitleColor(@ColorRes int acBarTitleColor) {
        this.acBarTitleColor = acBarTitleColor;
        return this;
    }

    public int getThemeColor() {
        return themeColor;
    }

    public PictureVideoSelectorThemeConfig setThemeColor(@ColorRes int themeColor) {
        this.themeColor = themeColor;
        return this;
    }

    public int getAcBarCancelColor() {
        return acBarCancelColor;
    }

    public PictureVideoSelectorThemeConfig setAcBarCancelColor(@ColorRes int acBarCancelColor) {
        this.acBarCancelColor = acBarCancelColor;
        return this;
    }

    public int getAcBarConfirmColor() {
        return acBarConfirmColor;
    }

    public PictureVideoSelectorThemeConfig setAcBarConfirmColor(@ColorRes int acBarConfirmColor) {
        this.acBarConfirmColor = acBarConfirmColor;
        return this;
    }

    public int getAcBarTitleSize() {
        return acBarTitleSize;
    }

    public PictureVideoSelectorThemeConfig setAcBarTitleSize(@DimenRes int acBarTitleSize) {
        this.acBarTitleSize = acBarTitleSize;
        return this;
    }

    public int getAcBarCancelSize() {
        return acBarCancelSize;
    }

    public PictureVideoSelectorThemeConfig setAcBarCancelSize(@DimenRes int acBarCancelSize) {
        this.acBarCancelSize = acBarCancelSize;
        return this;
    }

    public int getAcBarConfirmSize() {
        return acBarConfirmSize;
    }

    public PictureVideoSelectorThemeConfig setAcBarConfirmSize(@DimenRes int acBarConfirmSize) {
        this.acBarConfirmSize = acBarConfirmSize;
        return this;
    }

    public int getAcBarHeight() {
        return acBarHeight;
    }

    public PictureVideoSelectorThemeConfig setAcBarHeight(@DimenRes int acBarHeight) {
        this.acBarHeight = acBarHeight;
        return this;
    }

    public int getOriginPictureTextColor() {
        return originPictureTextColor;
    }

    public PictureVideoSelectorThemeConfig setOriginPictureTextColor(@ColorRes int originPictureTextColor) {
        this.originPictureTextColor = originPictureTextColor;
        return this;
    }

    public int getOriginPictureTextSize() {
        return originPictureTextSize;
    }

    public PictureVideoSelectorThemeConfig setOriginPictureTextSize(@DimenRes int originPictureTextSize) {
        this.originPictureTextSize = originPictureTextSize;
        return this;
    }

    public int getPreviewTextColor() {
        return previewTextColor;
    }

    public PictureVideoSelectorThemeConfig setPreviewTextColor(@ColorRes int previewTextColor) {
        this.previewTextColor = previewTextColor;
        return this;
    }

    public int getPreviewTextSize() {
        return previewTextSize;
    }

    public PictureVideoSelectorThemeConfig setPreviewTextSize(@DimenRes int previewTextSize) {
        this.previewTextSize = previewTextSize;
        return this;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public PictureVideoSelectorThemeConfig setSelectedTextColor(@ColorRes int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        return this;
    }

    public int getSelectedTextSize() {
        return selectedTextSize;
    }

    public PictureVideoSelectorThemeConfig setSelectedTextSize(@DimenRes int selectedTextSize) {
        this.selectedTextSize = selectedTextSize;
        return this;
    }

    public int getAcBarContentViewHeight() {
        return acBarContentViewHeight;
    }

    public PictureVideoSelectorThemeConfig setAcBarContentViewHeight(@DimenRes int acBarContentViewHeight) {
        this.acBarContentViewHeight = acBarContentViewHeight;
        return this;
    }

    public int getBottomOptionsHeight() {
        return bottomOptionsHeight;
    }

    public PictureVideoSelectorThemeConfig setBottomOptionsHeight(@DimenRes int bottomOptionsHeight) {
        this.bottomOptionsHeight = bottomOptionsHeight;
        return this;
    }

    public Integer getSelectStateY() {
        return selectStateY;
    }

    public PictureVideoSelectorThemeConfig setSelectStateY(@DrawableRes Integer selectStateY) {
        this.selectStateY = selectStateY;
        return this;
    }

    public Integer getSelectStateN() {
        return selectStateN;
    }

    public PictureVideoSelectorThemeConfig setSelectStateN(@DrawableRes Integer selectStateN) {
        this.selectStateN = selectStateN;
        return this;
    }

    public int getShowRowCount() {
        return showRowCount;
    }

    public PictureVideoSelectorThemeConfig setShowRowCount(int showRowCount) {
        this.showRowCount = showRowCount;
        return this;
    }

    public ArrayList<String> getScanDirList() {
        return scanDirList;
    }

    public PictureVideoSelectorThemeConfig setScanDirList(ArrayList<String> scanDirList) {
        this.scanDirList = scanDirList;
        return this;
    }

    @StyleRes
    public int getThemeId() {
        return themeId;
    }

    public PictureVideoSelectorThemeConfig setThemeId(@StyleRes int themeId) {
        this.themeId = themeId;
        return this;
    }
}
