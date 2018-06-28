package com.pictureselect.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lorenwang.tools.android.LogUtils;
import com.pictureselect.android.database.DbColumnsAndProperty;
import com.pictureselect.android.database.DbScanSdCardForPicture;
import com.pictureselect.android.database.DbScanSdCardForVideo;
import com.pictureselect.android.setting.AppConfigSetting;
import com.pictureselect.android.utils.PictureVideoDbUtils;

import java.io.File;
import java.util.ArrayList;

public class SdCardDirService extends Service {
    private final String TAG = getClass().getName();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        if(extras != null){
            //开启扫描
            ArrayList<String> pathList = extras.getStringArrayList("scanDir");

            //清空数据库
            PictureVideoDbUtils.getInstance(getApplicationContext()).deleteDatabaseTableData(DbColumnsAndProperty.getInstance().TB_SCAN_SD_CARD_FOR_PICTURE);
            PictureVideoDbUtils.getInstance(getApplicationContext()).deleteDatabaseTableData(DbColumnsAndProperty.getInstance().TB_SCAN_SD_CARD_FOR_VIDEO);
            //开始扫描
            for(String observerPath : pathList) {
                //检测网址是否为空
                if (TextUtils.isEmpty(observerPath)) {
                    LogUtils.logD(TAG, "需要监控的文件地址为空");
                    continue;
                }
                putFileDir( new File(observerPath));
            }

            //通知页面进行刷新
            if(AppConfigSetting.pictureVideoSelectActivity != null){
                AppConfigSetting.pictureVideoSelectActivity.initPictureList();
            }
        }
        stopSelf();



        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 递归扫描
     * @param dirFile
     */
    private void putFileDir(File dirFile){
        if(dirFile.isDirectory()){
            if(dirFile.getName().indexOf(".") == 0){
                return;
            }
            File[] files = dirFile.listFiles();
            if(files  != null) {
                for (File childFile : files) {
                    putFileDir(childFile);
                }
            }
        }else if(dirFile.isFile()){
            DbScanSdCardForPicture.getInstance(getApplicationContext()).insert(dirFile.getAbsolutePath());
            DbScanSdCardForVideo.getInstance(getApplicationContext()).insert(dirFile.getAbsolutePath());
        }
    }
}
