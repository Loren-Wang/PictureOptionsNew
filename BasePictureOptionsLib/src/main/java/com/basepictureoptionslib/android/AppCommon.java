package com.basepictureoptionslib.android;

import android.content.Context;

public class AppCommon {

    public static Context APPLICATION_CONTEXT;
    public static final String OPTIONS_CONFIG_KEY = "options_config_key";//功能操作配置实体的key，有使用ongoing方通过bundle传递
    public static final String PICTURE_SELECT_RESULT_DATA_FOR_DTO = "result_data_for_dto";//图片选择后台返回实体类数据
    public static final String PICTURE_SELECT_RESULT_DATA_FOR_PATH = "result_data_for_path";//图片选择后返回图片绝对路径数据
}
