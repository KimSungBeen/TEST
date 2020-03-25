package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Loading_Activity extends AppCompatActivity {

    public static final int LOADING_SCREEN_TIME = 200;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_);

        //로딩화면 쓰레드
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),Home_Activity.class);
                startActivity(intent);
                finish();
            }
        }, LOADING_SCREEN_TIME);

    }
}
