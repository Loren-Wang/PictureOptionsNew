package com.basepictureoptionslib.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.basepictureoptionslib.android.plugin.wxplayer.WxMediaController;
import com.basepictureoptionslib.android.plugin.wxplayer.WxPlayer;
import com.basepictureoptionslib.android.utils.SharedPrefUtils;


public class VideoPlayActivity extends AppCompatActivity{
    private String videoUrl;
    private WxPlayer mWxPlayer;
    private boolean isMute;
    private ImageView getmMute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        mWxPlayer = findViewById(R.id.wx_player);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE| //保持布局状态
//                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        final WxMediaController controller = new WxMediaController(this);
        if (getIntent().getExtras() != null) {
            videoUrl = getIntent().getExtras().getString(AppCommon.VIDEO_PLAY_FOR_LOCAL_PATH, "");
            if(!videoUrl.isEmpty()){
                mWxPlayer.setVideoPath(videoUrl);

                //初始话参数
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                controller.setThumbImage(videoUrl)
                        .setThumbWidth(dm.widthPixels)
                        .setThumbHeight(dm.heightPixels);
            }
        }
        mWxPlayer.setMediaController(controller);
        getmMute = controller.getmMute();

        if (isMute) {
            mWxPlayer.setVolumeOff();
            getmMute.setImageResource(R.mipmap.icon_mute);
        } else {
            mWxPlayer.setVolumeOn();
            getmMute.setImageResource(R.mipmap.icon_unmute);
        }

        controller.getmMute().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mute = SharedPrefUtils.getBoolean(getApplicationContext(),AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
                if (mute) {
                    mWxPlayer.setVolumeOn();
                    SharedPrefUtils.putBoolean(getApplicationContext(),AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, false);
                    getmMute.setImageResource(R.mipmap.icon_unmute);
                } else {
                    mWxPlayer.setVolumeOff();
                    SharedPrefUtils.putBoolean(getApplicationContext(),AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
                    getmMute.setImageResource(R.mipmap.icon_mute);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWxPlayer.release();
        SharedPrefUtils.putBoolean(getApplicationContext(),AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWxPlayer.release();
        SharedPrefUtils.putBoolean(getApplicationContext(),AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
    }
}
