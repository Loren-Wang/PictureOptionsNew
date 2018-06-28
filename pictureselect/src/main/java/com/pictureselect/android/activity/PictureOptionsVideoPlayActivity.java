package com.pictureselect.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.lorenwang.tools.android.SharedPrefUtils;
import com.pictureselect.android.AppCommon;
import com.pictureselect.android.R;
import com.pictureselect.android.wxplayer.PictureOptionsWxMediaController;
import com.pictureselect.android.wxplayer.PictureOptionsWxPlayer;


public class PictureOptionsVideoPlayActivity extends AppCompatActivity{
    private String videoUrl;
    private PictureOptionsWxPlayer pictureOpWxPlayer;
    private boolean isMute;
    private ImageView getmMute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_video_play);
        pictureOpWxPlayer = findViewById(R.id.pictureOpWxPlayer);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE| //保持布局状态
//                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        final PictureOptionsWxMediaController controller = new PictureOptionsWxMediaController(this);
        if (getIntent().getExtras() != null) {
            videoUrl = getIntent().getExtras().getString(AppCommon.VIDEO_PLAY_FOR_LOCAL_PATH, "");
            if(!videoUrl.isEmpty()){
                pictureOpWxPlayer.setVideoPath(videoUrl);

                //初始话参数
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                controller.setThumbImage(videoUrl)
                        .setThumbWidth(dm.widthPixels)
                        .setThumbHeight(dm.heightPixels);
            }
        }
        pictureOpWxPlayer.setMediaController(controller);
        getmMute = controller.getmMute();

        if (isMute) {
            pictureOpWxPlayer.setVolumeOff();
            getmMute.setImageResource(R.mipmap.icon_mute);
        } else {
            pictureOpWxPlayer.setVolumeOn();
            getmMute.setImageResource(R.mipmap.icon_unmute);
        }

        controller.getmMute().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mute = SharedPrefUtils.getInstance(getApplicationContext()).getBoolean(AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
                if (mute) {
                    pictureOpWxPlayer.setVolumeOn();
                    SharedPrefUtils.getInstance(getApplicationContext()).putBoolean(AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, false);                   getmMute.setImageResource(R.mipmap.icon_unmute);
                } else {
                    pictureOpWxPlayer.setVolumeOff();
                    SharedPrefUtils.getInstance(getApplicationContext()).putBoolean(AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
                    getmMute.setImageResource(R.mipmap.icon_mute);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pictureOpWxPlayer.release();
        SharedPrefUtils.getInstance(getApplicationContext()).putBoolean(AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pictureOpWxPlayer.release();
        SharedPrefUtils.getInstance(getApplicationContext()).putBoolean(AppCommon.VIDEO_PLAY_MUTE_OR_UNMUTE, true);
    }
}
