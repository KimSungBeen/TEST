package com.example.myapplication;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.myapplication.Notice_write_Activity.SP_data;
import static com.example.myapplication.Workout_Friend_Adapter.contacts;

public class Workout_Friend_Activity extends AppCompatActivity {

    Workout_Friend_Adapter workout_friend_adapter = new Workout_Friend_Adapter();

    ListView LV_Contact;
    Button BTN_addKeyword, BTN_removeKeyword, BTN_exit;
    String keyword;
    String[] keywordArray;

//==================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_friend);

        //뷰 초기화
        LV_Contact = findViewById(R.id.LV_contact);
        BTN_addKeyword = findViewById(R.id.BTN_addKeyword);
        BTN_removeKeyword = findViewById(R.id.BTN_removeKeyword);
        BTN_exit = findViewById(R.id.BTN_exit);

    }

//==================================================================================================

    @Override
    protected void onStart() {
        super.onStart();

    //주소록 키워드
        SharedPreferences sharedPreferences = getSharedPreferences(SP_data, 0);
        keyword = sharedPreferences.getString("keyword", "");
        keywordArray = sharedPreferences.getString("keyword", "").split(",");


        contacts.clear(); //리스트에 남아있는 데이터 clear
        LV_Contact.setAdapter(workout_friend_adapter); //리스트뷰에 어댑터 연결

        //소모임 목록을 불러와 띄워주는 AsyncTask
        WorkoutFriendTask workoutFriendTask = new WorkoutFriendTask();
        workoutFriendTask.execute();

    }

//==================================================================================================

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

                            onStart(); //액티비티 데이터 다시불러오기 위해

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
                    public void onClick(DialogInterface dialog, int index) {

                        //dialog (선언, 아이콘, 제목)
                        AlertDialog.Builder request = new AlertDialog.Builder(v.getContext());
                        request.setIcon(R.mipmap.ic_launcher);
                        request.setTitle("제거하시겠습니까?");

                        request.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String keyword = null;

                                //리스트를 클릭할시 해당하는 인덱스: which
                                removeKeyword(index, keyword);
                            }
                        });
                        request.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Workout_Friend_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                            }
                        });
                        request.show();
                    }
                });

                AlertDialog removeDialog = dialog.create();
                removeDialog.show();
            }
        });

        //리스트 클릭
        LV_Contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setIcon(R.mipmap.ic_launcher); //Dialog icon
                dialog.setTitle("작업을 선택하십시오."); //Dialog title

                //전화걸기
                dialog.setNegativeButton("전화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = String.valueOf(((TextView) view.findViewById(R.id.TV_number)).getText());
                        number = "tel:" + number;

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(number));
                        startActivity(intent);
                    }
                });

                //메시지 보내기
                dialog.setPositiveButton("메시지", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = String.valueOf(((TextView) view.findViewById(R.id.TV_number)).getText());
                        number = "sms:" + number;

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(number));
                        startActivity(intent);
                    }
                });

                //취소
                dialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Workout_Friend_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

//==================================================================================================

    //키워드를 제거하는 메소드
    private void removeKeyword(int index, String keyword) {
        for (int i = 0; i < keywordArray.length; i++) {
            if (i != index) {
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

        Toast.makeText(Workout_Friend_Activity.this, "[" + keywordArray[index] + "]키워드 제거", Toast.LENGTH_SHORT).show();

        onStart(); //액티비티 데이터 다시불러오기 위해
    }

//==================================================================================================

    //주소록에서 데이터를 가져오는 클래스(AsyncTask)
    private class WorkoutFriendTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

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
                String[] name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).split(" ");
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String nameOutput = ""; //출력될 이름

                //name[0]:키워드, name[1]:사람이름, number:전화번호, nameOutput:화면에 출력될 이름(키워드+사람이름)
                publishProgress(name[0], name[1], number, nameOutput);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //keywordArray의 크기만큼 반복해서 리스트에 데이터를 저장
            for (String s : keywordArray) {

                //키워드가 비어있지 않을때 키워드를 포함하는 데이터를 저장함
                if (!s.equals("") && values[0].equals(s)) { //name[0]: 주소록에 저장될 키워드

                    //name[0]:키워드, name[1]:사람이름, number:전화번호, nameOutput:화면에 출력될 이름(키워드+사람이름)
                    values[3] = values[0] + " " + values[1];
                    workout_friend_adapter.addItem(values[3], values[2]);
                }
            }

        }
    }

//==================================================================================================
}
