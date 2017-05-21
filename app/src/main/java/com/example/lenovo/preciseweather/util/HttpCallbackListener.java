package com.example.lenovo.preciseweather.util;

/**
 * Created by lenovo on 2017/3/7.
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
