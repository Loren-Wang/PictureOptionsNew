package com.pictureselect.android;

import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;

import com.basepictureoptionslib.android.config.BaseConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间： 0026/2018/2/26 下午 3:10
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择配置类，例如最大张数等等
 * 功能方法：1、最大选择张数
 *         2、是否需要预览
 *         3、是否需要原图选择
 *         4、已选择列表
 *         5、显示列数
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class PictureSelectConfirg extends BaseConfig{
    private int maxSelectNum = 9;//最大选择张数
    private int showRowCount = 3;//显示的列数
    private List<String> selectedPicList = new ArrayList<>();//已选择图片列表
    private boolean isShowPreview = true;//是否需要预览（即是否显示预览按钮以及点击图片预览），默认不需要
    private boolean isShowOriginPicSelect = false;//是否需要显示原图选择
    @ColorRes
    private int bottomOptionsBackground;//底部操作栏背景颜色
    @DimenRes
    private int bottomOptionsHeight;//底部操作栏高度
    @ColorRes
    private int bottomOptionsTextColor;//底部操作栏文字颜色
    @DimenRes
    private int bottomOptionsTextSize;//底部操作栏文字大小

    public PictureSelectConfirg() {
        super();
        bottomOptionsBackground = R.color.yudao_primary;
        bottomOptionsTextColor = Color.WHITE;
        bottomOptionsHeight = 44;
        bottomOptionsTextSize = 14;
    }


    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public PictureSelectConfirg setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
        return this;
    }

    public boolean isShowPreview() {
        return isShowPreview;
    }

    public PictureSelectConfirg setShowPreview(boolean showPreview) {
        isShowPreview = showPreview;
        return this;
    }

    public boolean isShowOriginPicSelect() {
        return isShowOriginPicSelect;
    }

    public PictureSelectConfirg setShowOriginPicSelect(boolean showOriginPicSelect) {
        isShowOriginPicSelect = showOriginPicSelect;
        return this;
    }

    public List<String> getSelectedPicList() {
        return selectedPicList;
    }

    public PictureSelectConfirg setSelectedPicList(List<String> selectedPicList) {
        this.selectedPicList = selectedPicList;
        return this;
    }

    public int getShowRowCount() {
        return showRowCount;
    }

    public PictureSelectConfirg setShowRowCount(int showRowCount) {
        this.showRowCount = showRowCount;
        return this;
    }


    public int getBottomOptionsBackground() {
        return bottomOptionsBackground;
    }

    public PictureSelectConfirg setBottomOptionsBackground(@ColorRes int bottomOptionsBackground) {
        this.bottomOptionsBackground = bottomOptionsBackground;
        return this;
    }

    public int getBottomOptionsHeight() {
        return bottomOptionsHeight;
    }

    public PictureSelectConfirg setBottomOptionsHeight(@DimenRes int bottomOptionsHeight) {
        this.bottomOptionsHeight = bottomOptionsHeight;
        return this;
    }

    public int getBottomOptionsTextColor() {
        return bottomOptionsTextColor;
    }

    public PictureSelectConfirg setBottomOptionsTextColor(@ColorRes int bottomOptionsTextColor) {
        this.bottomOptionsTextColor = bottomOptionsTextColor;
        return this;
    }

    public int getBottomOptionsTextSize() {
        return bottomOptionsTextSize;
    }

    public PictureSelectConfirg setBottomOptionsTextSize(@DimenRes int bottomOptionsTextSize) {
        this.bottomOptionsTextSize = bottomOptionsTextSize;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxSelectNum);
        dest.writeInt(this.showRowCount);
        dest.writeStringList(this.selectedPicList);
        dest.writeByte(this.isShowPreview ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowOriginPicSelect ? (byte) 1 : (byte) 0);
        dest.writeInt(this.bottomOptionsBackground);
        dest.writeInt(this.bottomOptionsHeight);
        dest.writeInt(this.bottomOptionsTextColor);
        dest.writeInt(this.bottomOptionsTextSize);
        dest.writeInt(this.aBarHeight);
        dest.writeInt(this.aBarColor);
        dest.writeInt(this.titleColor);
        dest.writeString(this.titleText);
        dest.writeInt(this.aBarTextColor);
        dest.writeInt(this.aBarTextSize);
        dest.writeInt(this.themeId);
    }

    protected PictureSelectConfirg(Parcel in) {
        this.maxSelectNum = in.readInt();
        this.showRowCount = in.readInt();
        this.selectedPicList = in.createStringArrayList();
        this.isShowPreview = in.readByte() != 0;
        this.isShowOriginPicSelect = in.readByte() != 0;
        this.bottomOptionsBackground = in.readInt();
        this.bottomOptionsHeight = in.readInt();
        this.bottomOptionsTextColor = in.readInt();
        this.bottomOptionsTextSize = in.readInt();
        this.aBarHeight = in.readInt();
        this.aBarColor = in.readInt();
        this.titleColor = in.readInt();
        this.titleText = in.readString();
        this.aBarTextColor = in.readInt();
        this.aBarTextSize = in.readInt();
        this.themeId = in.readInt();
    }

    public static final Creator<PictureSelectConfirg> CREATOR = new Creator<PictureSelectConfirg>() {
        @Override
        public PictureSelectConfirg createFromParcel(Parcel source) {
            return new PictureSelectConfirg(source);
        }

        @Override
        public PictureSelectConfirg[] newArray(int size) {
            return new PictureSelectConfirg[size];
        }
    };
}
