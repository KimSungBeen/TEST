package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import static com.example.myapplication.Home_Activity.receiver;
import static com.example.myapplication.Notice_write_Activity.SP_data;
import static com.example.myapplication.Video_Adapter.REQUEST_CORRECTION;
import static com.example.myapplication.Video_Adapter.arrayList;
import static com.example.myapplication.Video_Adapter.currentVideoPosition;

public class Workout_info_Activity extends AppCompatActivity {

    public static final int REQUEST_WRITE_COMPLETE = 3;

    Video_Adapter video_adapter; //리싸이클러뷰와 아이템을 연결 시켜주는 Adapter
    LinearLayoutManager linearLayoutManager; //어댑터에게 해당 항목을 정렬하는 방법을 알려주는 클래스
                                            //세로, 가로의 방향설정 혹은 스크롤에 대한 정보를 알 수 있음

    //뷰 선언
    Button BTN_home, BTN_stopwatch, BTN_diary, BTN_file, BTN_write;
    TextView test;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_info);

        SingleTon.broadcastReceiver(this, receiver);

        //뷰 초기화
        BTN_home = findViewById(R.id.BTN_home);
        BTN_stopwatch = findViewById(R.id.BTN_stopwatch);
        BTN_diary = findViewById(R.id.BTN_diary);
        BTN_file = findViewById(R.id.BTN_file);
        BTN_write = findViewById(R.id.BTN_write);
        test = findViewById(R.id.test);

     //리싸이클러뷰
        recyclerView = findViewById(R.id.RV_video);

        //리싸이클러뷰내의 레이아웃을 컨트롤할 수 있게 해줌
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //아이템의 데이터가 저장될 ArrayList 생성
        arrayList = new ArrayList<>();

        // arrayList에 있는 값을 video_adapter에 저장
        //리사이클러뷰에 video_adapter안의 값을 셋팅
        video_adapter = new Video_Adapter(arrayList);
        recyclerView.setAdapter(video_adapter);

        //샘플리스트
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.video_image1);
        Video_Item sample = new Video_Item(icon,"운동할 때 뼈 소리. 어떻게해야 할까?",
                "[YOUTUBE]","https://www.youtube.com/watch?v=RoXScP8wZQc");
        arrayList.add(sample);
        video_adapter.notifyDataSetChanged();

        SharedPreferences SP_video = getSharedPreferences(SP_data, 0);

        //리스트의 총 갯수를 불러옴
        int itemCount = SP_video.getInt("itemCount", 0);

        //저장했던 리스트만큼의 데이터를 복구하는 반복문
        for(int i = 1; i < itemCount; i++) {

            //저장되었던 데이터를 split을 이용해 ","을 기준으로 나눠서 저장
            //(이미지, 제목, 내용, URL) 순서로 저장되어 있음
            String[] videoData = SP_video.getString("video"+i, null).split(",");
            Bitmap image = null;
            String url;

            String imageString = videoData[0];
            Uri imageUri = Uri.parse(imageString);

            //이미지URI를 비트맵형식으로 변환하여 변수에 저장
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);

                if (inputStream != null) {
                    image = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String title = videoData[1];
            String contents = videoData[2];

            try {
                url = videoData[3];
            }catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                url = null;
            }

            //복구한 리스트 다시 생성
            Video_Item item = new Video_Item(image, title, contents, url);
            arrayList.add(item);
        }

