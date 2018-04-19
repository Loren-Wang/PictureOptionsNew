package com.basepictureoptionslib.android.plugin.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.basepictureoptionslib.android.plugin.wxplayer.PictureOptionsWxPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

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

    public void loadingPictureListItemImage(String path, ImageView imageView,RequestOptions requestOptions){
        if(path != null && imageView != null && !path.isEmpty()) {
            //本地文件
            File file = new File(path);
            //加载图片
            RequestBuilder<Drawable> load = Glide.with(context).load(file);
            if(requestOptions != null){
                load = load.apply(requestOptions);
            }
            load.into(imageView);
        }
    }
    public void loadingVideoAndPlay(String path, PictureOptionsWxPlayer imageView){
        if(path != null && imageView != null && !path.isEmpty()) {
            imageView.setVideoPath(path);
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
