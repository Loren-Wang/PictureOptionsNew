package com.pictureoptions.android;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.pictureselect.android.AppCommon;
import com.pictureselect.android.activity.PictureVideoSelectActivity;
import com.pictureselect.android.config.PictureVideoSelectConfirg;
import com.pictureselect.android.config.PictureVideoSelectorThemeConfig;

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
        PictureVideoSelectorThemeConfig.getInstance(getApplicationContext()).setScanDirList( new String[]{
                PROJECT_FILE_DIR_VIDEO,PROJECT_FILE_DIR_CAMERA_IMAGE,Environment.getExternalStorageDirectory().getPath() + "/DCIM/"
                ,Environment.getExternalStorageDirectory().getPath() + "/Pictures/"
        });

        //异常信息收集
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());



        //注册短信变化监听
//        this.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, new SmsContent(new Handler()));



    }

    class SmsContent extends ContentObserver {
        private Cursor cursor = null;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SmsContent(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
//
//        /**
//         * @Description 当短信表发送改变时，调用该方法
//         * 需要两种权限
//         * android.permission.READ_SMS 读取短信
//         * android.permission.WRITE_SMS 写短信
//         * @Author Snake
//         * @Date 2010-1-12
//         */
//        @Override
//        public void onChange(boolean selfChange) {
//            // TODO Auto-generated method stub
//            super.onChange(selfChange);
////            // 读取收件箱中指定号码的短信
////            cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read"}, " address=? and read=?", new String[]{"12345678901", "0"}, "date desc");
////            if (cursor != null) {
////                ContentValues values = new ContentValues();
////                values.put("read", "1"); //修改短信为已读模式
////                cursor.moveToFirst();
////                while (cursor.isLast()){
////                    //更新当前未读短信状态为已读
////                    getContentResolver().update(Uri.parse("content://sms/inbox"), values, " _id=?", new String[]{""+cursor.getInt(0)});
////                    cursor.moveToNext();
////                }
////            }
//        }
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
//                        LogUtils.logD(TAG, "未获取到最大选择图片数量");
                    }
                    try {
                        pictureVideoSelectConfirg.setMaxSelectVideoNum(Integer.parseInt(edtMaxVideoNum.getText().toString()));
                    } catch (Exception e) {
//                        LogUtils.logD(TAG, "未获取到最大选择视频数量");
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
                        pictureVideoSelectConfirg.setPictureFilter(PictureVideoSelectConfirg.FILTER_PICTURE_FOR_NONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        pictureVideoSelectConfirg.setVideoFilter(PictureVideoSelectConfirg.FILTER_VIDEO_FOR_NONE);
//                        pictureVideoSelectConfirg.setVideoFilter(PictureVideoSelectConfirg.FILTER_VIDEO_FOR_SIZE_AND_DURATION,null,3065964,2000,null);

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
