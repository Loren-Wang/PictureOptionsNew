package com.pictureselect.android.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.pictureselect.android.dto.StorePictureVideoItemDto;
import com.pictureselect.android.interfaces_abstract.ChangeSelectStateViewCallback;
import com.pictureselect.android.recycleViewHolder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class PictureSelectNoCameraAdapter extends BasePictureSelectAdapter {

    private List<StorePictureVideoItemDto> list = new ArrayList<>();
    private RequestOptions requestOptions;
    public PictureSelectNoCameraAdapter(Context context, int width, int heigt, @DrawableRes Integer selectY, @DrawableRes Integer selectN) {
        super(context,selectY,selectN);
        requestOptions = new RequestOptions();
        requestOptions.override(width,heigt);
    }

    public PictureSelectNoCameraAdapter setList(List<StorePictureVideoItemDto> list) {
        if(list != null) {
            this.list = list;
        }
        return this;
    }

    public void addItemDto(StorePictureVideoItemDto itemDto, int position){
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
    public void modifySelectState(StorePictureVideoItemDto storePictureItemDto, int position){
        if(this.list != null && storePictureItemDto != null){
            this.list.set(position,storePictureItemDto);
            notifyItemChanged(position);
        }
    }


    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        final StorePictureVideoItemDto storePictureItemDto = list.get(position);
        holder.setImageInfo(storePictureItemDto,requestOptions);
        holder.setSelectState(storePictureItemDto.isSelect());

        holder.viewSelect.setChangeSelectStateViewCallback(new ChangeSelectStateViewCallback() {
            @Override
            public void chageState() {
                onSelceChangeClick(holder,storePictureItemDto,list.indexOf(storePictureItemDto));
            }

            @Override
            public void otherTouch() {
                onImgClick(holder,storePictureItemDto,list.indexOf(storePictureItemDto));
            }
        });
        holder.imgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgClick(holder,storePictureItemDto,list.indexOf(storePictureItemDto));
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
    public abstract void onSelceChangeClick(BaseViewHolder holder, StorePictureVideoItemDto storePictureItemDto, int position);
    public abstract void onImgClick(BaseViewHolder holder, StorePictureVideoItemDto storePictureItemDto, int position);
}
