package com.pictureselect.android;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.basepictureoptionslib.android.AppCommon;
import com.basepictureoptionslib.android.BaseActivity;
import com.basepictureoptionslib.android.plugin.image.ImageLoadingUtis;
import com.basepictureoptionslib.android.utils.HintPopUtils;
import com.basepictureoptionslib.android.utils.ParamsAndJudgeUtils;
import com.pictureselect.android.adapter.PictureSelectNoCameraAdapter;
import com.pictureselect.android.database.DbPhonePicturesList;
import com.pictureselect.android.dto.StorePictureItemDto;
import com.pictureselect.android.recycleViewHolder.BaseViewHolder;
import com.pictureselect.android.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class PictureSelectActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyList;//图片列表
    private View viewBottomOptions;//底部操作栏
    private CheckBox cbShowOriginPic;//原图选择
    private Button btnPreview;//预览按钮

    private PictureSelectConfirg pictureSelectConfirg;
    private ArrayList<StorePictureItemDto> allList = new ArrayList<>();//所有的图片集合
    private ArrayList<StorePictureItemDto> selectedPicturesList = new ArrayList<StorePictureItemDto>();//已经选中的图片列表,使用哈希表存储已选中的数据由于key的唯一
    private PictureSelectNoCameraAdapter pictureSelectsAdapter;

    protected int windowWidth;//屏幕宽度
    private final int PERMISSTION_REQUEST_FOR_EXTERNAL_STORAGE = 0;//请求存储卡权限
    private final int GO_TO_PREVIEW_ACT_REQUES_CODE = 1;//跳转到预览界面的请求码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取配置
        if(getIntent().getExtras() != null){
            Parcelable parcelable = getIntent().getExtras().getParcelable(AppCommon.OPTIONS_CONFIG_KEY);
            if(parcelable == null){
                pictureSelectConfirg = new PictureSelectConfirg();
            }else {
                pictureSelectConfirg = (PictureSelectConfirg) parcelable;
            }
        }else {
            pictureSelectConfirg = new PictureSelectConfirg();
        }
        setTheme(pictureSelectConfirg.getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        recyList = findViewById(R.id.recyList);
        viewBottomOptions = findViewById(R.id.viewBottomOptions);
        cbShowOriginPic = findViewById(R.id.cbShowOriginPic);
        btnPreview = findViewById(R.id.btnPreview);

        //初始话参数
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWidth = dm.widthPixels;

        //初始化列表显示参数
        recyList.setLayoutManager(new GridLayoutManager(getApplicationContext(),pictureSelectConfirg.getShowRowCount()));
        recyList.addItemDecoration(new DividerGridItemDecoration(getApplicationContext(),null));
        //初始化适配器
        pictureSelectsAdapter = new PictureSelectNoCameraAdapter(getApplicationContext()) {
            @Override
            public void onSelceChangeClick(BaseViewHolder holder, StorePictureItemDto storePictureItemDto, int position) {
                //设置选中
                setSelectForNoCamera(storePictureItemDto,!storePictureItemDto.isSelect(),position);
            }

            @Override
            public void onImgClick(BaseViewHolder holder, StorePictureItemDto storePictureItemDto, int position) {
                Intent intent = new Intent(PictureSelectActivity.this, PicturePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppCommon.OPTIONS_CONFIG_KEY,pictureSelectConfirg);
                bundle.putParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_select_list),selectedPicturesList);
                bundle.putParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_all_list),allList);
                bundle.putInt(getString(R.string.go_to_poreview_act_key_for_all_list_show_posi),position);
                intent.putExtras(bundle);
                startActivityForResult(intent, GO_TO_PREVIEW_ACT_REQUES_CODE);
                overridePendingTransition(R.anim.anim_from_center,0);
            }

        };


        //初始化线程
        initHandler();
        //初始化标题栏
        setAcBar(pictureSelectConfirg);
        //初始化底部操作栏
        initBottomOptions();
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
        btnPreview.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

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
     * 初始化底部操作栏
     */
    private void initBottomOptions() {
        if(pictureSelectConfirg.isShowPreview() || pictureSelectConfirg.isShowOriginPicSelect()){
            //判断是否需要预览
            if(!pictureSelectConfirg.isShowPreview()){
               btnPreview.setVisibility(View.GONE);
            }
            //判断是否需要原图选择
            if(!pictureSelectConfirg.isShowOriginPicSelect()){
                cbShowOriginPic.setVisibility(View.GONE);
            }

            //设置底部操作栏高度以及背景颜色
            viewBottomOptions.setBackgroundResource(pictureSelectConfirg.getBottomOptionsBackground());
            int height = ParamsAndJudgeUtils.dip2px(getApplicationContext(),pictureSelectConfirg.getBottomOptionsHeight());
            ViewGroup.LayoutParams layoutParams = viewBottomOptions.getLayoutParams();
            if(layoutParams == null){
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
            }else {
                layoutParams.height = height;
            }
            viewBottomOptions.setLayoutParams(layoutParams);

            //设置底部操作栏文字颜色以及大小
            btnPreview.setTextSize(pictureSelectConfirg.getBottomOptionsTextSize());
            cbShowOriginPic.setTextSize(pictureSelectConfirg.getBottomOptionsTextSize());
            btnPreview.setTextColor(pictureSelectConfirg.getBottomOptionsTextColor());
            cbShowOriginPic.setTextColor(pictureSelectConfirg.getBottomOptionsTextColor());
        }
    }

    /**
     * 初始化图片列表
      */
    private void initPictureList(){
        handlerChild.post(new Runnable() {
            @Override
            public void run() {
                allList = DbPhonePicturesList.getInstance(PictureSelectActivity.this).getAllList(DbPhonePicturesList.getInstance(PictureSelectActivity.this).getAllMapList());
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
                        StorePictureItemDto itemDto;
                        String nowSelectPicturePath;
                        int posi;//记录在所有图片当中位置的
                        Iterator<String> selectIterator = finalSelectPathList.iterator();
                        Iterator<StorePictureItemDto> allIterator;
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
                                    isHave = true;
                                    break;
                                }
                                posi++;
                                itemDto = null;
                            }

                            if(!isHave){
                                itemDto = new StorePictureItemDto();
                                itemDto.setAbsolutePath(nowSelectPicturePath);
                                allList.add(0, itemDto);
                                pictureSelectsAdapter.addItemDto(itemDto, 0);
                                setSelectForNoCamera(itemDto, true, 0);
                            }
                        }
                    }
                });


            }
        });
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
    private void setSelectForNoCamera(StorePictureItemDto selectDto, boolean selectState, int postion){
        //判定传入的数据是否为空
        if(selectDto == null ){
            return;
        }
        //先判定是否大于最大的选择图片的数量
        if(selectedPicturesList.size() >= pictureSelectConfirg.getMaxSelectNum() && selectState){
            HintPopUtils.getInstance(getApplicationContext()).toastMsg(R.string.toast_hint_exceed_max_selected_num,null);
            return;
        }

        //查找相同实体
        Iterator<StorePictureItemDto> iterator = selectedPicturesList.iterator();
        StorePictureItemDto sameOptionsDto = null;//在已选中里面查找是否有和要做操做的图片相同的实体，有的话则不为空
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
            pictureSelectsAdapter.modifySelectState(selectDto, postion);

            //显示选中数量
            showSelectSize();

        }else if(!selectState && sameOptionsDto != null){
            selectDto.setSelect(selectState);
            selectedPicturesList.remove(selectDto);
            pictureSelectsAdapter.modifySelectState(selectDto, postion);
            //显示选中数量
            showSelectSize();

        }
    }

    /**
     * 显示选中大小
     */
    private void showSelectSize(){
        int nowSize = selectedPicturesList.size();
        if(nowSize > 0) {
            btnPreview.setText(getResources().getString(R.string.preview_have_size) + "(" + nowSize + ")");
            btnConfirm.setText(getResources().getString(R.string.confirm_have_size) + "(" + nowSize + "/" + pictureSelectConfirg.getMaxSelectNum() + ")");
        }else {
            btnPreview.setText(R.string.preview);
            btnConfirm.setText(R.string.confirm);
        }
    }

    @Override
    public void onClick(View v) {
        if (Integer.compare(v.getId(),R.id.btnPreview) == 0) {
            if(selectedPicturesList.size() > 0) {
                Intent intent = new Intent(this, PicturePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppCommon.OPTIONS_CONFIG_KEY, pictureSelectConfirg);
                bundle.putParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_select_list), selectedPicturesList);
                intent.putExtras(bundle);
                startActivityForResult(intent, GO_TO_PREVIEW_ACT_REQUES_CODE);
                overridePendingTransition(R.anim.anim_from_center, 0);
            }
        } else if(Integer.compare(v.getId(),R.id.btnCancel) == 0){
            finish();
        }else if(Integer.compare(v.getId(),R.id.btnConfirm) == 0){
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
                    boolean isFinish = data.getExtras().getBoolean(getString(R.string.preview_end_result_for_is_finish_select_and_result), false);
                    ArrayList<StorePictureItemDto> list = data.getExtras().getParcelableArrayList(getString(R.string.go_to_poreview_act_key_for_select_list));
                    if(isFinish){
                        resultData(RESULT_OK);
                    }else {
                        List<StorePictureItemDto> previewSlectList = new ArrayList<>();
                        Iterator<StorePictureItemDto> iteratorAll = allList.iterator();
                        Iterator<StorePictureItemDto> iteratorPreview;
                        StorePictureItemDto dtoForAll;
                        StorePictureItemDto dtoForPreview;
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


                        List<StorePictureItemDto> removeList = new ArrayList<>();
                        List<StorePictureItemDto> addList = new ArrayList<>();
                        StorePictureItemDto dto;
                        //获取被移除图片
                        Iterator<StorePictureItemDto> iterator = selectedPicturesList.iterator();
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
                        while (iterator.hasNext()){
                            dto = iterator.next();
                            setSelectForNoCamera(dto,false,allList.indexOf(dto));
                            dto = null;
                        }
                        //在该页面选中中添加预览中新添加的图片
                        iterator = addList.iterator();
                        while (iterator.hasNext()){
                            dto = iterator.next();
                            setSelectForNoCamera(dto,true,allList.indexOf(dto));
                            dto = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 返回结果数据
     * @param resultCode 返回类型code码
     */
    private void resultData(int resultCode){
        ArrayList<String> selectPicturePathList = new ArrayList<>();
        Iterator<StorePictureItemDto> iterator = selectedPicturesList.iterator();
        StorePictureItemDto dto;
        while (iterator.hasNext()){
            dto = iterator.next();
            selectPicturePathList.add(dto.getAbsolutePath());
            dto = null;
        }
        iterator = null;

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
        pictureSelectsAdapter = null;
        selectedPicturesList.clear();
        selectedPicturesList = null;
        allList.clear();
        allList = null;
        pictureSelectConfirg = null;
        btnPreview = null;
        cbShowOriginPic = null;
        viewBottomOptions = null;
        recyList = null;

        super.onDestroy();
    }
}
