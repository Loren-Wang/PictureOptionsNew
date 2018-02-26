package com.pictureselect.android.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import com.pictureselect.android.dto.StorePictureItemDto;

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

public class DbPhonePicturesList{
    private static Context context;
    private static DbPhonePicturesList dbPhonePictures;

    public static DbPhonePicturesList getInstance(Context ctx){
        if(ctx != null){
            context = ctx;
        }
        if(dbPhonePictures == null){
            dbPhonePictures = new DbPhonePicturesList();
        }

        return dbPhonePictures;
    }


    /**
     * 获取指定文件夹下的图片的图片列表
     * @param directoryPath
     * @return
     */
    public List<StorePictureItemDto> getList(Map<String, List<StorePictureItemDto>> map, String directoryPath){
        List<StorePictureItemDto> list = new ArrayList<>();
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
    public List<StorePictureItemDto> getList(List<StorePictureItemDto> searchList, String directoryPath){
        List<StorePictureItemDto> list = new ArrayList<>();
        if(!check(searchList,directoryPath)){
            return list;
        }

        for(StorePictureItemDto itemDto : searchList){
            if(itemDto.directoryPath.equals(directoryPath)){
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
    public List<StorePictureItemDto> getAllList(Map<String, List<StorePictureItemDto>> map){
        List<StorePictureItemDto> list = new ArrayList<>();
        if(map != null){
            Iterator<Map.Entry<String, List<StorePictureItemDto>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()){
                list.addAll(iterator.next().getValue());
            }
        }
        list = wipeOffRepetitionDto(list);
        Collections.sort(list,sortList);
        return list;
    }

    /**
     * 获取所有的数据集合，以map的形式返回
     * @return
     */
    public Map<String,List<StorePictureItemDto>> getAllMapList(){
        Map<String, List<StorePictureItemDto>> mapInfoList = getInfoList();
        Map<String, List<StorePictureItemDto>> mapList = new HashMap<>();
        Iterator<Map.Entry<String, List<StorePictureItemDto>>> iterator = mapInfoList.entrySet().iterator();
        Map.Entry<String, List<StorePictureItemDto>> next;
        List<StorePictureItemDto> list;
        while (iterator.hasNext()){
            next = iterator.next();
            list = wipeOffRepetitionDto(next.getValue());
            Collections.sort(list,sortList);
            mapList.put(next.getKey(),list);
        }
        return mapList;
    }

    //排序方法
    private Comparator<StorePictureItemDto> sortList = new Comparator<StorePictureItemDto>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(StorePictureItemDto o1, StorePictureItemDto o2) {
            int compare = Long.compare(o1.timeModify, o2.timeModify);
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
    private List<StorePictureItemDto> wipeOffRepetitionDto(List<StorePictureItemDto> searchList){
        List<StorePictureItemDto> list = new ArrayList<>();
        if(!check(searchList)){
            return list;
        }
        Map<String,StorePictureItemDto> map = new HashMap<>();
        for(StorePictureItemDto itemDto : searchList){
            map.put(itemDto.absolutePath,itemDto);
        }
        list.addAll(map.values());
        return list;
    }

    /**
     * 基方法，获取系统数据库图片列表并以map的形式存储
     * @return
     */
    private Map<String,List<StorePictureItemDto>> getInfoList(){
        Map<String,List<StorePictureItemDto>> map = new HashMap<>();

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        //查询图片
        Cursor cursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/bmp"}, MediaStore.Images.Media.DATE_MODIFIED);

        List<StorePictureItemDto> list = null;
        StorePictureItemDto storePictureItemDto;
        int pictureDegree;
        File absolutePathFile;
        while (cursor.moveToNext()){
            storePictureItemDto = new StorePictureItemDto();
            absolutePathFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            //先判定文件是否存在同时大小要不能小于0，不存在则不要管
            if(absolutePathFile.getAbsolutePath() != null
                    && absolutePathFile.isFile()
                    && absolutePathFile.exists()
                    && absolutePathFile.length() > 0
                    && cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null){
                storePictureItemDto.absolutePath = absolutePathFile.getAbsolutePath();
            }else {
                continue;
            }

            storePictureItemDto._id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            storePictureItemDto.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            storePictureItemDto.fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            storePictureItemDto.mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            storePictureItemDto.timeAdd = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            storePictureItemDto.timeModify = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            storePictureItemDto.lat = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
            storePictureItemDto.lng = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
            storePictureItemDto.width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            storePictureItemDto.height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
            storePictureItemDto.orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
            storePictureItemDto.directoryPath = String.valueOf(storePictureItemDto.absolutePath)
                    .replace(String.valueOf(storePictureItemDto.fileName), "");//使用intern()方法防止原有数据被破坏，该方法会创建一个新的对象字符串

            list = map.get(storePictureItemDto.directoryPath);
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(storePictureItemDto);
            map.put(storePictureItemDto.directoryPath,list);
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
