package com.example.myapplication;

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

import java.io.InputStream;

import static com.example.myapplication.Music_Service.isPlaying;
import static com.example.myapplication.Music_Service.mediaPlayer;
import static com.example.myapplication.Music_Service.musicAmount;
import static com.example.myapplication.Music_Service.musicNum;
import static com.example.myapplication.Notice_write_Activity.SP_data;

public class Home_Activity extends AppCompatActivity {

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
    Button BTN_menu;
    TextView TV_notice, TV_back, TV_play, TV_pause, TV_stop, TV_next,
            TV_bookmarkNum1, TV_bookmarkNum2, TV_bookmarkNum3, TV_bookmarkNum4, TV_bookmarkNum5, TV_bookmarkNum6;
    ImageView  IV_thumbnail,
            IV_bookmarkNum1, IV_bookmarkNum2, IV_bookmarkNum3, IV_bookmarkNum4, IV_bookmarkNum5, IV_bookmarkNum6;

//    PlayerView playerView;
//    SimpleExoPlayer simpleExoPlayer;

    VideoView VV_movie;
    MediaController mediaController; // 미디어 제어 (재생이나 정지) 버튼을 담당

//    String videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";//테스트용 영상
    String path = "android.resource://com.example.myapplication/" + R.raw.video; //(홈)추천영상
    String noticeTitle      = "Empty";

    //액티비티를 생성하는 구간
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SingleTon.broadcastReceiver(this, receiver);

        //뷰 초기화
        BTN_info        = findViewById(R.id.BTN_info);
        BTN_stopwatch   = findViewById(R.id.BTN_stopwatch);
        BTN_diary       = findViewById(R.id.BTN_diary);
        BTN_menu        = findViewById(R.id.BTN_menu);
        TV_back         = findViewById(R.id.TV_back);
        TV_play         = findViewById(R.id.TV_play);
        TV_pause        = findViewById(R.id.TV_pause);
        TV_stop         = findViewById(R.id.TV_stop);
        TV_next         = findViewById(R.id.TV_next);
        TV_notice       = findViewById(R.id.TV_notice);
        VV_movie        = findViewById(R.id.VV_movie);
        IV_thumbnail    = findViewById(R.id.IV_thumbnail);
        TV_currentMusicTime = findViewById(R.id.TV_currentMusicTime);
        TV_allMusicTime    = findViewById(R.id.TV_allMusicTime);

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

//        Uri uri = Uri.parse(videoURL); //영상 주소를 저장
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

        //---------------------------------------------------------------------------

//        iniExoplayer();

        //---------------------------------------------------------------------------

    }

//    private void iniExoplayer() {
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

    //액티비티가 사용자와 상호작용하는 구간
    @Override
    protected void onResume() {
        super.onResume();

//=========================================북마크 클릭이벤트=========================================

        AlertDialog.Builder dialog = new AlertDialog.Builder(Home_Activity.this);
        dialog.setIcon(R.mipmap.ic_launcher); //Dialog icon
        dialog.setTitle(""); //Dialog title
        dialog.setMessage("제거 하시겠습니까?."); //Dialog Message


        //1번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkNum1Url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //1번 북마크 롱클릭(제거기능)
        IV_bookmarkNum1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog.setNegativeButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                        SharedPreferences.Editor editor = SP_bookmark.edit();
                        editor.remove("bookmarkNum1");
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

                return true;
            }
        });

        //2번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkNum2Url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //2번 북마크 롱클릭(제거기능)
        IV_bookmarkNum2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog.setNegativeButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                        SharedPreferences.Editor editor = SP_bookmark.edit();
                        editor.remove("bookmarkNum2");
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

                return true;
            }
        });

        //3번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkNum3Url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //3번 북마크 롱클릭(제거기능)
        IV_bookmarkNum3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog.setNegativeButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                        SharedPreferences.Editor editor = SP_bookmark.edit();
                        editor.remove("bookmarkNum3");
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

                return true;
            }
        });

        //4번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkNum4Url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //4번 북마크 롱클릭(제거기능)
        IV_bookmarkNum4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog.setNegativeButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                        SharedPreferences.Editor editor = SP_bookmark.edit();
                        editor.remove("bookmarkNum4");
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

                return true;
            }
        });

        //5번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkNum5Url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //5번 북마크 롱클릭(제거기능)
        IV_bookmarkNum5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog.setNegativeButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                        SharedPreferences.Editor editor = SP_bookmark.edit();
                        editor.remove("bookmarkNum5");
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

                return true;
            }
        });

        //6번 북마크 클릭(해당Url로 이동)
        IV_bookmarkNum6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkNum6Url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //6번 북마크 롱클릭(제거기능)
        IV_bookmarkNum6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog.setNegativeButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                        SharedPreferences.Editor editor = SP_bookmark.edit();
                        editor.remove("bookmarkNum6");
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
        TV_back.setOnClickListener(new View.OnClickListener() {
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
                }

                //실행중이 아니라면 일반 재생
                if(!isPlaying) {
                    startService(serviceIntent);
                    TV_play.setVisibility(View.INVISIBLE);
                    TV_pause.setVisibility(View.VISIBLE);
                }
            }
        });

        //음악플레이어 플레이
        TV_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying) {
                    mediaPlayer[musicNum].start();
                    Music_Service.ThreadClass thread = new Music_Service.ThreadClass();
                    thread.start();

                }else {
                    startService(serviceIntent);
                }
                TV_play.setVisibility(View.INVISIBLE);
                TV_pause.setVisibility(View.VISIBLE);
            }
        });

        //음악플레이어 일시정지
        TV_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer[musicNum].pause();
                TV_play.setVisibility(View.VISIBLE);
                TV_pause.setVisibility(View.INVISIBLE);
            }
        });

        //음악플레이어 정지
        TV_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
                TV_play.setVisibility(View.VISIBLE);
                TV_pause.setVisibility(View.INVISIBLE);
                isPlaying = false;
            }
        });

        //음악플레이어 앞으로가기
        TV_next.setOnClickListener(new View.OnClickListener() {
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
                }

                //실행중이 아니라면 일반 재생
                if(!isPlaying) {
                    startService(serviceIntent);
                    TV_play.setVisibility(View.INVISIBLE);
                    TV_pause.setVisibility(View.VISIBLE);
                }
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

//    public Object getNotice() {
//        SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
//        Set<String> notice = SP_notice.getStringSet("notice", new HashSet<String>());
//
//        Iterator it = notice.iterator();
//
//        return it.next();
//    }
//==================================================================================================
}

