package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import javax.security.auth.login.LoginException;

import static com.example.myapplication.Home_Activity.TV_musicInfo;
import static com.example.myapplication.Stop_watch_Activity.chronometer;

public class Broadcast_Receiver extends BroadcastReceiver {
    public static final String ACTION_PLAY_MUSIC =
            "com.example.myapplication.action.ACTION_PLAY_MUSIC_BROADCAST";
    public static final String ACTION_STOPWATCH =
            "com.example.myapplication.action.ACTION_STOPWATCH_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "전원 연결", Toast.LENGTH_SHORT).show();
        }

        if(Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "전원 연결 해제", Toast.LENGTH_SHORT).show();
        }

        if(ACTION_PLAY_MUSIC.equals(intent.getAction())) {
            Toast.makeText(context, TV_musicInfo.getText().toString() + " 재생", Toast.LENGTH_SHORT).show();
        }

        if(ACTION_STOPWATCH.equals(intent.getAction())) {
            Log.i("ooo", "onReceive: ");
            int timeAlarm = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase() ) /1000) / 60;
            Toast.makeText(context,  "스탑워치 " + timeAlarm + "분 경과", Toast.LENGTH_SHORT).show();
        }
    }
}
