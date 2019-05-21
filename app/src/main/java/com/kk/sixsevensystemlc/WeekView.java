package com.kk.sixsevensystemlc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.List;


public class WeekView extends View {

    /**View宽度*/
    private int mViewWidth;
    /** View高度*/
    private int mViewHeight;
    /**边框线画笔*/
    private Paint mBorderLinePaint;
    /**文本画笔*/
    private Paint mTextPaint;
    /**要绘制的折线线画笔*/
    private Paint mBrokenLinePaint;
    /**圆画笔*/
    private Paint mCirclePaint;
    /**边框的左边距*/
    private float mBrokenLineLeft=110;
    /**边框的上边距*/
    private float mBrokenLineTop=100;
    /**边框的下边距*/
    private float mBrokenLineBottom=160;
    /**边框的右边距*/
    private float mBrokenLinerRight=50;
    /**需要绘制的宽度*/
    private float mNeedDrawWidth;
    /**需要绘制的高度*/
    private float mNeedDrawHeight;
    /**数据值*/
    private int[] value = new int[24];
    private String[] time = new String[24];
    /**图表的最大值*/
    private int maxValue;
    /**折线文本画笔*/
    private Paint mBrokenLineTextPaint;

    public WeekView(Context context) {
        super(context);
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度设置不知道怎么弄
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        setMeasuredDimension(4130,heightMeasureSpec);
        initNeedDrawWidthAndHeight();
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DataProcess();
        /**根据数据绘制线*/
        DrawBrokenLine(canvas);
        DrawLineCircle(canvas);
    }

    private void DataProcess(){
       /** SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String weatherStringDay = prefs.getString("StringDay", null);
        ForecastDay day = Utility.handleWeatherDayResponse(weatherStringDay);
        List<ForecastDay.Day.Hourly.Temperature> temperature = day.day.hourly.dayTemperatureList;
        for (int i = 0 ; i < 24; i++){
            if(temperature.get(i).value.contains(".")){
                value[i] =Integer.parseInt(temperature.get(i).value.substring(0, temperature.get(i).value.indexOf(".")));
            }else {
                value[i] =Integer.parseInt(temperature.get(i).value);
            }
            time[i] = temperature.get(i).datetime.substring(11,13);
            Log.d("date",""+time[i]);
            Log.d("valueList"+i,""+value[i]);
        }
        maxValue = Arrays.stream(value).max().getAsInt() - Arrays.stream(value).min().getAsInt();*/
    }

    /**绘制线上的圆*/
    private void DrawLineCircle(Canvas canvas) {
        Point[] points= getPoints(value,mNeedDrawHeight,mNeedDrawWidth,maxValue,mBrokenLineLeft,mBrokenLineTop);
        for (int i = 0; i <points.length ; i++) {
            Point point=points[i];
            mCirclePaint.setColor(Color.WHITE);
            mCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(point.x,point.y,8,mCirclePaint);

            mCirclePaint.setColor(Color.rgb(248,215,78));
            mCirclePaint.setStyle(Paint.Style.STROKE);
            mCirclePaint.setStrokeWidth(8);
            canvas.drawCircle(point.x,point.y,8,mCirclePaint);
            /**
             * drawCircle(float cx, float cy, float radius, Paint paint)
             * cx 中间x坐标
             * xy 中间y坐标
             * radius 圆的半径
             * paint 绘制圆的画笔
             * */
        }
    }

    /**根据值绘制折线*/
    private void DrawBrokenLine(Canvas canvas) {
        Path mPath=new Path();
        mBrokenLinePaint.setColor(Color.rgb(248,215,78));
        mBrokenLinePaint.setStrokeWidth(8);
        Point[] points= getPoints(value,mNeedDrawHeight,mNeedDrawWidth,maxValue,mBrokenLineLeft,mBrokenLineTop);
        for (int i = 0; i < points.length; i++) {
            Point point=points[i];
            if(i==0){
                mPath.moveTo(point.x,point.y);
            }else {
                mPath.lineTo(point.x,point.y);
            }
            canvas.drawText(value[i]+"°",point.x,point.y-20,mBrokenLineTextPaint);
            canvas.drawText(time[i]+":00",point.x,400,mBrokenLineTextPaint);
        }
        canvas.drawPath(mPath,mBrokenLinePaint);
    }

    /**初始化画笔*/
    private void initPaint() {

        /**初始化文本画笔*/
        if(mTextPaint==null){
            mTextPaint=new Paint();
        }
        initPaint(mTextPaint);


        /**初始化折线画笔*/
        if(mBrokenLinePaint==null){
            mBrokenLinePaint=new Paint();
        }
        initPaint(mBrokenLinePaint);

        if(mCirclePaint==null){
            mCirclePaint=new Paint();
        }
        initPaint(mCirclePaint);

        /**折线文本画笔*/
        if (mBrokenLineTextPaint == null){
            mBrokenLineTextPaint=new Paint();
            initPaint(mBrokenLineTextPaint);
        }
        mBrokenLineTextPaint.setTextAlign(Paint.Align.CENTER);
        mBrokenLineTextPaint.setColor(Color.BLACK);
        mBrokenLineTextPaint.setTextSize(40);

    }

    /**初始化画笔默认属性*/
    private void initPaint(Paint paint){
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
    }


    /**初始化绘制折线图的宽高*/
    private void initNeedDrawWidthAndHeight(){
        mNeedDrawWidth = mViewWidth-mBrokenLineLeft-mBrokenLinerRight;
        mNeedDrawHeight = mViewHeight-mBrokenLineTop-mBrokenLineBottom;
    }
    /**根据值计算在该值的 x，y坐标*/
    public Point[] getPoints(int[] values, float height, float width, int max , float left,float top) {
        float leftPadding = 170;//绘制边距
        //计算每点高度所以对应的值
        double mean = (double) height/max;
        Point[] points = new Point[values.length];
        int min = Arrays.stream(values).min().getAsInt();
        for (int i = 0; i < values.length; i++) {
            float value = values[i];
            Log.d("mean:",""+mean);
            //获取要绘制的高度
            float drawHeight = (float) (mean * (value - min));
            int pointY = (int) (height+top  - drawHeight );
            Log.d("pointY",""+pointY+" height:"+height+" top"+top+" drawheight"+drawHeight);
            int pointX = (int) (leftPadding * i + left);
            Point point = new Point(pointX, pointY);
            points[i] = point;
        }
        return points;
    }
}