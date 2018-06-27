package com.pictureselect.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basepictureoptionslib.android.AppCommon;
import com.pictureselect.android.activity.PictureOptionsVideoPlayActivity;
import com.pictureselect.android.R;
import com.pictureselect.android.dto.StorePictureVideoItemDto;
import com.pictureselect.android.recycleViewHolder.PicturePreviewViewHolder;

import java.util.ArrayList;
import java.util.List;


public class PicturePreviewAdapter extends RecyclerView.Adapter<PicturePreviewViewHolder> {

    private List<StorePictureVideoItemDto> list = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private int windowWidth;
    private int windowHeight;
    public PicturePreviewAdapter(Context context, int windowWidth, int windowHeight){
        inflater = LayoutInflater.from(context);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.context = context;
    }

    public PicturePreviewAdapter setList(ArrayList<StorePictureVideoItemDto> list) {
        if(list != null) {
            this.list = list;
        }
        return this;
    }


    @Override
    public PicturePreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicturePreviewViewHolder(context,inflater.inflate(R.layout.item_list_picture_and_video_preview,null),windowWidth,windowHeight);
    }

    @Override
    public void onBindViewHolder(PicturePreviewViewHolder holder, int position) {
        final StorePictureVideoItemDto storePictureItemDto = list.get(position);
        holder.setImageInfo(storePictureItemDto,windowWidth,windowHeight);

        holder.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureOptionsVideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AppCommon.VIDEO_PLAY_FOR_LOCAL_PATH,storePictureItemDto.getAbsolutePath());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
