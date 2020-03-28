package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.Notice_write_Activity.SP_data;

public class Video_Adapter extends RecyclerView.Adapter<Video_Adapter.MyViewHolder> {

    static ArrayList<Video_Item> arrayList; //아이템의 데이터가 들어갈 ArrayList
    static final int REQUEST_CORRECTION = 4; //수정하기의 리퀘스트코드

    private Object object; //startActivityForResult 사용을 위해 ViewGroup을 저장할 변수

    static int currentVideoPosition;// 수정시 아이템의 위치값을 알아야 하기 때문에 static 변수에 저장

    Video_Adapter(ArrayList<Video_Item> arrayList) {
        Video_Adapter.arrayList = arrayList;
    }

    //viewHolder객체를 생성하는 구간
    @NonNull
    @Override
    public Video_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d("Video Adapter", "onCreateViewHolder");

        //레이아웃과 아이템 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        object = parent.getContext();

        return holder;
    }

    //ViewHolder에 데이터를 넣는 작업을 수행함
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Log.d("Video Adapter", "onBindViewHolder [Position: " + position + "]");

        //아이템 데이터를 셋팅하는 부분
        holder.IV_thumbnail.setImageBitmap(arrayList.get(position).getIV_thumbnail());
        holder.TV_url.setText(arrayList.get(position).getTV_url());
        holder.TV_title.setText(arrayList.get(position).getTV_title());
        holder.TV_contents.setText(arrayList.get(position).getTV_contents());

        holder.itemView.setTag(position);

        //아이템을 클릭했을 시 해당 URL로 이동
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //각 TextView 안의 데이터를 String 변수 안에 저장
                String url = String.valueOf(holder.TV_url.getText());
                String title = String.valueOf(holder.TV_title.getText());

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); //해당 url로 이동하는 Intent

                //입력한 URL로 이동 (잘못된URL 입력 시 & URL값이 없을 시 해당 Toast출력)
                //Adapter는 Activity가 아니라서 startActivity, startActivityForResult를 사용할 수 없기 때문에
                //Context라는 Activity 변수를 통하여 메소드를 받아와서 사용
                try {
                    if(url.equals("https://")) {
                        Toast.makeText(v.getContext(), "잘못된 URL", Toast.LENGTH_SHORT).show();
                    }else {
                        v.getContext().startActivity(intent);
                        Toast.makeText(v.getContext(), title + "로 이동", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), "잘못된 URL", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //아이템을 롱클릭했을시 수정, 제거 Dialog 띄우는 부분
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //dialog (선언, 아이콘, 제목, 셋팅 메시지)
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setIcon(R.mipmap.ic_launcher); //Dialog icon
                dialog.setTitle("작업을 선택하십시오."); //Dialog title

                //롱클릭 Dialog안에 북마크를 클릭했을 시 북마크 설정이 가능한 Dialog를 다시 띄움
                dialog.setNeutralButton("북마크", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentVideoPosition = holder.getAdapterPosition(); //리스트의 위치번호

                        showDialog(v.getContext()); //북마크 추가하는 다이얼로그창 호출 메소드
                    }
                });

                //Dialog의 수정버튼
                dialog.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(v.getContext(), Info_write_Activity.class); //작성페이지로 연결된 Intent
                        currentVideoPosition = holder.getAdapterPosition(); // 수정시 아이템의 위치값을 알아야 하기 때문에 static 변수에 저장

                        //Bitmap의 Uri를 얻어와서 변수에 저장(이미지 수정클릭시 가지고있던 데이터를 다시 보여주기위해)
                        if(arrayList.get(currentVideoPosition).getIV_thumbnail() != null) {
                            Uri BitmapUri = getImageUri(v.getContext(), (arrayList.get(currentVideoPosition).getIV_thumbnail()));
                            intent.putExtra("imageBitmap", String.valueOf(BitmapUri));
                        }
                        intent.putExtra("url", String.valueOf(arrayList.get(currentVideoPosition).getTV_url()));
                        intent.putExtra("title", String.valueOf(arrayList.get(currentVideoPosition).getTV_title()));
                        intent.putExtra("contents", String.valueOf(arrayList.get(currentVideoPosition).getTV_contents()));

                        //Adapter는 Activity가 아니라서 startActivity, startActivityForResult를 사용할 수 없기 때문에
                        //Context라는 Activity 변수를 통하여 메소드를 받아와서 사용
                        ((Activity) object).startActivityForResult(intent, REQUEST_CORRECTION);

                        dialog.dismiss(); //dialog 닫기
                    }
                });

                //Dialog의 제거버튼
                dialog.setPositiveButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(holder.getAdapterPosition());//해당 아이템의 위치를 받아서 그 아이템을 제거
                        dialog.dismiss(); //dialog 닫기
                    }
                });

                dialog.show(); //dialog창을 띄운다.

                return true;
            }
        });
    }

