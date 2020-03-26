package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import static com.example.myapplication.File_Adapter.fileItems;
import static com.example.myapplication.Home_Activity.receiver;
import static com.example.myapplication.Notice_write_Activity.SP_data;

public class Workout_info_files_Activity extends AppCompatActivity {

    public static final int REQUEST_WRITE_COMPLETE = 3;
    public static final int REQUEST_CORRECTION = 4;
    int currentFilePosition; //문서의 위치번호

    Button BTN_home, BTN_stopwatch, BTN_diary, BTN_video, BTN_write;//버튼객체 선언
    File_Adapter File_Adapter = new File_Adapter(); //Adapter객체 선언
    ListView LV_file; //리스트뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_info_files);

        SingleTon.broadcastReceiver(this, receiver);
//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        //하단 메뉴버튼
        BTN_home        = findViewById(R.id.BTN_home);
        BTN_stopwatch   = findViewById(R.id.BTN_stopwatch);
        BTN_diary       = findViewById(R.id.BTN_diary);
        BTN_video       = findViewById(R.id.BTN_video);
        BTN_write       = findViewById(R.id.BTN_write);

        LV_file         = findViewById(R.id.LV_file);

        LV_file.setAdapter(File_Adapter); //사용할 리스트뷰에 만들어준 Adapter를 연결시킴

