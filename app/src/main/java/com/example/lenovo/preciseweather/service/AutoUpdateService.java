package com.example.lenovo.preciseweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lenovo.preciseweather.util.HttpCallbackListener;
import com.example.lenovo.preciseweather.util.HttpUtil;
import com.example.lenovo.preciseweather.util.UtiLity;

/**
 * Created by lenovo on 2017/3/9.
 */

public class AutoUpdateService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;
        long triggerAttime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME,triggerAttime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String name=prefs.getString("name","");
        String address1="https://api.thinkpage.cn/v3/weather/daily.json?key=pmbulurcoxdqcs1q&location=" +
                name + "&language=zh-Hans&unit=c&start=0&days=5";
        HttpUtil.sendHttpRequest(address1, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.e("AutoUp1", response );
                UtiLity.handleWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        String address2="https://api.thinkpage.cn/v3/weather/now.json?key=pmbulurcoxdqcs1q&location=" +
                name+
                "&language=zh-Hans&unit=c";
        HttpUtil.sendHttpRequest(address2, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.e("AutoUp2", response );
                UtiLity.handleNowWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
