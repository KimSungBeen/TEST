package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.myapplication.Home_Activity.receiver;
import static com.example.myapplication.Notice_write_Activity.SP_data;

public class Notice_read_Activity extends AppCompatActivity {

    public static final int REQUEST_NOTICE_WRITE = 10;
    //뷰 선언
    TextView TV_noticeTitle, TV_noticeContents;
    Button BTN_noticeWrite, BTN_exit;

    String noticeTitle, noticeContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_read);

        SingleTon.broadcastReceiver(this, receiver);

        //뷰 초기화
        TV_noticeTitle      = findViewById(R.id.TV_noticeTitle);
        TV_noticeContents   = findViewById(R.id.TV_noticeContents);
        BTN_noticeWrite     = findViewById(R.id.BTN_noticeWrite);
        BTN_exit            = findViewById(R.id.BTN_exit);

        //저장한 데이터 불러오기
//        String noticeTitle = String.valueOf(getNotice());
//        String noticeContents = String.valueOf(getNotice());
//        TV_noticeTitle.setText(noticeTitle);
//        TV_noticeContents.setText(noticeContents);

        // notice 키 안의 데이터를 해당하는 변수로 분할해 저장
        try {
            SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
            String[] notice = SP_notice.getString("notice", "").split(",");
            noticeTitle = notice[0];
            noticeContents = notice[1];
        }catch (Exception e) {
            e.printStackTrace();
        }
        TV_noticeTitle.setText(noticeTitle);
        TV_noticeContents.setText(noticeContents);

    }

    @Override
    protected void onStart() {
        super.onStart();

//        SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
//        SharedPreferences.Editor editor = SP_notice.edit();
//        String noticeTitle = String.valueOf(TV_noticeTitle.getText());
//        String noticeContents = String.valueOf(TV_noticeContents.getText());
//        editor.putString("title", noticeTitle);
//        editor.putString("contents", noticeContents);
//        editor.commit();

//        setNotice();

    }

//    public void setNotice() {
//        SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
//        SharedPreferences.Editor editor = SP_notice.edit();
//
//        Set<String> notice = new HashSet<String>();
//        notice.add(String.valueOf(TV_noticeTitle.getText()));
//        notice.add(String.valueOf(TV_noticeContents.getText()));
//        editor.putStringSet("notice", notice);
//        editor.commit();
//
//    }
//
//    public String getNotice(String value) {
//        SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
//        Set<String> notice = SP_notice.getStringSet("notice", new HashSet<String>());
//
//        Iterator it = notice.iterator();
//
//        String title = String.valueOf(it.next());
//        String contents = String.valueOf(it.next());
//
//        if(value == "title") {
//            return title;
//        }else if(value == "contents") {
//            return contents;
//        }else{
//            return "잘못된 값";
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        //나가기 버튼
        BTN_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //공지사항 작성버튼
        BTN_noticeWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notice_read_Activity.this, Notice_write_Activity.class);
                startActivityForResult(intent, REQUEST_NOTICE_WRITE);

            }
        });
    }

    //공지사항이 작성되면 데이터를 받아와 읽기페이지에 보여주고 홈액티비티에 데이터를전송
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_NOTICE_WRITE) {
            if(resultCode == RESULT_OK) {
                noticeTitle = data.getStringExtra("noticeTitle");
                noticeContents = data.getStringExtra("noticeContents");

                TV_noticeTitle.setText(noticeTitle);
                TV_noticeContents.setText(noticeContents);

                Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show();

            }else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
        }
    }


}