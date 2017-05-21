package com.example.lenovo.preciseweather.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.lenovo.preciseweather.activity.SecondWeatherActivity;
import com.example.lenovo.preciseweather.db.PreciseWeatherDB;
import com.example.lenovo.preciseweather.model.City;
import com.example.lenovo.preciseweather.model.County;
import com.example.lenovo.preciseweather.model.Province;
import com.example.lenovo.preciseweather.service.AutoUpdateService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lenovo on 2017/3/7.
 */

public class UtiLity {

    public synchronized static boolean handleProvincesResponse(
            PreciseWeatherDB preciseWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if(allProvinces!=null&&allProvinces.length>0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    preciseWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCitiesResponse(PreciseWeatherDB preciseWeatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for (String p:allCities) {
                    String[] array=p.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    preciseWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(PreciseWeatherDB preciseWeatherDB,String response,int citiesId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for (String p:allCities) {
                    String[] array=p.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(citiesId);
                    preciseWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleNowWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            JSONObject realjsonObject=jsonArray.getJSONObject(0);
            JSONObject nowInfo=realjsonObject.getJSONObject("now");
            String tempNow=nowInfo.getString("temperature");
            String textNow=nowInfo.getString("text");
            saveNowWeatherInfo(context,tempNow,textNow);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private static void saveNowWeatherInfo(Context context, String tempNow, String textNow) {
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("tempNow",tempNow);
        editor.putString("textNow",textNow);
        editor.commit();
    }

    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            JSONObject realjsonObject=jsonArray.getJSONObject(0);
            JSONObject locationInfo=realjsonObject.getJSONObject("location");
            JSONArray dailyArray=realjsonObject.getJSONArray("daily");
            JSONObject todayObject=dailyArray.getJSONObject(0);
            JSONObject nextdayObject=dailyArray.getJSONObject(1);
            JSONObject thirdObject=dailyArray.getJSONObject(2);




            String cityName=locationInfo.getString("name");
            Log.e("ming",cityName);

            String weatherCode=locationInfo.getString("id");
            String temp11=todayObject.getString("high");
            String temp12=todayObject.getString("low");
            String temp21=nextdayObject.getString("high");
            String temp22=nextdayObject.getString("low");
            String temp31=thirdObject.getString("high");
            String temp32=thirdObject.getString("low");
            String weatherday1=todayObject.getString("text_day");
            String weatherday2=nextdayObject.getString("text_day");
            String weatherday3=thirdObject.getString("text_day");
            String weathernight1=todayObject.getString("text_night");
            String weathernight2=nextdayObject.getString("text_night");
            String weathernight3=thirdObject.getString("text_night");
            String wind_direction=todayObject.getString("wind_direction");
            String wind_scale=todayObject.getString("wind_scale");
            String codeDay1=todayObject.getString("code_day");
            String codeDay2=nextdayObject.getString("code_day");
            String codeDay3=thirdObject.getString("code_day");

//            String weatherDesp=weatherInfo.getString("weather");
            String publishTime=realjsonObject.getString("last_update");
            saveWeatherInfo(context,cityName,weatherCode,temp11, temp12,temp21,temp22,temp31,  temp32,weatherday1,
               weatherday2,weatherday3,weathernight1,weathernight2, weathernight3,wind_direction,wind_scale,codeDay1,
                    codeDay2,codeDay3,publishTime);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp11,
        String temp12, String temp21, String temp22, String temp31, String temp32,String weatherday1,
        String weatherday2, String weatherday3, String weathernight1, String weathernight2,
        String weathernight3, String wind_direction, String wind_scale,String codeDay1,String codeDay2,
                                        String codeDay3,String publishTime) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日 HH:mm:ss", Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp11",temp11);
        editor.putString("temp12",temp12);
        editor.putString("temp21",temp21);
        editor.putString("temp22",temp22);
        editor.putString("temp31",temp31);
        editor.putString("temp32",temp32);
        editor.putString("weatherday1",weatherday1);
        editor.putString("weatherday2",weatherday2);
        editor.putString("weatherday3",weatherday3);
        editor.putString("weathernight1",weathernight1);
        editor.putString("weathernight2",weathernight2);
        editor.putString("weathernight3",weathernight3);
        editor.putString("winddirection",wind_direction);
        editor.putString("windscale",wind_scale);
        editor.putString("codeday1",codeDay1);
        editor.putString("codeday2",codeDay2);
        editor.putString("codeday3",codeDay3);


//        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();



    }

    public static void handleLocationResponse(Context context, String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("results");

            JSONObject realjsonObject=jsonArray.getJSONObject(1);
            JSONArray jsonArray1=realjsonObject.getJSONArray("address_components");
            JSONObject nowInfo=jsonArray1.getJSONObject(1);

            String area=nowInfo.getString("short_name");
            saveArea(context,area);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private static void saveArea(Context context, String area) {
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("area",area);
        Log.e("area",area);
        editor.commit();
    }
}
