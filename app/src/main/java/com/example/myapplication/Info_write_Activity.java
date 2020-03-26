package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import static com.example.myapplication.Home_Activity.receiver;

public class Info_write_Activity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_LOAD = 2;

    ImageView IV_thumbnail;
    Button BTN_addImage, BTN_complete;
    EditText ET_url, ET_title, ET_contents;
    String url, title, contents;
    Bitmap bitmap; //사진첩에서 가져온 사진
    Uri bitmapUri; //사진첩에서 가져온 사진의 Uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_write);

        SingleTon.broadcastReceiver(this, receiver);

        //뷰 초기화
        IV_thumbnail = findViewById(R.id.IV_thumbnail);
        BTN_addImage = findViewById(R.id.BTN_addImage);
        BTN_complete = findViewById(R.id.BTN_complete);
        ET_url       = findViewById(R.id.ET_url);
        ET_title     = findViewById(R.id.ET_title);
        ET_contents  = findViewById(R.id.ET_contents);

        //리스트 수정시 원래 가지고있던 데이터를 보여주기 위한 구간
        Intent intent = getIntent();

        try {
            bitmapUri = Uri.parse(intent.getStringExtra("imageBitmap"));

            InputStream inputStream = getContentResolver().openInputStream(bitmapUri);

            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            IV_thumbnail.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        IV_thumbnail.setImageBitmap(bitmap);

        ET_url.setText(intent.getStringExtra("url"));
        ET_title.setText(intent.getStringExtra("title"));
        ET_contents.setText(intent.getStringExtra("contents"));

    }

    @Override
    protected void onResume() {
        super.onResume();

        //사진추가버튼 클릭시 사진첩으로 이동
        BTN_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE_LOAD);// 이동한 곳에서 작업이 완료되면 데이터를 받아서 다시 돌아옴
            }
        });

        //작성완료 버튼
        BTN_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                url = ET_url.getText().toString();
                title = ET_title.getText().toString();
                contents = ET_contents.getText().toString();

                intent.putExtra("url", url);
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);

                //이미지 URI를 String으로 전달
                //bitmapUri.toSting: null이면 에러남
                //String.valueOf(bitmapUri): null이면 null값을 줌
                intent.putExtra("bitmapUri",  String.valueOf(bitmapUri));

                if((!title.equals("")) && (!contents.equals(""))) {
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(v.getContext(), "제목과 내용을 작성해 주십시오.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //==================================================================================================
    //사진첩에서 추가할 사진 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_LOAD) {
            if (resultCode == RESULT_OK) {
                try {
                    bitmapUri = data.getData(); //이미지 데이터의 URI 저장 (위에서 인텐트로 보내기위해)

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());

                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();

                    IV_thumbnail.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

}
