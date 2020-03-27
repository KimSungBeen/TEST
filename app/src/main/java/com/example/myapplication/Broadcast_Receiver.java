package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Broadcast_Receiver extends BroadcastReceiver {
    public static final String ACTION_NEXT_MUSIC =
            "com.example.myapplication.action.ACTION_NEXT_MUSIC_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "전원 연결", Toast.LENGTH_SHORT).show();
        }

        if(Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "전원 연결 해제", Toast.LENGTH_SHORT).show();
        }

        if(ACTION_NEXT_MUSIC.equals(intent.getAction())) {
            Toast.makeText(context, "다음 곡 재생", Toast.LENGTH_SHORT).show();
        }

    }
}
