package com.pictureoptions.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.basepictureoptionslib.android.AppCommon;
import com.basepictureoptionslib.android.utils.LogUtils;
import com.pictureselect.android.PictureVideoSelectActivity;
import com.pictureselect.android.PictureVideoSelectConfirg;
import com.pictureselect.android.utils.SdCardFileChangeUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = getClass().getName() + hashCode();
    private EditText edtMaxPictureNum;
    private EditText edtMaxVideoNum;
    private CheckBox cbShowAllPicture;
    private CheckBox cbShowAllVideo;
    private CheckBox cbAllowSelectAll;
    private Button btnBegin;
    private ListView lvList;

    private boolean isAllowClick = true;
    private final int SELECT_PICTURE_AND_VIDEO_REQUEST_CODE = 0;//图片视频选择请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtMaxPictureNum = findViewById(R.id.edtMaxPictureNum);
        edtMaxVideoNum = findViewById(R.id.edtMaxVideoNum);
        cbShowAllPicture = findViewById(R.id.cbShowAllPicture);
        cbShowAllVideo = findViewById(R.id.cbShowAllVideo);
        cbAllowSelectAll = findViewById(R.id.cbAllowSelectAll);
        btnBegin = findViewById(R.id.btnBegin);
        lvList = findViewById(R.id.lvList);

        btnBegin.setOnClickListener(this);
        String PROJECT_FILE_DIR = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.ve_link.androids/cache/file/";
        String PROJECT_FILE_DIR_VIDEO = PROJECT_FILE_DIR +  "video/";
        String PROJECT_FILE_DIR_CAMERA_IMAGE = PROJECT_FILE_DIR +  "ydCameraImages/";
        SdCardFileChangeUtils.geInstance(getApplicationContext()).startWatching(new String[]{
                PROJECT_FILE_DIR_VIDEO,PROJECT_FILE_DIR_CAMERA_IMAGE
        });


    }

    /**
     * 设置点击事件
     * @param states
     */
    private void setClickEnabledStates(boolean states){
        isAllowClick = states;
    }

    @Override
    public void onClick(View v) {
        if(isAllowClick) {
            setClickEnabledStates(false);
            switch (v.getId()) {
                case R.id.btnBegin:
                    PictureVideoSelectConfirg pictureVideoSelectConfirg = new PictureVideoSelectConfirg();
                    try {
                        pictureVideoSelectConfirg.setMaxSelectPictureNum(Integer.parseInt(edtMaxPictureNum.getText().toString()));
                    } catch (Exception e) {
                        LogUtils.logD(TAG, "未获取到最大选择图片数量");
                    }
                    try {
                        pictureVideoSelectConfirg.setMaxSelectVideoNum(Integer.parseInt(edtMaxVideoNum.getText().toString()));
                    } catch (Exception e) {
                        LogUtils.logD(TAG, "未获取到最大选择视频数量");
                    }

                    pictureVideoSelectConfirg.setAllowConcurrentSelection(cbAllowSelectAll.isChecked());

                    if (cbShowAllPicture.isChecked() && cbShowAllVideo.isChecked()) {
                        pictureVideoSelectConfirg.setSelectType(PictureVideoSelectConfirg.SELECT_TYPE_FOR_PICTURE_AND_VIDEO);
                    } else if (cbShowAllPicture.isChecked()) {
                        pictureVideoSelectConfirg.setSelectType(PictureVideoSelectConfirg.SELECT_TYPE_FOR_PICTURE);
                    } else if (cbShowAllVideo.isChecked()) {
                        pictureVideoSelectConfirg.setSelectType(PictureVideoSelectConfirg.SELECT_TYPE_FOR_VIDEO);
                    }

                    try {
                        pictureVideoSelectConfirg.setPictureFilter(PictureVideoSelectConfirg.FILTER_PICTURE_FOR_WIDTH_HEIGHT,200,null,200,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
//                        pictureVideoSelectConfirg.setVideoFilter(PictureVideoSelectConfirg.FILTER_VIDEO_FOR_NONE);
                        pictureVideoSelectConfirg.setVideoFilter(PictureVideoSelectConfirg.FILTER_VIDEO_FOR_SIZE_AND_DURATION,null,3065964,2000,null);

//                        pictureVideoSelectConfirg.setVideoFilter(PictureVideoSelectConfirg.FILTER_VIDEO_FOR_SIZE_AND_DURATION_AND_WIDTH_HEIGHT
//                                ,null,3065964,2000,null,321,null,177,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(this, PictureVideoSelectActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppCommon.OPTIONS_CONFIG_KEY, pictureVideoSelectConfirg);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,SELECT_PICTURE_AND_VIDEO_REQUEST_CODE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        setClickEnabledStates(true);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data.getExtras() != null){
            switch (requestCode){
                case SELECT_PICTURE_AND_VIDEO_REQUEST_CODE:
                    ArrayList<String> pathList = data.getExtras().getStringArrayList(AppCommon.PICTURE_SELECT_RESULT_DATA_FOR_PATH);
                    if(pathList != null) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,pathList);
                        lvList.setAdapter(arrayAdapter);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
