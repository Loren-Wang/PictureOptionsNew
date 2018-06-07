package com.pictureselect.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.basepictureoptionslib.android.AppCommon;
import com.basepictureoptionslib.android.plugin.image.ImageLoadingUtis;
import com.basepictureoptionslib.android.utils.ParamsAndJudgeUtils;
import com.pictureselect.android.adapter.PicturePreviewAdapter;
import com.pictureselect.android.dto.StorePictureVideoItemDto;
import com.pictureselect.android.interfaces_abstract.RecycleviewViewPageOnPageChangeListener;
import com.pictureselect.android.view.RecycleViewViewpager;

import java.util.ArrayList;

/**
 * 创建时间： 0001/2018/3/1 下午 5:55
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择的预览图界面
 * 思路： 两种模式，一种是通过预览按钮进行预览，这个时候仅仅显示选中的图片
 *               第二种是点击图片进行预览，这个时候就要浏览所有的图片同时定位到点击的图片
 *       两种模式通用部分：①、标题栏以及状态栏使用半透明模式，同时预览的图片使用全屏模式，标题栏为三个模块，分别是后退，
 *                         图片位置显示，以及确定按钮，底部操作栏为选中按钮以及原图按钮
 *                      ②、图片预览列表使用recycleview的仿viewpager效果的；
 *                      ③、界面主题使用传入的config配置主题，同样，标题栏以及状态栏高度也使用config配置主题；
 *       通过intent接收参数信息，参数key分别为{@link R.string.go_to_poreview_act_key_for_select_list}:已选择图片列表
 *                                       {@link R.string.go_to_poreview_act_key_for_all_list}:所有图片列表
 *                                       {@link com.basepictureoptionslib.android.AppCommon.OPTIONS_CONFIG_KEY}:主题等参数配置类
 *       进入该界面需要使用带有返回值的方式进入，同时操作完成后需要销毁该界面所有数据，后续通过setResult()返回最后数据
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class PictureVideoPreviewActivity extends BasePictureVideoActivity {

    private View viewAcBar;//标题栏背景
    private TextView tvTitle;//标题
    private ImageButton imgBtnback;//后退按钮
    private Button btnConfirm;//确定按钮
    private View viewBottomOptions;//底部操作栏背景
    private CheckBox cbShowOriginPic;//原图选择
    private CheckBox cbShowOriginSelect;//当前图片选中选择
    private RecycleViewViewpager recyList;//预览图列表

    private int aBarAndBottomAlpha = (int) (255 * 0.5f);//标题栏以及底部操作栏透明度
    private int aBarAndBottomBgColor;//标题栏以及底部操作栏颜色
    private PicturePreviewAdapter picturePreviewAdapter;
    private ArrayList<StorePictureVideoItemDto> adapterShowList;//适配器显示列表
    private int windowWidth;
    private int windowHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getIntent().getExtras() != null){
            Parcelable parcelable = getIntent().getExtras().getParcelable(AppCommon.OPTIONS_CONFIG_KEY);
            if(parcelable == null){
                pictureSelectConfirg = new PictureVideoSelectConfirg();
            }else {
                pictureSelectConfirg = (PictureVideoSelectConfirg) parcelable;
            }
        }else {
            pictureSelectConfirg = new PictureVideoSelectConfirg();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture_and_video_preview);

        viewAcBar = findViewById(R.id.viewOpAcBar);
        tvTitle = findViewById(R.id.tvOpTitle);
        imgBtnback = findViewById(R.id.imgBtnOpback);
        btnConfirm = findViewById(R.id.btnOpConfirm);
        viewBottomOptions = findViewById(R.id.viewOpBottomOptions);
        cbShowOriginPic = findViewById(R.id.cbOpShowOriginPic);
        cbShowOriginSelect = findViewById(R.id.cbOpShowOriginSelect);
        recyList = findViewById(R.id.recyOpList);
        recyList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        //初始话参数
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWidth = dm.widthPixels;
        windowHeight = dm.heightPixels;
        picturePreviewAdapter = new PicturePreviewAdapter(this,windowWidth,windowHeight);
        recyList.setRecycleviewViewPageOnPageChangeListener(new RecycleviewViewPageOnPageChangeListener() {
            @Override
            public void onPageChange(int nowPagePosition) {
                StorePictureVideoItemDto itemDto = adapterShowList.get(nowPagePosition);
                if(itemDto.isSelect()){
                    cbShowOriginSelect.setChecked(true);
                }else {
                    cbShowOriginSelect.setChecked(false);
                }
            }
        });

        cbShowOriginSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorePictureVideoItemDto nowPosiShowDto = adapterShowList.get(recyList.getNowPosi());
                if(nowPosiShowDto != null) {
                    boolean state = setSelectForNoCamera(nowPosiShowDto, cbShowOriginSelect.isChecked(), recyList.getNowPosi());
                    if(state) {
                        adapterShowList.set(recyList.getNowPosi(), nowPosiShowDto);
                        showSelectSize();
                    }else {//未选中恢复原状态
                        cbShowOriginSelect.setChecked(!cbShowOriginSelect.isChecked());
                    }
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultData(true);
            }
        });

        imgBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultData(false);
            }
        });

        recyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        ImageLoadingUtis.getInstance(getApplicationContext()).onResume();
                    } else {
                        ImageLoadingUtis.getInstance(getApplicationContext()).onPause();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });





        //设置标题栏属性
        setAcBar();
        //初始化底部操作栏
        initBottomOptions();
        //初始化图片显示列表
        initShowList();



    }

    /**
     * 设置标题栏属性
     */
    private void setAcBar(){
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        //设置标题栏最外层布局属性
        int color = getResources().getColor(pictureSelectConfirg.getaBarColor());
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        aBarAndBottomBgColor = (aBarAndBottomAlpha << 24) | (red << 16) | (green << 8) | blue;
        ViewGroup.LayoutParams viewAcBarLayoutParams = viewAcBar.getLayoutParams();
        int height = ParamsAndJudgeUtils.dip2px(getApplicationContext(), pictureSelectConfirg.getaBarHeight()) + statusBarHeight;
        if(viewAcBarLayoutParams == null){
            viewAcBarLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        }else {
            viewAcBarLayoutParams.height = height;
        }
        viewAcBar.setBackgroundColor(aBarAndBottomBgColor);
        viewAcBar.setLayoutParams(viewAcBarLayoutParams);

        //设置标题文字颜色
        tvTitle.setTextColor(pictureSelectConfirg.getTitleColor());
        //设置标题栏确认取消文字大小颜色
        btnConfirm.setTextSize(pictureSelectConfirg.getaBarTextSize());
        btnConfirm.setTextColor(pictureSelectConfirg.getaBarTextColor());
        //设置标题、确认按钮、后退按钮padding
        tvTitle.setPadding(tvTitle.getPaddingLeft(),tvTitle.getPaddingTop() + statusBarHeight,tvTitle.getPaddingRight(),tvTitle.getPaddingBottom());
        imgBtnback.setPadding(imgBtnback.getPaddingLeft(),imgBtnback.getPaddingTop() + statusBarHeight,imgBtnback.getPaddingRight(),imgBtnback.getPaddingBottom());
        btnConfirm.setPadding(btnConfirm.getPaddingLeft(),btnConfirm.getPaddingTop() + statusBarHeight,btnConfirm.getPaddingRight(),btnConfirm.getPaddingBottom());
    }

    /**
     * 初始化底部操作栏
     */
    private void initBottomOptions() {
        if(pictureSelectConfirg.isShowPreview() || pictureSelectConfirg.isShowOriginPicSelect()){
            //判断是否需要原图选择
            if(!pictureSelectConfirg.isShowOriginPicSelect()){
                cbShowOriginPic.setVisibility(View.GONE);
            }

            //设置底部操作栏高度以及背景颜色
            int height = ParamsAndJudgeUtils.dip2px(getApplicationContext(),pictureSelectConfirg.getBottomOptionsHeight());
            ViewGroup.LayoutParams layoutParams = viewBottomOptions.getLayoutParams();
            if(layoutParams == null){
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
            }else {
                layoutParams.height = height;
            }
            viewBottomOptions.setBackgroundColor(aBarAndBottomBgColor);
            viewBottomOptions.setLayoutParams(layoutParams);

            //设置底部操作栏文字颜色以及大小
            cbShowOriginSelect.setTextSize(pictureSelectConfirg.getBottomOptionsTextSize());
            cbShowOriginPic.setTextSize(pictureSelectConfirg.getBottomOptionsTextSize());
            cbShowOriginSelect.setTextColor(pictureSelectConfirg.getBottomOptionsTextColor());
            cbShowOriginPic.setTextColor(pictureSelectConfirg.getBottomOptionsTextColor());
        }
    }

    /**
     * 初始化预览图片列表
     */
    private void initShowList(){
        if (getIntent().getExtras() != null){
            allList = getIntent().getExtras().getParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_all_list));
            selectedPicturesList = getIntent().getExtras().getParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_select_list));
            isSelectPicture = getIntent().getExtras().getBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_picture),isSelectPicture);
            isSelectVideo = getIntent().getExtras().getBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_video),isSelectVideo);
            if(selectedPicturesList == null){
                selectedPicturesList = new ArrayList();
            }
            if(allList == null) {
                adapterShowList = new ArrayList<>(selectedPicturesList);
            }else {
                adapterShowList = new ArrayList<>(allList);
            }
            picturePreviewAdapter.setList(adapterShowList);
            recyList.setAdapter(picturePreviewAdapter);
            recyList.scrollToPosition(getIntent().getExtras().getInt(getString(R.string.go_to_poreview_act_key_for_all_list_show_posi),0));

            showSelectSize();
        }
    }

    /**
     * 显示选中大小
     */
    private void showSelectSize(){
        int nowSize = selectedPicturesList.size();
        if(nowSize > 0) {
            btnConfirm.setText(getResources().getString(R.string.confirm_have_size) + "(" + nowSize + "/" + getMaxSelectNum() + ")");
        }else {
            btnConfirm.setText(R.string.confirm);
        }
    }


    private void resultData(boolean isFinishPictureSelect){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.preview_end_result_for_is_finish_select_and_result),isFinishPictureSelect);
        bundle.putParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_select_list),selectedPicturesList);
        bundle.putBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_picture),isSelectPicture);
        bundle.putBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_video),isSelectVideo);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
        overridePendingTransition(0,R.anim.frame_anim_to_bottom);
    }

    @Override
    public void onBackPressed() {
        resultData(false);
    }

    @Override
    public void finish() {
        ImageLoadingUtis.getInstance(PictureVideoPreviewActivity.this).clearImageMemoryCache();
        super.finish();
    }

    @Override
    protected void onDestroy() {

        if(adapterShowList != null){
            adapterShowList.clear();
            adapterShowList = null;
        }
        if(selectedPicturesList != null){
            selectedPicturesList.clear();
            selectedPicturesList = null;
        }
        if(allList != null){
            allList.clear();
            allList = null;
        }

        picturePreviewAdapter = null;
        pictureSelectConfirg = null;
        recyList = null;
        cbShowOriginSelect = null;
        cbShowOriginPic = null;
        viewBottomOptions = null;
        btnConfirm = null;
        imgBtnback = null;
        tvTitle = null;
        viewAcBar = null;
        setContentView(R.layout.activity_options_base_null);
        System.gc();
        super.onDestroy();
    }

    private void recycleBitmap(ViewGroup viewGroup) {
        if(viewGroup != null) {
            int count =  viewGroup.getChildCount();
            for(int i=0; i <count; i++) {
                View view = viewGroup.getChildAt(i);
                if(view != null) {
                    if (view instanceof ViewGroup) {
                        recycleBitmap((ViewGroup) view);
                    } else {
                        if (view instanceof ImageView) {
                            Drawable drawable = ((ImageView)view).getDrawable();
                            if (drawable != null) {
                                if (drawable instanceof BitmapDrawable) {
                                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                    Bitmap bitmap = bitmapDrawable.getBitmap();
                                    if (bitmap != null)
                                        bitmap.recycle();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
