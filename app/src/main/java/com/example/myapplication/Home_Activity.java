package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.io.InputStream;

import static com.example.myapplication.Music_Service.isPause;
import static com.example.myapplication.Music_Service.isPlaying;
import static com.example.myapplication.Music_Service.mediaPlayer;
import static com.example.myapplication.Music_Service.musicAmount;
import static com.example.myapplication.Music_Service.musicNum;
import static com.example.myapplication.Notice_write_Activity.SP_data;

public class Home_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private  View drawerView;

    public static Broadcast_Receiver receiver = new Broadcast_Receiver();

    public static final int REQUEST_TO_NOTICE = 11;
    public static final int BOOKMARK_COUNT = 6;
    public static SeekBar musicSeekBar; //MP3 Player SeekBar
    public static TextView TV_currentMusicTime, TV_allMusicTime;
    int videoPosition; // 영상이 Pause 되면 그 위치를 저장하기 위한 변수

    //북마크번호에 따른 Url
    String bookmarkNum1Url, bookmarkNum2Url, bookmarkNum3Url,
            bookmarkNum4Url, bookmarkNum5Url, bookmarkNum6Url;

    //뷰 선언
    Button BTN_info, BTN_stopwatch, BTN_diary;
    TextView TV_notice, TV_workoutFriend, TV_BMI, TV_gymInfo,
            TV_bookmarkNum1, TV_bookmarkNum2, TV_bookmarkNum3, TV_bookmarkNum4, TV_bookmarkNum5, TV_bookmarkNum6;
    static TextView TV_musicInfo;
    ImageView  IV_thumbnail,
            IV_bookmarkNum1, IV_bookmarkNum2, IV_bookmarkNum3, IV_bookmarkNum4, IV_bookmarkNum5, IV_bookmarkNum6;
    LottieAnimationView LA_menu, LA_isPlayMusic, LA_play, LA_stop, LA_next, LA_back;

//    PlayerView playerView;
//    SimpleExoPlayer simpleExoPlayer;

    VideoView VV_movie;
    MediaController mediaController; // 미디어 제어 (재생이나 정지) 버튼을 담당

