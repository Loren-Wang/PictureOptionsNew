package com.pictureselect.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pictureselect.android.R;
import com.pictureselect.android.dto.StorePictureItemDto;
import com.pictureselect.android.recycleViewHolder.PicturePreviewViewHolder;

import java.util.ArrayList;
import java.util.List;


public class PicturePreviewAdapter extends RecyclerView.Adapter<PicturePreviewViewHolder> {

    private List<StorePictureItemDto> list = new ArrayList<>();
    private LayoutInflater inflater;
    private int windowWidth;
    private int windowHeight;
    public PicturePreviewAdapter(Context context,int windowWidth,int windowHeight){
        inflater = LayoutInflater.from(context);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public PicturePreviewAdapter setList(ArrayList<StorePictureItemDto> list) {
        if(list != null) {
            this.list = list;
        }
        return this;
    }

    @Override
    public PicturePreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicturePreviewViewHolder(inflater.inflate(R.layout.item_list_picture_preview,null),windowWidth,windowHeight);
    }

    @Override
    public void onBindViewHolder(PicturePreviewViewHolder holder, int position) {
        final StorePictureItemDto storePictureItemDto = list.get(position);
        holder.setImagePath(storePictureItemDto.getAbsolutePath());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
