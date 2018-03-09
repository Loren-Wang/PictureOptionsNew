package com.pictureselect.android.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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

public class DbPhonePictureVideoList {
    private static Context context;
    private static DbPhonePictureVideoList dbPhonePictures;

    public static DbPhonePictureVideoList getInstance(Context ctx){
        if(ctx != null){
            context = ctx;
        }
        if(dbPhonePictures == null){
            dbPhonePictures = new DbPhonePictureVideoList();
        }

        return dbPhonePictures;
    }


    /**
     * 获取指定文件夹下的图片的图片列表
     * @param directoryPath
     * @return
     */
    public List<StorePictureVideoItemDto> getList(Map<String, List<StorePictureVideoItemDto>> map, String directoryPath){
        List<StorePictureVideoItemDto> list = new ArrayList<>();
        if(!check(directoryPath)){
            return list;
        }

        if(map != null){
            list = map.get(directoryPath);
            if(list == null){
                list = new ArrayList<>();
            }
        }
        list = wipeOffRepetitionDto(list);
        Collections.sort(list,sortList);
        return list;
    }

    /**
     * 根据传入的列表获取指定文件夹下的图片的图片列表
     * @param directoryPath
     * @return
     */
    public List<StorePictureVideoItemDto> getList(List<StorePictureVideoItemDto> searchList, String directoryPath){
        List<StorePictureVideoItemDto> list = new ArrayList<>();
        if(!check(searchList,directoryPath)){
            return list;
        }

        for(StorePictureVideoItemDto itemDto : searchList){
            if(itemDto.getDirectoryPath().equals(directoryPath)){
                list.add(itemDto);
            }
        }
        list = wipeOffRepetitionDto(list);
        Collections.sort(list,sortList);
        return list;
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
        }
        list = wipeOffRepetitionDto(list);
        Collections.sort(list,sortList);
        return list;
    }

    /**
     * 获取所有的图片
     * @return
     */
    public Map<String,List<StorePictureVideoItemDto>> getPictureAllMapList(){
        Map<String, List<StorePictureVideoItemDto>> mapInfoList = getInfoListForPicture();
        Map<String, List<StorePictureVideoItemDto>> mapList = new HashMap<>();
        Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = mapInfoList.entrySet().iterator();
        Map.Entry<String, List<StorePictureVideoItemDto>> next;
        List<StorePictureVideoItemDto> list;
        while (iterator.hasNext()){
            next = iterator.next();
            list = wipeOffRepetitionDto(next.getValue());
            Collections.sort(list,sortList);
            mapList.put(next.getKey(),list);
        }
        return mapList;
    }
    /**
     * 获取所有的视频
     * @return
     */
    public Map<String,List<StorePictureVideoItemDto>> getVideoAllMapList(Long minDuration,Long maxDuration){
        Map<String, List<StorePictureVideoItemDto>> mapInfoList = getInfoListForVideo(minDuration,maxDuration);
        Map<String, List<StorePictureVideoItemDto>> mapList = new HashMap<>();
        Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = mapInfoList.entrySet().iterator();
        Map.Entry<String, List<StorePictureVideoItemDto>> next;
        List<StorePictureVideoItemDto> list;
        while (iterator.hasNext()){
            next = iterator.next();
            list = wipeOffRepetitionDto(next.getValue());
            Collections.sort(list,sortList);
            mapList.put(next.getKey(),list);
        }
        return mapList;
    }

    /**
     * 获取所有的数据集合，以map的形式返回
     * @return
     */
    public Map<String,List<StorePictureVideoItemDto>> getAllMapList(Long minDuration,Long maxDuration){
        Map<String, List<StorePictureVideoItemDto>> mapInfoList = getInfoListForPicture();
        Map<String, List<StorePictureVideoItemDto>> mapVideoInfoList = getInfoListForVideo(minDuration,maxDuration);
        Map<String, List<StorePictureVideoItemDto>> mapList = new HashMap<>();
        Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = mapInfoList.entrySet().iterator();
        Map.Entry<String, List<StorePictureVideoItemDto>> next;
        List<StorePictureVideoItemDto> list;
        List<StorePictureVideoItemDto> videoList;
        while (iterator.hasNext()){
            next = iterator.next();
            videoList = mapVideoInfoList.get(next.getKey());
            if(videoList != null){
                videoList.addAll(next.getValue());
                list = wipeOffRepetitionDto(videoList);
            }else {
                list = wipeOffRepetitionDto(next.getValue());
            }
            Collections.sort(list,sortList);
            mapList.put(next.getKey(),list);
        }
        return mapList;
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
        return list;
    }

    /**
     * 基方法，获取系统数据库图片列表并以map的形式存储
     * @return
     */
    private Map<String,List<StorePictureVideoItemDto>> getInfoListForPicture(){
        Map<String,List<StorePictureVideoItemDto>> map = new HashMap<>();

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        //查询图片
        Cursor cursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/bmp"}, MediaStore.Images.Media.DATE_MODIFIED);

        List<StorePictureVideoItemDto> list = null;
        StorePictureVideoItemDto storePictureItemDto;
        int pictureDegree;
        File absolutePathFile;
        while (cursor.moveToNext()){
            storePictureItemDto = new StorePictureVideoItemDto();
            absolutePathFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            //先判定文件是否存在同时大小要不能小于0，不存在则不要管
            if(absolutePathFile.getAbsolutePath() != null
                    && absolutePathFile.isFile()
                    && absolutePathFile.exists()
                    && absolutePathFile.length() > 0
                    && cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null){
                storePictureItemDto.setAbsolutePath(absolutePathFile.getAbsolutePath());
            }else {
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
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(storePictureItemDto);
            map.put(storePictureItemDto.getDirectoryPath(),list);
        }
        cursor.close();
        return map;
    }

    /**
     * 基方法，获取系统数据库图片列表并以map的形式存储
     * @return
     */
    private Map<String,List<StorePictureVideoItemDto>> getInfoListForVideo(Long minDuration,Long maxDuration){
        Map<String,List<StorePictureVideoItemDto>> map = new HashMap<>();

        Uri mImageUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        StringBuffer selection = new StringBuffer(MediaStore.Video.Media.MIME_TYPE).append("=?");
        String[] selectionArgs;
        if(minDuration != null && maxDuration == null){
            selectionArgs = new String[]{"video/mp4",String.valueOf(minDuration)};

            selection.append(" and ")
                    .append(MediaStore.Video.Media.DURATION)
                    .append(">=?");

        }else if(minDuration == null && maxDuration != null){
            selectionArgs = new String[]{"video/mp4",String.valueOf(maxDuration)};

            selection.append(" and ")
                    .append(MediaStore.Video.Media.DURATION)
                    .append("<?");

        }else if(minDuration != null && maxDuration != null){
            selectionArgs = new String[]{"video/mp4",String.valueOf(minDuration),String.valueOf(maxDuration)};

            selection.append(" and ")
                    .append(MediaStore.Video.Media.DURATION)
                    .append(">=?");
            selection.append(" and ")
                    .append(MediaStore.Video.Media.DURATION)
                    .append("<?");

        }else {
            selectionArgs =  new String[]{"video/mp4"};
        }

        //查询图片
        Cursor cursor = mContentResolver.query(mImageUri, null,
                selection.toString(), selectionArgs
                , MediaStore.Images.Media.DATE_MODIFIED);

        List<StorePictureVideoItemDto> list = null;
        StorePictureVideoItemDto storePictureItemDto;
        int pictureDegree;
        File absolutePathFile;
        while (cursor.moveToNext()){
            storePictureItemDto = new StorePictureVideoItemDto();
            absolutePathFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            //先判定文件是否存在同时大小要不能小于0，不存在则不要管
            if(absolutePathFile.getAbsolutePath() != null
                    && absolutePathFile.isFile()
                    && absolutePathFile.exists()
                    && absolutePathFile.length() > 0
                    && cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null){
                storePictureItemDto.setAbsolutePath(absolutePathFile.getAbsolutePath());
            }else {
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
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(storePictureItemDto);
            map.put(storePictureItemDto.getDirectoryPath(),list);
        }
        cursor.close();
        return map;
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