//    String videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";//테스트용 영상
//        Uri uri = Uri.parse(videoURL); //영상 주소를 저장
    String path = "android.resource://com.example.myapplication/" + R.raw.video; //(홈)추천영상
    String noticeTitle      = "Empty";

    //액티비티를 생성하는 구간
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SingleTon.broadcastReceiver(this, receiver);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        //뷰 초기화
        BTN_info            = findViewById(R.id.BTN_info);
        BTN_stopwatch       = findViewById(R.id.BTN_stopwatch);
        BTN_diary           = findViewById(R.id.BTN_diary);
        TV_workoutFriend    = findViewById(R.id.TV_workoutFriend);
        TV_BMI              = findViewById(R.id.TV_BMI);
        TV_gymInfo          = findViewById(R.id.TV_gymInfo);
        LA_menu             = findViewById(R.id.LA_menu);
        LA_back             = findViewById(R.id.LA_back);
        LA_play             = findViewById(R.id.LA_play);
        LA_stop             = findViewById(R.id.LA_stop);
        LA_next             = findViewById(R.id.LA_next);
        TV_notice           = findViewById(R.id.TV_notice);
        VV_movie            = findViewById(R.id.VV_movie);
        IV_thumbnail        = findViewById(R.id.IV_thumbnail);
        TV_currentMusicTime = findViewById(R.id.TV_currentMusicTime);
        TV_allMusicTime     = findViewById(R.id.TV_allMusicTime);
        TV_musicInfo        = findViewById(R.id.TV_musicInfo);
        LA_isPlayMusic        = findViewById(R.id.LA_isPlayMusic);

        //북마크 뷰 초기화
        IV_bookmarkNum1 = findViewById(R.id.IV_bookmarkNum1);
        TV_bookmarkNum1 = findViewById(R.id.TV_bookmarkNum1);
        IV_bookmarkNum2 = findViewById(R.id.IV_bookmarkNum2);
        TV_bookmarkNum2 = findViewById(R.id.TV_bookmarkNum2);
        IV_bookmarkNum3 = findViewById(R.id.IV_bookmarkNum3);
        TV_bookmarkNum3 = findViewById(R.id.TV_bookmarkNum3);
        IV_bookmarkNum4 = findViewById(R.id.IV_bookmarkNum4);
        TV_bookmarkNum4 = findViewById(R.id.TV_bookmarkNum4);
        IV_bookmarkNum5 = findViewById(R.id.IV_bookmarkNum5);
        TV_bookmarkNum5 = findViewById(R.id.TV_bookmarkNum5);
        IV_bookmarkNum6 = findViewById(R.id.IV_bookmarkNum6);
        TV_bookmarkNum6 = findViewById(R.id.TV_bookmarkNum6);

        //MP3 Player SeekBar
        musicSeekBar = findViewById(R.id.musicSeekBar);

        //운동 영상 Player
        mediaController = new MediaController(this); // 컨트롤러 생성
        mediaController.setAnchorView(VV_movie); //컨트롤러를 비디오뷰에 셋팅

        VV_movie.setMediaController(mediaController); //비디오 뷰에 영상 컨트롤러 셋팅
        VV_movie.setVideoURI(Uri.parse(path)); //비디오뷰에 영상URI 셋팅

        // notice 키 안에서 제목을 가져와서 공지사항제목 호출
        try {
            SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
            SP_notice.getString("notice", "");
            String[] notice = (SP_notice.getString("notice", "")).split(",");
            noticeTitle = notice[0];
        }catch (Exception e) {
            e.printStackTrace();
        }
        TV_notice.setText(noticeTitle);

//        iniExoplayer();

    }

