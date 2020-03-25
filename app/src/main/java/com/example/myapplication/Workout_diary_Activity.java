package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.myapplication.Diary_Adapter.REQUEST_CORRECTION;
import static com.example.myapplication.Diary_Adapter.currentDate;
import static com.example.myapplication.Diary_Adapter.currentDiaryPosition;
import static com.example.myapplication.Home_Activity.receiver;

public class Workout_diary_Activity extends AppCompatActivity {

    public static final int REQUEST_WRITE_DIARY = 5;
    String date, diaryInput;
    ArrayList<Diary_Item> arrayList; //리싸이클러뷰 아이템의 데이터가 담길 ArrayList
    Diary_Adapter Diary_adapter; //리싸이클러뷰와 아이템을 연결 시켜주는 Adapter

    //어댑터에게 해당 항목을 정렬하는 방법을 알려주는 클래스
    //세로, 가로의 방향설정 혹은 스크롤에 대한 정보를 알 수 있음
    LinearLayoutManager linearLayoutManager;

    //뷰 선언
    Button BTN_home, BTN_info, BTN_stopwatch;
    ImageView IV_image;
    TextView    TV_mainLogo, TV_diaryInput, test;
    CalendarView calendarView;
    RecyclerView RV_diary; //리싸이클러뷰 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_diary);

        SingleTon.broadcastReceiver(this, receiver);

        //뷰 초기화
        TV_mainLogo     = findViewById(R.id.TV_mainLogo);
        test = findViewById(R.id.test);

        BTN_home        = findViewById(R.id.BTN_home);
        BTN_info        = findViewById(R.id.BTN_info);
        BTN_stopwatch   = findViewById(R.id.BTN_stopwatch);

        IV_image        = findViewById(R.id.IV_image);

        calendarView    = findViewById(R.id.calendarView);

        RV_diary = findViewById(R.id.RV_diary);

        //리싸이클러뷰내의 레이아웃을 컨트롤할 수 있게 해줌
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RV_diary.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();//아이템의 데이터가 저장될 ArrayList 생성
        Diary_adapter = new Diary_Adapter(arrayList); // arrayList에 있는 값을 Diary_adapter에 저장
        RV_diary.setAdapter(Diary_adapter); //리사이클러뷰에 Diary_adapter안의 값을 셋팅

        try {
            //내부저장소에서 파일을 읽어와 파일 객체생성
            File file = new File(getFilesDir(), "workout_diary");
            FileReader fileReader= new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            //readLine: 한줄을 읽어옴
            String line = bufferedReader.readLine();

            while (line != null) {
                //line안에 문자열 + \n
                stringBuilder.append(line).append("\n");

                //더이상 읽을 줄이없으면 null -> while문 종료
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String diaryData = stringBuilder.toString();
            JSONArray jsonArray = new JSONArray(diaryData);
            JSONObject jsonObject;

            //JSON 배열의 크기만큼 반복해서 데이터를 읽음
            for(int i = 0; i < jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);

                Uri bitmapUri = Uri.parse(jsonObject.get("image").toString()); //이미지의 uri 읽어오기
                Bitmap image = null;

                //이미지URI를 비트맵형식으로 변환하여 변수에 저장
                try {
                    InputStream inputStream = getContentResolver().openInputStream(bitmapUri);

                    if (inputStream != null) {
                        image = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                    }
                } catch (Exception ignored) {
                }

                //읽은 데이터로 리스트 복구
                Diary_Item item = new Diary_Item(image, jsonObject.get("title").toString(),
                        jsonObject.get("contents").toString(), null);
                arrayList.add(item);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //홈 액티비티로 이동
        BTN_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_diary_Activity.this, Home_Activity.class);
                //FLAG_ACTIVITY_REORDER_TO_FRONT: 실행하려는 액티비티가 이미 스택에 존재하면 그 액티비티를
                //                                스택의 맨 위로 이동시켜서 실행하게만들어 줌
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //운동정보 액티비티로 이동
        BTN_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_diary_Activity.this, Workout_info_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //스탑워치 액티비티로 이동
        BTN_stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_diary_Activity.this, Stop_watch_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //일정작성(캘린더뷰에 속한 메소드)
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(Workout_diary_Activity.this, Diary_write_Activity.class);
                date = "[" + year + "/" + (month+1) + "/" + dayOfMonth +"]";
                intent.putExtra("date", date);

                startActivityForResult(intent, REQUEST_WRITE_DIARY);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        //제이슨 배열 생성
        JSONArray workoutdiary = new JSONArray();

        try {

            //리스트의 갯수만큼 반복해서 데이터를 저장
            for(int i = 0; i < Diary_adapter.getItemCount(); i++) {
                JSONObject diaryObject = new JSONObject();

                //이미지를 Bitmap -> Uri -> String 으로 바꿔서 저장
                Uri uri = SingleTon.getImageUri(getApplicationContext(), arrayList.get(i).getIV_thumbnail());
                diaryObject.put("image", String.valueOf(uri));

                diaryObject.put("title", String.valueOf(arrayList.get(i).getTV_title()));
                diaryObject.put("contents", String.valueOf(arrayList.get(i).getTV_contents()));

                workoutdiary.put(diaryObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Json 객체를 String 으로 변환
        String diaryData = String.valueOf(workoutdiary);

        // 데이터가 저장될 파일 설정(경로, 이름)
        File file = new File(getApplicationContext().getFilesDir(), "workout_diary");
        FileWriter fileWriter = null;

        //fileWrite: 파일에 데이터를 작성할 수 있게 해줌
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Buffer: 한 장소에서 다른 장소로 이동하는 동안 데이터를 임시로 보유하는 데 사용되는 메모리 영역
        //BufferedWriter: fileWriter와 연결하여 file에 데이터를 작성
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        try {
            bufferedWriter.write(diaryData);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //작성이 완료되어 돌아오게 되면 결과를 출력하는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //받아온 이미지가 촬영한 이미지인지 앨범에서 가져온 이미지인지 구분
            boolean isCamera = data.getBooleanExtra("isCamera", false);
            Bitmap image = null;
            diaryInput = data.getStringExtra("diary");

            if(isCamera) {
                image = data.getParcelableExtra("cameraImage");
            }

            else if(!isCamera) {
                Uri bitmapUri = Uri.parse(data.getStringExtra("bitmapUri"));
                //이미지URI를 비트맵형식으로 변환하여 변수에 저장
                try {
                    InputStream inputStream = getContentResolver().openInputStream(bitmapUri);

                    if (inputStream != null) {
                        image = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                    }
                } catch (Exception ignored) {
                }
            }

            //작성을 완료 했을 때
            if ((requestCode == REQUEST_WRITE_DIARY)) {
                Diary_Item item = new Diary_Item(image, date, diaryInput, null); //아이템의 구조 생성

//                for(int i = 0; i < 1000; i++) {
                    arrayList.add(item); //생성한 아이템 리스트로 저장
//                }

                Diary_adapter.notifyDataSetChanged(); //(=새로고침효과) 아이템을 리스트에 저장하면 생성된 뷰 확인가능

                Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show();//작성완료 문구
            }

            //게시글을 수정할 때
            if (requestCode == REQUEST_CORRECTION) {

                Diary_adapter.change(currentDiaryPosition, image, currentDate, diaryInput, null); //아이템을 수정하는 메소드

                Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show(); //수정완료 문구
            }
        }

        //작성을 취소했을 때
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_LONG).show();
        }
    }

//======================================================================================================
}
