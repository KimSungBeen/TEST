package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import static com.example.myapplication.Home_Activity.receiver;

public class Diary_write_Activity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_LOAD = 2;
    boolean isCamera;

    String date; //추가한 일정의 날짜
    Uri bitmapUri; //Workout_diary로 넘겨줄 이미지 URI
    Bitmap bitmap;
    Bitmap cameraImage; //미리보기용 이미지

    //뷰 선언
    TextView TV_date;
    EditText ET_diary;
    Button BTN_diaryComplete, BTN_camera, BTN_gallary;
    ImageView IV_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        SingleTon.broadcastReceiver(this, receiver);

        //뷰 초기화
        TV_date             = findViewById(R.id.TV_date);
        ET_diary            = findViewById(R.id.ET_diary);
        BTN_diaryComplete   = findViewById(R.id.BTN_diaryComplete);
        BTN_camera          = findViewById(R.id.BTN_camera);
        BTN_gallary         = findViewById(R.id.BTN_gallary);
        IV_image            = findViewById(R.id.IV_image);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        boolean isCorrection = bundle.getBoolean("isCorrection");

        //받아온 값이 있을 때만 실행
        if(getIntent() != null) {
            date = intent.getStringExtra("date");
            TV_date.setText(date); //날짜 설정
        }

        //리스트 수정시 원래 가지고있던 데이터를 보여주기 위한 구간
        if(isCorrection) {
            try {
                bitmapUri = Uri.parse(intent.getStringExtra("imageBitmap"));

                InputStream inputStream = getContentResolver().openInputStream(bitmapUri);

                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                IV_image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            IV_image.setImageBitmap(bitmap);

            TV_date.setText(intent.getStringExtra("title"));
            ET_diary.setText(intent.getStringExtra("contents"));
        }

    }

    @Override
    protected void onResume() {
            super.onResume();

        //카메라어플 실행버튼
        BTN_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }

        });

        //앨범 실행 버튼
        BTN_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE_LOAD);// 이동한 곳에서 작업이 완료되면 데이터를 받아서 다시 돌아옴
            }
        });

        //작성완료 클릭시 작성날짜, 내용 Workout_diary로 전달
        BTN_diaryComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String diary = String.valueOf(ET_diary.getText());

                //작성한 일정 전달
                Intent intent = new Intent();
                if(isCamera) {
                    intent.putExtra("isCamera", isCamera);
                }
                intent.putExtra("diary", diary);
                intent.putExtra("cameraImage", cameraImage);
                intent.putExtra("bitmapUri", String.valueOf(bitmapUri));

//                Toast.makeText(v.getContext(), diary, Toast.LENGTH_SHORT).show();

                if(!diary.equals("")) {
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(v.getContext(), "내용을 작성해 주십시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //카메라 어플 실행하는 메소드
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //카메라어플에서 찍은 이미지를 미리보기로 가져옴
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            isCamera = true;
            Bundle bundle = data.getExtras();
            cameraImage = (Bitmap) bundle.get("data");
            IV_image.setImageBitmap(cameraImage); //촬영한 이미지를 미리보기로 이미지 뷰에 보여줌
        }

        //앨범에서 이미지를 가져오는 부분
        if(requestCode == REQUEST_IMAGE_LOAD) {
            isCamera = false;
            if(resultCode == RESULT_OK) {

                try {
                    bitmapUri = data.getData(); //이미지 데이터의 URI 저장

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());

                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();

                    IV_image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "이미지 가져오기 성공", Toast.LENGTH_SHORT).show();

            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진선택 취소", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