//==================================================================================================

    //액티비티가 생성된 후 사용자에게 보여지기 시작하는 구간
    //onStart ~ onStop 은 반복적으로 실행 될 수 있기 때문에 보통 생성 관련된 명령은 지양
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);

        //북마크갯수인 6개만큼 반복하여 데이터를 불러옴
        for(int bookmarkNum = 1; bookmarkNum <= BOOKMARK_COUNT; bookmarkNum++) {
            String[] bookmarkData = new String[BOOKMARK_COUNT];
            Bitmap image = null;

            try {
                bookmarkData = SP_bookmark.getString("bookmarkNum"+bookmarkNum, null).split(",");

                //이미지를 String으로 받아 URI로 변환
                String imageString = bookmarkData[0];
                Uri imageUri = Uri.parse(imageString);

                //이미지URI를 비트맵형식으로 변환하여 변수에 저장

                InputStream inputStream = getContentResolver().openInputStream(imageUri);

                if (inputStream != null) {
                    image = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //북마크 번호에 맞는 데이터를 각 View에 셋팅
            switch (bookmarkNum) {
                case 1:
                    //image에 데이터가 이미지 데이터가 들어오면 이비지뷰로 출력
                    if(image != null) {
                        IV_bookmarkNum1.setImageBitmap(image); //image

                        //image에 데이터가 이미지 데이터가 들어오지 않으면 이미지뷰에 ic_launcher 출력
                    } else {
                        IV_bookmarkNum1.setImageResource(R.mipmap.ic_launcher);
                    }
                    if(bookmarkData[1] != null) {
                        TV_bookmarkNum1.setText(bookmarkData[1]);//title
                    }else {
                        TV_bookmarkNum1.setText("1번 북마크");
                    }
                    bookmarkNum1Url = bookmarkData[2]; //url
                    break;

                case 2:
                    if(image != null) {
                        IV_bookmarkNum2.setImageBitmap(image); //image
                    } else {
                        IV_bookmarkNum2.setImageResource(R.mipmap.ic_launcher);
                    }
                    if(bookmarkData[1] != null) {
                        TV_bookmarkNum2.setText(bookmarkData[1]); //title
                    }else {
                        TV_bookmarkNum2.setText("2번 북마크");
                    }
                    bookmarkNum2Url = bookmarkData[2]; //url
                    break;

                case 3:
                    if(image != null) {
                        IV_bookmarkNum3.setImageBitmap(image); //image
                    } else {
                        IV_bookmarkNum3.setImageResource(R.mipmap.ic_launcher);
                    }
                    if(bookmarkData[1] != null) {
                        TV_bookmarkNum3.setText(bookmarkData[1]); //title
                    }else {
                        TV_bookmarkNum3.setText("3번 북마크");
                    }
                    bookmarkNum3Url = bookmarkData[2]; //url
                    break;

                case 4:
                    if(image != null) {
                        IV_bookmarkNum4.setImageBitmap(image); //image
                    } else {
                        IV_bookmarkNum4.setImageResource(R.mipmap.ic_launcher);
                    }
                    if(bookmarkData[1] != null) {
                        TV_bookmarkNum4.setText(bookmarkData[1]); //title
                    }else {
                        TV_bookmarkNum4.setText("4번 북마크");
                    }
                    bookmarkNum4Url = bookmarkData[2]; //url
                    break;

                case 5:
                    if(image != null) {
                        IV_bookmarkNum5.setImageBitmap(image); //image
                    } else {
                        IV_bookmarkNum5.setImageResource(R.mipmap.ic_launcher);
                    }
                    if(bookmarkData[1] != null) {
                        TV_bookmarkNum5.setText(bookmarkData[1]); //title
                    }else {
                        TV_bookmarkNum5.setText("5번 북마크");
                    }
                    bookmarkNum5Url = bookmarkData[2]; //url
                    break;

                case 6:
                    if(image != null) {
                        IV_bookmarkNum6.setImageBitmap(image); //image
                    } else {
                        IV_bookmarkNum6.setImageResource(R.mipmap.ic_launcher);
                    }
                    if(bookmarkData[1] != null) {
                        TV_bookmarkNum6.setText(bookmarkData[1]); //title
                    }else {
                        TV_bookmarkNum6.setText("6번 북마크");
                    }
                    bookmarkNum6Url = bookmarkData[2]; //url
                    break;
            }

        }

        VV_movie.seekTo(videoPosition);// 영상이 Pause 되면 다시 실행될때 이전 위치부터 실행
    }

//==================================================================================================

    //액티비티가 사용자와 상호작용하는 구간
    @Override
    protected void onResume() {
        super.onResume();

        //좌상단 메뉴애니메이션 클릭리스너
        LA_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        //운동 친구목록으로 이동
        TV_workoutFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Workout_Friend_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //BMI 측정메뉴로 이동
        TV_BMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Calculation_BIM_Activity.class);
                startActivity(intent);
            }
        });

        //헬스장 현황목록으로 이동
        TV_gymInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Gym_Info_Activity.class);
                startActivity(intent);
            }
        });

//==================================================================================================

    //북마크 롱클릭리스너
        AlertDialog.Builder dialog = new AlertDialog.Builder(Home_Activity.this);
        dialog.setIcon(R.mipmap.ic_launcher); //Dialog icon
        dialog.setTitle("제거하시겠습니까?"); //Dialog title

        //1번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkClick(bookmarkNum1Url);
            }
        });

        //1번 북마크 롱클릭(제거기능)
        IV_bookmarkNum1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bookmarkLongClick(dialog, 1);

                return true;
            }
        });

        //2번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkClick(bookmarkNum2Url);
            }
        });

        //2번 북마크 롱클릭(제거기능)
        IV_bookmarkNum2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bookmarkLongClick(dialog, 2);

                return true;
            }
        });

        //3번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkClick(bookmarkNum3Url);
            }
        });

        //3번 북마크 롱클릭(제거기능)
        IV_bookmarkNum3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bookmarkLongClick(dialog, 3);

                return true;
            }
        });

        //4번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkClick(bookmarkNum4Url);
            }
        });

        //4번 북마크 롱클릭(제거기능)
        IV_bookmarkNum4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bookmarkLongClick(dialog, 4);

                return true;
            }
        });

        //5번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkClick(bookmarkNum5Url);
            }
        });

        //5번 북마크 롱클릭(제거기능)
        IV_bookmarkNum5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bookmarkLongClick(dialog, 5);

                return true;
            }
        });

        //6번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkClick(bookmarkNum6Url);
            }
        });

        //6번 북마크 롱클릭(제거기능)
        IV_bookmarkNum6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bookmarkLongClick(dialog, 6);

                return true;
            }
        });

