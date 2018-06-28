package com.pictureselect.android.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间： 0026/2018/2/26 下午 3:10
 * 创建人：王亮（Loren wang）
 * 功能作用：图片视频选择配置类，例如最大张数等等
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：    1、图片最大选择张数
 2、视频最大选择数量；
 3、已选择图片视频列表（路径列表）；
 4、是否需要预览（默认需要）；
 5、是否需要显示原图选择（默认不需要）
 6、是否允许图片视频同时选择（默认不允许）；
 7、图片视频选择类型；
 8、图片数据库筛选条件；
 9、视频数据库筛选条件；
 */
public class PictureVideoSelectConfirg implements Parcelable{
    public static final int SELECT_TYPE_FOR_PICTURE = 0;//仅选择图片
    public static final int SELECT_TYPE_FOR_VIDEO = 1;//仅选择视频
    public static final int SELECT_TYPE_FOR_PICTURE_AND_VIDEO = 2;//图片视频一起选择

    //图片的与FILTER_PICTURE_FOR_NONE为0
    public static final int FILTER_PICTURE_FOR_NONE = 1;//筛选图片无限制
    public static final int FILTER_PICTURE_FOR_SIZE = 2;//根据图片大小限制图片选择
    public static final int FILTER_PICTURE_FOR_WIDTH_HEIGHT = 4;//根据图片宽高分辨率限制图片选择
    public static final int FILTER_PICTURE_FOR_SIZE_AND_WIDTH_HEIGHT = 8;//根据图片大小以及宽高分辨率限制图片选择
    //视频的或FILTER_VIDEO_FOR_NONE为255
    public static final int FILTER_VIDEO_FOR_NONE = 255;//筛选视频无限制
    public static final int FILTER_VIDEO_FOR_SIZE = 254;//根据视频大小限制选择
    public static final int FILTER_VIDEO_FOR_DURATION = 252;//根据视频时长限制选择
    public static final int FILTER_VIDEO_FOR_WIDTH_HEIGHT = 248;//根据视频宽高分辨率限制选择
    public static final int FILTER_VIDEO_FOR_SIZE_AND_DURATION = 240;//根据视频大小和时长限制选择
    public static final int FILTER_VIDEO_FOR_SIZE_AND_WIDTH_HEIGHT = 224;//根据视频大小和宽高分辨率限制选择
    public static final int FILTER_VIDEO_FOR_DURATION_AND_WIDTH_HEIGHT = 192;//根据视频时长和宽高分辨率限制选择
    public static final int FILTER_VIDEO_FOR_SIZE_AND_DURATION_AND_WIDTH_HEIGHT = 128;//根据视频大小、时长和宽高分辨率限制选择

