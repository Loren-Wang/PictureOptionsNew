package com.pictureselect.android.interfaces_abstract;

/**
 * 创建时间： 0002/2018/3/2 下午 3:00
 * 创建人：王亮（Loren wang）
 * 功能作用：改变图片选中状态的控件回调
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public interface ChangeSelectStateViewCallback {
    void chageState();//改变选中状态
    void otherTouch();//选中范围之外的地点触摸
}
