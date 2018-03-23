package com.pictureselect.android.utils;

import android.content.Context;
import android.os.FileObserver;
import android.support.annotation.Nullable;

import com.basepictureoptionslib.android.database.DbColumnsAndProperty;
import com.basepictureoptionslib.android.utils.DbUtils;
import com.basepictureoptionslib.android.utils.LogUtils;
import com.pictureselect.android.database.DbScanDirForPicture;
import com.pictureselect.android.database.DbScanDirForVideo;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SdCardFileChangeUtils {
    private final String TAG = getClass().getName() + hashCode();
    private static SdCardFileChangeUtils sdCardFileChangeUtils;
    private Context context;

    private Map<String,ChildFileObserver> childFileObserverMap = new HashMap<>();
    private String observerPath;

    private SdCardFileChangeUtils(Context context) {
        this.context = context;
    }

    public static SdCardFileChangeUtils geInstance(Context context){
        if(sdCardFileChangeUtils == null){
            sdCardFileChangeUtils = new SdCardFileChangeUtils(context);
        }
        return sdCardFileChangeUtils;
    }


    public void startWatching(@Nullable String[] observerPaths){
        if(observerPaths == null){
            return;
        }
        //清空数据库
        DbUtils.getInstance(context).deleteDatabaseTableData(DbColumnsAndProperty.getInstance().TB_SCAN_DIR_FOR_PICTURE);
        DbUtils.getInstance(context).deleteDatabaseTableData(DbColumnsAndProperty.getInstance().TB_SCAN_DIR_FOR_VIDEO);
        for(String observerPath : observerPaths) {
            //检测网址是否为空
            if (observerPath == null || observerPath.isEmpty()) {
                LogUtils.logD(TAG, "需要监控的文件地址为空");
                continue;
            }

            //检测监听地址和之前的地址是否相同，如果不相同则将就文件或文件夹监听取消
            ChildFileObserver childFileObserver;
            if (this.observerPath != null && !this.observerPath.equals(observerPath)) {
                Iterator<ChildFileObserver> iterator = childFileObserverMap.values().iterator();
                while (iterator.hasNext()) {
                    childFileObserver = iterator.next();
                    childFileObserver.stopWatching();
                    childFileObserver = null;
                }
                iterator = null;
                childFileObserverMap.clear();
            }
            this.observerPath = observerPath;
            File file = new File(observerPath);
            if (file.isFile()) {
                childFileObserver = new ChildFileObserver(file);
                childFileObserver.startWatching();
                childFileObserverMap.put(file.getAbsolutePath(), childFileObserver);
            } else if (file.isDirectory()) {
                putFileDir(file);
            }
        }
    }

    private void putFileDir(File dirFile){
        if(dirFile.isDirectory()){
            //将这个文件夹监听加入
            ChildFileObserver childFileObserver = new ChildFileObserver(dirFile);
            childFileObserver.startWatching();
            childFileObserverMap.put(dirFile.getAbsolutePath(),childFileObserver);
            File[] files = dirFile.listFiles();
            if(files  != null) {
                for (File childFile : files) {
                    putFileDir(childFile);
                }
            }
        }else if(dirFile.isFile()){
            DbScanDirForPicture.getInstance(context).insert(dirFile.getAbsolutePath());
            DbScanDirForVideo.getInstance(context).insert(dirFile.getAbsolutePath());
        }
    }

    private boolean isUpdataMedia = false;//是否需要通知更新媒体库
    private void onEvent(int event,@Nullable String dir, @Nullable String path) {
        if(path == null){
            path = "";
        }
        switch (event) {
            case FileObserver.CREATE:
                LogUtils.logD(TAG,"创建文件:::" + path);
                DbScanDirForPicture.getInstance(context).insert(dir + path);
                DbScanDirForVideo.getInstance(context).insert(dir + path);
                break;
            case FileObserver.DELETE:
                LogUtils.logD(TAG,"删除文件:::" + path);
                DbScanDirForPicture.getInstance(context).delete(dir + path);
                DbScanDirForVideo.getInstance(context).delete(dir + path);
                break;
            case FileObserver.DELETE_SELF:
                LogUtils.logD(TAG,"自删除文件:::" + path);
                DbScanDirForPicture.getInstance(context).delete(dir + path);
                DbScanDirForVideo.getInstance(context).delete(dir + path);
                break;
            case FileObserver.MODIFY:
                LogUtils.logD(TAG,"修改文件:::" + path);
                break;
            case FileObserver.MOVE_SELF:
                LogUtils.logD(TAG,"自移动文件:::" + path);
                break;
            case FileObserver.MOVED_FROM:
                LogUtils.logD(TAG,"文件移出:::" + path);
                DbScanDirForPicture.getInstance(context).delete(dir + path);
                DbScanDirForVideo.getInstance(context).delete(dir + path);
                break;
            case FileObserver.MOVED_TO:
                LogUtils.logD(TAG,"文件移入:::" + path);
                DbScanDirForPicture.getInstance(context).insert(dir + path);
                DbScanDirForVideo.getInstance(context).insert(dir + path);
                break;
            case FileObserver.ATTRIB:
            default:
                LogUtils.logD(TAG,"文件夹未知操作");
                break;
        }
    }


    private class ChildFileObserver extends FileObserver {
        private String optionsPath = "";//被操作的文件夹，只有当监控的是文件夹的时候才起作用
        public ChildFileObserver(File file) {
            super(file.getAbsolutePath());
            String absolutePath = file.getAbsolutePath();
            try {
                if(absolutePath.lastIndexOf("/") == absolutePath.length() - 1 ){
                    optionsPath = absolutePath.substring(0,absolutePath.length() - 1);
                }else {
                    optionsPath = absolutePath;
                }
            }catch (Exception e){
                LogUtils.logD(TAG,"裁剪字符串失败");
                optionsPath = absolutePath;
            }
        }

        @Override
        public void onEvent(int i, @Nullable String path) {
            sdCardFileChangeUtils.onEvent(i,optionsPath,path);
        }

    }

}
