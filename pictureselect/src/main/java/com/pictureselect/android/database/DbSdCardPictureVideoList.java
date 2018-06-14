package com.pictureselect.android.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import com.pictureselect.android.dto.StorePictureVideoItemDto;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wangliang on 0013/2017/3/13.
 * 创建时间： 0013/2017/3/13 16:54
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class DbSdCardPictureVideoList {
    private static Context context;
    private static DbSdCardPictureVideoList dbPhonePictures;

    public static DbSdCardPictureVideoList getInstance(Context ctx){
        if(ctx != null){
            context = ctx;
        }
        if(dbPhonePictures == null){
            dbPhonePictures = new DbSdCardPictureVideoList();
        }

        return dbPhonePictures;
    }

    /**
     * 获取所有的图片
     * @return
     */
    public Map<String,List<StorePictureVideoItemDto>> getPictureAllMapList(String selection, String[] selectionArgs){
        Map<String, List<StorePictureVideoItemDto>> mapInfoList =
                getInfoListForPicture(new Cursor[]{getInfoListForPictureSystemCursor(selection,selectionArgs)
                        , DbScanSdCardForPicture.getInstance(context).getCursor(selection,selectionArgs)});
        Map<String, List<StorePictureVideoItemDto>> mapList = new HashMap<>();
        Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = mapInfoList.entrySet().iterator();
        Map.Entry<String, List<StorePictureVideoItemDto>> next;
        List<StorePictureVideoItemDto> list;
        while (iterator.hasNext()){
            next = iterator.next();
            list = wipeOffRepetitionDto(next.getValue());
            Collections.sort(list,sortList);
            mapList.put(next.getKey(),list);
            next = null;
        }
        mapInfoList.clear();
        mapInfoList = null;
        iterator = null;
        return mapList;
    }
    /**
     * 获取所有的视频
     * @return
     * @param minDuration
     * @param maxDuration
     */
    public Map<String,List<StorePictureVideoItemDto>> getVideoAllMapList(String selection, String[] selectionArgs){
        Map<String, List<StorePictureVideoItemDto>> mapInfoList =
                getInfoListForVideo(new Cursor[]{getInfoListForVideoSystemCursor(selection,selectionArgs)
                        ,DbScanSdCardForVideo.getInstance(context).getCursor(selection,selectionArgs)});
        Map<String, List<StorePictureVideoItemDto>> mapList = new HashMap<>();
        Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = mapInfoList.entrySet().iterator();
        Map.Entry<String, List<StorePictureVideoItemDto>> next;
        List<StorePictureVideoItemDto> list;
        while (iterator.hasNext()){
            next = iterator.next();
            list = wipeOffRepetitionDto(next.getValue());
            Collections.sort(list,sortList);
            mapList.put(next.getKey(),list);
            next = null;
        }
        mapInfoList.clear();
        mapInfoList = null;
        iterator = null;
        return mapList;
    }

    /**
     * 获取所有的图片
     * @return
     */
    public ArrayList<StorePictureVideoItemDto> getAllList(Map<String, List<StorePictureVideoItemDto>> map){
        ArrayList<StorePictureVideoItemDto> list = new ArrayList<>();
        if(map != null){
            Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()){
                list.addAll(iterator.next().getValue());
            }
            map.clear();
            map = null;
            iterator = null;
        }
        list = wipeOffRepetitionDto(list);
        Collections.sort(list,sortList);
        return list;
    }


    /**
     * 获取所有的数据集合，以map的形式返回
     * @return
     * @param minDuration
     * @param maxDuration
     */
    public Map<String,List<StorePictureVideoItemDto>> getAllMapList(String pictureSelection
            , String[] pictureSelectionArgs,String videoSelection, String[] videoSelectionArgs){
        Map<String, List<StorePictureVideoItemDto>> mapPictureInfoList =
                getInfoListForPicture(new Cursor[]{DbScanSdCardForPicture.getInstance(context).getCursor(pictureSelection, pictureSelectionArgs)});
        Map<String, List<StorePictureVideoItemDto>> mapVideoInfoList =
                getInfoListForVideo(new Cursor[]{DbScanSdCardForVideo.getInstance(context).getCursor(videoSelection, videoSelectionArgs)});
        Map<String, List<StorePictureVideoItemDto>> resultMap = new HashMap<>();
        String key;
        List<StorePictureVideoItemDto> list;
        List<StorePictureVideoItemDto> resultMapList;

        //图片列表
        Iterator<String> pictureMapKeyListIterator = mapPictureInfoList.keySet().iterator();
        while (pictureMapKeyListIterator.hasNext()){
            key = pictureMapKeyListIterator.next();
            list = mapPictureInfoList.get(key);
            resultMapList = resultMap.get(key);
            if(resultMapList == null){
                resultMapList = new ArrayList<>();
            }
            if(list != null) {
                //去重
                list = wipeOffRepetitionDto(list);
                //排序
                Collections.sort(list,sortList);
                resultMapList.addAll(list);
            }
            resultMap.put(key,resultMapList);
            key = null;
        }
        pictureMapKeyListIterator = null;
        //视频列表
        Iterator<String> videoMapKeyListIterator = mapVideoInfoList.keySet().iterator();
        while (videoMapKeyListIterator.hasNext()){
            key = videoMapKeyListIterator.next();
            list = mapVideoInfoList.get(key);
            resultMapList = resultMap.get(key);
            if(resultMapList == null){
                resultMapList = new ArrayList<>();
            }
            if(list != null) {
                //去重
                list = wipeOffRepetitionDto(list);
                //排序
                Collections.sort(list,sortList);
                resultMapList.addAll(list);
            }
            resultMap.put(key,resultMapList);
            key = null;
        }
        videoMapKeyListIterator = null;

        mapPictureInfoList.clear();
        mapPictureInfoList = null;
        mapVideoInfoList.clear();
        mapVideoInfoList = null;

        return resultMap;
    }

    //排序方法
    private Comparator<StorePictureVideoItemDto> sortList = new Comparator<StorePictureVideoItemDto>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(StorePictureVideoItemDto o1, StorePictureVideoItemDto o2) {
            int compare = Long.compare(o1.getTimeModify(), o2.getTimeModify());
            if(compare > 0){
                return -1;
            }else if(compare < 0){
                return 1;
            }else {
                return 0;
            }
        }
    };

    //去除重复项
    private ArrayList<StorePictureVideoItemDto> wipeOffRepetitionDto(List<StorePictureVideoItemDto> searchList){
        ArrayList<StorePictureVideoItemDto> list = new ArrayList<>();
        if(!check(searchList)){
            return list;
        }
        Map<String,StorePictureVideoItemDto> map = new HashMap<>();
        for(StorePictureVideoItemDto itemDto : searchList){
            map.put(itemDto.getAbsolutePath(),itemDto);
        }
        list.addAll(map.values());
        searchList.clear();
        searchList = null;
        map.clear();
        map = null;

        return list;
    }

    /**
     * 基方法，获取系统数据库图片列表并以map的形式存储
     * @return
     */
    private Map<String,List<StorePictureVideoItemDto>> getInfoListForPicture(Cursor[] mImageCursors){
        Map<String,List<StorePictureVideoItemDto>> map = new HashMap<>();

        for(Cursor cursor : mImageCursors){
            List<StorePictureVideoItemDto> list = null;
            StorePictureVideoItemDto storePictureItemDto;
            File absolutePathFile;
            while (cursor.moveToNext()) {
                storePictureItemDto = new StorePictureVideoItemDto();
                absolutePathFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                //先判定文件是否存在同时大小要不能小于0，不存在则不要管
                if (absolutePathFile.getAbsolutePath() != null
                        && absolutePathFile.isFile()
                        && absolutePathFile.exists()
                        && absolutePathFile.length() > 0
                        && cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null) {
                    storePictureItemDto.setAbsolutePath(absolutePathFile.getAbsolutePath());
                    absolutePathFile = null;
                } else {
                    continue;
                }

                storePictureItemDto.set_id(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                storePictureItemDto.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                storePictureItemDto.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                storePictureItemDto.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));
                storePictureItemDto.setTimeAdd(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
                storePictureItemDto.setTimeModify(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)));
                storePictureItemDto.setLat(cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE)));
                storePictureItemDto.setLng(cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE)));
                storePictureItemDto.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)));
                storePictureItemDto.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)));
                storePictureItemDto.setOrientation(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)));
                storePictureItemDto.setDuration(0l);
                storePictureItemDto.setDirectoryPath(String.valueOf(storePictureItemDto.getAbsolutePath())
                        .replace(String.valueOf(storePictureItemDto.getFileName()), ""));//使用intern()方法防止原有数据被破坏，该方法会创建一个新的对象字符串

                list = map.get(storePictureItemDto.getDirectoryPath());
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(storePictureItemDto);
                map.put(storePictureItemDto.getDirectoryPath(), list);
            }
            cursor.close();
            cursor = null;
            storePictureItemDto = null;
        }
        mImageCursors = null;
        return map;
    }

    private Cursor getInfoListForPictureSystemCursor(String selection, String[] selectionArgs){
        ContentResolver mContentResolver = context.getContentResolver();
        return mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                selection,selectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
    }

    /**
     * 基方法，获取系统数据库图片列表并以map的形式存储
     * @return
     */
    private Map<String,List<StorePictureVideoItemDto>> getInfoListForVideo(Cursor[] mVideoCursors){
        Map<String,List<StorePictureVideoItemDto>> map = new HashMap<>();
        for(Cursor cursor : mVideoCursors){
            List<StorePictureVideoItemDto> list = null;
            StorePictureVideoItemDto storePictureItemDto;
            File absolutePathFile;
            while (cursor.moveToNext()) {
                storePictureItemDto = new StorePictureVideoItemDto();
                absolutePathFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                //先判定文件是否存在同时大小要不能小于0，不存在则不要管
                if (absolutePathFile.getAbsolutePath() != null
                        && absolutePathFile.isFile()
                        && absolutePathFile.exists()
                        && absolutePathFile.length() > 0
                        && cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null) {
                    storePictureItemDto.setAbsolutePath(absolutePathFile.getAbsolutePath());
                    absolutePathFile = null;
                } else {
                    continue;
                }

                storePictureItemDto.set_id(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
                storePictureItemDto.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                storePictureItemDto.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                storePictureItemDto.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)));
                storePictureItemDto.setTimeAdd(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
                storePictureItemDto.setTimeModify(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)));
                storePictureItemDto.setLat(cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE)));
                storePictureItemDto.setLng(cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE)));
                storePictureItemDto.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH)));
                storePictureItemDto.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT)));
                storePictureItemDto.setOrientation(0);
                storePictureItemDto.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
                storePictureItemDto.setDirectoryPath(String.valueOf(storePictureItemDto.getAbsolutePath())
                        .replace(String.valueOf(storePictureItemDto.getFileName()), ""));//使用intern()方法防止原有数据被破坏，该方法会创建一个新的对象字符串

                list = map.get(storePictureItemDto.getDirectoryPath());
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(storePictureItemDto);
                map.put(storePictureItemDto.getDirectoryPath(), list);
            }
            cursor.close();
            cursor = null;
            storePictureItemDto = null;
        }
        mVideoCursors = null;
        return map;
    }

    private Cursor getInfoListForVideoSystemCursor(String selection, String[] selectionArgs){
        ContentResolver mContentResolver = context.getContentResolver();
        return mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
    }

    /**
     * 检查传入参数是否为空
     * @param objects
     * @return
     */
    protected boolean check(Object... objects){
        for(int i = 0 ; i < objects.length ; i++){
            if(objects[i] != null && objects[i] instanceof String){
                return !((String) objects[i]).isEmpty();
            }
            return objects[i] != null;
        }

        return true;
    }
}
