package com.pictureselect.android.recycleViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.basepictureoptionslib.android.plugin.image.ImageLoadingUtis;
import com.pictureselect.android.R;
import com.pictureselect.android.dto.StorePictureVideoItemDto;


public class PicturePreviewViewHolder extends RecyclerView.ViewHolder {
    private RelativeLayout relItem;
    private ImageView imgPreview;
    public ImageButton imgBtnPlay;
    private Context context;
    public PicturePreviewViewHolder(Context context,View itemView,int windowWidth,int windowHeight) {
        super(itemView);
        this.context = context;
        relItem = itemView.findViewById(R.id.relItem);
        imgPreview = itemView.findViewById(R.id.imgPreview);
        imgBtnPlay = itemView.findViewById(R.id.imgBtnPlay);
        relItem.setLayoutParams(new ViewGroup.LayoutParams(windowWidth,windowHeight));
    }

    /**
     * 设置图片显示
     * @param path
     */
    public void setImageInfo(StorePictureVideoItemDto imageInfo){
        if(imageInfo.getDuration() > 0){
            imgBtnPlay.setVisibility(View.VISIBLE);
        }else {
            imgBtnPlay.setVisibility(View.GONE);
        }
        ImageLoadingUtis.getInstance(context).loadingPictureListItemImage(imageInfo.getAbsolutePath(),imgPreview);
    }
}
