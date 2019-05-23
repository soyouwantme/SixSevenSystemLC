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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.kk.sixsevensystemlc.sell.SellFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    private int[] value = new int[7];
    private int j=0;
    private int k=0;
    private List<List> numWeekList = new ArrayList<>();
    private List<List> priceWeekList = new ArrayList<>();
    private List<Integer> sumWeekList = new ArrayList<>();

    private List<Integer> numWeekList1 = new ArrayList<>();
    private List<Integer> numWeekList2 = new ArrayList<>();
    private List<Integer> numWeekList3 = new ArrayList<>();
    private List<Integer> numWeekList4 = new ArrayList<>();
    private List<Integer> numWeekList5 = new ArrayList<>();
    private List<Integer> numWeekList6 = new ArrayList<>();
    private List<Integer> numWeekList7 = new ArrayList<>();
    private List<Float> priceWeekList1 = new ArrayList<>();
    private List<Float> priceWeekList2 = new ArrayList<>();
    private List<Float> priceWeekList3 = new ArrayList<>();
    private List<Float> priceWeekList4 = new ArrayList<>();
    private List<Float> priceWeekList5 = new ArrayList<>();
    private List<Float> priceWeekList6 = new ArrayList<>();
    private List<Float> priceWeekList7 = new ArrayList<>();
    float priceTotal = 0;

    private String[] time = new String[7];
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

    private void DataProcess(){/*
        numWeekList1.clear();
        numWeekList2.clear();
        numWeekList3.clear();
        numWeekList4.clear();
        numWeekList5.clear();
        numWeekList6.clear();
        numWeekList7.clear();
        priceWeekList1.clear();
        priceWeekList2.clear();
        priceWeekList3.clear();
        priceWeekList4.clear();
        priceWeekList5.clear();
        priceWeekList6.clear();
        priceWeekList7.clear();
        numWeekList.add(numWeekList1);
        numWeekList.add(numWeekList2);
        numWeekList.add(numWeekList3);
        numWeekList.add(numWeekList4);
        numWeekList.add(numWeekList5);
        numWeekList.add(numWeekList6);
        numWeekList.add(numWeekList7);
        priceWeekList.add(priceWeekList1);
        priceWeekList.add(priceWeekList2);
        priceWeekList.add(priceWeekList3);
        priceWeekList.add(priceWeekList4);
        priceWeekList.add(priceWeekList5);
        priceWeekList.add(priceWeekList6);
        priceWeekList.add(priceWeekList7);

        Calendar time1 = Calendar.getInstance();
        Calendar time2 = Calendar.getInstance();
        Calendar start = time1,end = time2;
        j = 0;
        for (int i = 0 ; i < 7; i++){
            start.add(start.DATE, -1);
            Date startDate = getDatString(start);
            //Log.d("time",i+":"+startDate);
            if(i!=0) {
                end.add(end.DATE, -1);
            }
            Date endDate = getDatString(end);
            Log.d("time",i+":"+startDate+";"+endDate);

            AVQuery<AVObject> startDateQuery = new AVQuery<>("Record");
            startDateQuery.whereGreaterThanOrEqualTo("sellDate", startDate);
            AVQuery<AVObject> endDateQuery = new AVQuery<>("Record");
            endDateQuery.whereLessThanOrEqualTo("sellDate", endDate);

            //获取这日的全部订单
            AVQuery<AVObject> query = AVQuery.and(Arrays.asList(startDateQuery, endDateQuery));
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(final List<AVObject> avObjects, AVException avException) {
                    for (AVObject record : avObjects) {
                        //Log.d("time","已进入2"+j);
                        Log.d("time","已进入1 "+record.get("recordNum").toString());
                        numWeekList.get(j).add(Integer.parseInt(record.get("recordNum") + ""));
                    }
                    Log.e("time","j+++++:"+numWeekList.get(j)+" "+j);
                    j++;
                    sumWeekList.clear();
                    for (k=0;k<7;k++){
                        sumWeekList.add(sumList(numWeekList.get(k)));
                        value[k]=sumWeekList.get(k);
                        time[k] = k+1+"";
                        Log.e("sum","value"+":"+k+":"+ value[k]);
                        Log.e("sumtime","value"+time[k]);
                        maxValue = Arrays.stream(value).max().getAsInt() - Arrays.stream(value).min().getAsInt();
                        Log.e("summax","value"+maxValue);
                    }
                }
            });
        }*/

    }

    int sumList(List<Integer> list){
        int sum = 0;
        for (Integer a:list){
            sum = sum +a;
        }
        return sum;
    }

    Date getDatString(Calendar time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        time.set(Calendar.HOUR, 5);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        //Log.d("timejkc",time.getTime()+"");
        return time.getTime();
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