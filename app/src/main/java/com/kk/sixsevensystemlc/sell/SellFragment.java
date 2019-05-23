package com.kk.sixsevensystemlc.sell;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.kk.sixsevensystemlc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SellFragment extends Fragment {

    TextView today;
    TextView thisMonth;
    TextView priceMonth;
    TextView profit;
    List<Integer> numTodayList = new ArrayList<>();
    List<Integer> numMonthList = new ArrayList<>();
    //List<String> nameList = new ArrayList<>();
    List<Float> sellTodayList = new ArrayList<>();
    List<Float> sellMonthList = new ArrayList<>();
    List<Float> priceMonthList = new ArrayList<>();
    float todayTotal = 0, monthTotal = 0, priceTotal = 0;
    int ti = 0, mi = 0, pi=0, tj = 0, mj = 0,pj=0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SellFragmentPageAdapter sellFragmentPageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_fragment, container, false);

        viewPager = view.findViewById(R.id.sell_view_pager);
        sellFragmentPageAdapter = new SellFragmentPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(sellFragmentPageAdapter);
        NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.nested_scrollview);
        scrollView.setFillViewport(true);//修正viewpager在nsv中显示不正常的问题
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        Calendar time = Calendar.getInstance();

        TextView t1= (TextView)view.findViewById(R.id.tt);
        t1.setText("今日收入(元)");

        today = view.findViewById(R.id.sell_today);
        calculatePeriod(time, time, 0);//方法0：获取今日总收入

        thisMonth = view.findViewById(R.id.month_sell);
        calculatePeriod(time, time, 2);

        priceMonth = view.findViewById(R.id.month_out);
        calculatePeriod(time,time,1);

        profit = view.findViewById(R.id.month_profit);

        return view;
    }

    public void calculatePeriod(Calendar start, Calendar end, final int method) {

        Date startDate = getDatString(start);
        Date endDate;
        numTodayList.clear();
        numMonthList.clear();
        sellTodayList.clear();
        sellMonthList.clear();

        end.add(start.DATE, 1);
        endDate = getDatString(end);
        switch (method) {
            case 0:
                //1.查询今日的销售记录
                break;
            case 1:
                //2.查询本月支出
            case 2:
                //3.查询本月销售
                start.set(Calendar.DAY_OF_MONTH, 1);
                startDate = getDatString(start);
                break;
        }

        AVQuery<AVObject> startDateQuery = new AVQuery<>("Record");
        startDateQuery.whereGreaterThanOrEqualTo("sellDate", startDate);
        AVQuery<AVObject> endDateQuery = new AVQuery<>("Record");
        endDateQuery.whereLessThanOrEqualTo("sellDate", endDate);

        AVQuery<AVObject> query = AVQuery.and(Arrays.asList(startDateQuery, endDateQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                //Log.e("res", "succeed");
                for (AVObject record : avObjects) {
                    //2.get每个的销售数量
                    //Log.e("res1", "succeed");
                    switch (method) {
                        case 0:
                            //Log.e("res10", "succeed");
                            numTodayList.add(Integer.parseInt(record.get("recordNum") + ""));
                            //Log.d("numTodayList", numTodayList.get(tj) + ":" + tj);
                            tj++;
                            break;
                        case 1:
                            //Log.e("res11", "succeed");
                        case 2:
                            //Log.e("res12", "succeed");
                            numMonthList.add(Integer.parseInt(record.get("recordNum") + ""));
                            //Log.d("numMonthList", numMonthList.get(mj) + ":" + mj);
                            mj++;
                            break;
                    }

                    //3.通过外键get每个售价
                    //通过record表关联数据获取商品表的商品名称name
                    AVObject re = AVObject.createWithoutData("Record", record.getObjectId());
                    re.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            AVObject merchandise = avObject.getAVObject("merchandiseId");
                            Float sell = Float.parseFloat(merchandise.get("sell") + "");
                            Float price = Float.parseFloat(merchandise.get("price")+"");
                            switch (method) {
                                case 0:
                                    sellTodayList.add(sell);
                                    //Log.d("sellTodayList", sellTodayList.get(ti) + ":" + ti);
                                    //4.计算今日收入
                                    todayTotal = todayTotal + sellTodayList.get(ti) * numTodayList.get(ti);
                                    ti++;
                                    today.setText(String.format("%.2f", todayTotal));
                                    //Log.d("today", todayTotal + "");
                                    break;
                                case 1:
                                case 2:
                                    priceMonthList.add(price);
                                    sellMonthList.add(sell);
                                    //4.计算收入
                                    //Log.d("sellMonthList", sellMonthList.get(mi) + ":" + mi);
                                    //Log.d("priceMonthList", priceMonthList.get(pi) + ":" + pi);
                                    priceTotal = priceTotal + priceMonthList.get(pi) * numMonthList.get(mi);
                                    monthTotal = monthTotal + sellMonthList.get(mi) * numMonthList.get(mi);
                                    mi++;
                                    priceMonth.setText(String.format("%.2f",priceTotal));
                                    thisMonth.setText(String.format("%.2f",monthTotal));
                                    float profitTotal = monthTotal - priceTotal;
                                    profit.setText(String.format("%.2f",profitTotal));
                                    break;
                            }
                        }
                    });
                }
            }
        });
    }

    Date getDatString(Calendar time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        time.set(Calendar.HOUR, 0);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        //Log.d("timejkc",time.getTime()+"");
        return time.getTime();
    }


}
