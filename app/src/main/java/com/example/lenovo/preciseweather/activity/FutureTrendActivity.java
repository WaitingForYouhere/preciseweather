package com.example.lenovo.preciseweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.lenovo.preciseweather.R;
import com.example.lenovo.preciseweather.view.WeatherTrendView;

public class FutureTrendActivity extends AppCompatActivity {

    private WeatherTrendView trendView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_future_trend);
        trendView= (WeatherTrendView) findViewById(R.id.weather_trend_view);
    }
}
