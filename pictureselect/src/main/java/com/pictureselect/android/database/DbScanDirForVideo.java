package com.pictureselect.android.database;

import android.content.Context;
import android.provider.MediaStore;

import com.pictureselect.android.utils.PictureVideoDbUtils;


public class DbScanDirForVideo extends DbBase{
    private static DbScanDirForVideo dbScanDirForVideo;
    private Context context;
    public static DbScanDirForVideo getInstance(Context context){
        if(dbScanDirForVideo == null){
            dbScanDirForVideo = new DbScanDirForVideo(context);
        }
        return dbScanDirForVideo;
    }
    private DbScanDirForVideo(Context context) {
        this.context = context;
        boolean state = PictureVideoDbUtils.getInstance(context).execSQL("select * from " + property.TB_SCAN_DIR_FOR_VIDEO);
        if(!state){
            createTable(context);
        }
    }


//    /**
//     * 插入數據
//     * @param path
//     * @return
//     */
//    public synchronized boolean insert(String path){
//        if(CheckUtils.checkIsVideo(path)){
//            try {
//                MediaMetadataRetriever retr = new MediaMetadataRetriever();
//                retr.setDataSource(path);
//                File file = new File(path);
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Video.Media.DATA,path);
//                values.put(MediaStore.Video.Media.SIZE,file.length());
//                values.put(MediaStore.Video.Media.DISPLAY_NAME,path.substring(path.lastIndexOf("/") + 1));
//                values.put(MediaStore.Video.Media.MIME_TYPE,"video/" + path.substring(path.lastIndexOf(".") + 1));
//                values.put(MediaStore.Video.Media.DATE_ADDED, ParamsAndJudgeUtils.getMillisecond());
//                values.put(MediaStore.Video.Media.DATE_MODIFIED, ParamsAndJudgeUtils.getMillisecond());
//                String loc = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
//                if(loc != null && !loc.isEmpty() && loc.split("\\+").length == 2) {
//                    values.put(MediaStore.Video.Media.LATITUDE,Double.valueOf(loc.substring(loc.indexOf("\\+"))));
//                    values.put(MediaStore.Video.Media.LONGITUDE,Double.valueOf(loc.substring(0,loc.indexOf("\\+"))));
//                }
//                values.put(MediaStore.Video.Media.WIDTH,Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
//                values.put(MediaStore.Video.Media.HEIGHT,Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)));
//                values.put(MediaStore.Video.Media.DURATION,Long.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
//                return DbUtils.getInstance(context).insert(property.TB_SCAN_DIR_FOR_VIDEO,values) > 0;
//            } catch (Exception e) {
//                return false;
//            }
//        }else {
//            return false;
//        }
//    }
//
//    /**
//     * 刪除圖片
//     * @param path
//     * @return
//     */
//    public synchronized boolean delete(String path){
//        if(CheckUtils.checkIsImage(path)){
//            return DbUtils.getInstance(context).delete(property.TB_SCAN_DIR_FOR_VIDEO,
//                    MediaStore.Video.Media.DATA + "=?",new String[]{path}) > 0;
//        }else {
//            return false;
//        }
//    }
//
//    public synchronized Cursor getCursor(String selection, String[] selectionArgs){
//        return DbUtils.getInstance(context).select2(property.TB_SCAN_DIR_FOR_VIDEO,null,selection,selectionArgs,null,null,null);
//    }
//


    /**
     * 创建表
     * @param context
     */
    public boolean createTable(Context context){
        StringBuffer createTbBUffer = new StringBuffer("create table if not exists ");
        createTbBUffer.append(property.TB_SCAN_DIR_FOR_VIDEO);
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
        boolean statues = PictureVideoDbUtils.getInstance(context).execSQL(createTbBUffer.toString());
        createTbBUffer = null;
        return statues;
    }
}
