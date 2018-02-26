package com.pictureselect.android;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.basepictureoptionslib.android.AppCommon;
import com.basepictureoptionslib.android.BaseActivity;
import com.pictureselect.android.dto.StorePictureItemDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间： 0026/2018/2/26 下午 3:02
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择界面
 * 思路：①.通过继承{@link com.basepictureoptionslib.android.config.BaseConfig}这个抽象类的类
 *         来实现相应的要求，例如最多选择几张图片、是否需要预览，已选择列表
 *      ②.图片的加载放在基础工具包中，这个activity只负责读取图片数据以及展示选择逻辑操作
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class PictureSelectActivity extends BaseActivity {

    private RecyclerView recyList;//图片列表
    private Button btnPreview;//预览按钮

    private PictureSelectConfirg pictureSelectConfirg;
    private List<StorePictureItemDto> allList = new ArrayList<>();//所有的图片集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        recyList = findViewById(R.id.recyList);
        btnPreview = findViewById(R.id.btnPreview);

        //获取配置
        if(getIntent().getExtras() != null){
            Parcelable parcelable = getIntent().getExtras().getParcelable(AppCommon.OPTIONS_CONFIG_KEY);
            if(parcelable == null){
                pictureSelectConfirg = new PictureSelectConfirg();
            }else {
                pictureSelectConfirg = (PictureSelectConfirg) parcelable;
            }
        }

        //初始化标题栏
        setAcBar(pictureSelectConfirg);
        //初始化底部操作栏


        //初始化线程
        initHandler();

    }

    /**
     * 初始化图片列表
      */
    private void initPictureList(){

    }

}
