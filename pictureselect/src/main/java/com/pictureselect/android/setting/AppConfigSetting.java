package com.pictureselect.android.setting;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import com.pictureselect.android.activity.PictureVideoSelectActivity;
import com.pictureselect.android.config.PictureVideoSelectConfirg;
import com.pictureselect.android.dto.StorePictureVideoItemDto;

import java.util.ArrayList;

/**
 * 创建时间： 0003/2018/5/3 下午 3:03
 * 创建人：王亮（Loren wang）
 * 功能作用：APP的全局配置设置工具类
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AppConfigSetting {
    public static Paint CHANGE_PICTURE_SELECT_STATE_VIEW_PAINT_STATE;
    public static Bitmap CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP;
    public static Bitmap CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP;
    public static Rect CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_SRCRECT;
    public static Rect CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_SRCRECT;
    public static Rect CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT;

    //页面传递使用
    public static ArrayList<StorePictureVideoItemDto> showAllList = new ArrayList<>();//要显示的所有的列表
    public static ArrayList<StorePictureVideoItemDto> showSelectList = new ArrayList<>();//要显示的所有的列表
    public static PictureVideoSelectConfirg pictureVideoSelectConfirg;//配置文件

    public static PictureVideoSelectActivity pictureVideoSelectActivity;
}
