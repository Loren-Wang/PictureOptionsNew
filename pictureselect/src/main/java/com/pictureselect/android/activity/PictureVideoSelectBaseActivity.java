package com.pictureselect.android.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.lorenwang.tools.android.LogUtils;
import com.lorenwang.tools.android.ParamsAndJudgeUtils;

import java.util.ArrayList;
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
}
