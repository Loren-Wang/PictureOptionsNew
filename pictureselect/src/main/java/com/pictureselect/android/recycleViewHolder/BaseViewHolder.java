package com.pictureselect.android.recycleViewHolder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.basepictureoptionslib.android.plugin.image.ImageLoadingUtis;
import com.pictureselect.android.R;
import com.pictureselect.android.view.ChangeSelectStateView;
import com.pictureselect.android.view.PictureSelectView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public PictureSelectView imgPic;
    public ChangeSelectStateView viewSelect;
    public BaseViewHolder(View itemView) {
        super(itemView);
        imgPic = itemView.findViewById(R.id.imgPic);
        viewSelect = itemView.findViewById(R.id.viewSelect);
        viewSelect.setEffectiveArea(0.5f,0f,1f,0.5f);
    }

    /**
     * 设置图片显示
     * @param path
     */
    public void setImagePath(String path){
        ImageLoadingUtis.getInstance().loadingPictureListItemImage(path,imgPic);
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