package com.example.mvvm_study.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/11 21:35
 * @Description: 返回基础类
 */
public class BaseResponse {

    // 返回码
    @SerializedName("res_code")
    @Expose
    public Integer responseCode;

    // 返回的错误信息
    @SerializedName("res_error")
    @Expose
    public String responseError;
}
