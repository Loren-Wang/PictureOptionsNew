package com.pictureselect.android.database;

import android.content.Context;
import android.provider.MediaStore;

import com.pictureselect.android.utils.PictureVideoDbUtils;


public class DbScanDirForPicture  extends DbBase{
    private static DbScanDirForPicture dbScanDirForPicture;
    private Context context;
    public static DbScanDirForPicture getInstance(Context context){
        if(dbScanDirForPicture == null){
            dbScanDirForPicture = new DbScanDirForPicture(context);
        }
        return dbScanDirForPicture;
    }
    private DbScanDirForPicture(Context context) {
        this.context = context;
        boolean state = PictureVideoDbUtils.getInstance(context).execSQL("select * from " + property.TB_SCAN_DIR_FOR_PICTURE);
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
//        if(CheckUtils.checkIsImage(path)){
//            try {
//                ExifInterface exifInterface = new ExifInterface(path);
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.SIZE,exifInterface.getAttribute(ExifInterface.TAG_DEFAULT_CROP_SIZE));
//                values.put(MediaStore.Images.Media.DATA,path);
//                values.put(MediaStore.Images.Media.DISPLAY_NAME,path.substring(path.lastIndexOf("/") + 1));
//                values.put(MediaStore.Images.Media.MIME_TYPE,"image/" + path.substring(path.lastIndexOf(".") + 1));
//                values.put(MediaStore.Images.Media.DATE_ADDED, ParamsAndJudgeUtils.getMillisecond());
//                values.put(MediaStore.Images.Media.DATE_MODIFIED, ParamsAndJudgeUtils.getMillisecond());
//                values.put(MediaStore.Images.Media.LATITUDE,exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
//                values.put(MediaStore.Images.Media.LONGITUDE,exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
//                values.put(MediaStore.Images.Media.WIDTH,exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
//                values.put(MediaStore.Images.Media.HEIGHT,exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
//                values.put(MediaStore.Images.Media.ORIENTATION,exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
//                return DbUtils.getInstance(context).insert(property.TB_SCAN_DIR_FOR_PICTURE,values) > 0;
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
//            return DbUtils.getInstance(context).delete(property.TB_SCAN_DIR_FOR_PICTURE,
//                    MediaStore.Images.Media.DATA + "=?",new String[]{path}) > 0;
//        }else {
//            return false;
//        }
//    }
//
//    public synchronized Cursor getCursor(String selection, String[] selectionArgs){
//        return DbUtils.getInstance(context).select2(property.TB_SCAN_DIR_FOR_PICTURE,null,selection,selectionArgs,null,null,MediaStore.Images.Media.DATE_MODIFIED);
//    }
//



    /**
     * 创建表
     * @param context
     */
    public boolean createTable(Context context){
        StringBuffer createTbBUffer = new StringBuffer("create table if not exists ");
        createTbBUffer.append(property.TB_SCAN_DIR_FOR_PICTURE);
        createTbBUffer.append("(");
        createTbBUffer.append(MediaStore.Images.Media._ID).append(" INTEGER PRIMARY KEY,");
        createTbBUffer.append(MediaStore.Images.Media.DATA).append(" text,");
        createTbBUffer.append(MediaStore.Images.Media.SIZE).append(" long,");
        createTbBUffer.append(MediaStore.Images.Media.DISPLAY_NAME).append(" text,");
        createTbBUffer.append(MediaStore.Images.Media.MIME_TYPE).append(" text,");
        createTbBUffer.append(MediaStore.Images.Media.DATE_ADDED).append(" long,");
        createTbBUffer.append(MediaStore.Images.Media.DATE_MODIFIED).append(" long,");
        createTbBUffer.append(MediaStore.Images.Media.LATITUDE).append(" double,");
        createTbBUffer.append(MediaStore.Images.Media.LONGITUDE).append(" double,");
        createTbBUffer.append(MediaStore.Images.Media.WIDTH).append(" int,");
        createTbBUffer.append(MediaStore.Images.Media.HEIGHT).append(" int,");
        createTbBUffer.append(MediaStore.Images.Media.ORIENTATION).append(" int");
        createTbBUffer.append(")");
        boolean statues = PictureVideoDbUtils.getInstance(context).execSQL(createTbBUffer.toString());
        createTbBUffer = null;
        return statues;
    }
}
