package com.pictureselect.android.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangliang on 0013/2017/3/13.
 * 创建时间： 0013/2017/3/13 16:28
 * 创建人：王亮（Loren wang）
 * 功能作用：解析系统是图片数据库的实体类
 * 思路：
 * 修改人：
 * 修改时间：
 */
public class StorePictureItemDto implements Parcelable {
    private Integer _id;//主键id
    private String directoryPath;//文件所在的文件夹
    private String absolutePath;//绝对路径
    private Long size;//文件大小
    private String fileName;//文件名称
    private String mimeType;//文件类型（image/png or image/jpeg or ...）
    private Long timeAdd;//添加进入系统数据库的时间（毫秒值）
    private Long timeModify;//图片最后的修改时间
    private Double lat;//拍摄的照片的纬度坐标
    private Double lng;//拍摄的照片的经度坐标
    private Integer orientation;//旋转角度
    private Integer width;//图片高度
    private Integer height;//图片宽度
    private boolean isSelect = false;//是否选中

    public Integer get_id() {
        return _id;
    }

    public StorePictureItemDto set_id(Integer _id) {
        this._id = _id;
        return this;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public StorePictureItemDto setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public StorePictureItemDto setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public StorePictureItemDto setSize(Long size) {
        this.size = size;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public StorePictureItemDto setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public StorePictureItemDto setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Long getTimeAdd() {
        return timeAdd;
    }

    public StorePictureItemDto setTimeAdd(Long timeAdd) {
        this.timeAdd = timeAdd;
        return this;
    }

    public Long getTimeModify() {
        return timeModify;
    }

    public StorePictureItemDto setTimeModify(Long timeModify) {
        this.timeModify = timeModify;
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public StorePictureItemDto setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public Double getLng() {
        return lng;
    }

    public StorePictureItemDto setLng(Double lng) {
        this.lng = lng;
        return this;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public StorePictureItemDto setOrientation(Integer orientation) {
        this.orientation = orientation;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public StorePictureItemDto setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public StorePictureItemDto setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public StorePictureItemDto setSelect(boolean select) {
        isSelect = select;
        return this;
    }

    @Override
    public String toString() {
        return "StorePictureItemDto{" +
                "_id='" + _id + '\'' +
                ", directoryPath='" + directoryPath + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", size=" + size +
                ", fileName='" + fileName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", timeAdd=" + timeAdd +
                ", timeModify=" + timeModify +
                ", lat=" + lat +
                ", lng=" + lng +
                ", orientation=" + orientation +
                ", width=" + width +
                ", height=" + height +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.directoryPath);
        dest.writeString(this.absolutePath);
        dest.writeValue(this.size);
        dest.writeString(this.fileName);
        dest.writeString(this.mimeType);
        dest.writeValue(this.timeAdd);
        dest.writeValue(this.timeModify);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeValue(this.orientation);
        dest.writeValue(this.width);
        dest.writeValue(this.height);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    public StorePictureItemDto() {
    }

    protected StorePictureItemDto(Parcel in) {
        this._id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.directoryPath = in.readString();
        this.absolutePath = in.readString();
        this.size = (Long) in.readValue(Long.class.getClassLoader());
        this.fileName = in.readString();
        this.mimeType = in.readString();
        this.timeAdd = (Long) in.readValue(Long.class.getClassLoader());
        this.timeModify = (Long) in.readValue(Long.class.getClassLoader());
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.orientation = (Integer) in.readValue(Integer.class.getClassLoader());
        this.width = (Integer) in.readValue(Integer.class.getClassLoader());
        this.height = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<StorePictureItemDto> CREATOR = new Creator<StorePictureItemDto>() {
        @Override
        public StorePictureItemDto createFromParcel(Parcel source) {
            return new StorePictureItemDto(source);
        }

        @Override
        public StorePictureItemDto[] newArray(int size) {
            return new StorePictureItemDto[size];
        }
    };
}
