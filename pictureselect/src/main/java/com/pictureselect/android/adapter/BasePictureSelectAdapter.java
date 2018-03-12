package com.pictureselect.android.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pictureselect.android.R;
import com.pictureselect.android.recycleViewHolder.BaseViewHolder;


public abstract class BasePictureSelectAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private Integer selectY;
    private Integer selectN;

    protected BasePictureSelectAdapter(Context context, @DrawableRes Integer selectY, @DrawableRes Integer selectN){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.selectY = selectY;
        this.selectN = selectN;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(context,inflater.inflate(R.layout.item_list_picture_select_options,null),selectY,selectN);
    }

}
