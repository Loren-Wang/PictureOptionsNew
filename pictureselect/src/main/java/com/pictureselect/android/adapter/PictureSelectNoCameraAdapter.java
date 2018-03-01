package com.pictureselect.android.adapter;

import android.content.Context;
import android.view.View;

import com.pictureselect.android.dto.StorePictureItemDto;
import com.pictureselect.android.recycleViewHolder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class PictureSelectNoCameraAdapter extends BasePictureSelectAdapter {

    private List<StorePictureItemDto> list = new ArrayList<>();
    public PictureSelectNoCameraAdapter(Context context, int windowWidth) {
        super(context, windowWidth);
    }

    public PictureSelectNoCameraAdapter setList(List<StorePictureItemDto> list) {
        if(list != null) {
            this.list = list;
        }
        return this;
    }

    public void addItemDto(StorePictureItemDto itemDto, int position){
        if(itemDto != null){
            this.list.add(position,itemDto);
            notifyItemInserted(position);
        }
    }

    /**
     * 修改莫一项的状态
     * @param storePictureItemDto
     * @param position
     */
    public void modifySelectState(StorePictureItemDto storePictureItemDto, int position){
        if(this.list != null && storePictureItemDto != null){
            this.list.set(position,storePictureItemDto);
            notifyItemChanged(position);
        }
    }


    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        final StorePictureItemDto storePictureItemDto = list.get(position);
        holder.setImagePath(storePictureItemDto.getAbsolutePath());
        holder.setSelectState(storePictureItemDto.isSelect());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder,storePictureItemDto,list.indexOf(storePictureItemDto));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * item点击事件，
     * @param storePictureItemDto
     * @param position
     * @return
     */
    public abstract void onItemClick(BaseViewHolder holder,StorePictureItemDto storePictureItemDto,int position);
}
