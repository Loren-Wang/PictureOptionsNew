package com.pictureselect.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 创建时间： 0001/2018/3/1 上午 10:03
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选中与未选中
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@SuppressLint("AppCompatCustomView")
public class PictureSelectView extends ImageView {

    public PictureSelectView(Context context) {
        super(context);
        init();
    }

    public PictureSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PictureSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
