package com.kk.sixsevensystemlc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kk.sixsevensystemlc.R;

public class StockActivity extends AppCompatActivity {

    public static final String STOCK_ID = "stock_id";

    public static final String STOCK_NAME = "stock_name";

    public static final String STOCK_LEFT = "stock_left";

    public static final String STOCK_IMAGE_URL = "stock_image_url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int stockId = intent.getIntExtra(STOCK_ID,0);
        final String merchandiseName = intent.getStringExtra(STOCK_NAME);
        String merchandiseImageURL = intent.getStringExtra(STOCK_IMAGE_URL);
        int left = intent.getIntExtra(STOCK_LEFT,0);


    }

}
