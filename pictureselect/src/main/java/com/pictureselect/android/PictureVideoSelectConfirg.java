package com.pictureselect.android;

import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;

import com.basepictureoptionslib.android.config.BaseConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间： 0026/2018/2/26 下午 3:10
 * 创建人：王亮（Loren wang）
 * 功能作用：图片视频选择配置类，例如最大张数等等
 * 功能方法：1、最大选择张数
 *         2、是否需要预览
 *         3、是否需要原图选择
 *         4、已选择列表
 *         5、显示列数
 *         6、选择类型 0-仅图片，1-仅视频，2-图片视频一起选择
// *         7、视频选择的最长时间
// *         8、视频选择的最短时间
 *         9、选中状态图片
 *         10、非选中状态图片
 *         11、是否允许图片视频同时选择
 *         12、设置图片筛选类型（模式，可变参数）
 *         13、设置视频筛选类型（模式，可变参数）
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class PictureVideoSelectConfirg extends BaseConfig{
    public static final int SELECT_TYPE_FOR_PICTURE = 0;//仅选择图片
    public static final int SELECT_TYPE_FOR_VIDEO = 1;//仅选择视频
    public static final int SELECT_TYPE_FOR_PICTURE_AND_VIDEO = 2;//图片视频一起选择

    //图片的与FILTER_PICTURE_FOR_NONE为0
    public static final int FILTER_PICTURE_FOR_NONE = 0x0001;//筛选图片无限制
    public static final int FILTER_PICTURE_FOR_SIZE = 0x0010;//根据图片大小限制图片选择
    public static final int FILTER_PICTURE_FOR_WIDTH_HEIGHT = 0x0100;//根据图片宽高分辨率限制图片选择
    public static final int FILTER_PICTURE_FOR_SIZE_AND_WIDTH_HEIGHT = 0x1000;//根据图片大小以及宽高分辨率限制图片选择
    //视频的与FILTER_VIDEO_FOR_NONE为0
    public static final int FILTER_VIDEO_FOR_NONE = 0x11111110;//筛选视频无限制
    public static final int FILTER_VIDEO_FOR_SIZE = 0x11111101;//根据视频大小限制选择
    public static final int FILTER_VIDEO_FOR_DURATION = 0x11111011;//根据视频时长限制选择
    public static final int FILTER_VIDEO_FOR_WIDTH_HEIGHT = 0x11110111;//根据视频宽高分辨率限制选择
    public static final int FILTER_VIDEO_FOR_SIZE_AND_DURATION = 0x11101111;//根据视频大小和时长限制选择
    public static final int FILTER_VIDEO_FOR_SIZE_AND_WIDTH_HEIGHT = 0x11011111;//根据视频大小和宽高分辨率限制选择
    public static final int FILTER_VIDEO_FOR_DURATION_AND_WIDTH_HEIGHT = 0x10111111;//根据视频时长和宽高分辨率限制选择
    public static final int FILTER_VIDEO_FOR_SIZE_AND_DURATION_AND_WIDTH_HEIGHT = 0x01111111;//根据视频大小、时长和宽高分辨率限制选择

    private int maxSelectPictureNum = 9;//最大选择张数
    private int maxSelectVideoNum = 1;//最大选择视频的数量
    private int showRowCount = 3;//显示的列数
    private List<String> selectedPicList = new ArrayList<>();//已选择图片列表
    private boolean isShowPreview = true;//是否需要预览（即是否显示预览按钮以及点击图片预览），默认不需要
    private boolean isShowOriginPicSelect = false;//是否需要显示原图选择
    private boolean isAllowConcurrentSelection = false;//是否允许图片视频同时选择
    private int selectType = SELECT_TYPE_FOR_PICTURE_AND_VIDEO;//选择类型
    private int filterPictureType = FILTER_PICTURE_FOR_NONE;//图片筛选模式类型
    private Long pictureMinSize;//图片最小大小
    private Long pictureMaxSize;//图片最大大小
    private Integer pictureMinWidth;//图片选择最低宽度
    private Integer pictureMinHeight;//图片选择最低高度
    private Integer pictureMaxWidth;//图片选择最高宽度
    private Integer pictureMaxHeight;//图片选择最高高度
    private boolean pictureNoSizeIsResult = true;//未获取到图片大小的时候是否要返回该图片
    private boolean pictureNoWidthHeightIsResult = true;//未获取到图片宽高分辨率的时候是否要返回该图片
    private int filterVideoType = FILTER_VIDEO_FOR_NONE;//视频筛选模式类型
    private Long videoMinSize;//视频最小大小
    private Long videoMaxSize;//视频最大大小
    private Integer videoMinWidth;//视频最小分辨率宽度
    private Integer videoMinHeight;//视频最小分辨率高度
    private Integer videoMaxWidth;//视频最大分辨率高度
    private Integer videoMaxHeight;//视频最大分辨率高度
    private Long videoMinDuration;//视频最小大小
    private Long videoMaxDuration;//视频最大大小
    private boolean videoNoSizeIsResult = true;//未获取到视频大小的时候是否要返回该图片
    private boolean videoNoWidthHeightIsResult = true;//未获取到视频宽高分辨率的时候是否要返回该图片
    private boolean videoNoDurationIsResult = true;//未获取到视频时长的时候是否要返回该图片



    @ColorRes
    private int bottomOptionsBackground;//底部操作栏背景颜色
    @DimenRes
    private int bottomOptionsHeight;//底部操作栏高度
    @ColorRes
    private int bottomOptionsTextColor;//底部操作栏文字颜色
    @DimenRes
    private int bottomOptionsTextSize;//底部操作栏文字大小
    @DrawableRes
    private Integer selectStateY = null;//选中状态图标
    @DrawableRes
    private Integer selectStateN = null;//非选中状态图标

    public PictureVideoSelectConfirg() {
        super();
        bottomOptionsBackground = R.color.yudao_primary;
        bottomOptionsTextColor = Color.WHITE;
        bottomOptionsHeight = 44;
        bottomOptionsTextSize = 14;
    }

    public Integer getSelectStateY() {
        return selectStateY;
    }

    public PictureVideoSelectConfirg setSelectStateY(@DrawableRes Integer selectStateY) {
        this.selectStateY = selectStateY;
        return this;
    }

    public Integer getSelectStateN() {
        return selectStateN;
    }

    public PictureVideoSelectConfirg setSelectStateN(@DrawableRes Integer selectStateN) {
        this.selectStateN = selectStateN;
        return this;
    }

    public boolean isShowPreview() {
        return isShowPreview;
    }

    public PictureVideoSelectConfirg setShowPreview(boolean showPreview) {
        isShowPreview = showPreview;
        return this;
    }

    public boolean isShowOriginPicSelect() {
        return isShowOriginPicSelect;
    }

    public PictureVideoSelectConfirg setShowOriginPicSelect(boolean showOriginPicSelect) {
        isShowOriginPicSelect = showOriginPicSelect;
        return this;
    }

    public List<String> getSelectedPicList() {
        return selectedPicList;
    }

    public PictureVideoSelectConfirg setSelectedPicList(List<String> selectedPicList) {
        this.selectedPicList = selectedPicList;
        return this;
    }

    public int getShowRowCount() {
        return showRowCount;
    }

    public PictureVideoSelectConfirg setShowRowCount(int showRowCount) {
        this.showRowCount = showRowCount;
        return this;
    }


    public int getBottomOptionsBackground() {
        return bottomOptionsBackground;
    }

    public PictureVideoSelectConfirg setBottomOptionsBackground(@ColorRes int bottomOptionsBackground) {
        this.bottomOptionsBackground = bottomOptionsBackground;
        return this;
    }

    public int getBottomOptionsHeight() {
        return bottomOptionsHeight;
    }

    public PictureVideoSelectConfirg setBottomOptionsHeight(@DimenRes int bottomOptionsHeight) {
        this.bottomOptionsHeight = bottomOptionsHeight;
        return this;
    }

    public int getBottomOptionsTextColor() {
        return bottomOptionsTextColor;
    }

    public PictureVideoSelectConfirg setBottomOptionsTextColor(@ColorRes int bottomOptionsTextColor) {
        this.bottomOptionsTextColor = bottomOptionsTextColor;
        return this;
    }

    public int getBottomOptionsTextSize() {
        return bottomOptionsTextSize;
    }

    public PictureVideoSelectConfirg setBottomOptionsTextSize(@DimenRes int bottomOptionsTextSize) {
        this.bottomOptionsTextSize = bottomOptionsTextSize;
        return this;
    }

    public PictureVideoSelectConfirg setSelectType(int selectType) {
        this.selectType = selectType;
        return this;
    }

    public int getSelectType() {
        return selectType;
    }


    public int getMaxSelectPictureNum() {
        return maxSelectPictureNum;
    }

    public PictureVideoSelectConfirg setMaxSelectPictureNum(int maxSelectPictureNum) {
        this.maxSelectPictureNum = maxSelectPictureNum;
        return this;
    }

    public int getMaxSelectVideoNum() {
        return maxSelectVideoNum;
    }

    public PictureVideoSelectConfirg setMaxSelectVideoNum(int maxSelectVideoNum) {
        this.maxSelectVideoNum = maxSelectVideoNum;
        return this;
    }

    public boolean isAllowConcurrentSelection() {
        return isAllowConcurrentSelection;
    }

    public PictureVideoSelectConfirg setAllowConcurrentSelection(boolean allowConcurrentSelection) {
        isAllowConcurrentSelection = allowConcurrentSelection;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxSelectPictureNum);
        dest.writeInt(this.maxSelectVideoNum);
        dest.writeInt(this.showRowCount);
        dest.writeStringList(this.selectedPicList);
        dest.writeByte(this.isShowPreview ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowOriginPicSelect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAllowConcurrentSelection ? (byte) 1 : (byte) 0);
        dest.writeInt(this.selectType);
        dest.writeValue(this.videoMaxDuration);
        dest.writeValue(this.videoMinDuration);
        dest.writeInt(this.bottomOptionsBackground);
        dest.writeInt(this.bottomOptionsHeight);
        dest.writeInt(this.bottomOptionsTextColor);
        dest.writeInt(this.bottomOptionsTextSize);
        dest.writeValue(this.selectStateY);
        dest.writeValue(this.selectStateN);
        dest.writeInt(this.aBarHeight);
        dest.writeInt(this.aBarColor);
        dest.writeInt(this.titleColor);
        dest.writeString(this.titleText);
        dest.writeInt(this.aBarTextColor);
        dest.writeInt(this.aBarTextSize);
        dest.writeInt(this.themeId);
    }

    protected PictureVideoSelectConfirg(Parcel in) {
        this.maxSelectPictureNum = in.readInt();
        this.maxSelectVideoNum = in.readInt();
        this.showRowCount = in.readInt();
        this.selectedPicList = in.createStringArrayList();
        this.isShowPreview = in.readByte() != 0;
        this.isShowOriginPicSelect = in.readByte() != 0;
        this.isAllowConcurrentSelection = in.readByte() != 0;
        this.selectType = in.readInt();
        this.videoMaxDuration = (Long) in.readValue(Long.class.getClassLoader());
        this.videoMinDuration = (Long) in.readValue(Long.class.getClassLoader());
        this.bottomOptionsBackground = in.readInt();
        this.bottomOptionsHeight = in.readInt();
        this.bottomOptionsTextColor = in.readInt();
        this.bottomOptionsTextSize = in.readInt();
        this.selectStateY = (Integer) in.readValue(Integer.class.getClassLoader());
        this.selectStateN = (Integer) in.readValue(Integer.class.getClassLoader());
        this.aBarHeight = in.readInt();
        this.aBarColor = in.readInt();
        this.titleColor = in.readInt();
        this.titleText = in.readString();
        this.aBarTextColor = in.readInt();
        this.aBarTextSize = in.readInt();
        this.themeId = in.readInt();
    }

    public static final Creator<PictureVideoSelectConfirg> CREATOR = new Creator<PictureVideoSelectConfirg>() {
        @Override
        public PictureVideoSelectConfirg createFromParcel(Parcel source) {
            return new PictureVideoSelectConfirg(source);
        }

        @Override
        public PictureVideoSelectConfirg[] newArray(int size) {
            return new PictureVideoSelectConfirg[size];
        }
    };
}
