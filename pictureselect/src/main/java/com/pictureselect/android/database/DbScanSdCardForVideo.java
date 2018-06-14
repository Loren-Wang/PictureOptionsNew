package com.pictureselect.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.basepictureoptionslib.android.database.DbBase;
import com.basepictureoptionslib.android.utils.CheckUtils;
import com.basepictureoptionslib.android.utils.DbUtils;

import java.io.File;

public class DbScanSdCardForVideo extends DbBase{
    private static DbScanSdCardForVideo dbScanDirForVideo;
    private Context context;
    public static DbScanSdCardForVideo getInstance(Context context){
        if(dbScanDirForVideo == null){
            dbScanDirForVideo = new DbScanSdCardForVideo(context);
        }
        return dbScanDirForVideo;
    }
    private DbScanSdCardForVideo(Context context) {
        this.context = context;
        boolean state = DbUtils.getInstance(context).execSQL("select * from " + property.TB_SCAN_SD_CARD_FOR_VIDEO);
        if(!state){
            createTable(context);
        }
    }


    /**
     * 插入數據
     * @param path
     * @return
     */
    public synchronized boolean insert(String path){
        if(CheckUtils.checkIsVideo(path)){
            try {
                MediaMetadataRetriever retr = new MediaMetadataRetriever();
                retr.setDataSource(path);
                //有些数据是通过file进行获取的
                File file = new File(path);
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA,path);
                //获取文件大小
                values.put(MediaStore.Video.Media.SIZE,file.length());
                //获取文件名
                values.put(MediaStore.Video.Media.DISPLAY_NAME,path.substring(path.lastIndexOf("/") + 1));
                //获取文件类型
                values.put(MediaStore.Video.Media.MIME_TYPE,"video/" + path.substring(path.lastIndexOf(".") + 1));
                //获取文件添加时间
                values.put(MediaStore.Video.Media.DATE_ADDED,file.lastModified());
                //获取文件修改时间
                values.put(MediaStore.Video.Media.DATE_MODIFIED,file.lastModified());
                //获取文件坐标
                String loc = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
                if(loc != null && !loc.isEmpty() && loc.split("\\+").length == 2) {
                    values.put(MediaStore.Video.Media.LATITUDE,Double.valueOf(loc.substring(loc.indexOf("\\+"))));
                    values.put(MediaStore.Video.Media.LONGITUDE,Double.valueOf(loc.substring(0,loc.indexOf("\\+"))));
                }
                //获取文件宽高
                values.put(MediaStore.Video.Media.WIDTH,Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
                values.put(MediaStore.Video.Media.HEIGHT,Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)));
                //获取文件旋转角度
                values.put(MediaStore.Video.Media.DURATION,Long.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));

                //释放数据
                retr.release();
                retr = null;
                file = null;
                return DbUtils.getInstance(context).insert(property.TB_SCAN_SD_CARD_FOR_VIDEO,values) > 0;
            } catch (Exception e) {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 刪除圖片
     * @param path
     * @return
     */
    public synchronized boolean delete(String path){
        if(CheckUtils.checkIsImage(path)){
            return DbUtils.getInstance(context).delete(property.TB_SCAN_SD_CARD_FOR_VIDEO,
                    MediaStore.Video.Media.DATA + "=?",new String[]{path}) > 0;
        }else {
            return false;
        }
    }

    public synchronized Cursor getCursor(String selection, String[] selectionArgs){
        return DbUtils.getInstance(context).select2(property.TB_SCAN_SD_CARD_FOR_VIDEO,null,selection,selectionArgs,null,null,null);
    }



    /**
     * 创建表
     * @param context
     */
    public boolean createTable(Context context){
        StringBuffer createTbBUffer = new StringBuffer("create table if not exists ");
        createTbBUffer.append(property.TB_SCAN_SD_CARD_FOR_VIDEO);
        createTbBUffer.append("(");
        createTbBUffer.append(MediaStore.Video.Media._ID).append(" INTEGER PRIMARY KEY,");
        createTbBUffer.append(MediaStore.Video.Media.DATA).append(" text,");
        createTbBUffer.append(MediaStore.Video.Media.MIME_TYPE).append(" varchar(50),");
        createTbBUffer.append(MediaStore.Video.Media.SIZE).append(" long,");
        createTbBUffer.append(MediaStore.Video.Media.DISPLAY_NAME).append(" text,");
        createTbBUffer.append(MediaStore.Video.Media.DATE_ADDED).append(" long,");
        createTbBUffer.append(MediaStore.Video.Media.DATE_MODIFIED).append(" long,");
        createTbBUffer.append(MediaStore.Video.Media.LATITUDE).append(" double," );
        createTbBUffer.append(MediaStore.Video.Media.LONGITUDE).append(" double,");
        createTbBUffer.append(MediaStore.Video.Media.WIDTH).append(" int,");
        createTbBUffer.append(MediaStore.Video.Media.HEIGHT).append(" int,");
        createTbBUffer.append(MediaStore.Video.Media.DURATION).append(" long");
        createTbBUffer.append(")");
        boolean statues = DbUtils.getInstance(context).execSQL(createTbBUffer.toString());
        createTbBUffer = null;
        return statues;
    }
}
