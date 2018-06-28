package com.pictureselect.android.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.pictureselect.android.contentobserver.SystemPictureVideoContentObserver;
import com.pictureselect.android.database.DbScanSdCardForPicture;
import com.pictureselect.android.database.DbScanSdCardForVideo;
import com.pictureselect.android.service.SdCardDirService;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class SdCardFileChangeUtils {
    private final String TAG = getClass().getName() + hashCode();
    private static SdCardFileChangeUtils sdCardFileChangeUtils;
    private Context context;
    private ArrayList<String> scanDirList = new ArrayList<>();


    private SdCardFileChangeUtils(Context context) {
        this.context = context;
    }

    public static SdCardFileChangeUtils geInstance(Context context){
        if(sdCardFileChangeUtils == null){
            sdCardFileChangeUtils = new SdCardFileChangeUtils(context);
        }
        return sdCardFileChangeUtils;
    }
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

    /**
     * 初始化
     * @param observerPaths 额外扫描
     */
    public void init(@Nullable String[] observerPaths){
        if(observerPaths == null){
            return;
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
        if(absFile != null) {
            for (File file : absFile.listFiles()) {
                if (file.isDirectory() && file.getName().toLowerCase().equals("dcim")) {
                    scanDirList.add(file.getAbsolutePath() + "/");
                    break;
                }
            }
        }

        startScanSdCard();
        


        //开启图片系统数据库监听
        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , true, new SystemPictureVideoContentObserver(handler,SYS_DATABASE_CHANGE_FOR_PICTURE));
        //开启视频系统数据库监听
        context.getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                , true, new SystemPictureVideoContentObserver(handler,SYS_DATABASE_CHANGE_FOR_VIDEO));
        
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

    /**
     * 通过地址去扫描视频
     * @param path
     */
    public void scanVideoForPath(String path){
        DbScanSdCardForVideo.getInstance(context).insert(path);
    }


}
