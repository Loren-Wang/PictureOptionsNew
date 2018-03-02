package com.pictureselect.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pictureselect.android.R;
import com.pictureselect.android.recycleViewHolder.BaseViewHolder;


public abstract class BasePictureSelectAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private LayoutInflater inflater;
    protected BasePictureSelectAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater.inflate(R.layout.item_list_picture_select,null));
    }

}
