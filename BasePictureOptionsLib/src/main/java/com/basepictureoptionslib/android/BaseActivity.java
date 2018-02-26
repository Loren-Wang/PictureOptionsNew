package com.basepictureoptionslib.android;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basepictureoptionslib.android.config.BaseConfig;


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
    private LinearLayout lnAcBar;//标题栏
    private Button btnCancel;//取消按钮
    private TextView tvTitle;//标题
    private Button btnConfirm;//确认按钮

    private HandlerThread handlerThread = new HandlerThread(getClass().getName());
    protected int statusBarHeight;//状态栏高度
    protected Handler handlerChild;//异步线程
    protected Handler handlerUi;//ui主线程

    /**
     * 设置标题栏属性（只有在选择页以及裁剪页才会用的到）
     */
    protected void setAcBar(BaseConfig baseConfig){
        lnAcBar = findViewById(R.id.lnAcBar);
        btnCancel = findViewById(R.id.btnCancel);
        tvTitle = findViewById(R.id.tvTitle);
        btnConfirm = findViewById(R.id.btnConfirm);
        if(baseConfig != null && baseConfig.getaBarHeight() > 0 && lnAcBar != null
                && btnCancel != null && tvTitle != null && btnConfirm != null){

            statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            //设置标题栏最外层布局属性
            ViewGroup.LayoutParams lnAcBarLayoutParams = lnAcBar.getLayoutParams();
            if(lnAcBarLayoutParams == null){
                lnAcBarLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,baseConfig.getaBarHeight() + statusBarHeight);
            }else {
                lnAcBarLayoutParams.height = baseConfig.getaBarHeight()  + statusBarHeight;
            }
            lnAcBar.setPadding(0,statusBarHeight,0,0);
            lnAcBar.setBackgroundColor(baseConfig.getaBarColor());
            lnAcBar.setLayoutParams(lnAcBarLayoutParams);

            //设置标题文字以及颜色
            tvTitle.setTextColor(baseConfig.getTitleColor());
            if(baseConfig.getTitleText() != null){
                tvTitle.setText(baseConfig.getTitleText());
            }else {
                tvTitle.setText(R.string.app_name);
            }
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

}
