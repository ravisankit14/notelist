package com.note.views;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.note.Constants;
import com.note.MainActivity;
import com.note.R;

import java.io.File;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    TextView note_title;
    TextView note_desc;
    TextView note_createdAt;
    ImageView note_image;

    String sTitle;
    String sDesc;
    String sCreatedAt;
    String sImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeView();

        sTitle = getIntent().getStringExtra(Constants.TITLE);
        sDesc = getIntent().getStringExtra(Constants.DESC);
        sCreatedAt = getIntent().getStringExtra(Constants.CREATED_AT);
        sImagePath = getIntent().getStringExtra(Constants.IMAGE_PATH);

        note_title.setText(sTitle);
        note_desc.setText(sDesc);
        note_createdAt.setText(sCreatedAt);

        if(sImagePath != null){
            Glide.with(this).load(new File(sImagePath))
                    .override(80, 80)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(note_image);
        }else{
            Glide.with(this).load(R.mipmap.note)
                    .override(80, 80)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(note_image);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.note_imageView2:
                if(sImagePath != null){
                    Intent intent = new Intent(this,ImageViewActivity.class);
                    intent.putExtra(Constants.IMAGE_PATH,sImagePath);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this,"No image to show",Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void initializeView(){
        note_title = (TextView) findViewById(R.id.note_title2);
        note_desc = (TextView) findViewById(R.id.note_description2);
        note_createdAt = (TextView) findViewById(R.id.note_createdTime2);
        note_image = (ImageView) findViewById(R.id.note_imageView2);

        note_image.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
