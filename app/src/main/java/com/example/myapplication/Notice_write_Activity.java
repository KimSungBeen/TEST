package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.myapplication.Home_Activity.receiver;
import static com.example.myapplication.Notice_read_Activity.REQUEST_NOTICE_WRITE;

public class Notice_write_Activity extends AppCompatActivity {

    //SP: SharedPreferences
    static String SP_data;

    //뷰 선언
    EditText ET_noticeTitle, ET_noticeContents;
    Button BTN_noticeComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);

        SingleTon.broadcastReceiver(this, receiver);

        //뷰 초기화
        ET_noticeTitle      = findViewById(R.id.ET_noticeTitle);
        ET_noticeContents   = findViewById(R.id.ET_noticeContents);
        BTN_noticeComplete  = findViewById(R.id.BTN_noticeComplete);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //작성완료 버튼
        BTN_noticeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noticeTitle      = String.valueOf(ET_noticeTitle.getText());
                String noticeContents   = String.valueOf(ET_noticeContents.getText());

                //사용자가 입력한 공지사항의 제목과 내용을 합쳐 SharedPreferences의 notice라는 키로 저장
                //notice: 제목 + 내용
                String notice = noticeTitle + "," + noticeContents;
                SharedPreferences SP_notice = getSharedPreferences(SP_data, 0);
                SharedPreferences.Editor editor = SP_notice.edit();
                editor.putString("notice", notice);
                editor.apply();

                //제목, 내용 미작성시의 예외처리 구간
                if((!noticeTitle.equals("")) && (!noticeContents.equals(""))) {

                    //홈 액티비티로 값 전달
                    Intent intent = new Intent();
                    intent.putExtra("noticeTitle", noticeTitle);
                    intent.putExtra("noticeContents", noticeContents);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "제목과 내용을 입력해 주십시오.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
