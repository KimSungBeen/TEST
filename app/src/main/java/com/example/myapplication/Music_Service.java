package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import static com.example.myapplication.Home_Activity.TV_allMusicTime;
import static com.example.myapplication.Home_Activity.TV_currentMusicTime;
import static com.example.myapplication.Home_Activity.musicSeekBar;

public class Music_Service extends Service {

    public static final int SEEKBAR_INITIAL_TIME = 1000; //Seekbar의 이동 주기(1000ms: 1초)
    public static final int musicAmount = 3; //노래의 갯수
    public static int musicNum = 0; //노래의 번호
    public static volatile boolean isPlaying; //음악이 재생되고 있는지에 따른 boolean
    static Handler musicHandler = new Handler();

    static MediaPlayer[] mediaPlayer = new MediaPlayer[musicAmount]; //음악파일 배열

    public Music_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service","oncreate");

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
        Log.i("service","onStartCommand");

        //mediaPlayer[musicNum]내의 음악파일의 출력을 관리
        mediaPlayer[musicNum].setAudioStreamType(AudioManager.STREAM_MUSIC);

        //현재 재생중인 음악이 끝났을 때 감지 -> 다음 음악 재생
            mediaPlayer[musicNum].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isPlaying) {
                        mediaPlayer[musicNum].stop();
                        mediaPlayer[musicNum].reset();
                        stopService(intent);

                        //다음곡 재생
                        if (musicNum < musicAmount - 1) {
                            musicNum++;
                        }
                        startService(intent);
                    }
                }
            });

        //음악재생이 준비되면 호출되는 메소드
        mediaPlayer[musicNum].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPlaying = true;
                mp.start(); //재생
                moveSeekBarThread.run();//SeekBar 움직임 출력하는 쓰레드
            }
        });

            musicHandler.post(new Thread() {
                @Override
                public void run() {
                    while(true){

                    }

                }
            });

        return super.onStartCommand(intent, flags, startId);
    }

    //Service 종료
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service","onDestroy");

        if (mediaPlayer[musicNum] != null) {
            mediaPlayer[musicNum].release(); // on Destroy시 메모리에서 음악의 데이터 삭제
        }

    }

//    //SeekBar 움직임 출력하는 쓰레드
//    public static final Runnable moveSeekBarThread = new Runnable() {
//        public void run() {
//            try {
//                if (mediaPlayer[musicNum].isPlaying()) {
//                    int position = mediaPlayer[musicNum].getCurrentPosition(); //현재 재생위치
//                    int musicTime = mediaPlayer[musicNum].getDuration(); //한 음악파일의 재생시간
//                    musicSeekBar.setMax(musicTime);
//                    musicSeekBar.setProgress(position);
//
//                    //음악의 현재시간, 총 재생시간 계산
//                    int currentMinute = (position/1000)/60;
//                    int currentSeconds = (position/1000)%60;
//                    int allMinute = (musicTime/1000)/60;
//                    int allSeconds = (musicTime/1000)%60;
//
//                    //음악의 현재시간, 총 재생시간 출력
//                    if(currentSeconds < 10) {
//                        String currentMusicTime = currentMinute + ":0" + currentSeconds;
//                        TV_currentMusicTime.setText(currentMusicTime);
//                    }else {
//                        String currentMusicTime = currentMinute + ":" + currentSeconds;
//                        TV_currentMusicTime.setText(currentMusicTime);
//                    }
//                    if(allSeconds < 10) {
//                        String allMusicTime = allMinute + ":0" + allSeconds;
//                        TV_allMusicTime.setText(allMusicTime);
//                    }else {
//                        String allMusicTime = allMinute + ":" + allSeconds;
//                        TV_allMusicTime.setText(allMusicTime);
//                    }
//
//                    //(SEEKBAR_INITIAL_TIME: 1000) 1초 마다 쓰레드동작
//                    musicHandler.postDelayed(this, SEEKBAR_INITIAL_TIME);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };

    //SeekBar 움직임 출력하는 쓰레드
    public static final Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            try {
                if (mediaPlayer[musicNum].isPlaying()) {
                    int position = mediaPlayer[musicNum].getCurrentPosition(); //현재 재생위치
                    int musicTime = mediaPlayer[musicNum].getDuration(); //한 음악파일의 재생시간
                    musicSeekBar.setMax(musicTime);
                    musicSeekBar.setProgress(position);

                    //음악의 현재시간, 총 재생시간 계산
                    int currentMinute = (position/1000)/60;
                    int currentSeconds = (position/1000)%60;
                    int allMinute = (musicTime/1000)/60;
                    int allSeconds = (musicTime/1000)%60;

                    //음악의 현재시간, 총 재생시간 출력
                    if(currentSeconds < 10) {
                        String currentMusicTime = currentMinute + ":0" + currentSeconds;
                        TV_currentMusicTime.setText(currentMusicTime);
                    }else {
                        String currentMusicTime = currentMinute + ":" + currentSeconds;
                        TV_currentMusicTime.setText(currentMusicTime);
                    }
                    if(allSeconds < 10) {
                        String allMusicTime = allMinute + ":0" + allSeconds;
                        TV_allMusicTime.setText(allMusicTime);
                    }else {
                        String allMusicTime = allMinute + ":" + allSeconds;
                        TV_allMusicTime.setText(allMusicTime);
                    }

                    //(SEEKBAR_INITIAL_TIME: 1000) 1초 마다 쓰레드동작
                    musicHandler.postDelayed(this, SEEKBAR_INITIAL_TIME);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
