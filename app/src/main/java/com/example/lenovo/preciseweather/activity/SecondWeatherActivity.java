package com.example.lenovo.preciseweather.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.preciseweather.R;
import com.example.lenovo.preciseweather.service.AutoUpdateService;
import com.example.lenovo.preciseweather.util.HttpCallbackListener;
import com.example.lenovo.preciseweather.util.HttpUtil;
import com.example.lenovo.preciseweather.util.UtiLity;
import com.example.lenovo.preciseweather.view.NowWeatherView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.InputStream;
import java.util.List;

/**
 * Created by lenovo on 2017/3/10.
 */

public class SecondWeatherActivity extends Activity implements View.OnClickListener {

    private int[] picid = new int[]{R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12,
            R.drawable.p13, R.drawable.p14, R.drawable.p15, R.drawable.p16, R.drawable.p17, R.drawable.p18,
            R.drawable.p19, R.drawable.p20, R.drawable.p21, R.drawable.p22, R.drawable.p23, R.drawable.p24, R.drawable.p25,
            R.drawable.p26, R.drawable.p27, R.drawable.p28, R.drawable.p29, R.drawable.p30, R.drawable.p31, R.drawable.p32,
            R.drawable.p33, R.drawable.p34, R.drawable.p35, R.drawable.p36, R.drawable.p37, R.drawable.p38};

    private ProgressDialog progressDialog;
    private LocationManager locationmanager;
    private String provider;

    private TextView currentTemp;
    private TextView cityname;
    private TextView weatherProfile;
    private TextView windDirection;
    private TextView windLevel;
    private TextView relativeHumidity;
    private TextView relativeHumidityPercentage;
    private TextView airQuality;
    private TextView airNum;
    private TextView todayRain;
    private TextView todayAirLevel;
    private TextView todayTempRange;
    private TextView tomorrowRain;
    private TextView tomorrowAirLevel;
    private TextView tomorrowTempRange;
    private TextView dayAfterTomorrowAirLevel;
    private TextView dayAfterTomorrowRain;
    private TextView dayAfterTomorrowTempRange;
    private TextView loveTips;
    private TextView update_time;


    private ScrollView scrolllayout;


    private RelativeLayout weather_now;
    private LinearLayout today;
    private LinearLayout tomorrew;
    private LinearLayout dayAfterTomorrow;

    private ImageView todayImage;
    private ImageView tomorrowImage;
    private ImageView dayaftertomrrowImage;
    private ImageButton update;
    private Button setting;
    private Button trend;
    private Button location;

    private NowWeatherView mNowWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.secondweather_layout);

        mNowWeather= (NowWeatherView) findViewById(R.id.now_weather_view);
        InputStream is = getResources().openRawResource(R.drawable.p2);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        mNowWeather.setBitmap(mBitmap);

//        currentTemp = (TextView) findViewById(R.id.current_temp);
        cityname = (TextView) findViewById(R.id.the_city);
//        weatherProfile = (TextView) findViewById(R.id.weather_profile);
        windDirection = (TextView) findViewById(R.id.wind_detail);
        windLevel = (TextView) findViewById(R.id.wind_level);
        relativeHumidity = (TextView) findViewById(R.id.relative_humidity);
        relativeHumidityPercentage = (TextView) findViewById(R.id.relative_humidity_percentage);
        airQuality = (TextView) findViewById(R.id.air_quality);
        airNum = (TextView) findViewById(R.id.air_num);

        update_time = (TextView) findViewById(R.id.update_time);

        todayRain = (TextView) findViewById(R.id.rain_today);
        tomorrowRain = (TextView) findViewById(R.id.rain_tomorrow);
        dayAfterTomorrowRain = (TextView) findViewById(R.id.day_after_tomorrow_rain);
        todayAirLevel = (TextView) findViewById(R.id.today_air_level);
        tomorrowAirLevel = (TextView) findViewById(R.id.tomorrow_air_level);
        dayAfterTomorrowAirLevel = (TextView) findViewById(R.id.day_after_tomorrow_air_level);
        todayTempRange = (TextView) findViewById(R.id.today_temp_range);
        tomorrowTempRange = (TextView) findViewById(R.id.tomorrow_temp_range);
        dayAfterTomorrowTempRange = (TextView) findViewById(R.id.dayaftertomorrow_temp_range);
        loveTips = (TextView) findViewById(R.id.love_tips);

        todayImage = (ImageView) findViewById(R.id.today_weather_image);
        tomorrowImage = (ImageView) findViewById(R.id.tomorrow_weather_image);
        dayaftertomrrowImage = (ImageView) findViewById(R.id.day_after_tomorrow_weather_image);


        scrolllayout = (ScrollView) findViewById(R.id.scroll_layout);
        weather_now = (RelativeLayout) findViewById(R.id.weather_layout_now);
        today = (LinearLayout) findViewById(R.id.today);
        tomorrew = (LinearLayout) findViewById(R.id.tomorrow);
        dayAfterTomorrow = (LinearLayout) findViewById(R.id.day_after_tomorrow);

        update = (ImageButton) findViewById(R.id.update);
        trend = (Button) findViewById(R.id.trend);
        setting = (Button) findViewById(R.id.setting);
        location = (Button) findViewById(R.id.location);

        trend.setOnClickListener(this);
        update.setOnClickListener(this);
        setting.setOnClickListener(this);
        today.setOnClickListener(this);
        tomorrew.setOnClickListener(this);
        dayAfterTomorrow.setOnClickListener(this);
        location.setOnClickListener(this);


