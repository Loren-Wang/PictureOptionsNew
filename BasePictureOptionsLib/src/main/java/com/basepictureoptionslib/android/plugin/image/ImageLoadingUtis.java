package com.basepictureoptionslib.android.plugin.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageLoadingUtis {
    private static ImageLoadingUtis imageLoadingUtis;
    private Context context;

    private ImageLoadingUtis(Context context){
        this.context = context;
    }

    public static ImageLoadingUtis getInstance(Context context){
        if(imageLoadingUtis == null){
            imageLoadingUtis = new ImageLoadingUtis(context);
        }
        return imageLoadingUtis;
    }

    public void loadingPictureListItemImage(String path, ImageView imageView){
        if(path != null && imageView != null && !path.isEmpty()) {
            //本地文件
            File file = new File(path);
            //加载图片
            Glide.with(context).load(file).into(imageView);
        }
    }

    public void onResume(){
        Glide.with(context).resumeRequests();
    }
    public void onPause(){
        Glide.with(context).pauseRequests();
        Glide.get(context).clearMemory();
    }


}
