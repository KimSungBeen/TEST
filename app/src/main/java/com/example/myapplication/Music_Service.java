package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class Music_Service extends Service {

    public static final int musicAmount = 3; //노래의 갯수
    public static int musicNum = 0; //노래의 번호
    public static boolean isPlaying;

    static MediaPlayer[] mediaPlayer = new MediaPlayer[musicAmount]; //음악파일을 넣을 배열

    public Music_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer[0] = MediaPlayer.create(this, R.raw.sametime); //음악 생성
        mediaPlayer[0].setLooping(false); //반복재생여부 off

        mediaPlayer[1] = MediaPlayer.create(this, R.raw.trespasss);
        mediaPlayer[1].setLooping(false);

        mediaPlayer[2] = MediaPlayer.create(this, R.raw.nothinyet);
        mediaPlayer[2].setLooping(false);

    }

    //Service 시작
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isPlaying = true;
        mediaPlayer[musicNum].start();

        return super.onStartCommand(intent, flags, startId);
    }

    //Service 종료
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer[musicNum] != null) {
            mediaPlayer[musicNum].release(); // on Destroy시 메모리에서 음악의 데이터 삭제
        }

    }

}
