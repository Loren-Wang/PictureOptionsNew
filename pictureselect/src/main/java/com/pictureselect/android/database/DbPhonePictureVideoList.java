package com.pictureselect.android.database;

import android.content.Context;

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


//    /**
//     * 获取指定文件夹下的图片的图片列表
//     * @param directoryPath
//     * @return
//     */
//    public List<StorePictureVideoItemDto> getList(Map<String, List<StorePictureVideoItemDto>> map, String directoryPath){
//        List<StorePictureVideoItemDto> list = new ArrayList<>();
//        if(!check(directoryPath)){
//            return list;
//        }
//
//        if(map != null){
//            list = map.get(directoryPath);
//            if(list == null){
//                list = new ArrayList<>();
//            }
//        }
//        list = wipeOffRepetitionDto(list);
//        Collections.sort(list,sortList);
//        return list;
//    }
//
//    /**
//     * 根据传入的列表获取指定文件夹下的图片的图片列表
//     * @param directoryPath
//     * @return
//     */
//    public List<StorePictureVideoItemDto> getList(List<StorePictureVideoItemDto> searchList, String directoryPath){
//        List<StorePictureVideoItemDto> list = new ArrayList<>();
//        if(!check(searchList,directoryPath)){
//            return list;
//        }
//
//        for(StorePictureVideoItemDto itemDto : searchList){
//            if(itemDto.getDirectoryPath().equals(directoryPath)){
//                list.add(itemDto);
//            }
//        }
//        list = wipeOffRepetitionDto(list);
//        Collections.sort(list,sortList);
//        return list;
//    }
//
//    /**
//     * 获取所有的图片
//     * @return
//     */
//    public ArrayList<StorePictureVideoItemDto> getAllList(Map<String, List<StorePictureVideoItemDto>> map){
//        ArrayList<StorePictureVideoItemDto> list = new ArrayList<>();
//        if(map != null){
//            Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = map.entrySet().iterator();
//            while (iterator.hasNext()){
//                list.addAll(iterator.next().getValue());
//            }
//            map.clear();
//            map = null;
//            iterator = null;
//        }
//        list = wipeOffRepetitionDto(list);
//        Collections.sort(list,sortList);
//        return list;
//    }
//
//    /**
//     * 获取所有的图片
//     * @return
//     */
//    public Map<String,List<StorePictureVideoItemDto>> getPictureAllMapList(String selection, String[] selectionArgs){
//        Map<String, List<StorePictureVideoItemDto>> mapInfoList =
//                getInfoListForPicture(new Cursor[]{getInfoListForPictureSystemCursor(selection,selectionArgs)
//                        , DbScanDirForPicture.getInstance(context).getCursor(selection,selectionArgs)});
//        Map<String, List<StorePictureVideoItemDto>> mapList = new HashMap<>();
//        Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = mapInfoList.entrySet().iterator();
//        Map.Entry<String, List<StorePictureVideoItemDto>> next;
//        List<StorePictureVideoItemDto> list;
//        while (iterator.hasNext()){
//            next = iterator.next();
//            list = wipeOffRepetitionDto(next.getValue());
//            Collections.sort(list,sortList);
//            mapList.put(next.getKey(),list);
//            next = null;
//        }
//        mapInfoList.clear();
//        mapInfoList = null;
//        iterator = null;
//        return mapList;
//    }
//    /**
//     * 获取所有的视频
//     * @return
//     * @param minDuration
//     * @param maxDuration
//     */
//    public Map<String,List<StorePictureVideoItemDto>> getVideoAllMapList(String selection, String[] selectionArgs){
//        Map<String, List<StorePictureVideoItemDto>> mapInfoList =
//                getInfoListForVideo(new Cursor[]{getInfoListForVideoSystemCursor(selection,selectionArgs)
//                        ,DbScanDirForVideo.getInstance(context).getCursor(selection,selectionArgs)});
//        Map<String, List<StorePictureVideoItemDto>> mapList = new HashMap<>();
//        Iterator<Map.Entry<String, List<StorePictureVideoItemDto>>> iterator = mapInfoList.entrySet().iterator();
//        Map.Entry<String, List<StorePictureVideoItemDto>> next;
//        List<StorePictureVideoItemDto> list;
//        while (iterator.hasNext()){
//            next = iterator.next();
//            list = wipeOffRepetitionDto(next.getValue());
//            Collections.sort(list,sortList);
//            mapList.put(next.getKey(),list);
//            next = null;
//        }
//        mapInfoList.clear();
//        mapInfoList = null;
//        iterator = null;
//        return mapList;
//    }
//
//    /**
//     * 获取所有的数据集合，以map的形式返回
//     * @return
//     * @param minDuration
//     * @param maxDuration
//     */
//    public Map<String,List<StorePictureVideoItemDto>> getAllMapList(String pictureSelection
//            , String[] pictureSelectionArgs,String videoSelection, String[] videoSelectionArgs){
//        Map<String, List<StorePictureVideoItemDto>> mapPictureInfoList =
//                getInfoListForPicture(new Cursor[]{getInfoListForPictureSystemCursor(pictureSelection,pictureSelectionArgs),
//                        DbScanDirForPicture.getInstance(context).getCursor(pictureSelection, pictureSelectionArgs)});
//        Map<String, List<StorePictureVideoItemDto>> mapVideoInfoList =
//                getInfoListForVideo(new Cursor[]{getInfoListForVideoSystemCursor(videoSelection,videoSelectionArgs)
//                        ,DbScanDirForVideo.getInstance(context).getCursor(videoSelection, videoSelectionArgs)});
//        Map<String, List<StorePictureVideoItemDto>> resultMap = new HashMap<>();
//        String key;
//        List<StorePictureVideoItemDto> list;
//        List<StorePictureVideoItemDto> resultMapList;
//
//        //图片列表
//        Iterator<String> pictureMapKeyListIterator = mapPictureInfoList.keySet().iterator();
//        while (pictureMapKeyListIterator.hasNext()){
//            key = pictureMapKeyListIterator.next();
//            list = mapPictureInfoList.get(key);
//            resultMapList = resultMap.get(key);
//            if(resultMapList == null){
//                resultMapList = new ArrayList<>();
//            }
//            if(list != null) {
//                //去重
//                wipeOffRepetitionDto(resultMapList);
//                //排序
//                Collections.sort(list,sortList);
//                resultMapList.addAll(list);
//            }
//            resultMap.put(key,resultMapList);
//            key = null;
//        }
//        pictureMapKeyListIterator = null;
//        //视频列表
//        Iterator<String> videoMapKeyListIterator = mapVideoInfoList.keySet().iterator();
//        while (videoMapKeyListIterator.hasNext()){
//            key = videoMapKeyListIterator.next();
//            list = mapVideoInfoList.get(key);
//            resultMapList = resultMap.get(key);
//            if(resultMapList == null){
//                resultMapList = new ArrayList<>();
//            }
//            if(list != null) {
//                //去重
//                wipeOffRepetitionDto(resultMapList);
//                //排序
//                Collections.sort(list,sortList);
//                resultMapList.addAll(list);
//            }
//            resultMap.put(key,resultMapList);
//            key = null;
//        }
//        videoMapKeyListIterator = null;
//
//        mapPictureInfoList.clear();
//        mapPictureInfoList = null;
//        mapVideoInfoList.clear();
//        mapVideoInfoList = null;
//
//        return resultMap;
//    }
//
//    //排序方法
//    private Comparator<StorePictureVideoItemDto> sortList = new Comparator<StorePictureVideoItemDto>() {
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        public int compare(StorePictureVideoItemDto o1, StorePictureVideoItemDto o2) {
//            int compare = Long.compare(o1.getTimeModify(), o2.getTimeModify());
//            if(compare > 0){
//                return -1;
//            }else if(compare < 0){
//                return 1;
//            }else {
//                return 0;
//            }
//        }
//    };
//
//    //去除重复项
//    private ArrayList<StorePictureVideoItemDto> wipeOffRepetitionDto(List<StorePictureVideoItemDto> searchList){
//        ArrayList<StorePictureVideoItemDto> list = new ArrayList<>();
//        if(!check(searchList)){
//            return list;
//        }
//        Map<String,StorePictureVideoItemDto> map = new HashMap<>();
//        for(StorePictureVideoItemDto itemDto : searchList){
//            map.put(itemDto.getAbsolutePath(),itemDto);
//        }
//        list.addAll(map.values());
//        searchList.clear();
//        searchList = null;
//        map.clear();
//        map = null;
//
//        return list;
//    }
//
//    /**
//     * 基方法，获取系统数据库图片列表并以map的形式存储
//     * @return
//     */
//    private Map<String,List<StorePictureVideoItemDto>> getInfoListForPicture(Cursor[] mImageCursors){
//        Map<String,List<StorePictureVideoItemDto>> map = new HashMap<>();
//
//        for(Cursor cursor : mImageCursors){
//            List<StorePictureVideoItemDto> list = null;
//            StorePictureVideoItemDto storePictureItemDto;
//            File absolutePathFile;
//            while (cursor.moveToNext()) {
//                storePictureItemDto = new StorePictureVideoItemDto();
//                absolutePathFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
//                //先判定文件是否存在同时大小要不能小于0，不存在则不要管
//                if (absolutePathFile.getAbsolutePath() != null
//                        && absolutePathFile.isFile()
//                        && absolutePathFile.exists()
//                        && absolutePathFile.length() > 0
//                        && cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null) {
//                    storePictureItemDto.setAbsolutePath(absolutePathFile.getAbsolutePath());
//                    absolutePathFile = null;
//                } else {
//                    continue;
//                }
//
//                storePictureItemDto.set_id(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
//                storePictureItemDto.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
//                storePictureItemDto.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
//                storePictureItemDto.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));
//                storePictureItemDto.setTimeAdd(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
//                storePictureItemDto.setTimeModify(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)));
//                storePictureItemDto.setLat(cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE)));
//                storePictureItemDto.setLng(cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE)));
//                storePictureItemDto.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)));
//                storePictureItemDto.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)));
//                storePictureItemDto.setOrientation(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)));
//                storePictureItemDto.setDuration(0l);
//                storePictureItemDto.setDirectoryPath(String.valueOf(storePictureItemDto.getAbsolutePath())
//                        .replace(String.valueOf(storePictureItemDto.getFileName()), ""));//使用intern()方法防止原有数据被破坏，该方法会创建一个新的对象字符串
//
//                list = map.get(storePictureItemDto.getDirectoryPath());
//                if (list == null) {
//                    list = new ArrayList<>();
//                }
//                list.add(storePictureItemDto);
//                map.put(storePictureItemDto.getDirectoryPath(), list);
//            }
//            cursor.close();
//            cursor = null;
//            storePictureItemDto = null;
//        }
//        mImageCursors = null;
//        return map;
//    }
//
//    private Cursor getInfoListForPictureSystemCursor(String selection, String[] selectionArgs){
//        ContentResolver mContentResolver = context.getContentResolver();
//        return mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
//                selection,selectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
//    }
//
//    /**
//     * 基方法，获取系统数据库图片列表并以map的形式存储
//     * @return
//     */
//    private Map<String,List<StorePictureVideoItemDto>> getInfoListForVideo(Cursor[] mVideoCursors){
//        Map<String,List<StorePictureVideoItemDto>> map = new HashMap<>();
//        for(Cursor cursor : mVideoCursors){
//            List<StorePictureVideoItemDto> list = null;
//            StorePictureVideoItemDto storePictureItemDto;
//            File absolutePathFile;
//            while (cursor.moveToNext()) {
//                storePictureItemDto = new StorePictureVideoItemDto();
//                absolutePathFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
//                //先判定文件是否存在同时大小要不能小于0，不存在则不要管
//                if (absolutePathFile.getAbsolutePath() != null
//                        && absolutePathFile.isFile()
//                        && absolutePathFile.exists()
//                        && absolutePathFile.length() > 0
//                        && cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null) {
//                    storePictureItemDto.setAbsolutePath(absolutePathFile.getAbsolutePath());
//                    absolutePathFile = null;
//                } else {
//                    continue;
//                }
//
//                storePictureItemDto.set_id(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
//                storePictureItemDto.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
//                storePictureItemDto.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
//                storePictureItemDto.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)));
//                storePictureItemDto.setTimeAdd(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
//                storePictureItemDto.setTimeModify(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)));
//                storePictureItemDto.setLat(cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE)));
//                storePictureItemDto.setLng(cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE)));
//                storePictureItemDto.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH)));
//                storePictureItemDto.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT)));
//                storePictureItemDto.setOrientation(0);
//                storePictureItemDto.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
//                storePictureItemDto.setDirectoryPath(String.valueOf(storePictureItemDto.getAbsolutePath())
//                        .replace(String.valueOf(storePictureItemDto.getFileName()), ""));//使用intern()方法防止原有数据被破坏，该方法会创建一个新的对象字符串
//
//                list = map.get(storePictureItemDto.getDirectoryPath());
//                if (list == null) {
//                    list = new ArrayList<>();
//                }
//                list.add(storePictureItemDto);
//                map.put(storePictureItemDto.getDirectoryPath(), list);
//            }
//            cursor.close();
//            cursor = null;
//            storePictureItemDto = null;
//        }
//        mVideoCursors = null;
//        return map;
//    }
//
//    private Cursor getInfoListForVideoSystemCursor(String selection, String[] selectionArgs){
//        ContentResolver mContentResolver = context.getContentResolver();
//        return mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
//                selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
//    }
//
//    /**
//     * 检查传入参数是否为空
//     * @param objects
//     * @return
//     */
//    protected boolean check(Object... objects){
//        for(int i = 0 ; i < objects.length ; i++){
//            if(objects[i] != null && objects[i] instanceof String){
//                return !((String) objects[i]).isEmpty();
//            }
//            return objects[i] != null;
//        }
//
//        return true;
//    }
}
