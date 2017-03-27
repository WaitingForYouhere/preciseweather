package com.example.lenovo.preciseweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.preciseweather.R;
import com.example.lenovo.preciseweather.db.PreciseWeatherDB;
import com.example.lenovo.preciseweather.model.City;
import com.example.lenovo.preciseweather.model.County;
import com.example.lenovo.preciseweather.model.Province;
import com.example.lenovo.preciseweather.util.HttpCallbackListener;
import com.example.lenovo.preciseweather.util.HttpUtil;
import com.example.lenovo.preciseweather.util.UtiLity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/3/8.
 */

public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private ProgressDialog progressDialog;
    private TextView titleTest;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private PreciseWeatherDB preciseWeatherDB;
    private List<String> dataList=new ArrayList<String>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectProvince;
    private City selectCity;
    private int currentLevel;
    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromWeatherActivity=getIntent().getBooleanExtra("from_weather_activity",false);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected",false)&&!isFromWeatherActivity){
            Intent intent=new Intent(this,SecondWeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView= (ListView) findViewById(R.id.list_view);
        titleTest= (TextView) findViewById(R.id.title_text);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        preciseWeatherDB=PreciseWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectProvince=provinceList.get(position);
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    selectCity=cityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){
                    String countyCode=countyList.get(position).getCountyCode();
                    String countyName=countyList.get(position).getCountyName();
                    Intent intent=new Intent(ChooseAreaActivity.this,SecondWeatherActivity.class);
                    SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(ChooseAreaActivity.this).edit();
                    editor.putString("county_code",countyCode);
                    editor.putString("county_name",countyName);
                    editor.commit();
                    startActivity(intent);
                    finish();
                }
            }

        });
        queryProvinces();
    }


    private void queryProvinces() {
        provinceList=preciseWeatherDB.loadProvince();
        if(provinceList.size()>0){
            dataList.clear();
            for (Province p:provinceList) {
                dataList.add(p.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleTest.setText("中国");
            currentLevel=LEVEL_PROVINCE;
        }else {
            queryFromServer(null,"province");
        }
    }


    private void queryCities() {
        cityList=preciseWeatherDB.loadCities(selectProvince.getId());
        if(cityList.size()>0){
            dataList.clear();
            for (City city:cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleTest.setText(selectProvince.getProvinceName());
            currentLevel=LEVEL_CITY;
        }else {
            queryFromServer(selectProvince.getProvinceCode(),"city");
        }
    }

    private void queryCounties() {
        countyList=preciseWeatherDB.loadCounty(selectCity.getId());
        if(countyList.size()>0){
            dataList.clear();
            for (County county:countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleTest.setText(selectCity.getCityName());
            currentLevel=LEVEL_COUNTY;
        }else {
            queryFromServer(selectCity.getCityCode(),"county");
        }
    }

    private void queryFromServer(final String code,final String type) {
        String address;
        if(!TextUtils.isEmpty(code)){
            address="http://www.weather.com.cn/data/list3/city"+code+".xml" ;
        }else {
            address="http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result=false;
                if("province".equals(type)){
                    result= UtiLity.handleProvincesResponse(preciseWeatherDB,response);
                }else if("city".equals(type)){
                    result=UtiLity.handleCitiesResponse(preciseWeatherDB,response,selectProvince.getId());
                }else if("county".equals(type)){
                    result=UtiLity.handleCountiesResponse(preciseWeatherDB,response,selectCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();

                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void closeProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(currentLevel==LEVEL_COUNTY){
            queryCities();
        }else if(currentLevel==LEVEL_CITY){
            queryProvinces();
        }else {if(isFromWeatherActivity){
            Intent intent=new Intent(this,SecondWeatherActivity.class);
            startActivity(intent);
        }
            finish();
        }
    }
}