//        실험실패...?
//        for(int i = 1; i <= 50; i++) {
//            if((i%10) == 1) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test1), "1", "1", null);
//                arrayList.add(item); //생성한 아이템 리스트로 저장
//            }else if((i%10) == 2) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test2), "2", "2", null);
//                arrayList.add(item);
//            }else if((i%10) == 3) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test3), "3", "3", null);
//                arrayList.add(item);
//            }else if((i%10) == 4) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test4), "4", "4", null);
//                arrayList.add(item);
//            }else if((i%10) == 5) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test5), "5", "5", null);
//                arrayList.add(item);
//            }else if((i%10) == 6) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test6), "6", "6", null);
//                arrayList.add(item);
//            }else if((i%10) == 7) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test7), "7", "7", null);
//                arrayList.add(item);
//            }else if((i%10) == 8) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test8), "8", "8", null);
//                arrayList.add(item);
//            }else if((i%10) == 9) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test9), "9", "9", null);
//                arrayList.add(item);
//            }else if((i%10) == 0) {
//                Video_Item item = new Video_Item(BitmapFactory.decodeResource(getResources(), R.drawable.test10), "10", "10", null);
//                arrayList.add(item);
//            }
//        }
        video_adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //홈 액티비티로 이동
        BTN_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_Activity.this, Home_Activity.class);
                //FLAG_ACTIVITY_REORDER_TO_FRONT: 실행하려는 액티비티가 이미 스택에 존재하면 그 액티비티를
                //                                스택의 맨 위로 이동시켜서 실행하게만들어 줌
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //스톱워치 액티비티로 이동
        BTN_stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_Activity.this, Stop_watch_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });


        //운동일지 액티비티로 이동
        BTN_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_Activity.this, Workout_diary_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //문서목록으로 이동
        BTN_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_Activity.this, Workout_info_files_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //게시글작성 액티비티로 이동
        BTN_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_Activity.this, Info_write_Activity.class);
                startActivityForResult(intent, REQUEST_WRITE_COMPLETE);
            }
        });

    }

    //리스트에 저장되어있는 데이터를 SharedPreference 로 저장하는 구간
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences SP_video = getSharedPreferences(SP_data, 0);
        SharedPreferences.Editor editor = SP_video.edit();

        //리스트의 총 갯수
        int itemCount = video_adapter.getItemCount();
        editor.putInt("itemCount", itemCount);
//        Toast.makeText(this, ""+itemCount, Toast.LENGTH_SHORT).show();

        //리스트의 갯수만큼 데이터를 저장
        for(int i = 1; i < itemCount; i++) {

            //리스트의 이미지를 bitmap -> Uri -> String 단계를 거쳐 저장
            Uri saveImageUri = SingleTon.getImageUri(this, arrayList.get(i).getIV_thumbnail());
            String image = String.valueOf(saveImageUri);

            //데이터를 한 문자열로 구성
            String title = String.valueOf(arrayList.get(i).getTV_title());
            String contents = String.valueOf(arrayList.get(i).getTV_contents());
            String url = String.valueOf(arrayList.get(i).getTV_url());
            String videoData = image + "," + title + "," + contents + "," + url;

            //각각 리스트에 인덱스 번호(i)를 매겨 저장
            editor.putString("video"+i, videoData);
        }

        editor.apply();
    }

//==================================================================================================

    //작성이 완료되어 돌아오게 되면 결과를 출력하는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            assert data != null; // data가 null이면 오류발생

            //Write액티비티에서 작성한 정보를 읽어와서 저장
            String title = data.getStringExtra("title"); //title 읽어오기
            String contents = data.getStringExtra("contents"); //contents 읽어오기
            Uri bitmapUri = Uri.parse(data.getStringExtra("bitmapUri")); //이미지의 uri 읽어오기
            Bitmap image = null; //이미지의 uri가 Bitmap으로 저장되어질 변수
                                // onActivityResult가 다시 실핼 될 때 이전에 들어있던 값을 비우고 사용하기 위해

            //이미지URI를 비트맵형식으로 변환하여 변수에 저장
            try {
                InputStream inputStream = getContentResolver().openInputStream(bitmapUri);

                if (inputStream != null) {
                    image = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //게시글 작성완료할 때
            if (requestCode == REQUEST_WRITE_COMPLETE) {

                String url = "https://" +  data.getStringExtra("url"); //url 읽어오기
                //Adapter로 item클래스와 Listview를 연결해 lsitView에 이미지, 타이틀, 내용 출력
                Video_Item item = new Video_Item(image, title, contents, url); //아이템의 구조 생성

//                Long time = SystemClock.elapsedRealtimeNanos();
//                for(int i = 0; i < 21000000; i++) {
                    arrayList.add(item); //생성한 아이템 리스트로 저장
//                }
                video_adapter.notifyDataSetChanged(); //(=새로고침효과) 아이템을 리스트에 저장하면 생성된 뷰 확인가능
//                Log.e("RecyclerView Time", String.valueOf(SystemClock.elapsedRealtimeNanos() - time));

                test.setText(""); //테스트용
                Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show(); //작성완료 문구
            }

            //게시글을 수정할 때
            if (requestCode == REQUEST_CORRECTION) {

                String url = data.getStringExtra("url"); //url 읽어오기
                video_adapter.change(currentVideoPosition, image, title, contents, url); //아이템을 수정하는 메소드

                Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show(); //수정완료 문구
            }
        }

        //뒤로가기 눌렀을때 취소 문구호출
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_LONG).show();
        }
    }


}