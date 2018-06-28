package com.pictureselect.android.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pictureselect.android.AppCommon;
import com.pictureselect.android.R;
import com.pictureselect.android.adapter.PictureSelectNoCameraAdapter;
import com.pictureselect.android.config.PictureVideoSelectConfirg;
import com.pictureselect.android.database.DbScanSdCardForPicture;
import com.pictureselect.android.database.DbScanSdCardForVideo;
import com.pictureselect.android.database.DbSdCardPictureVideoList;
import com.pictureselect.android.dto.StorePictureVideoItemDto;
import com.pictureselect.android.recycleViewHolder.BaseViewHolder;
import com.pictureselect.android.setting.AppConfigSetting;
import com.pictureselect.android.utils.ImageLoadingUtis;
import com.pictureselect.android.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.pictureselect.android.setting.AppConfigSetting.showSelectList;

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
public class PictureVideoSelectActivity extends PictureVideoSelectBaseActivity implements View.OnClickListener {

    private RecyclerView recyList;//图片列表
    private Button btnPreview;//预览按钮
    private Button btnConfirm;//确定按钮

    private PictureSelectNoCameraAdapter pictureSelectsAdapter;

    protected int windowWidth;//屏幕宽度
    private final int PERMISSTION_REQUEST_FOR_EXTERNAL_STORAGE = 0;//请求存储卡权限
    private final int GO_TO_PREVIEW_ACT_REQUES_CODE = 1;//跳转到预览界面的请求码
    private boolean isAllowGoToPreview = true;//是否允许去预览


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        //获取配置
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

        //设置标题
        setTheme(getPictureVideoSelectorThemeConfig().getThemeId());

