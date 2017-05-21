package com.example.lenovo.preciseweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.lenovo.preciseweather.R;

/**
 * Created by lenovo on 2017/3/27.
 */

public class WeatherTrendView extends View {

    public static final String weeks[]={"今天","星期二","星期三","星期四","星期五"};
    public static final String dayWeather[]={"晴天","小雨","小雨","中雨","小雨"};
    public static final int  dayRes[]={R.drawable.p0,R.drawable.p7,R.drawable.p7,R.drawable.p8,R.drawable.p7};
    public static final int dayTemp[]={23,22,23,20,17};
    public static final int nightTemp[]={18,17,18,12,11};
    public static final int  nightRes[]={R.drawable.p2,R.drawable.p7,R.drawable.p7,R.drawable.p8,R.drawable.p7};
    public static final String nightWeather[]={"小雨","小雨","小雨","中雨","小雨"};
    public static final String date[]={"02/20","02/21","02/22","02/23","02/24"};
    public static final String wind[]={"南风","东风","西风","东南风","西北风"};
    public static final String windVilue[]={"微风","3-4级","5-6级","2-3级","微风"};

    private float startX;
    private float endX;
    private float startY;
    private float endY;
    private int mVerticalCount;
    private int mHorizontalCount;
    private float itemWidth;
    private float itemHeight;

    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mSpotPaint;
    private Paint mTempLinePaint;

    private Path mPathday;
    private Path mPathnight;
    private CornerPathEffect mPathEffect;

    public WeatherTrendView(Context context) {
        this(context,null);
    }

