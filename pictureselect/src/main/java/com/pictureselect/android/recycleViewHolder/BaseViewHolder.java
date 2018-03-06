package com.pictureselect.android.recycleViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.basepictureoptionslib.android.plugin.image.ImageLoadingUtis;
import com.pictureselect.android.R;
import com.pictureselect.android.view.ChangePictureSelectStateView;
import com.pictureselect.android.view.PictureSelectsView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private RelativeLayout relItem;
    public PictureSelectsView imgPic;
    public ChangePictureSelectStateView viewSelect;
    private Context context;
    public BaseViewHolder(Context context,View itemView) {
        super(itemView);
        this.context = context;
        relItem = itemView.findViewById(R.id.relItem);
        imgPic = itemView.findViewById(R.id.imgPicture);
        viewSelect = itemView.findViewById(R.id.viewSelect);
        viewSelect.setEffectiveArea(0.5f,0f,1f,0.5f);
    }

    /**
     * 设置图片显示
     * @param path
     */
    public void setImagePath(String path){
        ImageLoadingUtis.getInstance(context).loadingPictureListItemImage(path,imgPic);
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
