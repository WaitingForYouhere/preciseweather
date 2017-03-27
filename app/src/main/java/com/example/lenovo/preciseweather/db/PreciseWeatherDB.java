package com.example.lenovo.preciseweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lenovo.preciseweather.model.City;
import com.example.lenovo.preciseweather.model.County;
import com.example.lenovo.preciseweather.model.Province;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/3/7.
 */

public class PreciseWeatherDB {

    private  static final String DB_NAME="precise_weather";
    private static final int VERSION=1;
    private static PreciseWeatherDB preciseWeatherDB;
    private SQLiteDatabase db;

    public PreciseWeatherDB(Context context) {
        PreciseWeatherHelper dbHelper=new PreciseWeatherHelper(context,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();
    }
    public synchronized static PreciseWeatherDB getInstance(Context context){
        if(preciseWeatherDB==null){
            preciseWeatherDB=new PreciseWeatherDB(context);
        }
        return  preciseWeatherDB;
    }

    public void  saveProvince(Province province){
        if(province!=null){
            ContentValues values=new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    public List<Province> loadProvince(){
        List<Province> list=new ArrayList<Province>();
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
            if(cursor!=null){
                cursor.close();
            }
        return list;
    }

    public void saveCity(City city){
        if(city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City",null,values);
        }
    }

    public List<City> loadCities(int provinceId){
        List<City> list =new ArrayList<City>();
        Cursor cursor=db.query("City",null,"province_id=?",
                new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return list;
    }

    public void saveCounty(County county){
        if(county!=null){
            ContentValues values=new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null,values);
        }
    }

    public List<County> loadCounty(int cityId){
        List<County> list=new ArrayList<County>();
        Cursor cursor=db.query("County",null,"city_id=?",
                new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                County county=new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return list;
    }


}
