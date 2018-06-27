package com.pictureselect.android.activity;

import com.basepictureoptionslib.android.BaseActivity;
import com.pictureselect.android.PictureVideoSelectConfirg;
import com.pictureselect.android.R;
import com.pictureselect.android.dto.StorePictureVideoItemDto;

import java.util.ArrayList;
import java.util.Iterator;


public abstract class BasePictureVideoActivity extends BaseActivity {
    protected boolean isSelectPicture = false;//是否选中了图片
    protected boolean isSelectVideo = false;//是否选择了视频
    protected PictureVideoSelectConfirg pictureSelectConfirg;
    protected ArrayList<StorePictureVideoItemDto> allList = new ArrayList<>();//所有的图片集合
    protected ArrayList<StorePictureVideoItemDto> selectedPicturesList = new ArrayList<StorePictureVideoItemDto>();//已经选中的图片列表,使用哈希表存储已选中的数据由于key的唯一


    /**
     * 先盘空以及判定最大数量；第二步遍历已选择列表查看是否在已选择列表中有要操作的图片，
     * 如果有同时要被操作状态的参数是选中的话则不操作，否则的话则在已选中里面移除改图片
     * 如果没有但是操作状态是选中的话则在已选中里面新增该图片，否则的话不操作
     *
     * @param selectDto 当前被选中的图片信息
     * @param selectState 是否被选中
     * @param postion 更新位置，位置小于0的话那么就不更新适配器
     */
    protected boolean setSelectForNoCamera(StorePictureVideoItemDto selectDto
            , boolean selectState, int postion){
        //判定传入的数据是否为空
        if(selectDto == null ){
            return false;
        }
        //先判定是否大于最大的选择图片的数量
        if(selectedPicturesList.size() >= getMaxSelectNum() && selectState){
            HintPopUtils.getInstance(getApplicationContext()).toastMsg(R.string.toast_hint_exceed_max_selected_num,null);
            return false;
        }

        //当是选中状态且是不允许图片视频同时选择的时候进行图片视频等逻辑判断
        //先判断是否已经选择了图片，如果已选择就不能在选择视频，如果已选择了视频那么就不能再选择图片
        if(selectState && !pictureSelectConfirg.isAllowConcurrentSelection()
                && ( (isSelectPicture && selectDto.getDuration() > 0) || (isSelectVideo && selectDto.getDuration() == 0)) ){
            HintPopUtils.getInstance(getApplicationContext()).toastMsg(R.string.toast_hint_not_allow_concurrent_selection,null);
            return false;
        }



        //查找相同实体
        Iterator<StorePictureVideoItemDto> iterator = selectedPicturesList.iterator();
        StorePictureVideoItemDto sameOptionsDto = null;//在已选中里面查找是否有和要做操做的图片相同的实体，有的话则不为空
        while (iterator.hasNext()){
            sameOptionsDto = iterator.next();
            if(sameOptionsDto.getAbsolutePath().equals(selectDto.getAbsolutePath())){
                //查找到相同实体，直接弹出
                break;
            }
            sameOptionsDto = null;//没查到相同实体
        }
        iterator = null;

        //设置选中状态改变以及界面改变
        if(selectState && sameOptionsDto == null){
            selectDto.setSelect(selectState);
            selectedPicturesList.add(selectDto);

            //先判断是否允许同时选择
            if(pictureSelectConfirg.isAllowConcurrentSelection()){
                if(selectDto.getDuration() > 0){
                    isSelectVideo = true;
                }else {
                    isSelectPicture = true;
                }
            }else {
                if(selectDto.getDuration() > 0){
                    isSelectVideo = true;
                    isSelectPicture = false;
                }else {
                    isSelectVideo = false;
                    isSelectPicture = true;
                }
            }

            return true;
        }else if(!selectState && sameOptionsDto != null){
            selectDto.setSelect(selectState);
            selectedPicturesList.remove(selectDto);

            //被移除后如果选择列表数量为0的时候选中状态都要为false
            if(selectedPicturesList.size() == 0){
                isSelectPicture = false;
                isSelectVideo = false;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取最大能选中的图片、视频数量
     * @return
     */
    protected int getMaxSelectNum() {
        int maxSelectNum;
        switch (pictureSelectConfirg.getSelectType()){
            case PictureVideoSelectConfirg.SELECT_TYPE_FOR_PICTURE:
                maxSelectNum = pictureSelectConfirg.getMaxSelectPictureNum();
                break;
            case PictureVideoSelectConfirg.SELECT_TYPE_FOR_VIDEO:
                maxSelectNum = pictureSelectConfirg.getMaxSelectVideoNum();
                break;
            case PictureVideoSelectConfirg.SELECT_TYPE_FOR_PICTURE_AND_VIDEO:
                //需要判断是否可以一起选择，如果不可以的话判断下已选择的是什么类型，如果已经选择了视频那么显示
                // 的最大数量应该就是视频的最大数量，反之应该是图片的最大数量
                if(pictureSelectConfirg.isAllowConcurrentSelection()){
                    maxSelectNum = pictureSelectConfirg.getMaxSelectVideoNum() + pictureSelectConfirg.getMaxSelectPictureNum();
                }else if(isSelectPicture){
                    maxSelectNum = pictureSelectConfirg.getMaxSelectPictureNum();
                }else if(isSelectVideo){
                    maxSelectNum = pictureSelectConfirg.getMaxSelectVideoNum();
                }else {
                    maxSelectNum = 1;
                }
                break;
            default:
                maxSelectNum = 1;
                break;
        }
        return maxSelectNum;
    }
}