        //初始化全局配置文件
        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_PAINT_STATE = new Paint();
        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_PAINT_STATE.setAntiAlias(true);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture_and_video_select);
        recyList = findViewById(R.id.recyOpList);
        btnPreview = findViewById(R.id.btnOpPreview);
        btnConfirm = findViewById(R.id.btnOpConfirm);


        //初始话参数
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWidth = dm.widthPixels;

        //初始化线程
        initHandler();
        //根据主题配置文件设置主题
        initAcBarOptions();//初始化标题栏
        initBottomOptions();//初始化底部操作栏

        //设置图片选择显示列数
        recyList.setLayoutManager(new GridLayoutManager(getApplicationContext(),getPictureVideoSelectorThemeConfig().getShowRowCount()));
        recyList.addItemDecoration(new DividerGridItemDecoration(getApplicationContext(),null));
        //初始化适配器
        pictureSelectsAdapter = new PictureSelectNoCameraAdapter(getApplicationContext(),windowWidth / 3,windowWidth / 3
                ,getPictureVideoSelectorThemeConfig().getSelectStateY(),getPictureVideoSelectorThemeConfig().getSelectStateN()) {
            @Override
            public void onSelceChangeClick(BaseViewHolder holder, StorePictureVideoItemDto storePictureItemDto, int position) {
                //设置选中
                setSelectForNoCamera(storePictureItemDto,!storePictureItemDto.isSelect(),position);
                pictureSelectsAdapter.modifySelectState(storePictureItemDto, position);
                showSelectSize();
            }

            @Override
            public void onImgClick(BaseViewHolder holder, StorePictureVideoItemDto storePictureItemDto, int position) {
                if(storePictureItemDto.isSelect()) {
                    goToPreview(null,false);
                }else {
                    goToPreview(position,true);
                }
            }

        };

        //初始化图片列表
        permisstionRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSTION_REQUEST_FOR_EXTERNAL_STORAGE);

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


    }

    @Override
    protected void perissionRequestSuccessCallback(List<String> perissionList, int permissionsRequestCode) {
        super.perissionRequestSuccessCallback(perissionList, permissionsRequestCode);
        switch (permissionsRequestCode){
            case PERMISSTION_REQUEST_FOR_EXTERNAL_STORAGE:
                //初始化图片列表
                initPictureList();
                break;
            default:
                break;
        }
    }


    /**
     * 初始化标题栏参数
     */
    private void initAcBarOptions(){
        //初始化标题栏背景
        setViewWidthHeight(findViewById(R.id.viewOpAcBar),RelativeLayout.LayoutParams.MATCH_PARENT
                ,getPictureVideoSelectorThemeConfig().getAcBarHeight());
        findViewById(R.id.viewOpAcBar).setBackgroundResource(getPictureVideoSelectorThemeConfig().getThemeColor());
        //设置标题
        ((TextView)findViewById(R.id.tvOpTitle)).setTextColor(getPictureVideoSelectorThemeConfig().getAcBarTitleColor());
        ((TextView)findViewById(R.id.tvOpTitle)).setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(getPictureVideoSelectorThemeConfig().getAcBarTitleSize()));
        setViewWidthHeight(findViewById(R.id.tvOpTitle),RelativeLayout.LayoutParams.WRAP_CONTENT
                ,getPictureVideoSelectorThemeConfig().getAcBarContentViewHeight());
        //设置左上角文字
        ((Button)findViewById(R.id.btnOpCancel)).setTextColor(getPictureVideoSelectorThemeConfig().getAcBarCancelColor());
        ((Button)findViewById(R.id.btnOpCancel)).setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(getPictureVideoSelectorThemeConfig().getAcBarCancelSize()));
        setViewWidthHeight(findViewById(R.id.btnOpCancel),RelativeLayout.LayoutParams.WRAP_CONTENT
                ,getPictureVideoSelectorThemeConfig().getAcBarContentViewHeight());
        //设置右上角文字
        btnConfirm.setTextColor(getPictureVideoSelectorThemeConfig().getAcBarConfirmColor());
        btnConfirm.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(getPictureVideoSelectorThemeConfig().getAcBarConfirmSize()));
        setViewWidthHeight(btnConfirm,RelativeLayout.LayoutParams.WRAP_CONTENT
                ,getPictureVideoSelectorThemeConfig().getAcBarContentViewHeight());
        findViewById(R.id.btnOpCancel).setOnClickListener(this);
        findViewById(R.id.btnOpConfirm).setOnClickListener(this);
    }

    /**
     * 初始化底部操作栏
     */
    private void initBottomOptions() {
        if(pictureSelectConfirg.isShowPreview() || pictureSelectConfirg.isShowOriginPicSelect()){
            //判断是否需要预览
            if(!pictureSelectConfirg.isShowPreview()){
                btnPreview.setVisibility(View.GONE);
            }else {
                //设置预览选择文字
                btnPreview.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(getPictureVideoSelectorThemeConfig().getPreviewTextSize()));
                btnPreview.setTextColor(getPictureVideoSelectorThemeConfig().getPreviewTextColor());
                btnPreview.setOnClickListener(this);
            }
            //判断是否需要原图选择
            if(!pictureSelectConfirg.isShowOriginPicSelect()){
                findViewById(R.id.cbOpShowOriginPic).setVisibility(View.GONE);
            }else {
                //设置原图选择文字
                ((CheckBox)findViewById(R.id.cbOpShowOriginPic)).setTextColor(getPictureVideoSelectorThemeConfig().getOriginPictureTextColor());
                ((CheckBox)findViewById(R.id.cbOpShowOriginPic)).setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(getPictureVideoSelectorThemeConfig().getOriginPictureTextSize()));
            }
            //设置底部操作栏高度
            setViewWidthHeight(btnConfirm,RelativeLayout.LayoutParams.MATCH_PARENT
                    ,getPictureVideoSelectorThemeConfig().getBottomOptionsHeight());
            findViewById(R.id.viewOpBottomOptions).setBackgroundResource(getPictureVideoSelectorThemeConfig().getThemeColor());
        }
    }

    /**
     * 初始化图片列表
      */
    public void initPictureList(){
        handlerChild.post(new Runnable() {
            @Override
            public void run() {
                switch (pictureSelectConfirg.getSelectType()){
                    case 0://仅图片
                        allList = DbSdCardPictureVideoList.getInstance(getApplicationContext())
                                .getAllList(DbSdCardPictureVideoList.getInstance(getApplicationContext()).getPictureAllMapList(pictureSelectConfirg.getPictureFilterSelection()
                                        ,pictureSelectConfirg.getPictureFilterSelectionArgs()));
                        break;
                    case 1://仅视频
                        allList = DbSdCardPictureVideoList.getInstance(getApplicationContext())
                                .getAllList(DbSdCardPictureVideoList.getInstance(getApplicationContext()).getVideoAllMapList(pictureSelectConfirg.getVideoFilterSelection(),pictureSelectConfirg.getVideoFilterSelectionArgs()));
                        break;
                    case 2://两者都有
                    default:
                        allList = DbSdCardPictureVideoList.getInstance(getApplicationContext())
                                .getAllList(DbSdCardPictureVideoList.getInstance(getApplicationContext()).getAllMapList(pictureSelectConfirg.getPictureFilterSelection()
                                        ,pictureSelectConfirg.getPictureFilterSelectionArgs(),pictureSelectConfirg.getVideoFilterSelection()
                                        ,pictureSelectConfirg.getVideoFilterSelectionArgs()));
                        break;
                }
                if(allList.size() == 0){
                    //重新扫描文件夹
                    getPictureVideoSelectorThemeConfig().startScanSdCard();
                }

                //获取已选择的列表
                List<String> selectPathList = pictureSelectConfirg.getSelectedPicList();
                //移除重复项
                Map<String,String> selectPathMap = new HashMap<>();
                if(selectPathList != null) {
                    Iterator<String> iterator = selectPathList.iterator();
                    String path;
                    while (iterator.hasNext()) {
                        path = iterator.next();
                        selectPathMap.put(path, path);
                        path = null;
                    }
                    iterator = null;
                    selectPathList.clear();
                    selectPathList = null;
                }
                selectPathList = new ArrayList<>(selectPathMap.values());
                //设置adapter
                final List<String> finalSelectPathList = selectPathList;
                handlerUi.post(new Runnable() {
                    @Override
                    public void run() {
                        //初始化图片列表数据
                        pictureSelectsAdapter.setList(allList);
                        recyList.setAdapter(pictureSelectsAdapter);

                        //在已选择列表中寻找所有图片中不存在的图片
                        boolean isHave;//在所有图片中是否有已选择图片
                        StorePictureVideoItemDto itemDto;
                        String nowSelectPicturePath;
                        int posi;//记录在所有图片当中位置的
                        Iterator<String> selectIterator = finalSelectPathList.iterator();
                        Iterator<StorePictureVideoItemDto> allIterator;
                        List<StorePictureVideoItemDto> newList = new ArrayList<>();
                        while (selectIterator.hasNext()){
                            nowSelectPicturePath = selectIterator.next();
                            isHave = false;
                            posi = 0;
                            allIterator = allList.iterator();
                            while (allIterator.hasNext()){
                                itemDto = allIterator.next();
                                //找到相应数据
                                if(itemDto != null && itemDto.getAbsolutePath() != null && nowSelectPicturePath.equals(itemDto.getAbsolutePath())){
                                    setSelectForNoCamera(itemDto, true, posi);
                                    pictureSelectsAdapter.modifySelectState(itemDto, posi);
                                    showSelectSize();
                                    isHave = true;
                                    break;
                                }
                                posi++;
                                itemDto = null;
                            }

                            if(!isHave){
                                itemDto = new StorePictureVideoItemDto();
                                itemDto.setAbsolutePath(nowSelectPicturePath);
                                newList.add(itemDto);
                            }
                        }

                        Iterator<StorePictureVideoItemDto> iterator = newList.iterator();
                        while (iterator.hasNext()){
                            itemDto = iterator.next();
                            DbScanSdCardForPicture.getInstance(getApplicationContext()).insert(itemDto.getAbsolutePath());
                            DbScanSdCardForVideo.getInstance(getApplicationContext()).insert(itemDto.getAbsolutePath());
                            pictureSelectsAdapter.addItemDto(itemDto, 0);
                            setSelectForNoCamera(itemDto, true, 0);
                        }
                        showSelectSize();
                    }
                });


            }
        });
    }



    /**
     * 显示选中大小
     */
    private void showSelectSize(){
        int nowSize = selectedPicturesList.size();
        if(nowSize > 0) {
            btnPreview.setText(getResources().getString(R.string.preview_have_size) + "(" + nowSize + ")");
            int maxShowNum = getMaxSelectNum();
            if(maxShowNum > 0){
                btnConfirm.setText(getResources().getString(R.string.confirm_have_size) + "(" + nowSize + "/" + maxShowNum + ")");
            }else {
                btnConfirm.setText(R.string.confirm);
            }
        }else {
            btnPreview.setText(R.string.preview);
            btnConfirm.setText(R.string.confirm);
        }
    }



    @Override
    public void onClick(View v) {
        if (Integer.compare(v.getId(),R.id.btnOpPreview) == 0) {
            if(selectedPicturesList.size() > 0) {
                goToPreview(null,false);
            }
        } else if(Integer.compare(v.getId(),R.id.btnOpCancel) == 0){
            finish();
        }else if(Integer.compare(v.getId(),R.id.btnOpConfirm) == 0){
            resultData(RESULT_OK);
        }
    }

    /**
     * 接收预览界面操作后的返回值处理，不管怎样都要返回已选列表，即使已选择列表为空；
     * ①、在接受返回值的时候需要通过一个参数来判断是否需要直接返回选中数据，并销毁该界面参数
     *     对应的key{@link R.string.preview_end_result_for_is_finish_select_and_result}，值为布尔型
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getExtras() != null){
            switch (requestCode){
                case GO_TO_PREVIEW_ACT_REQUES_CODE:
                    //判断是否需要结束该界面并返回数据
                    isSelectPicture = getIntent().getExtras().getBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_picture),isSelectPicture);
                    isSelectVideo = getIntent().getExtras().getBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_video),isSelectVideo);

                    boolean isFinish = data.getExtras().getBoolean(getString(R.string.preview_end_result_for_is_finish_select_and_result), false);
                    ArrayList<StorePictureVideoItemDto> list = data.getExtras().getParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_select_list));

                    List<StorePictureVideoItemDto> previewSlectList = new ArrayList<>();
                    Iterator<StorePictureVideoItemDto> iteratorAll = allList.iterator();
                    Iterator<StorePictureVideoItemDto> iteratorPreview;
                    StorePictureVideoItemDto dtoForAll;
                    StorePictureVideoItemDto dtoForPreview;
                    while (iteratorAll.hasNext()){
                        dtoForAll = iteratorAll.next();
                        if(list.size() <= 0){
                            dtoForAll = null;
                            break;
                        }else {
                            iteratorPreview = list.iterator();
                            while (iteratorPreview.hasNext()) {
                                dtoForPreview = iteratorPreview.next();
                                if (dtoForAll.getAbsolutePath().equals(dtoForPreview.getAbsolutePath())) {
                                    previewSlectList.add(dtoForAll);
                                    list.remove(dtoForPreview);
                                    dtoForPreview = null;
                                    break;
                                }
                                dtoForPreview = null;
                            }
                        }
                        dtoForAll = null;
                    }
                    list.clear();
                    list = null;


                    List<StorePictureVideoItemDto> removeList = new ArrayList<>();
                    List<StorePictureVideoItemDto> addList = new ArrayList<>();
                    StorePictureVideoItemDto dto;
                    //获取被移除图片
                    Iterator<StorePictureVideoItemDto> iterator = selectedPicturesList.iterator();
                    while (iterator.hasNext()){
                        dto = iterator.next();
                        //在返回列表中没有该页面选中中的一个的话，代表该图片是被取消选中了
                        if(!previewSlectList.contains(dto)){
                            removeList.add(dto);
                        }
                        dto = null;
                    }
                    //获取被添加图片
                    iterator = previewSlectList.iterator();
                    while (iterator.hasNext()){
                        dto = iterator.next();
                        //在该页选中列表中不存在返回列表中的图片的话代表图片是新加的
                        if(!selectedPicturesList.contains(dto)){
                            addList.add(dto);
                        }
                        dto = null;
                    }
                    //在该页面选中中移除被移除图片
                    iterator = removeList.iterator();
                    int indexOf;
                    while (iterator.hasNext()){
                        dto = iterator.next();
                        indexOf = allList.indexOf(dto);
                        setSelectForNoCamera(dto,false,indexOf);
                        pictureSelectsAdapter.modifySelectState(dto, indexOf);
                        showSelectSize();
                        dto = null;
                    }
                    //在该页面选中中添加预览中新添加的图片
                    iterator = addList.iterator();
                    while (iterator.hasNext()){
                        dto = iterator.next();
                        indexOf = allList.indexOf(dto);
                        setSelectForNoCamera(dto,true,indexOf);
                        pictureSelectsAdapter.modifySelectState(dto, indexOf);
                        showSelectSize();
                        dto = null;
                    }

                    if(isFinish){
                        resultData(RESULT_OK);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 跳转到预览界面
     */
    private void goToPreview(Integer allListShowPosi,boolean isShowAllList){
        if(isAllowGoToPreview) {
            Intent intent = new Intent(getApplicationContext(), PictureVideoPreviewActivity.class);
            Bundle bundle = new Bundle();
            AppConfigSetting.pictureVideoSelectConfirg = pictureSelectConfirg;
            showSelectList.clear();
            showSelectList.addAll(selectedPicturesList);
            if (allListShowPosi != null && isShowAllList) {
                AppConfigSetting.showAllList.clear();
                AppConfigSetting.showAllList.addAll(allList);
                bundle.putInt(getString(R.string.go_to_poreview_act_key_for_all_list_show_posi), allListShowPosi);
            }
            bundle.putBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_picture), isSelectPicture);
            bundle.putBoolean(getResources().getString(R.string.go_to_poreview_act_key_for_is_select_video), isSelectVideo);
            intent.putExtras(bundle);
            startActivityForResult(intent, GO_TO_PREVIEW_ACT_REQUES_CODE);
            overridePendingTransition(com.lorenwang.anim.android.R.anim.anim_from_center, 0);
            isAllowGoToPreview = false;
        }
    }

    /**
     * 返回结果数据
     * @param resultCode 返回类型code码
     */
    private void resultData(int resultCode){
        ArrayList<String> selectPicturePathList = new ArrayList<>();
        StorePictureVideoItemDto dto;
        int size = selectedPicturesList.size();
        Cursor thumbCursor = null;
        for(int i = 0 ; i < size ; i++){
            dto = selectedPicturesList.get(i);
            selectPicturePathList.add(dto.getAbsolutePath());

            //获取缩略图
            if(dto.getDuration() > 0){
                thumbCursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
                        ,null,MediaStore.Video.Thumbnails.VIDEO_ID + "=" + dto.get_id(),null,null);
                if(thumbCursor.getCount() > 0 && thumbCursor.moveToNext()){
                    dto.setThumbPath(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                    dto.setThumbWidth(thumbCursor.getInt(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.WIDTH)));
                    dto.setThumbHeight(thumbCursor.getInt(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.HEIGHT)));
                    selectedPicturesList.set(i,dto);
                }
            }else {
                thumbCursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
                        ,null,MediaStore.Images.Thumbnails.IMAGE_ID + "=" + dto.get_id(),null,null);
                if(thumbCursor.getCount() > 0 && thumbCursor.moveToNext()){
                    dto.setThumbPath(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA)));
                    dto.setThumbWidth(thumbCursor.getInt(thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.WIDTH)));
                    dto.setThumbHeight(thumbCursor.getInt(thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.HEIGHT)));
                    selectedPicturesList.set(i,dto);
                }
            }
            if(thumbCursor != null) {
                thumbCursor.close();
                thumbCursor = null;
            }
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppCommon.PICTURE_SELECT_RESULT_DATA_FOR_DTO,selectedPicturesList);
        bundle.putStringArrayList(AppCommon.PICTURE_SELECT_RESULT_DATA_FOR_PATH,selectPicturePathList);
        intent.putExtras(bundle);
        setResult(resultCode,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageLoadingUtis.getInstance(PictureVideoSelectActivity.this).clearImageMemoryCache();
            }
        });

        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_PAINT_STATE = null;
        //释放bitmap
        if( AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP != null
                && !AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP.isRecycled()){
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP.recycle();
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP = null;
        }
        //释放bitmap
        if( AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP != null
                && !AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP.isRecycled()){
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP.recycle();
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP = null;
        }

        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_SRCRECT = null;
        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_SRCRECT = null;
        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT = null;

        pictureSelectsAdapter = null;
        selectedPicturesList.clear();
        selectedPicturesList = null;
        allList.clear();
        allList = null;
        pictureSelectConfirg = null;
        btnPreview = null;
        recyList = null;

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        isAllowGoToPreview = true;
        AppConfigSetting.pictureVideoSelectActivity = this;
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(isFinishing()){
            AppConfigSetting.pictureVideoSelectActivity = null;
        }
        super.onPause();
    }
}
