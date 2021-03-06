package com.pictureselect.android.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pictureselect.android.R;
import com.pictureselect.android.interfaces_abstract.ChangeSelectStateViewCallback;
import com.pictureselect.android.setting.AppConfigSetting;


/**
 * 创建时间： 0002/2018/3/2 下午 3:02
 * 创建人：王亮（Loren wang）
 * 功能作用：点击改变图片选中状态的控件
 * 思路：通过设置那部分区域点击属于是有效区域，那部分区域属于无效区域的方式来做回调；
 *      需要设置的参数有：①、有效区域起始位置所处宽高的百分比位置、结束位置处宽高百分比位置；
 *                     ②、触摸点击回调；
 *                     ③、选中状态背景颜色、非选中状态背景颜色；（暂时用不到）
 *                     ④、选中状态以及非选中情况下有效区域的图片显示;(暂时用不到)
 *
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class ChangePictureSelectStateView extends View {

    private float startPosiForWidthPercent = 0f;//起始位置所处宽度百分比
    private float startPosiForHeightPercent = 0f;//起始位置所处高度百分比
    private float endPosiForWidthPercent = 1f;//结束位置所处宽度百分比
    private float endPosiForHeightPercent = 1f;//结束位置所处高度百分比
    private ChangeSelectStateViewCallback changeSelectStateViewCallback;//触摸点击回调

    private boolean isSelect = false;

    public ChangePictureSelectStateView(Context context) {
        super(context);
        init(null,null);
    }

    public ChangePictureSelectStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null,null);
    }

    public ChangePictureSelectStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null,null);
    }

    private void init(@DrawableRes Integer selectY,@DrawableRes Integer selectN){

        //释放bitmap
        if( AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP != null
                && !AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP.isRecycled()){
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP.recycle();
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP = null;
        }
        //释放bitmap
        if( AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP != null
                && !AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP.isRecycled()){
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP.recycle();
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP = null;
        }

        if(selectY == null){
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP = BitmapFactory.decodeResource(getResources(), R.drawable.icon_picture_select_y);
        }else {
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP = BitmapFactory.decodeResource(getResources(), selectY);
        }
        if(selectN == null){
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP = BitmapFactory.decodeResource(getResources(), R.drawable.icon_picture_select_n);
        }else {
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP = BitmapFactory.decodeResource(getResources(), selectN);
        }


        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_SRCRECT = new Rect(0,0,AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP.getWidth(),AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP.getHeight());
        AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_SRCRECT = new Rect(0,0,AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP.getWidth(),AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP.getHeight());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(changeSelectStateViewCallback != null){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    boolean contains = calcViewScreenLocation().contains(event.getRawX(), event.getRawY());
                    //判断手势抬起位置是否在空间内
                    if(contains){
                        double xUpPosiPercent = event.getX() * 1.0 / getWidth();
                        double yUpPosiPercent = event.getY() * 1.0 / getWidth();
                        //判断是否在有效区域
                        if(xUpPosiPercent >= startPosiForWidthPercent
                                && xUpPosiPercent <= endPosiForWidthPercent
                                && yUpPosiPercent >= startPosiForHeightPercent
                                && yUpPosiPercent <= endPosiForHeightPercent){
                            changeSelectStateViewCallback.chageState();
                        }else {
                            changeSelectStateViewCallback.otherTouch();
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }else {
            return super.dispatchTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT == null
                || AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT.width() == 0
                || AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT.height() == 0){
            AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT = new Rect(getWidth() / 4 * 3,0,getWidth(),getWidth() / 4);
        }

        if(isSelect){
            canvas.drawBitmap(AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_BITMAP
                    ,AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_Y_SRCRECT
                    ,AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT
                    , AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_PAINT_STATE);
        }else {
            canvas.drawBitmap(AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_BITMAP
                    ,AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_N_SRCRECT
                    ,AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_SELECT_DST_RECT
                    ,AppConfigSetting.CHANGE_PICTURE_SELECT_STATE_VIEW_PAINT_STATE);
        }
    }

    public void setSelected(boolean selected,boolean isChangeBg) {
        isSelect = selected;
        if(isChangeBg) {
            if (isSelect) {
                setBackgroundColor(Color.parseColor("#99000000"));
            } else {
                setBackgroundColor(Color.parseColor("#00000000"));
            }
        }
        postInvalidate();
    }

    @Override
    public void setSelected(boolean selected) {
        setSelected(selected,true);
    }

    public boolean isSelect() {
        return isSelect;
    }

    /**
     * 设置有效范围
     * @param startPosiForWidthPercent
     * @param startPosiForHeightPercent
     * @param endPosiForWidthPercent
     * @param endPosiForHeightPercent
     */
    public ChangePictureSelectStateView setEffectiveArea(@Size(min = 0,max = 1) float startPosiForWidthPercent
            , @Size(min = 0,max = 1) float startPosiForHeightPercent
            , @Size(min = 0,max = 1) float endPosiForWidthPercent
            , @Size(min = 0,max = 1) float endPosiForHeightPercent){
        this.startPosiForWidthPercent = startPosiForWidthPercent;
        this.startPosiForHeightPercent = startPosiForHeightPercent;
        this.endPosiForWidthPercent = endPosiForWidthPercent;
        this.endPosiForHeightPercent = endPosiForHeightPercent;
        return this;
    }

    /**
     * 设置回调
     * @param changeSelectStateViewCallback
     * @return
     */
    public ChangePictureSelectStateView setChangeSelectStateViewCallback(ChangeSelectStateViewCallback changeSelectStateViewCallback) {
        this.changeSelectStateViewCallback = changeSelectStateViewCallback;
        return this;
    }

    /**
     * 设置选中以及未选中图标
     * @param selectY
     * @param selectN
     */
    public void setStateChangeImage(@DrawableRes Integer selectY,@DrawableRes Integer selectN){
        init(selectY,selectN);
    }



    /**
     * 计算View 在屏幕中的坐标。
     */
    public RectF calcViewScreenLocation() {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + getWidth(),
                location[1] + getHeight());
    }

}
