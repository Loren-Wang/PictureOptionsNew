package com.basepictureoptionslib.android.plugin.wxplayer;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.basepictureoptionslib.android.R;
import com.bumptech.glide.Glide;

/**
 * author   maimingliang
 */
public class PictureOptionsWxMediaController extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Context mContext;
    private ImageView mImage;
    private ImageView mBack;
    private LinearLayout mBottom;
    private TextView mPosition;
    private TextView mDuration;
    private SeekBar mSeek;
    private LinearLayout mCenterStart;

    private WxMediaControll mControll;
    private ImageView mPasue;
    private Handler mHandler = new Handler();
    private LinearLayout mTop;
    private boolean mTopBottomVisible;
    private LinearLayout mLoading;

    private int thumbWidth;
    private int thumbHeight;
    private LinearLayout mError;
    private ImageView mMute;
    private boolean isMute = true;

    public PictureOptionsWxMediaController(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        View.inflate(mContext, R.layout.options_video_palyer_controller, this);

        mImage = (ImageView) findViewById(R.id.imageOp);
        mBack = (ImageView) findViewById(R.id.backOp);
        mBottom = (LinearLayout) findViewById(R.id.bottomOp);
        mPosition = ((TextView) findViewById(R.id.positionOp));
        mDuration = (TextView) findViewById(R.id.durationOp);
        mSeek = (SeekBar) findViewById(R.id.seekOp);
        mCenterStart = (LinearLayout) findViewById(R.id.rl_center_startOp);
        mPasue = (ImageView) findViewById(R.id.restart_or_pauseOp);
        mTop = (LinearLayout) findViewById(R.id.topOp);
        mLoading = (LinearLayout) findViewById(R.id.loadingOp);
        mError = (LinearLayout) findViewById(R.id.errorOp);
        mMute = findViewById(R.id.muteOp);
        mCenterStart.setOnClickListener(this);
        mPasue.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(this);
        mBack.setOnClickListener(this);
        mMute.setOnClickListener(this);
        this.setOnClickListener(this);
        setTopBottomVisible(false);
    }

    public PictureOptionsWxMediaController setThumbImage(String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                .into(mImage);
        return this;
    }

    public PictureOptionsWxMediaController setThumbWidth(int width){
        thumbWidth = width;
        return this;
    }

    public PictureOptionsWxMediaController setThumbHeight(int height){
        thumbHeight = height;

        ViewGroup.LayoutParams layoutParams = mImage.getLayoutParams();

        layoutParams.height =thumbHeight;

        mImage.setLayoutParams(layoutParams);

        return this;
    }

    public ImageView getmMute() {
        return mMute;
    }

    public void setThumbImage(@DrawableRes int resId) {
        mImage.setImageResource(resId);
    }

    public void setWxPlayer(WxMediaControll controll) {
        mControll = controll;
    }

    public void setControllerState(int currState) {

        Log.e("tag", "------ currState = " + currState);
        switch (currState) {
            case PictureOptionsWxPlayer.STATE_IDLE:
                break;
            case PictureOptionsWxPlayer.STATE_ERROR:
                mImage.setVisibility(GONE);
                mCenterStart.setVisibility(GONE);
                mLoading.setVisibility(GONE);
                mError.setVisibility(VISIBLE);
                removeCallback();
                setTopBottomVisible(false);
                break;
            case PictureOptionsWxPlayer.STATE_COMPLETED:
                mImage.setVisibility(VISIBLE);
                mCenterStart.setVisibility(VISIBLE);
                mLoading.setVisibility(GONE);
                removeCallback();
                setTopBottomVisible(false);
                break;
            case PictureOptionsWxPlayer.STATE_PREPARING:
                mImage.setVisibility(VISIBLE);
                mCenterStart.setVisibility(GONE);
                mLoading.setVisibility(VISIBLE);
                mError.setVisibility(GONE);
                mPasue.setImageResource(R.mipmap.icon_player_start);
                break;
            case PictureOptionsWxPlayer.STATE_PREPARED:
                mImage.setVisibility(GONE);
                mCenterStart.setVisibility(GONE);
                mPasue.setImageResource(R.mipmap.icon_player_start);
                mLoading.setVisibility(VISIBLE);
                startUpdateProgress();
                break;
            case PictureOptionsWxPlayer.STATE_PLAYING:
                mImage.setVisibility(GONE);
                mCenterStart.setVisibility(GONE);
                mLoading.setVisibility(GONE);
                mPasue.setImageResource(R.mipmap.icon_player_pause);
                break;
            case PictureOptionsWxPlayer.STATE_PAUSED:
                mImage.setVisibility(GONE);
                mCenterStart.setVisibility(GONE);
                mLoading.setVisibility(GONE);
                mPasue.setImageResource(R.mipmap.icon_player_start);
                break;
            case PictureOptionsWxPlayer.STATE_BUFFERING_PAUSED:
                mImage.setVisibility(GONE);
                mCenterStart.setVisibility(GONE);
                mLoading.setVisibility(VISIBLE);
                mPasue.setImageResource(R.mipmap.icon_player_start);
                break;
            case PictureOptionsWxPlayer.STATE_BUFFERING_PLAYING:
                mImage.setVisibility(GONE);
                mCenterStart.setVisibility(GONE);
                mLoading.setVisibility(VISIBLE);
                mPasue.setImageResource(R.mipmap.icon_player_pause);
                break;
        }

    }



    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
            startUpdateProgress();
        }
    };

    private Runnable dismissTopBottomRunnable = new Runnable() {
        @Override
        public void run() {
            setTopBottomVisible(false);
        }
    };

    /**
     * 更新进度条
     */
    private void updateProgress() {

        int position = mControll.getCurrentPosition();
        int duration = mControll.getDuration();

        if (duration == 0) {
            return;
        }
        int bufferPercentage = mControll.getBufferPercentage();

        mSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int) (100f * position / duration);
        mSeek.setProgress(progress);

        mPosition.setText(PictureOptionsNiceUtil.formatTime(position));
        mDuration.setText(PictureOptionsNiceUtil.formatTime(duration));
    }

    /**
     * 发送更新进条postDelayed
     */
    public void startUpdateProgress() {
        mHandler.postDelayed(progressRunnable, 100);
    }

    public void removeCallback() {
        mHandler.removeCallbacks(progressRunnable);
    }

    private void startDismissTopBottomTimer() {
        mHandler.postDelayed(dismissTopBottomRunnable, 5000);
    }

    private void setTopBottomVisible(boolean visible) {

        mTopBottomVisible = visible;
        mTop.setVisibility(visible == true ? VISIBLE : INVISIBLE);
        mBottom.setVisibility(visible == true ? VISIBLE : INVISIBLE);
        if (!visible) {
            cancelDismissTopBottomTimer();
        } else {
            startDismissTopBottomTimer();
        }


    }

    private void cancelDismissTopBottomTimer() {
        mHandler.removeCallbacks(dismissTopBottomRunnable);
    }

    @Override
    public void onClick(View v) {
        if (v == mCenterStart) {

            mControll.release();
            mControll.start();

        } else if (v == mPasue) {
            if (mControll.isPlaying()) {
                mControll.pause();
            } else {
                mControll.restart();
            }
        } else if (mBack == v) {
            mControll.finish();
        } else if (v == this) {
            setTopBottomVisible(!mTopBottomVisible);
        } else if (v == mMute) {
            setAudioMuteOrUnMute();
        }
    }

    private void setAudioMuteOrUnMute() {
//        if (isMute) {
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setVolume(1.0f,1.0f);
//            mMute.setImageResource(R.mipmap.icon_unmute);
//            isMute = false;
//        } else {
//            mMediaPlayer.setVolume(0.0f,0.0f);
//            mMute.setImageResource(R.mipmap.icon_mute);
//            isMute = true;
//        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //滑动开始
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //滑动结束
        if (mControll.isPause() || mControll.isBuffPause()) {
            mControll.restart();
        }

        int pos = (int) (mControll.getDuration() * seekBar.getProgress() / 100f); //
        mControll.seekTo(pos);

    }


    public interface WxMediaControll {
        void release();

        void start();

        void restart();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        boolean isIDLE();

        boolean isPause();

        boolean isBuffPause();

        int getBufferPercentage();

        void finish();

    }

}
