package com.note.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.note.Constants;
import com.note.R;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    ImageView viewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        viewImage = findViewById(R.id.viewImage);

        String view = getIntent().getStringExtra(Constants.IMAGE_PATH);

        if(view != null){
            Glide.with(this).load(new File(view))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewImage);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
