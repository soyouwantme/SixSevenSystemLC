package com.kk.sixsevensystemlc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.List;


public class SellRecordFragment extends Fragment {

    private View view;

    private MerchandiseAdapter merchandiseAdapter;
    private List<AVObject> merchandiseList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sell_record_fragment, container, false);

        TextView busDetailContent=(TextView)view.findViewById(R.id.tv);
        busDetailContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        return view;
    }

}