        //샘플리스트
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.file_image1);
//        File_Adapter.addItem(icon, "[NAVER]", "코로나19, 이제는 헬스장 가도 될까?",
//                "https://m.post.naver.com/viewer/postView.nhn?volumeNo=27687875&memberNo=3551273&vType=VERTICAL");

        if(!SingleTon.isBackPress) { //Back키를 눌렀을때는 작동하지 않게 하기위함(중복생성되는 오류 해결을위해)
            try {
                //내부저장소에서 파일을 읽어와 파일 객체생성
                File file = new File(getFilesDir(), "workout_file");

                //FileReader: file에서 문자를 읽어줌
                //BufferedReader: Reader객체에서 문자를 읽어줌 readLine이라는 메소드를 통해서
                //JSON을 구조를 사용했을때 데이터를 읽어오는데에 있어 용이하게 해줌
                FileReader fileReader = new FileReader(file);
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

                //fileData: 리스트를 저장한 데이터값
                String fileData = stringBuilder.toString();
                JSONArray jsonArray = new JSONArray(fileData);
                JSONObject jsonObject;

                //JSON 배열의 크기만큼 반복해서 데이터를 읽음
                for (int i = 0; i < jsonArray.length(); i++) {
//                    Log.d("onnn", "생성");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //읽은 데이터로 리스트 복구
                    File_Adapter.addItem(image, jsonObject.get("title").toString(),
                            jsonObject.get("contents").toString(),
                            jsonObject.get("url").toString());
                }
                //예외처리
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SingleTon.isBackPress = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //홈 액티비티로 이동
        BTN_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_files_Activity.this, Home_Activity.class);
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
                Intent intent = new Intent(Workout_info_files_Activity.this, Stop_watch_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });


        //운동일지 액티비티로 이동
        BTN_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_files_Activity.this, Workout_diary_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //동영상목록으로 이동
        BTN_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_files_Activity.this, Workout_info_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //문서작성으로 이동
        BTN_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workout_info_files_Activity.this, Info_write_Activity.class);

                startActivityForResult(intent, REQUEST_WRITE_COMPLETE);
            }
        });

        //아이템을 클릭하면 해당 사이트로 이동
        LV_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //아이템의 텍스트뷰 내용을 가져옴
                String url = String.valueOf(((TextView) view.findViewById(R.id.TV_url)).getText());
                String title = String.valueOf(((TextView) view.findViewById(R.id.TV_title)).getText());

                //입력한 URL로 이동 (잘못된URL 입력 시 & URL값이 없을 시 해당 Toast출력)
                try {
                    if(url.equals("https://")) {
                        Toast.makeText(Workout_info_files_Activity.this, "잘못된 URL", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        Toast.makeText(Workout_info_files_Activity.this, title + "로 이동", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Workout_info_files_Activity.this, "잘못된 URL", Toast.LENGTH_SHORT).show();
                }

//테스트용          Toast.makeText(Workout_info_files_Activity.this, url, Toast.LENGTH_SHORT).show();

            }
        });

        //리스트에서 아이템을 길게 누르면 수정, 제거 dialog 생성
        LV_file.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentFilePosition = position; //롱클릭한 아이템의 위치값

                //dialog 선언
                AlertDialog.Builder dialog = new AlertDialog.Builder(Workout_info_files_Activity.this);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("");
                dialog.setMessage("작업을 선택하십시오.");

                //롱클릭 Dialog안에 북마크를 클릭했을 시 북마크 설정이 가능한 Dialog를 다시 띄움
                dialog.setNeutralButton("북마크", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Dialog 선언과정
                        AlertDialog.Builder bookmarkDialog = new AlertDialog.Builder(Workout_info_files_Activity.this);
                        bookmarkDialog.setIcon(R.mipmap.ic_launcher);//Dialog icon
                        bookmarkDialog.setTitle(""); //Dialog title
                        bookmarkDialog.setMessage("북마크 번호를 입력하세요. (1 ~ 6)"); //Dialog Message
                        EditText ET_bookmarkNum = new EditText(getApplicationContext()); //Dialog EditText
                        bookmarkDialog.setView(ET_bookmarkNum);

                        bookmarkDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String bookmarkNum = ET_bookmarkNum.getText().toString();

                                //북마크 번호에 해당되는 경우에만 데이터 저장
                                if(bookmarkNum.equals("1") || bookmarkNum.equals("2") || bookmarkNum.equals("3")
                                        || bookmarkNum.equals("4") || bookmarkNum.equals("5") || bookmarkNum.equals("6")) {

                                    //SharedPreferences선언
                                    SharedPreferences SP_bookmark = getSharedPreferences(SP_data, 0);
                                    SharedPreferences.Editor editor = SP_bookmark.edit();

                                    //북마크데이터 (이미지, 제목, url)저장
                                    Uri saveImageUri = SingleTon.getImageUri(getApplicationContext(), fileItems.get(currentFilePosition).getIV_thumbnail());
                                    String image = String.valueOf(saveImageUri);
                                    String title = String.valueOf(fileItems.get(currentFilePosition).getTV_title());
                                    String url = String.valueOf(fileItems.get(currentFilePosition).getTV_url());
                                    String bookmarkData = image + "," + title + "," + url;

                                    editor.putString("bookmarkNum"+bookmarkNum, bookmarkData);
                                    editor.apply();

                                    Toast.makeText(getApplicationContext(), bookmarkNum+"번 북마크에 추가완료.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "잘못된 입력입니다. 1 ~ 6의 수를 입력하십시오.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        bookmarkDialog.show();

                    }
                });

                //수정
                dialog.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Workout_info_files_Activity.this, Info_write_Activity.class);

                        //Bitmap의 Uri를 얻어와서 변수에 저장(이미지 수정클릭시 가지고있던 데이터를 다시 보여주기위해)
                        if(fileItems.get(currentFilePosition).getIV_thumbnail() != null) {
                            Uri BitmapUri = SingleTon.getImageUri(Workout_info_files_Activity.this, (fileItems.get(currentFilePosition).getIV_thumbnail()));
                            intent.putExtra("imageBitmap", String.valueOf(BitmapUri));
                        } else {
                            Log.d("onClick", "else");
                        }
                        intent.putExtra("url", String.valueOf(fileItems.get(currentFilePosition).getTV_url()));
                        intent.putExtra("title", String.valueOf(fileItems.get(currentFilePosition).getTV_title()));
                        intent.putExtra("contents", String.valueOf(fileItems.get(currentFilePosition).getTV_contents()));


                        startActivityForResult(intent, REQUEST_CORRECTION);

                        dialog.dismiss(); //dialog 닫기
                    }
                });

                //제거
                dialog.setPositiveButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileItems.remove(position);
                        File_Adapter.notifyDataSetChanged();
                        dialog.dismiss(); //dialog 닫기
                    }
                });
                dialog.show(); //dialog창을 띄운다.

                return true;
            }
        });

    }

    //리스트 데이터 저장
    @Override
    protected void onStop() {
        super.onStop();

        //제이슨 배열 생성
        JSONArray workoutFile = new JSONArray();

        try {

            //리스트의 갯수만큼 반복해서 데이터를 저장
            for(int i = 0; i < File_Adapter.getCount(); i++) {
//                Log.d("onnn", "저장");
                JSONObject fileObject = new JSONObject();

                //이미지를 Bitmap -> Uri -> String 으로 바꿔서 저장
                Uri uri = SingleTon.getImageUri(getApplicationContext(), fileItems.get(i).getIV_thumbnail());
                fileObject.put("image", String.valueOf(uri));

                fileObject.put("title", String.valueOf(fileItems.get(i).getTV_title()));
                fileObject.put("contents", String.valueOf(fileItems.get(i).getTV_contents()));
                fileObject.put("url", String.valueOf(fileItems.get(i).getTV_url()));

                workoutFile.put(fileObject);
//                Log.d("myl", ""+workoutFile);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Json 객체를 String 으로 변환
        String fileData = String.valueOf(workoutFile);

        // 데이터가 저장될 파일 설정(경로, 이름)
        File file = new File(getApplicationContext().getFilesDir(), "workout_file");
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
            bufferedWriter.write(fileData);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //게시글 작성, 수정이 완료되어 돌아오게 되면 결과를 출력하는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            assert data != null; // data가 null이면 오류발생

            //Write액티비티에서 작성한 정보를 읽어와서 저장
            String title = data.getStringExtra("title"); //title 읽어오기
            String contents = data.getStringExtra("contents"); //contents 읽어오기
            Uri bitmapUri = Uri.parse(data.getStringExtra("bitmapUri")); //이미지의 uri 읽어오기
            Bitmap image = null; //이미지의 uri가 Bitmap으로 저장되어질 변수
            // onActivityResult가 다시 실핼 될 때 이전에 들어있던 값을

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

                String url = "https://" + data.getStringExtra("url"); //url 읽어오기

//                Long time = SystemClock.elapsedRealtimeNanos();
//                for(int i = 0; i < 1000; i++) {
                    //Adapter로 item과 Listview를 연결해 lsitView에 이미지, 타이틀, 내용 출력
                    File_Adapter.addItem(image, title, contents, url);
//                }
//                Log.w("ListView Time", String.valueOf(SystemClock.elapsedRealtimeNanos() - time));

                Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show(); //작성완료 문구
            }

            //게시글 수정할 때
            if (requestCode == REQUEST_CORRECTION) {

                String url = data.getStringExtra("url"); //url 읽어오기

                //현재 item의 List에 접근해서 변경한 데이터를 셋팅
                File_Adapter.changeItem(currentFilePosition, image, title, contents, url);

                Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show(); //수정완료 문구
            }
        }
        //뒤로가기 눌렀을때 취소 문구호출
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingleTon.isBackPress = true;
    }

//=================================================================================================
}
