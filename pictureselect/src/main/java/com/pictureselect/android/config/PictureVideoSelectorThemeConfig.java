package com.pictureselect.android.config;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;

import com.pictureselect.android.R;
import com.pictureselect.android.contentobserver.SystemPictureVideoContentObserver;
import com.pictureselect.android.database.DbScanSdCardForPicture;
import com.pictureselect.android.database.DbScanSdCardForVideo;
import com.pictureselect.android.service.SdCardDirService;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

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
    private static Context context;
    public static PictureVideoSelectorThemeConfig getInstance(Context ctx){
        if(ctx != null){
            context = ctx;
        }
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

    private int acBarHeight = 44;//标题栏高度
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

    private int acBarContentViewHeight = 44;//标题栏内部控件高度

    private int bottomOptionsHeight = 44;//底部操作栏高度
    @DrawableRes
    private Integer selectStateY = null;//选中状态图标
    @DrawableRes
    private Integer selectStateN = null;//非选中状态图标
    @StyleRes
    private int themeId = R.style.AppTheme_NoActionBar;//主题id
    private int showRowCount = 3;//显示的列数
    private ArrayList<String> scanDirList = new ArrayList<>();//  指定扫描文件夹（将扫描做到配置当中）

    private final int SYS_DATABASE_CHANGE_FOR_PICTURE = 0;//系统图片数据库变更
    private final int SYS_DATABASE_CHANGE_FOR_VIDEO = 1;//系统视频数据库变更

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Uri uri = (Uri) msg.obj;
            Cursor cursor;
            String displayName;
            String path;
            Iterator<String> iterator;
            boolean isHave;//是否在扫描文件夹中
            switch (msg.what){
                case SYS_DATABASE_CHANGE_FOR_PICTURE:
                    cursor = context.getContentResolver().query(uri,null,
                            null, null, null);
                    if(cursor != null){
                        if(cursor.moveToNext()) {
                            //判断是否在扫描的文件夹里
                            displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            if(isHaveScanDirList(path,displayName)) {
                                DbScanSdCardForPicture.getInstance(context).insert(path);
                            }
                            path = null;
                        }
                        cursor.close();
                    }
                    cursor = null;
                    break;
                case SYS_DATABASE_CHANGE_FOR_VIDEO:
                    cursor = context.getContentResolver().query(uri,null,
                            null, null, null);
                    if(cursor != null){
                        if(cursor.moveToNext()) {
                            //判断是否在扫描的文件夹里
                            displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                            if(isHaveScanDirList(path,displayName)) {
                                DbScanSdCardForVideo.getInstance(context).insert(path);
                            }
                            path = null;
                        }
                        cursor.close();
                    }
                    cursor = null;
                    break;
                default:
                    break;
            }

        }

        /**
         * 判断是否在扫描文件夹下
         * @param path
         * @param displayName
         * @return
         */
        private boolean isHaveScanDirList(String path,String displayName){
            //判断是否存在于被扫描文件夹中
            if(!TextUtils.isEmpty(displayName)){
                Iterator<String> iterator = scanDirList.iterator();
                path = path.replace(displayName,"");
                displayName = null;
                while (iterator.hasNext()){
                    if(path.indexOf(iterator.next()) == 0){
                        iterator = null;
                        return true;
                    }
                }
                iterator = null;
                //如果没有存在
                return false;
            }else {
                return false;
            }
        }
    };




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
    public PictureVideoSelectorThemeConfig setAcBarHeight(int acBarHeight) {
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
    public PictureVideoSelectorThemeConfig setAcBarContentViewHeight(int acBarContentViewHeight) {
        this.acBarContentViewHeight = acBarContentViewHeight;
        return this;
    }

    public int getBottomOptionsHeight() {
        return bottomOptionsHeight;
    }
    public PictureVideoSelectorThemeConfig setBottomOptionsHeight(int bottomOptionsHeight) {
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


    public PictureVideoSelectorThemeConfig setScanDirList(@Nullable String[] observerPaths){
        if(observerPaths == null){
            return this;
        }
        //开启扫描
        scanDirList.clear();
        for(String observerPath : observerPaths) {
            if(observerPath.trim().lastIndexOf("/") == observerPath.trim().length() - 1) {
                scanDirList.add(observerPath);
            }else {
                scanDirList.add(observerPath.trim() + "/");
            }
        }
        //添加相册扫描
        String absPath = Environment.getExternalStorageDirectory().getPath();
        File absFile = new File(absPath);
        if(absFile != null && absFile.listFiles() != null) {
            for (File file : absFile.listFiles()) {
                if (file.isDirectory() && file.getName().toLowerCase().equals("dcim")) {
                    scanDirList.add(file.getAbsolutePath() + "/");
                    break;
                }
            }
        }

        if(context != null) {
            //开始扫描
            startScanSdCard();
            //开启图片系统数据库监听
            context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , true, new SystemPictureVideoContentObserver(handler, SYS_DATABASE_CHANGE_FOR_PICTURE));
            //开启视频系统数据库监听
            context.getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , true, new SystemPictureVideoContentObserver(handler, SYS_DATABASE_CHANGE_FOR_VIDEO));
        }
        return this;
    }
    /**
     * 开始文件扫描
     */
    public void startScanSdCard(){
        //停止服务
        context.stopService(new Intent(context, SdCardDirService.class));
        //开启服务扫描
        Intent intent = new Intent(context, SdCardDirService.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("scanDir",scanDirList);
        intent.putExtras(bundle);
        context.startService(intent);
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
