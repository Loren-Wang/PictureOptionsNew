package com.basepictureoptionslib.android.plugin.image;

import android.widget.ImageView;

import com.basepictureoptionslib.android.AppCommon;
import com.bumptech.glide.Glide;

import java.io.File;

public class ImageLoadingUtis {
    private static ImageLoadingUtis imageLoadingUtis;

    private ImageLoadingUtis(){
    }

    public static ImageLoadingUtis getInstance(){
        if(imageLoadingUtis == null){
            imageLoadingUtis = new ImageLoadingUtis();
        }
        return imageLoadingUtis;
    }

    public void loadingPictureListItemImage(String path, ImageView imageView){
        if(path != null && imageView != null && !path.isEmpty()) {
            //本地文件
            File file = new File(path);
            //加载图片
            Glide.with(AppCommon.APPLICATION_CONTEXT).load(file).into(imageView);
        }
    }

    public void onResume(){
        Glide.with(AppCommon.APPLICATION_CONTEXT).resumeRequests();
    }
    public void onPause(){
        Glide.with(AppCommon.APPLICATION_CONTEXT).pauseRequests();
        Glide.get(AppCommon.APPLICATION_CONTEXT).clearMemory();
    }


}
