package com.pictureselect.android.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.lorenwang.tools.android.LogUtils;
import com.lorenwang.tools.android.ParamsAndJudgeUtils;
import com.lorenwang.tools.android.ToastHintUtils;
import com.pictureselect.android.R;
import com.pictureselect.android.config.PictureVideoSelectConfirg;
import com.pictureselect.android.config.PictureVideoSelectorThemeConfig;
import com.pictureselect.android.dto.StorePictureVideoItemDto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 创建时间： 0027/2018/6/27 下午 5:47
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择器基类
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public abstract class PictureVideoSelectBaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getName() + hashCode();
    private HandlerThread handlerThread = new HandlerThread(getClass().getName());
    protected Handler handlerChild;//异步线程
    protected Handler handlerUi;//ui主线程
    protected boolean isSelectPicture = false;//是否选中了图片
    protected boolean isSelectVideo = false;//是否选择了视频
    protected PictureVideoSelectConfirg pictureSelectConfirg;
    protected PictureVideoSelectorThemeConfig pictureVideoSelectorThemeConfig = PictureVideoSelectorThemeConfig.getInstance();
    protected ArrayList<StorePictureVideoItemDto> allList = new ArrayList<>();//所有的图片集合
    protected ArrayList<StorePictureVideoItemDto> selectedPicturesList = new ArrayList<StorePictureVideoItemDto>();//已经选中的图片列表,使用哈希表存储已选中的数据由于key的唯一


    /**
     * 发起权限请求
     */
    protected void permisstionRequest(@NonNull String[] permisstions, int permissionsRequestCode){
        //版本判断，小于23的不执行权限请求
        if(Build.VERSION.SDK_INT < 23){
            perissionRequestSuccessCallback(ParamsAndJudgeUtils.paramesArrayToList(permisstions),permissionsRequestCode);
            return;
        }else {
            //检测所有的权限是否都已经拥有
            boolean isAllowAllPermisstion = true;
            for (String permisstion : permisstions) {
                if (checkCallingOrSelfPermission(permisstion) != PackageManager.PERMISSION_GRANTED) {
                    isAllowAllPermisstion = false;
                    break;
                }
            }

            //判断所有的权限是否是通过的
            if (isAllowAllPermisstion) {
                perissionRequestSuccessCallback(ParamsAndJudgeUtils.paramesArrayToList(permisstions), permissionsRequestCode);
            } else {//请求权限
                requestPermissions(permisstions, permissionsRequestCode);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> successPermissionList = new ArrayList<>();
        List<String> failPermissionList = new ArrayList<>();

        if(grantResults.length > 0 && grantResults.length == permissions.length) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    successPermissionList.add(permissions[i]);
                    LogUtils.logI("用户同意权限", "user granted the permission!" + permissions[i]);
                } else {
                    LogUtils.logI("用户不同意权限", "user denied the permission!" + permissions[i]);
                    failPermissionList.add(permissions[i]);
                }
            }
        }else {
            for(int i = 0 ; i < permissions.length ; i++){
                failPermissionList.add(permissions[i]);
            }
        }

        try {
            if(failPermissionList.size() > 0){
                perissionRequestFailCallback(failPermissionList,requestCode);
            }else {
                perissionRequestSuccessCallback(successPermissionList, requestCode);
            }
        }catch (Exception e){
            LogUtils.logE(TAG,e.getMessage());
        }finally {
            successPermissionList.clear();
            failPermissionList.clear();
            successPermissionList = null;
            failPermissionList = null;
        }
        return;
    }
    protected void  perissionRequestSuccessCallback(List<String> perissionList, int permissionsRequestCode){};//请求成功权限列表
    protected void  perissionRequestFailCallback(List<String> perissionList, int permissionsRequestCode){};//请求失败权限列表

    /**
     * 初始化线程
     */
    protected void initHandler(){
        handlerThread.start();
        handlerChild = new Handler(handlerThread.getLooper());
        handlerUi = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onDestroy() {
        setContentView(R.layout.activity_options_base_null);
        System.gc();
        //退出looper
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        }else {
            handlerThread.quit();
        }
        handlerThread = null;
        handlerChild = null;
        handlerUi = null;
        super.onDestroy();
    }

    /**
     * 设置控件高度
     * @param view
     * @param width
     * @param height
     */
    protected void setViewWidthHeight(View view,int width,int height){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if(params == null){
            params = new RelativeLayout.LayoutParams(width,height);
        }else {
            if(width >= 0) {
                params.width = width;
            }
            if(height >= 0) {
                params.height = height;
            }
        }
        view.setLayoutParams(params);
    }

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
            ToastHintUtils.getInstance(getApplicationContext()).toastMsg(R.string.toast_hint_exceed_max_selected_num,null);
            return false;
        }

        //当是选中状态且是不允许图片视频同时选择的时候进行图片视频等逻辑判断
        //先判断是否已经选择了图片，如果已选择就不能在选择视频，如果已选择了视频那么就不能再选择图片
        if(selectState && !pictureSelectConfirg.isAllowConcurrentSelection()
                && ( (isSelectPicture && selectDto.getDuration() > 0) || (isSelectVideo && selectDto.getDuration() == 0)) ){
            ToastHintUtils.getInstance(getApplicationContext()).toastMsg(R.string.toast_hint_not_allow_concurrent_selection,null);
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
