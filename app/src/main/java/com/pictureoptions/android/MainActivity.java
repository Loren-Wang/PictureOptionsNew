package com.pictureoptions.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.basepictureoptionslib.android.AppCommon;
import com.pictureselect.android.PictureVideoSelectActivity;
import com.pictureselect.android.PictureVideoSelectConfirg;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PictureVideoSelectConfirg pictureSelectConfirg = new  PictureVideoSelectConfirg();
        Intent intent = new Intent(this, PictureVideoSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppCommon.OPTIONS_CONFIG_KEY,pictureSelectConfirg);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