//==================================================================================================

        //운동정보 액티비티로 이동
        BTN_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Workout_info_Activity.class);

                //FLAG_ACTIVITY_REORDER_TO_FRONT: 실행하려는 액티비티가 이미 스택에 존재하면 그 액티비티를
                //                                스택의 맨 위로 이동시켜서 실행하게만들어 줌
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //스탑워치 액티비티로 이동
        BTN_stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Stop_watch_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //운동일지 액티비티로 이동
        BTN_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Workout_diary_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

//==================================================================================================

        //공지사항 액티비티로 이동
        TV_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Notice_read_Activity.class);
                startActivityForResult(intent, REQUEST_TO_NOTICE);

            }
        });

//==================================================================================================

        //동영상을 클릭리스너
        IV_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IV_thumbnail.setVisibility(View.INVISIBLE);
                VV_movie.setVisibility(View.VISIBLE);
                VV_movie.start();
            }
        });

//==================================================================================================

        //서비스용 인텐트
        Intent serviceIntent = new Intent(Home_Activity.this, Music_Service.class);

        //음악플레이어 뒤로가기
        LA_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //이전에 실행되고 있던 음악 종료
                if (isPlaying) {
                    mediaPlayer[musicNum].stop();
                    mediaPlayer[musicNum].reset();
                    stopService(serviceIntent);

                    //이전곡 재생
                    if (musicNum > 0) {
                        musicNum--;
                    }
                    startService(serviceIntent);
                    LA_isPlayMusic.playAnimation();

                    if(isPause) {
                        isPause = false;

                        //애니메이션의 시작위치, 종료위치, 진행속도를 설정
                        playCustomAnimators(0f, 0.5f, 500);
                    }
                }

                //실행중이 아니라면 일반 재생
                if(!isPlaying) {
                    startService(serviceIntent);
                    LA_isPlayMusic.playAnimation();
                    isPause = false;

                    //애니메이션의 시작위치, 종료위치, 진행속도를 설정
                    playCustomAnimators(0f, 0.5f, 500);
                }
                LA_back.playAnimation();
            }
        });

        //음악플레이어 플레이, 일시정지
        LA_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying) {
                    if(!isPause) {
                        mediaPlayer[musicNum].pause();
                        LA_isPlayMusic.pauseAnimation();
                        isPause = true;
                        playCustomAnimators(0.5f, 1f, 500);
                    }else {
                        mediaPlayer[musicNum].start();
                        Music_Service.ThreadClass thread = new Music_Service.ThreadClass();
                        thread.start();
                        LA_isPlayMusic.playAnimation();
                        isPause = false; //다음 클릭시 pause상태

                        //애니메이션의 시작위치, 종료위치, 진행속도를 설정
                        playCustomAnimators(0f, 0.5f, 500);
                    }
                }else {
                    startService(serviceIntent);
                    LA_isPlayMusic.playAnimation(); //음악이 재생인지아닌
                    isPause = false; //다음 클릭시 pause상태

                    //애니메이션의 시작위치, 종료위치, 진행속도를 설정
                    playCustomAnimators(0f, 0.5f, 500);
                }
            }
        });

        //음악플레이어 정지
        LA_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying && !isPause) {
                    LA_isPlayMusic.pauseAnimation();
                    playCustomAnimators(0.5f, 1f, 500);
                }
                stopService(serviceIntent);
                isPlaying = false;
                isPause = true;

                LA_stop.playAnimation();
                musicSeekBar.setProgress(0);
                TV_currentMusicTime.setText("0:00");
            }
        });

        //음악플레이어 앞으로가기
        LA_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이전에 실행되고 있던 음악 종료
                if (isPlaying) {
                    mediaPlayer[musicNum].stop();
                    mediaPlayer[musicNum].reset();
                    stopService(serviceIntent);

                    //다음곡 재생
                    if (musicNum < musicAmount - 1) {
                        musicNum++;
                    }
                    startService(serviceIntent);
                    LA_isPlayMusic.playAnimation();

                    if(isPause) {
                        isPause = false;
                        playCustomAnimators(0f, 0.5f, 500);
                    }
                }

                //실행중이 아니라면 일반 재생
                if(!isPlaying) {
                    startService(serviceIntent);
                    LA_isPlayMusic.playAnimation();

                    isPause = false;
                    playCustomAnimators(0f, 0.5f, 500);
                }
                LA_next.playAnimation();
            }
        });

        //SeekBar 컨트롤에 반응하는 메소드
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                try {
                    if (fromUser) {
                        mediaPlayer[musicNum].seekTo(progress);
                        musicSeekBar.setProgress(progress);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

//==================================================================================================

    //다른 작업요청으로 인해 현재 액티비티가 멈춰진 구간
    //이 구간에서는 다시 원래 작업으로 돌아갈 시 onResume으로 돌아감
    //(생명주기 변화에 상태알림창은 포함 X)
    @Override
    protected void onPause() {
        super.onPause();
        VV_movie.pause();
        videoPosition = VV_movie.getCurrentPosition();

    }

//==================================================================================================

    //북마크 클릭 메소드
    public void bookmarkClick(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //북마크 롱클릭 메소드
    public void bookmarkLongClick(AlertDialog.Builder dialog, int number) {
        dialog.setNegativeButton("제거", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                SharedPreferences.Editor editor = SP_bookmark.edit();
                editor.remove("bookmarkNum"+number);
                editor.apply();
                dialog.dismiss();
                onStart(); //onstart메소드 호출(새로고침효과를 위해)
            }
        });

        dialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show(); //dialog창을 띄운다.
    }

//==================================================================================================

    //공지사항 작성에서 돌아왔을때 제목을 출력
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TO_NOTICE) {
            SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
            SP_notice.getString("notice", "");
            String[] notice = (SP_notice.getString("notice", "")).split(",");
            noticeTitle = notice[0];
            TV_notice.setText(noticeTitle);
        }

    }

//==================================================================================================

    private void playCustomAnimators(float startPosition, float finishPosition, int duration) {

        //애니메이션의 진행위치, 종료위치, 진행속도를 설정
        ValueAnimator animator = ValueAnimator.ofFloat(startPosition, finishPosition).setDuration(duration);
        animator.addUpdateListener(animation -> {
            LA_play.setProgress((Float) animation.getAnimatedValue());
        });
        animator.start();
    }

//==================================================================================================

//    public Object getNotice() {
//        SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
//        Set<String> notice = SP_notice.getStringSet("notice", new HashSet<String>());
//
//        Iterator it = notice.iterator();
//
//        return it.next();
//    }

//==================================================================================================

//        private void iniExoplayer() {
//        playerView = findViewById(R.id.EXO_movie);
//        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
//        playerView.setPlayer(simpleExoPlayer);
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
//                Util.getUserAgent(this, "MyApplication"));
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(videoURL));
//        simpleExoPlayer.prepare(videoSource);
//        simpleExoPlayer.setPlayWhenReady(true);
//    }

//==================================================================================================
}