    //图片数据库读取条件
    private StringBuffer pictureFilterSelection = new StringBuffer(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
            .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
            .append(MediaStore.Images.Media.MIME_TYPE).append("=?");
    private List<String> pictureFilterSelectionArgs = new ArrayList<>();
    private String[] pictureFilterSelectionArg;
    //视频数据库读取条件
    private StringBuffer videoFilterSelection = new StringBuffer(MediaStore.Images.Media.MIME_TYPE).append("=?");
    private List<String> videoFilterSelectionArgs = new ArrayList<>();
    private String[] videoFilterSelectionArg;

    private int maxSelectPictureNum = 9;//最大选择张数
    private int maxSelectVideoNum = 1;//最大选择视频的数量
    private List<String> selectedPicList = new ArrayList<>();//已选择图片列表
    private boolean isShowPreview = true;//是否需要预览（即是否显示预览按钮以及点击图片预览），默认不需要
    private boolean isShowOriginPicSelect = false;//是否需要显示原图选择
    private boolean isAllowConcurrentSelection = false;//是否允许图片视频同时选择
    private int selectType = SELECT_TYPE_FOR_PICTURE_AND_VIDEO;//选择类型


    public PictureVideoSelectConfirg() {
        try {
            setPictureFilter(FILTER_PICTURE_FOR_NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            setVideoFilter(FILTER_VIDEO_FOR_NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxSelectPictureNum() {
        return maxSelectPictureNum;
    }

    public PictureVideoSelectConfirg setMaxSelectPictureNum(int maxSelectPictureNum) {
        this.maxSelectPictureNum = maxSelectPictureNum;
        return this;
    }

    public int getMaxSelectVideoNum() {
        return maxSelectVideoNum;
    }

    public PictureVideoSelectConfirg setMaxSelectVideoNum(int maxSelectVideoNum) {
        this.maxSelectVideoNum = maxSelectVideoNum;
        return this;
    }

    public List<String> getSelectedPicList() {
        return selectedPicList;
    }

    public PictureVideoSelectConfirg setSelectedPicList(List<String> selectedPicList) {
        this.selectedPicList = selectedPicList;
        return this;
    }

    public boolean isShowPreview() {
        return isShowPreview;
    }

    public PictureVideoSelectConfirg setShowPreview(boolean showPreview) {
        isShowPreview = showPreview;
        return this;
    }

    public boolean isShowOriginPicSelect() {
        return isShowOriginPicSelect;
    }

    public PictureVideoSelectConfirg setShowOriginPicSelect(boolean showOriginPicSelect) {
        isShowOriginPicSelect = showOriginPicSelect;
        return this;
    }

    public boolean isAllowConcurrentSelection() {
        return isAllowConcurrentSelection;
    }

    public PictureVideoSelectConfirg setAllowConcurrentSelection(boolean allowConcurrentSelection) {
        isAllowConcurrentSelection = allowConcurrentSelection;
        return this;
    }

    public int getSelectType() {
        return selectType;
    }

    public PictureVideoSelectConfirg setSelectType(int selectType) {
        this.selectType = selectType;
        return this;
    }

    /**
     * 图片筛选条件
     * @param filterType
     * @param filters filterType 为 FILTER_PICTURE_FOR_NONE时参数：无限制，多出的属性不记录
     *                filterType 为 FILTER_PICTURE_FOR_SIZE时参数：必须有最大最小或最小其中一个，否则抛出异常，多出的属性不记录
     *                filterType 为 FILTER_PICTURE_FOR_WIDTH_HEIGHT时参数：必须有最大最小宽高或最小其中一个，否则抛出异常，多出的属性不记录
     *                filterType 为 FILTER_PICTURE_FOR_SIZE_AND_WIDTH_HEIGHT时参数：必须有最大最小宽高或最小其中一个以及最大最小或最小其中一个，否则抛出异常，多出的属性不记录
     * @return
     */
    public PictureVideoSelectConfirg setPictureFilter(int filterType,Object... filters) throws Exception {
        if(Integer.compare(filterType,FILTER_PICTURE_FOR_NONE) != 0 && (filterType & FILTER_PICTURE_FOR_NONE) != 0){
            return this;
        }

        pictureFilterSelection = new StringBuffer("(")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=?)");
        pictureFilterSelectionArgs.clear();
        pictureFilterSelectionArgs.add("image/jpeg");
        pictureFilterSelectionArgs.add("image/png");
        pictureFilterSelectionArgs.add("image/bmp");

        Integer minSize;
        Integer maxSize;
        Integer minWidth;
        Integer maxWidth;
        Integer minHeight;
        Integer maxHeight;

        switch (filterType){
            case FILTER_PICTURE_FOR_SIZE://只取0,1位置，为最大最小值，必须有参，可以为空
                minSize = (Integer) filters[0];
                maxSize = (Integer) filters[1];
                filterSizeAndDuration(minSize,maxSize,pictureFilterSelection,pictureFilterSelectionArgs,MediaStore.Images.Media.SIZE);
                break;
            case FILTER_PICTURE_FOR_WIDTH_HEIGHT://只取0,1,2,3位置，为最大最小值，必须有参，可以为空
                minWidth = (Integer) filters[0];
                maxWidth = (Integer) filters[1];
                minHeight = (Integer) filters[2];
                maxHeight = (Integer) filters[3];
                //检测是否同时为空
                if(minWidth == null && maxWidth == null && minHeight == null && maxHeight == null){
                    throw new Exception("传入的图片宽高分辨率筛选参数数据异常");
                }
                //宽度判定及新增
                filterWidthHeight(minWidth,maxWidth,pictureFilterSelection,pictureFilterSelectionArgs,MediaStore.Images.Media.WIDTH);
                filterWidthHeight(minHeight,maxHeight,pictureFilterSelection,pictureFilterSelectionArgs,MediaStore.Images.Media.HEIGHT);

                break;
            case FILTER_PICTURE_FOR_SIZE_AND_WIDTH_HEIGHT:
                minSize = (Integer) filters[0];
                maxSize = (Integer) filters[1];
                minWidth = (Integer) filters[2];
                maxWidth = (Integer) filters[3];
                minHeight = (Integer) filters[4];
                maxHeight = (Integer) filters[5];
                //检测是否同时为空
                if(minWidth == null && maxWidth == null && minHeight == null && maxHeight == null){
                    throw new Exception("传入的图片宽高分辨率筛选参数数据异常");
                }
                //大小判定及新增
                filterSizeAndDuration(minSize,maxSize,pictureFilterSelection,pictureFilterSelectionArgs,MediaStore.Images.Media.SIZE);
                //宽度判定及新增
                filterWidthHeight(minWidth,maxWidth,pictureFilterSelection,pictureFilterSelectionArgs,MediaStore.Images.Media.WIDTH);
                filterWidthHeight(minHeight,maxHeight,pictureFilterSelection,pictureFilterSelectionArgs,MediaStore.Images.Media.HEIGHT);
                break;
            case FILTER_PICTURE_FOR_NONE:
                default:
                break;
        }
        pictureFilterSelectionArg = new String[pictureFilterSelectionArgs.size()];
        pictureFilterSelectionArgs.toArray(pictureFilterSelectionArg);

        return this;
    }
    /**
     * 视频筛选条件
     * @param filterType
     * @param filters
     * @return
     * @throws Exception
     */
    public PictureVideoSelectConfirg setVideoFilter(int filterType,Object... filters) throws Exception{
        if(Integer.compare(filterType,FILTER_VIDEO_FOR_NONE) != 0 && (filterType | FILTER_VIDEO_FOR_NONE) != 255){
            return this;
        }

        videoFilterSelection = new StringBuffer(MediaStore.Images.Media.MIME_TYPE).append("=?");
        videoFilterSelectionArgs.clear();
        videoFilterSelectionArgs.add("video/mp4");

        Integer minSize;
        Integer maxSize;
        Integer minWidth;
        Integer maxWidth;
        Integer minHeight;
        Integer maxHeight;
        Integer minDuration;
        Integer maxDuration;

        switch (filterType){
            case FILTER_VIDEO_FOR_SIZE://根据视频大小限制选择
                minSize = (Integer) filters[0];
                maxSize = (Integer) filters[1];
                filterSizeAndDuration(minSize,maxSize,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.SIZE);
                break;
            case FILTER_VIDEO_FOR_DURATION://根据视频时长限制选择
                minDuration = (Integer) filters[0];
                maxDuration = (Integer) filters[1];
                filterSizeAndDuration(minDuration,maxDuration,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.DURATION);
                break;
            case FILTER_VIDEO_FOR_WIDTH_HEIGHT://根据视频宽高分辨率限制选择
                minWidth = (Integer) filters[0];
                maxWidth = (Integer) filters[1];
                minHeight = (Integer) filters[2];
                maxHeight = (Integer) filters[3];
                //检测是否同时为空
                if(minWidth == null && maxWidth == null && minHeight == null && maxHeight == null){
                    throw new Exception("传入的视频宽高分辨率筛选参数数据异常");
                }
                //宽度判定及新增
                filterWidthHeight(minWidth,maxWidth,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.WIDTH);
                filterWidthHeight(minHeight,maxHeight,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.HEIGHT);
                break;
            case FILTER_VIDEO_FOR_SIZE_AND_DURATION://根据视频大小和时长限制选择
                minSize = (Integer) filters[0];
                maxSize = (Integer) filters[1];
                minDuration = (Integer) filters[2];
                maxDuration = (Integer) filters[3];
                filterSizeAndDuration(minDuration,maxDuration,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.DURATION);
                filterSizeAndDuration(minSize,maxSize,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.SIZE);
                break;
            case FILTER_VIDEO_FOR_SIZE_AND_WIDTH_HEIGHT://根据视频大小和宽高分辨率限制选择
                minSize = (Integer) filters[0];
                maxSize = (Integer) filters[1];
                minWidth = (Integer) filters[2];
                maxWidth = (Integer) filters[3];
                minHeight = (Integer) filters[4];
                maxHeight = (Integer) filters[5];
                //检测是否同时为空
                if(minWidth == null && maxWidth == null && minHeight == null && maxHeight == null){
                    throw new Exception("传入的视频宽高分辨率筛选参数数据异常");
                }
                filterSizeAndDuration(minSize,maxSize,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.SIZE);
                //宽度判定及新增
                filterWidthHeight(minWidth,maxWidth,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.WIDTH);
                filterWidthHeight(minHeight,maxHeight,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.HEIGHT);
                break;
            case FILTER_VIDEO_FOR_DURATION_AND_WIDTH_HEIGHT://根据视频时长和宽高分辨率限制选择
                minDuration = (Integer) filters[0];
                maxDuration = (Integer) filters[1];
                minWidth = (Integer) filters[2];
                maxWidth = (Integer) filters[3];
                minHeight = (Integer) filters[4];
                maxHeight = (Integer) filters[5];
                //检测是否同时为空
                if(minWidth == null && maxWidth == null && minHeight == null && maxHeight == null){
                    throw new Exception("传入的视频宽高分辨率筛选参数数据异常");
                }
                filterSizeAndDuration(minDuration,maxDuration,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.DURATION);
                //宽度判定及新增
                filterWidthHeight(minWidth,maxWidth,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.WIDTH);
                filterWidthHeight(minHeight,maxHeight,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.HEIGHT);
                break;
            case FILTER_VIDEO_FOR_SIZE_AND_DURATION_AND_WIDTH_HEIGHT://根据视频大小、时长和宽高分辨率限制选择
                minSize = (Integer) filters[0];
                maxSize = (Integer) filters[1];
                minDuration = (Integer) filters[2];
                maxDuration = (Integer) filters[3];
                minWidth = (Integer) filters[4];
                maxWidth = (Integer) filters[5];
                minHeight = (Integer) filters[6];
                maxHeight = (Integer) filters[7];
                //检测是否同时为空
                if(minWidth == null && maxWidth == null && minHeight == null && maxHeight == null){
                    throw new Exception("传入的视频宽高分辨率筛选参数数据异常");
                }
                filterSizeAndDuration(minSize,maxSize,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.SIZE);
                filterSizeAndDuration(minDuration,maxDuration,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.DURATION);
                //宽度判定及新增
                filterWidthHeight(minWidth,maxWidth,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.WIDTH);
                filterWidthHeight(minHeight,maxHeight,videoFilterSelection,videoFilterSelectionArgs,MediaStore.Video.Media.HEIGHT);
                break;
            case FILTER_VIDEO_FOR_NONE:
            default:
                break;
        }
        videoFilterSelectionArg = new String[videoFilterSelectionArgs.size()];
        videoFilterSelectionArgs.toArray(videoFilterSelectionArg);
        return this;
    }
    /**
     * 大小筛选条件判定以及新增
     * @param min
     * @param max
     * @param selection
     * @param selectionArgs
     * @param column 字段名
     * @return
     * @throws Exception
     */
    private PictureVideoSelectConfirg filterSizeAndDuration(Integer min,Integer max,StringBuffer selection,List selectionArgs,String column) throws Exception{
        //检测是否同时为空
        if(min == null && max == null){
            throw new Exception("传入的图片大小或者时长筛选参数数据异常");
        }
        //检测最小最大值是否符合规则
        if((min != null && min < 0) || (max != null && max < 0) || (min != null && max != null && min > max)){
            throw new Exception("传入的图片大小或者时长筛选参数数据异常");
        }
        //设置字符串
        if(min != null){
            selection.append(" and ").append(column).append(">=?");
            selectionArgs.add(String.valueOf(min));
        }
        if(max != null){
            selection.append(" and ").append(column).append("<?");
            selectionArgs.add(String.valueOf(max));
        }
        return this;
    }
    /**
     * 宽高分辨率筛选条件判定以及新增
     * @param min
     * @param max
     * @param selection
     * @param selectionArgs
     * @param column 字段名
     * @return
     * @throws Exception
     */
    private PictureVideoSelectConfirg filterWidthHeight(Integer min,Integer max,StringBuffer selection,List selectionArgs,String column) throws Exception {
        //检测是否宽度符合规则、
        if((min != null && min < 0) || (max != null && max < 0) || (min != null && max != null && min > max)){
            throw new Exception("传入的宽高分辨率筛选参数数据异常");
        }
        //设置字符串
        if(min != null){
            selection.append(" and ").append(column).append(">=?");
            selectionArgs.add(String.valueOf(min));
        }
        if(max != null){
            selection.append(" and ").append(column).append("<?");
            selectionArgs.add(String.valueOf(max));
        }
        return this;
    }
    public String getPictureFilterSelection() {
        return pictureFilterSelection.toString();
    }
    public String[] getPictureFilterSelectionArgs() {
        return pictureFilterSelectionArg;
    }
    public String getVideoFilterSelection() {
        return videoFilterSelection.toString();
    }
    public String[] getVideoFilterSelectionArgs() {
        return videoFilterSelectionArg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.pictureFilterSelection);
        dest.writeStringList(this.pictureFilterSelectionArgs);
        dest.writeStringArray(this.pictureFilterSelectionArg);
        dest.writeSerializable(this.videoFilterSelection);
        dest.writeStringList(this.videoFilterSelectionArgs);
        dest.writeStringArray(this.videoFilterSelectionArg);
        dest.writeInt(this.maxSelectPictureNum);
        dest.writeInt(this.maxSelectVideoNum);
        dest.writeStringList(this.selectedPicList);
        dest.writeByte(this.isShowPreview ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowOriginPicSelect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAllowConcurrentSelection ? (byte) 1 : (byte) 0);
        dest.writeInt(this.selectType);
    }

    protected PictureVideoSelectConfirg(Parcel in) {
        this.pictureFilterSelection = (StringBuffer) in.readSerializable();
        this.pictureFilterSelectionArgs = in.createStringArrayList();
        this.pictureFilterSelectionArg = in.createStringArray();
        this.videoFilterSelection = (StringBuffer) in.readSerializable();
        this.videoFilterSelectionArgs = in.createStringArrayList();
        this.videoFilterSelectionArg = in.createStringArray();
        this.maxSelectPictureNum = in.readInt();
        this.maxSelectVideoNum = in.readInt();
        this.selectedPicList = in.createStringArrayList();
        this.isShowPreview = in.readByte() != 0;
        this.isShowOriginPicSelect = in.readByte() != 0;
        this.isAllowConcurrentSelection = in.readByte() != 0;
        this.selectType = in.readInt();
    }

    public static final Creator<PictureVideoSelectConfirg> CREATOR = new Creator<PictureVideoSelectConfirg>() {
        @Override
        public PictureVideoSelectConfirg createFromParcel(Parcel source) {
            return new PictureVideoSelectConfirg(source);
        }

        @Override
        public PictureVideoSelectConfirg[] newArray(int size) {
            return new PictureVideoSelectConfirg[size];
        }
    };
}
