package com.pictureselect.android.database;

/**
 * Created by wangliang on 0016/2017/10/16.
 * 创建时间： 0016/2017/10/16 13:17
 * 创建人：王亮（Loren wang）
 * 功能作用：用来存储记录数据库表的字段名以及属性
 * 思路：做成单例，减少内存空间的使用
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class DbColumnsAndProperty {
    public static DbColumnsAndProperty dbColumnsAndProperty;
    
    public static DbColumnsAndProperty getInstance(){
        if(dbColumnsAndProperty == null){
            dbColumnsAndProperty = new DbColumnsAndProperty();
        }
        return dbColumnsAndProperty;
    }

    //通用
    public final String _ID = "_id";
    
    //表名列表
    public final String TB_SCAN_DIR_FOR_PICTURE = "scanDirForPicture";//被扫描文件夹图片记录表
    public final String TB_SCAN_DIR_FOR_VIDEO = "scanDirForVideo";//被扫描文件夹视频记录表

    //仅通过扫描而非数据库进行图片视频数据获取
    public final String TB_SCAN_SD_CARD_FOR_PICTURE = "scanSdCardForPicture";//扫描sd卡图片记录表
    public final String TB_SCAN_SD_CARD_FOR_VIDEO = "scanSdCardForVideo";//扫描sd卡视频记录表



}