    public WeatherTrendView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WeatherTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        initPaint();
        mVerticalCount=5;
        mHorizontalCount=40;
    }

    private void initPaint() {
        mTextPaint=new Paint();
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(2);

        mLinePaint=new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);

        mSpotPaint=new Paint();
        mSpotPaint.setColor(Color.WHITE);
        mSpotPaint.setAntiAlias(true);
        mSpotPaint.setStyle(Paint.Style.FILL);



        mTempLinePaint=new Paint();
        mTempLinePaint.setColor(Color.WHITE);
        mTempLinePaint.setStyle(Paint.Style.STROKE);
        mTempLinePaint.setAntiAlias(true);
        mTempLinePaint.setStrokeWidth(2);
        mPathday=new Path();
        mPathnight=new Path();
        mPathEffect=new CornerPathEffect(20);
        mTempLinePaint.setPathEffect(mPathEffect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        itemWidth=(getWidth()-getPaddingLeft()-getPaddingRight())/mVerticalCount;
        itemHeight=(getHeight()-getPaddingTop()-getPaddingBottom())/mHorizontalCount;
        drawVerticalLine(canvas);
//        drawHorizontalLine(canvas);

        drawTextBitmap(canvas);
        drawPolyline(canvas);
    }

    private void drawPolyline(Canvas canvas) {
        float minTempX,minTempY,maxTempX,maxTempY;
        int minTemp=nightTemp[0];
        int maxTemp=dayTemp[0];
        for (int i = 0; i <dayTemp.length ; i++) {
            maxTemp=Math.max(maxTemp,dayTemp[i]);
        }
        for (int i = 0; i <nightTemp.length ; i++) {
            minTemp=Math.min(minTemp,nightTemp[i]);
        }
        for (int i = 0; i <mVerticalCount ; i++) {
            maxTempX=getPaddingLeft()+itemWidth/2+i*itemWidth;
            maxTempY=getPaddingTop()+12*itemHeight+(maxTemp-dayTemp[i])*itemHeight;
            canvas.drawCircle(maxTempX,(float)maxTempY,6,mSpotPaint);
            canvas.drawText(dayTemp[i]+"℃",maxTempX,maxTempY-itemHeight/2,mTextPaint);
            minTempX=maxTempX;
            minTempY=getPaddingTop()+27*itemHeight-(nightTemp[i]-minTemp)*itemHeight;
            canvas.drawCircle(minTempX,minTempY,6,mSpotPaint);
            canvas.drawText(nightTemp[i]+"℃",minTempX,minTempY-itemHeight/2,mTextPaint);
            if(i == 0) {
                mPathday.moveTo(maxTempX,maxTempY);
                mPathnight.moveTo(minTempX,minTempY);
            }
            mPathday.lineTo(maxTempX,maxTempY);
            mPathnight.lineTo(minTempX,minTempY);
            //tempLinePaint.setPathEffect(mPathEffect);
            canvas.drawPath(mPathday,mTempLinePaint);
            canvas.drawPath(mPathnight,mTempLinePaint);
        }
        }


    private void drawTextBitmap(Canvas canvas) {
        float weekX,weekY,dayWeatherX,dayWeatherY,nightWeatherX,nightWeatherY,dateX,dateY,windLevelX,windLevelY;
        for (int i = 0; i <mVerticalCount ; i++) {
            weekX=(getPaddingLeft()+itemWidth)/2+i*itemWidth;
            weekY=getPaddingTop()+itemHeight+getTextPaintOffset(mTextPaint);
            canvas.drawText(weeks[i],weekX,weekY,mTextPaint);

            dayWeatherX=weekX;
            dayWeatherY=getPaddingTop()+4*itemHeight+getTextPaintOffset(mTextPaint);
            canvas.drawText(dayWeather[i],dayWeatherX,dayWeatherY,mTextPaint);

            Drawable drawableday= ContextCompat.getDrawable(getContext(),nightRes[i]);
            drawableday.setBounds((int)(getPaddingLeft()+i*itemWidth+10),(int)(getPaddingTop()+6*itemHeight),
                    (int)(getPaddingLeft()+(i+1)*itemWidth-10),(int)(getPaddingTop()+10*itemHeight));
            drawableday.draw(canvas);

            Drawable drawablenight= ContextCompat.getDrawable(getContext(),dayRes[i]);
            drawablenight.setBounds((int)(getPaddingLeft()+i*itemWidth+10),(int)(getPaddingTop()+28 *itemHeight),
                    (int)(getPaddingLeft()+(i+1)*itemWidth-10),(int)(getPaddingTop()+32*itemHeight));
            drawablenight.draw(canvas);

            nightWeatherX=weekX;
            nightWeatherY=getPaddingTop()+33*itemHeight+getTextPaintOffset(mTextPaint);
            canvas.drawText(nightWeather[i],nightWeatherX,nightWeatherY,mTextPaint);

            dateX=weekX;
            dateY=getPaddingTop()+36*itemHeight+getTextPaintOffset(mTextPaint);
            canvas.drawText(date[i],dateX,dateY,mTextPaint);

            windLevelX=weekX;
            windLevelY=getPaddingTop()+39*itemHeight+getTextPaintOffset(mTextPaint);
            canvas.drawText(wind[i],windLevelX,windLevelY,mTextPaint);

        }


    }

    private void drawHorizontalLine(Canvas canvas) {
        for (int i = 0; i <=mHorizontalCount ; i++) {
            startX=getPaddingLeft();
            startY=getPaddingTop()+i*itemHeight;
            endX=getWidth()-getPaddingRight();
            endY=startY;
            canvas.drawLine(startX,startY,endX,endY,mLinePaint);
        }
    }

    private void drawVerticalLine(Canvas canvas) {
        for (int i = 1; i <mVerticalCount ; i++) {
            startX=getPaddingLeft()+i*itemWidth;
            startY=getPaddingTop();
            endX=startX;
            endY=getHeight()-getPaddingBottom();
            canvas.drawLine(startX,startY,endX,endY,mLinePaint);
        }

    }

    public float getTextPaintOffset(Paint paint){
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return  -fontMetrics.descent+(fontMetrics.bottom-fontMetrics.top)/2;
    }
}
