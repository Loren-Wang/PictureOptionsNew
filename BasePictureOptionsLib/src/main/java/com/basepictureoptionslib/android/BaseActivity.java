package com.basepictureoptionslib.android;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.basepictureoptionslib.android.config.BaseConfig;
import com.basepictureoptionslib.android.utils.LogUtils;
import com.lorenwang.tools.android.ParamsAndJudgeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 创建时间： 0026/2018/2/26 下午 2:53
 * 创建人：王亮（Loren wang）
 * 功能作用：activity基类，用来添加一些基础方法
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getName() + hashCode();

    private View viewAcBar;//标题栏
    private TextView tvTitle;//标题
    protected Button btnCancel;//取消按钮
    protected Button btnConfirm;//确认按钮

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
     * 设置标题栏属性（只有在选择页以及裁剪页才会用的到）
     */
    protected void setAcBar(BaseConfig baseConfig){
        viewAcBar = findViewById(R.id.viewOpAcBar);
        tvTitle = findViewById(R.id.tvOpTitle);
        btnCancel = findViewById(R.id.btnOpCancel);
        btnConfirm = findViewById(R.id.btnOpConfirm);
        if(baseConfig != null && viewAcBar != null && btnCancel != null
                && tvTitle != null && btnConfirm != null){

            //设置标题栏最外层布局属性
            ViewGroup.LayoutParams viewAcBarLayoutParams = viewAcBar.getLayoutParams();
            int height = ParamsAndJudgeUtils.dip2px(getApplicationContext(),baseConfig.getaBarHeight());
            if(viewAcBarLayoutParams == null){
                viewAcBarLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
            }else {
                viewAcBarLayoutParams.height = height;
            }
            viewAcBar.setBackgroundResource(baseConfig.getaBarColor());
            viewAcBar.setLayoutParams(viewAcBarLayoutParams);

            //设置标题文字以及颜色
            tvTitle.setTextColor(baseConfig.getTitleColor());
            if(baseConfig.getTitleText() != null){
                tvTitle.setText(baseConfig.getTitleText());
            }else {
                tvTitle.setText(R.string.app_name);
            }

            //设置标题栏确认取消文字大小颜色
            btnConfirm.setTextSize(baseConfig.getaBarTextSize());
            btnCancel.setTextSize(baseConfig.getaBarTextSize());
            btnConfirm.setTextColor(baseConfig.getaBarTextColor());
            btnCancel.setTextColor(baseConfig.getaBarTextColor());
        }
    }

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
        viewAcBar = null;
        tvTitle = null;
        btnCancel = null;
        btnConfirm = null;
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