//        设置高度

        WindowManager wm = this.getWindowManager();
        int screemheight = wm.getDefaultDisplay().getHeight();
        Log.e("屏高", String.valueOf(screemheight));

        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) weather_now.getLayoutParams();
        param.height = screemheight * 56 / 100;
        LinearLayout.LayoutParams param1 = (LinearLayout.LayoutParams) today.getLayoutParams();
        LinearLayout.LayoutParams param2 = (LinearLayout.LayoutParams) tomorrew.getLayoutParams();
        LinearLayout.LayoutParams param3 = (LinearLayout.LayoutParams) dayAfterTomorrow.getLayoutParams();
        LinearLayout.LayoutParams paramButton = (LinearLayout.LayoutParams) trend.getLayoutParams();
        param1.height = screemheight * 11/ 100;
        param2.height = screemheight * 11/ 100;
        param3.height = screemheight * 11/ 100;
        paramButton.height = screemheight * 8 / 100;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countyCode = prefs.getString("county_code", "");
        String countyName = prefs.getString("county_name", "");
        String name = getPinYin(countyName);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("name", name);
        editor.commit();
        Log.e("ming", name);
        if (!TextUtils.isEmpty(name)) {
//            loading.setText("同步中...");
//            scrolllayout.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode, name);
        } else {
            showWeather();
        }

    }


    private void queryWeatherCode(String countyCode, String name) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode", name);
    }

    private void queryWeatherInfo(String name) {
        Log.e("haha",name);
        String address1 = "https://api.thinkpage.cn/v3/weather/daily.json?key=pmbulurcoxdqcs1q&location=" +
                name + "&language=zh-Hans&unit=c&start=0&days=5";
        String address2 = "https://api.thinkpage.cn/v3/weather/now.json?key=pmbulurcoxdqcs1q&location=" +
                name + "&language=zh-Hans&unit=c";
        queryFromServer(address1, "weatherCode", name);
        queryFromServer(address2, "weatherCode", name);
    }

    private void queryFromServer(final String address, final String type, final String name) {

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.e("chaxun","hasjij");
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        queryWeatherInfo(name);

                    }
                } else if ("weatherCode".equals(type)) {
                    Log.e("mmname", name);
                    Log.e("maddr", address);
                    if (address.equals("https://api.thinkpage.cn/v3/weather/daily.json?key=pmbulurcoxdqcs1q&location=" +
                            name + "&language=zh-Hans&unit=c&start=0&days=5")) {
                        UtiLity.handleWeatherResponse(SecondWeatherActivity.this, response);
                    } else {
                        UtiLity.handleNowWeatherResponse(SecondWeatherActivity.this, response);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }else if("cityName".equals(type)){
                    Log.e("chaxun","haha");
                    UtiLity.handleLocationResponse(SecondWeatherActivity.this, response);
                    takeTheName();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        cityname.setText(prefs.getString("city_name", ""));
//        currentTemp.setText(prefs.getString("tempNow", "") + "°");
//        weatherProfile.setText(prefs.getString("textNow", ""));
        windDirection.setText(prefs.getString("winddirection", "") + "风");
        windLevel.setText(prefs.getString("windscale", "") + "级");

        if(prefs.getString("codeday1", "")!=null&&prefs.getString("codeday2", "")!=null&&prefs.getString("codeday3", "")!=null){
            todayImage.setImageResource(picid[Integer.valueOf(prefs.getString("codeday1", "")).intValue()]);
            tomorrowImage.setImageResource(picid[Integer.valueOf(prefs.getString("codeday2", "")).intValue()]);
            dayaftertomrrowImage.setImageResource(picid[Integer.valueOf(prefs.getString("codeday3", "")).intValue()]);
        }


        if (prefs.getString("weatherday1", "").equals(prefs.getString("weathernight1", ""))) {
            todayRain.setText(prefs.getString("weatherday1", ""));
        } else {
            todayRain.setText(prefs.getString("weatherday1", "") + "转" + prefs.getString("weathernight1", ""));
        }
        if (prefs.getString("weatherday2", "").equals(prefs.getString("weathernight2", ""))) {
            tomorrowRain.setText(prefs.getString("weatherday2", ""));
        } else {
            tomorrowRain.setText(prefs.getString("weatherday2", "") + "转" + prefs.getString("weathernight2", ""));
        }
        if (prefs.getString("weatherday3", "").equals(prefs.getString("weathernight3", ""))) {
            dayAfterTomorrowRain.setText(prefs.getString("weatherday3", ""));
        } else {
            dayAfterTomorrowRain.setText(prefs.getString("weatherday3", "") + "转" + prefs.getString("weathernight3", ""));
        }


        update_time.setText("更新于" + prefs.getString("current_date", ""));

//        todayAirLevel.setText(prefs.getString("city_name",""));
//        tomorrowAirLevel.setText(prefs.getString("city_name",""));
//        dayAfterTomorrowAirLevel.setText(prefs.getString("city_name",""));
        todayTempRange.setText(prefs.getString("temp11", "") + "°" + "/" + prefs.getString("temp12", "") + "°");
        tomorrowTempRange.setText(prefs.getString("temp21", "") + "°" + "/" + prefs.getString("temp22", "") + "°");
        dayAfterTomorrowTempRange.setText(prefs.getString("temp31", "") + "°" + "/" + prefs.getString("temp32", "") + "°");

        int maxTemp=Integer.valueOf(prefs.getString("temp11", ""));
        int minTemp=Integer.valueOf(prefs.getString("temp12", ""));
        int current=Integer.valueOf(prefs.getString("tempNow", ""));

        mNowWeather.setTemp(maxTemp,minTemp,current);
        closeProgressDialog();

        if ((Integer.valueOf(prefs.getString("tempNow", ""))) <= 10) {
            loveTips.setText("天气寒冷，小心保暖哦！美美的大衣穿起来，但是不要只求风度不求温度咯！");
        } else if ((Integer.valueOf(prefs.getString("tempNow", ""))) > 10 && (Integer.valueOf(prefs.getString("tempNow", ""))) < 23) {
            loveTips.setText("天气较暖和，但是也不要就此粗心大意咯，这个一不小心很可能就是在这不太冷的天气着凉了！");
        } else if ((Integer.valueOf(prefs.getString("tempNow", ""))) >= 23) {
            loveTips.setText("天气较热，要多运动咯，运动后出汗能够排毒！");
        }


        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.update:

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String name = prefs.getString("name", "");
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(name)) {
                    showProgressDialog();
                    queryWeatherInfo(name);

                }
                break;
            case R.id.location:
                getLocation();
                break;
            case R.id.trend:
                Intent inten1=new Intent(SecondWeatherActivity.this,FutureTrendActivity.class);
                startActivity(inten1);
                break;
            case R.id.tomorrow:
                break;
            case R.id.day_after_tomorrow:
                break;
            default:
                break;

        }
    }

    private void getLocation() {
        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationmanager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, "正在定位，请稍候...", Toast.LENGTH_LONG).show();
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No Location Provider to use", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationmanager.getLastKnownLocation(provider);
        if (location != null) {
            getLocationCounty(location);
        }
        locationmanager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    private void getLocationCounty(Location location) {
        Log.e("jingwei", location.getLatitude()+","+location.getLongitude());
//        String address="http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() +
//                "," + location.getLongitude() + "&sensor=true_or_false";
        String address="http://maps.googleapis.com/maps/api/geocode/json?latlng=23.162415%20,113.33884&sensor=true_or_false";
        String name="1";
        queryFromServer(address,"cityName",name);

    }

    private void takeTheName() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countyName = prefs.getString("area", "");
        String name = getPinYin(countyName);
        Log.e("areaname",name);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("name", name);
        editor.commit();
        if (!TextUtils.isEmpty(name)) {
            queryWeatherInfo(name);
            Log.e("areaname1",name);
        }
    }

    public static String getPinYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        // System.out.println(t1.length);
        String[] t2 = new String[t1.length];
        // System.out.println(t2.length);
        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i =0; i < t0; i++) {
                // 判断能否为汉字字符
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return t4;
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
    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                getLocationCounty(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
