package com.example.myapplication;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.myapplication.Notice_write_Activity.SP_data;
import static com.example.myapplication.Workout_Friend_Adapter.contacts;

public class Workout_Friend_Activity extends AppCompatActivity {

    Workout_Friend_Adapter workout_friend_adapter = new Workout_Friend_Adapter();

//    ArrayList<String> contact = new ArrayList<>();
    ListView LV_Contact;
    Button BTN_addKeyword, BTN_removeKeyword, BTN_exit;
    String keyword;
    String[] keywordArray;

//==================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_friend);

        Log.i("onnn", "onCreate: ");

        //뷰 초기화
        LV_Contact = findViewById(R.id.LV_contact);
        BTN_addKeyword = findViewById(R.id.BTN_addKeyword);
        BTN_removeKeyword = findViewById(R.id.BTN_removeKeyword);
        BTN_exit = findViewById(R.id.BTN_exit);

        //주소록 키워드
        SharedPreferences sharedPreferences = getSharedPreferences(SP_data, 0);
        keyword = sharedPreferences.getString("keyword", "");
        keywordArray = sharedPreferences.getString("keyword", "").split(",");

//==================================================================================================

        contacts.clear(); //리스트에 남아있는 데이터 clear
        LV_Contact.setAdapter(workout_friend_adapter); //리스트뷰에 어댑터 연결

        //ContentResolver로 주소록 가져오기
        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String selection = null;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        String[] selectionArgs = null;
        String sortOrder = null;

        //Cursor객체에 데이터 저장
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);

        //가져온 주소록을 키워드로 필터링하여 변수에 저장
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            //keywordArray의 크기만큼 반복해서 리스트에 데이터를 저장
            for (String s : keywordArray) {
                if (!s.equals("")) { //keywordArray이 비어있지 않을 때만
                    if (name.contains(s)) {
                        workout_friend_adapter.addItem(name + "\t" + num);
                    }
                }
            }

        }

    }

//==================================================================================================

    @Override
    protected void onResume() {
        super.onResume();

        //나가기 버튼
        BTN_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //키워드 추가 버튼
        BTN_addKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog (선언, 아이콘, 제목)
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setIcon(R.mipmap.ic_launcher); //Dialog icon
                dialog.setTitle("추가할 키워드를 입력하십시오."); //Dialog title
                EditText ET_addKeyword = new EditText(getApplicationContext());
                dialog.setView(ET_addKeyword);

                //확인버튼
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String addKeyword = ET_addKeyword.getText().toString();

                        //입력한 값이 있을때
                        if (!addKeyword.equals("")) {

                            //처음에 입력값이 없을때는 키워드만 추가되고
                            //값이 존재할 때는 ","로 구분되어 변수에 저장함
                            if (keyword.equals("")) {
                                keyword = addKeyword;
                            } else {
                                keyword = keyword + "," + addKeyword;
                            }

                            //SharedPreferences에 키워드 저장
                            SharedPreferences sharedPreferences = getSharedPreferences(SP_data, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("keyword", keyword);
                            editor.apply();

                            //키워드 저장후 액티비티 새로고침
                            Intent intent = new Intent(getApplicationContext(), Workout_Friend_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);

                            //입력한 값이 없을때
                        } else {
                            Toast.makeText(Workout_Friend_Activity.this, "입력값 없음.", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                //취소버튼
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(Workout_Friend_Activity.this, "취소.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

        //키워드 제거 버튼
        BTN_removeKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog (선언, 아이콘, 제목)
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setIcon(R.mipmap.ic_launcher); //Dialog icon
                dialog.setTitle("제거할 키워드를 선택하십시오."); //Dialog title

                //리스트형식의 dialog
                dialog.setItems(keywordArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String keyword = null;

                        //리스트를 클릭할시 해당하는 인덱스: which
                        removeKeyword(which, keyword);

                    }
                });

                AlertDialog removeDialog = dialog.create();
                removeDialog.show();
            }
        });

    }

//==================================================================================================

    //키워드를 제거하는 메소드
    private void removeKeyword(int which, String keyword) {
        for (int i = 0; i < keywordArray.length; i++) {
            if (i != which) {
                if (keyword == null) {
                    keyword = keywordArray[i];
                } else {
                    keyword = keyword + "," + keywordArray[i];
                }
            }
        }

        //제거한 키워드 Key에 저장되어 있는 값을 수정
        SharedPreferences sharedPreferences = getSharedPreferences(SP_data, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("keyword", keyword);
        editor.apply();

        Toast.makeText(Workout_Friend_Activity.this, keywordArray[which] + "제거", Toast.LENGTH_SHORT).show();

        //키워드 저장후 액티비티 새로고침
        Intent intent = new Intent(getApplicationContext(), Workout_Friend_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

//==================================================================================================
}
