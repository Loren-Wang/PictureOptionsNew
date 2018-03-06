package com.pictureselect.android.recycleViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.basepictureoptionslib.android.plugin.image.ImageLoadingUtis;
import com.pictureselect.android.R;


public class PicturePreviewViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgPreview;
    private Context context;
    public PicturePreviewViewHolder(Context context,View itemView,int windowWidth,int windowHeight) {
        super(itemView);
        this.context = context;
        imgPreview = itemView.findViewById(R.id.imgPreview);
        imgPreview.setMinimumWidth(windowWidth);
        imgPreview.setMinimumHeight(windowHeight);
    }

    /**
     * 设置图片显示
     * @param path
     */
    public void setImagePath(String path){
        ImageLoadingUtis.getInstance(context).loadingPictureListItemImage(path,imgPreview);
    }
}
