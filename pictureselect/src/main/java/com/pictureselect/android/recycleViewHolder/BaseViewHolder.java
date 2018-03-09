package com.pictureselect.android.recycleViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basepictureoptionslib.android.plugin.image.ImageLoadingUtis;
import com.pictureselect.android.R;
import com.pictureselect.android.dto.StorePictureVideoItemDto;
import com.pictureselect.android.view.ChangePictureSelectStateView;
import com.pictureselect.android.view.PictureSelectsView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private RelativeLayout relItem;
    public PictureSelectsView imgPic;
    public ChangePictureSelectStateView viewSelect;
    private TextView tvVideoTime;
    private Context context;
    public BaseViewHolder(Context context,View itemView) {
        super(itemView);
        this.context = context;
        relItem = itemView.findViewById(R.id.relItem);
        imgPic = itemView.findViewById(R.id.imgPicture);
        viewSelect = itemView.findViewById(R.id.viewSelect);
        tvVideoTime = itemView.findViewById(R.id.tvVideoTime);
        viewSelect.setEffectiveArea(0.5f,0f,1f,0.5f);
    }

    /**
     * 设置图片显示
     * @param path
     */
    public void setImageInfo(StorePictureVideoItemDto imageInfo){
        ImageLoadingUtis.getInstance(context).loadingPictureListItemImage(imageInfo.getAbsolutePath(),imgPic);
        if(imageInfo.getDuration() > 0){
            tvVideoTime.setVisibility(View.VISIBLE);
            tvVideoTime.setText(getTvVideoTime(imageInfo.getDuration()));
        }else {
            tvVideoTime.setVisibility(View.GONE);
        }
    }

    private String getTvVideoTime(long time){
        StringBuffer showTime = new StringBuffer();
        long nowTime = time / 1000;//总秒数
        int hour = (int) (nowTime / 3600);
        int minute = (int) (nowTime % 3600 / 60);
        int second = (int) (nowTime % 60);
        if(hour > 0){
            if(hour < 10){
                showTime.append("0");
            }
            showTime.append(hour);
            showTime.append(":");
        }

        if(hour < 10){
            showTime.append("0");
        }
        showTime.append(minute);
        showTime.append(":");

        if(second < 10){
            showTime.append("0");
        }
        showTime.append(second);

        return showTime.toString();
    }

    /**
     * 设置选中状态
     * @param state
     */
    public void setSelectState(boolean state){
        if(state){
            viewSelect.setBackgroundColor(Color.parseColor("#99000000"));
        }else {
            viewSelect.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }



}