//==================================================================================================

    //item의 총 갯수 리턴 (null이라면 리턴값: 0)
    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    //리스트에서 아이템을 제거하는 메소드
    private void remove(int position){
        try {
            arrayList.remove(position); //아이템에 해당하는 리스트를 제거
            notifyItemRemoved(position); //아이템을 제거(후에 Remove가 적용됨)
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Bitmap에서 Uri를 얻어오는 메소드
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    //아이템을 수정하는 메소드
    //들어가는 변수:
    //position: 해당 아이템의 위치,  image: 아이템의 사진, title: 아이템의 제목, contents: 아이템의 내용, url: 아이템의 url
    void change(int position, Bitmap image, String title, String contents, String url) {
        try{
            Video_Item item = new Video_Item(image, title, contents, url); //아이템의 구조 생성
            arrayList.set(position, item); //해당 아이템의 데이터를 다시 셋팅
            notifyDataSetChanged(); //바뀐 데이터의 값으로 아이템을 다시 셋팅
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

         ImageView IV_thumbnail;
         TextView TV_title, TV_contents, TV_url;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.IV_thumbnail=  itemView.findViewById(R.id.IV_thumbnail);
            this.TV_title    =  itemView.findViewById(R.id.TV_title);
            this.TV_contents =  itemView.findViewById(R.id.TV_contents);
            this.TV_url      =  itemView.findViewById(R.id.TV_url);
        }
    }

//==================================================================================================

    //커스텀 다이얼로그 호출 메소드
    private void showDialog(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);

        //뷰 선언
        TextView bookmarNum1 = dialog.findViewById(R.id.bookmarkNum1);
        TextView bookmarNum2 = dialog.findViewById(R.id.bookmarkNum2);
        TextView bookmarNum3 = dialog.findViewById(R.id.bookmarkNum3);
        TextView bookmarNum4 = dialog.findViewById(R.id.bookmarkNum4);
        TextView bookmarNum5 = dialog.findViewById(R.id.bookmarkNum5);
        TextView bookmarNum6 = dialog.findViewById(R.id.bookmarkNum6);
        TextView TV_cancel = dialog.findViewById(R.id.TV_cancel);

        //1번 북마크 클릭리스너
        bookmarNum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(v, 1);
                dialog.dismiss();
            }
        });

        //2번 북마크 클릭리스너
        bookmarNum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(v, 2);
                dialog.dismiss();
            }
        });

        //3번 북마크 클릭리스너
        bookmarNum3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(v, 3);
                dialog.dismiss();
            }
        });

        //4번 북마크 클릭리스너
        bookmarNum4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(v, 4);
                dialog.dismiss();
            }
        });

        //5번 북마크 클릭리스너
        bookmarNum5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(v, 5);
                dialog.dismiss();
            }
        });

        //6번 북마크 클릭리스너
        bookmarNum6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(v, 6);
                dialog.dismiss();
            }
        });

        //취소버튼
        TV_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "취소", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

//==================================================================================================

    private void addBookmark(View v, int number) {
        //SharedPreferences선언
        SharedPreferences SP_bookmark = v.getContext().getSharedPreferences(SP_data, 0);
        SharedPreferences.Editor editor = SP_bookmark.edit();

        //북마크데이터 (이미지, 제목, url)저장
        Uri saveImageUri = SingleTon.getImageUri(v.getContext(), arrayList.get(currentVideoPosition).getIV_thumbnail());
        String image = String.valueOf(saveImageUri);
        String title = String.valueOf(arrayList.get(currentVideoPosition).getTV_title());
        String url = String.valueOf(arrayList.get(currentVideoPosition).getTV_url());
        String bookmarkData = image + "," + title + "," + url;

        editor.putString("bookmarkNum"+number, bookmarkData);
        editor.apply();

        Toast.makeText(v.getContext(), number+"번 북마크에 추가완료.", Toast.LENGTH_SHORT).show();
    }

//==================================================================================================
}
