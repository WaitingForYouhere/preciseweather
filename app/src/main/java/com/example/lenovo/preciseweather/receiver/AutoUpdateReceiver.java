package com.example.lenovo.preciseweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lenovo.preciseweather.service.AutoUpdateService;

/**
 * Created by lenovo on 2017/3/9.
 */

public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, AutoUpdateService.class);
        context.startActivity(i);
    }
}
