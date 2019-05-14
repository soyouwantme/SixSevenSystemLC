package com.kk.sixsevensystemlc;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String MERCHANDISE_NAME = "merchandise_name";

    public static final String MERCHANDISE_IMAGE_URL = "merchandise_image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Intent intent = getIntent();
        String merchandiseName = intent.getStringExtra(MERCHANDISE_NAME);
        String merchandiseImageURL = intent.getStringExtra(MERCHANDISE_IMAGE_URL);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView merchandiseImageView = (ImageView)findViewById(R.id.detail_image);
        TextView merchandiseContentText = (TextView)findViewById(R.id.detail_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(merchandiseName);
        //Glide.with(this).load(merchandiseImageId).into(merchandiseImageView);
        Picasso.with(getBaseContext()).load(merchandiseImageURL).into(merchandiseImageView);
        String merchandiseContent = generateMerchandiseContent(merchandiseName);
        merchandiseContentText.setText(merchandiseContent);
    }

    private String generateMerchandiseContent(String merchandiseName){
        StringBuilder merchandiseContent = new StringBuilder();
        for (int i = 0; i < 500; i++){
            merchandiseContent.append(merchandiseName);
        }
        return merchandiseContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
