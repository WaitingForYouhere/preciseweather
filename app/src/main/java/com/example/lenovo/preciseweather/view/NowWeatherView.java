package com.example.lenovo.preciseweather.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.lenovo.preciseweather.R;

/**
 * Created by lenovo on 2017/3/26.
 */

public class NowWeatherView extends View {
    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mPointPaint;
    private Paint mBitmapPaint;


    private int mwidth;
    private int mheight;
    private int radius;

    private int startAngle;
    private int sweepAngle;
    private int count;

    private int currentTemp;
    private int maxTemp;
    private int minTemp;
    private Bitmap bitmap;
    private int ocAngle;
    private int fgAngle;
    private int offset;
    public NowWeatherView(Context context) {
        this(context,null);

    }

    public NowWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public NowWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        initPaint();
        startAngle = 120;
        sweepAngle=300;
        count=60;

        currentTemp=26;
        maxTemp=27;
        minTemp=20;
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.p9);
        ocAngle=230;
        fgAngle=90;
        offset=32;

    }

    private void initPaint() {
        mArcPaint=new Paint();
        mArcPaint.setColor(Color.WHITE);
        mArcPaint.setStrokeWidth(2);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setAntiAlias(true);

        mLinePaint=new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        mTextPaint=new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStrokeWidth(4);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(144);

        mPointPaint=new Paint();
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStrokeWidth(2);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setAntiAlias(true);

        mBitmapPaint=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wrap_len=600;
        int width=measureDimension(wrap_len,widthMeasureSpec);
        int height=measureDimension(wrap_len,heightMeasureSpec);
        int lenth=Math.min(width,height);
        setMeasuredDimension(lenth,lenth);
    }

    private int measureDimension(int defaultSize, int widthMeasureSpec) {
        int result;
        int specmode=MeasureSpec.getMode(widthMeasureSpec);
        int specsize=MeasureSpec.getSize(widthMeasureSpec);
        if(specmode==MeasureSpec.EXACTLY){
            result=specsize;
        }else {
            result=defaultSize;
            if(specmode==MeasureSpec.AT_MOST){
                result=Math.min(result,specsize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mwidth=getWidth();
        mheight=getHeight();
        radius=(mwidth-getPaddingRight()-getPaddingRight())/2;
        canvas.translate(mwidth/2,mheight/2);
//        drawArcView(canvas);
        drawLine(canvas);
        drawTextBitmap(canvas);
        drawTempLineView(canvas);
    }

    private void drawTempLineView(Canvas canvas) {
        mTextPaint.setTextSize(24);
//        canvas.drawText("0°C",getRealCosX(ocAngle,offset,true),getRealSinY(ocAngle,offset,true),mTextPaint);//固定0度的位置

        int startTempAngle=getStartAngle(minTemp,maxTemp);

        float minx,miny,maxx,maxy;
        minx=getRealCosX(startTempAngle, 22,true);
        miny=getRealSinY(startTempAngle, 22,true);
        maxx=getRealCosX(startTempAngle+fgAngle, 22,true);
        maxy= getRealSinY(startTempAngle+fgAngle, 22,true);
        canvas.drawText(minTemp + "°",minx ,miny, mTextPaint);
        canvas.drawText(maxTemp + "°",maxx,maxy, mTextPaint);

//        int circleAngle = startTempAngle+(currentTemp-minTemp)*fgAngle/(maxTemp-minTemp);
//        mPointPaint.setColor(getRealColor(minTemp,maxTemp));
//        canvas.drawCircle(getRealCosX(circleAngle,40,false),getRealSinY(circleAngle,40,false),7,mPointPaint);
    }
    //根据象限加一个偏移量
    private float getRealCosX(int Angle,int off,boolean outoff) {
        Angle=Angle%360;
        if (!outoff) {
            off = -off;
            if(getCosX(Angle)<0){
                return getCosX(Angle)-off;
            }else{
                return getCosX(Angle)+off;
            }
        }else {
            if (getCosX(Angle) < 0) {
                if (Angle < 230) {
                    return getCosX(Angle) -2*off;
                } else {
                    return getCosX(Angle) -2*off;
                }
            } else {
                if (Angle >270) {
                    return getCosX(Angle)-off/2;
                } else {
                    return getCosX(Angle);
                }
            }
        }
    }
    private float getRealSinY(int Angle,int off,boolean outoff) {
        if (!outoff) {
            off = -off;

            if (getSinY(Angle) < 0) {
                return getSinY(Angle)-off;
            } else {
                return getSinY(Angle)+off;
            }
        } else {
            if (getSinY(Angle) < 0) {
                if (Angle < 230) {
                    return getSinY(Angle) - off/3;
                } else {
                    return getSinY(Angle) - off;
                }
            } else {
                if (Angle < 320) {
                    return getSinY(Angle) + off;
                } else {
                    return getSinY(Angle) + off/6;
                }
            }
        }
    }

    private float getCosX(int Angle){

        return (float) (radius*Math.cos(Angle*Math.PI/180))+getTextPaintOffset(mTextPaint);
    }
    private float getSinY(int Angle){

        return (float)(radius*Math.sin(Angle*Math.PI/180))+getTextPaintOffset(mTextPaint);
    }

    private int getStartAngle(int minTemp,int maxTemp){
        int startFgAngle=0;
        if(minTemp>=maxTemp){
            return startFgAngle;
        }
        if(minTemp<=0){
            startFgAngle=ocAngle - (0 - minTemp)*fgAngle/(maxTemp-minTemp);
        }else{
            startFgAngle=ocAngle+(minTemp-0)*fgAngle/(maxTemp-minTemp);
        }
        //边界 start
        if(startFgAngle<=startAngle){//如果开始角小于startAngle，防止过边界
            startFgAngle=startAngle+10;
        }else if((startFgAngle+fgAngle)>=(startAngle+sweepAngle)){//如果结束角大于(startAngle+sweepAngle)
            startFgAngle =startAngle+sweepAngle-20-fgAngle;
        }
        //边界 end
        return startFgAngle;
    }


    private int getStartLineIndex(int minTemp,int maxTemp){
        return  (getStartAngle(minTemp,maxTemp)-startAngle)/(sweepAngle/count);
    }
    private int getEndLineIndex(int minTemp,int maxTemp){
        return  (getStartAngle(minTemp,maxTemp)-startAngle)/(sweepAngle/count)+fgAngle/(sweepAngle/count);
    }

    private void drawTextBitmap(Canvas canvas) {
        mTextPaint.setTextSize(144);
        canvas.drawText(currentTemp+"°",0,0+getTextPaintOffset(mTextPaint)-40,mTextPaint);
        canvas.drawBitmap(bitmap,0-bitmap.getWidth()/2,radius-bitmap.getHeight()/2-30,null);
    }

    private int getTextPaintOffset(Paint mTextPaint) {
        Paint.FontMetricsInt fontMetrics=mTextPaint.getFontMetricsInt();
        return fontMetrics.descent+(fontMetrics.bottom-fontMetrics.top)/2;
    }
    private void drawLine(Canvas canvas) {
        canvas.save();
        float angle=(float)sweepAngle/count;
        canvas.rotate(-270+startAngle);
        for (int i = 0; i <=count ; i++) {
            if(i==0||i==count){
                mLinePaint.setStrokeWidth(1);
                mLinePaint.setColor(Color.WHITE);
                canvas.drawLine(0,-radius,0,-radius+40,mLinePaint);
            }else if (i>=getStartLineIndex(minTemp,maxTemp)&&i<=getEndLineIndex(minTemp,maxTemp)){
                mLinePaint.setStrokeWidth(3);
                mLinePaint.setColor(getRealColor(minTemp,maxTemp));
                canvas.drawLine(0,-radius,0,-radius+30,mLinePaint);
            }else {
                mLinePaint.setStrokeWidth(2);
                mLinePaint.setColor(Color.WHITE);
                canvas.drawLine(0,-radius,0,-radius+30,mLinePaint);
            }
            canvas.rotate(angle);
        }
        canvas.restore();
    }

    private void drawArcView(Canvas canvas) {
        RectF mRect=new RectF(-radius,-radius,radius,radius);
        canvas.drawArc(mRect,startAngle,sweepAngle,false,mArcPaint);

    }
    public int getRealColor(int minTemp,int maxTemp){
        if(maxTemp<=0){
            return Color.parseColor("#00008B");//深海蓝
        }else if(minTemp<=0 && maxTemp>0){
            return Color.parseColor("#4169E1");//黄君兰
        }else if(minTemp>0 && minTemp<15 ){
            return Color.parseColor("#40E0D0");//宝石绿
        }else if(minTemp>=15 && minTemp<25){
            return Color.parseColor("#00FF00");//酸橙绿
        }else if(minTemp>=25 &&minTemp<30){
            return Color.parseColor("#FFD700");//金色
        }else  if(minTemp>=30){
            return Color.parseColor("#CD5C5C");//印度红
        }

        return Color.parseColor("#00FF00");//酸橙绿;
    }
    public void setBitmap(Bitmap mBitmap){
        this.bitmap=mBitmap;
        invalidate();
    }

    public void setTemp(int maxTemp,int minTemp,int currentTemp) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.currentTemp = currentTemp;
        invalidate();
    }
    public void setHeight(int height){
        this.mheight=height;
        invalidate();
    }
    public void setWidth(int width){
        this.mwidth=width;
        invalidate();
    }
}
