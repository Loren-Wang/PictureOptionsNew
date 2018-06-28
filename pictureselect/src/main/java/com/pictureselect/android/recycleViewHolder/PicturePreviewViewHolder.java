package com.pictureselect.android.recycleViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pictureselect.android.utils.ImageLoadingUtis;
import com.bumptech.glide.request.RequestOptions;
import com.pictureselect.android.R;
import com.pictureselect.android.dto.StorePictureVideoItemDto;


public class PicturePreviewViewHolder extends RecyclerView.ViewHolder {
    private RelativeLayout relItem;
    private ImageView imgPreview;
    public ImageButton imgBtnPlay;
    private Context context;
    private final RequestOptions requestOptions;
    public PicturePreviewViewHolder(Context context,View itemView,int windowWidth,int windowHeight) {
        super(itemView);
        this.context = context;
        relItem = itemView.findViewById(R.id.relOpItem);
        imgPreview = itemView.findViewById(R.id.imgOpPreview);
        imgBtnPlay = itemView.findViewById(R.id.imgBtnOpPlay);
        relItem.setLayoutParams(new ViewGroup.LayoutParams(windowWidth,windowHeight));
        //加载图片
        requestOptions = new RequestOptions();
        requestOptions.override(windowWidth,windowHeight);
        requestOptions.fitCenter();
    }

    /**
     * 设置图片显示
     * @param path
     * @param windowWidth
     * @param windowHeight
     */
    public void setImageInfo(StorePictureVideoItemDto imageInfo, int windowWidth, int windowHeight){
        if(imageInfo.getDuration() > 0){
            imgBtnPlay.setVisibility(View.VISIBLE);
        }else {
            imgBtnPlay.setVisibility(View.GONE);
        }
        ImageLoadingUtis.getInstance(context).loadingPictureListItemImage(
                imageInfo.getAbsolutePath(),imgPreview,requestOptions);
    }
}
