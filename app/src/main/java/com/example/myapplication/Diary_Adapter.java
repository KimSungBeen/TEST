package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Diary_Adapter extends RecyclerView.Adapter<Diary_Adapter.MyViewHolder> {

    private ArrayList<Diary_Item> arrayList; //아이템의 데이터가 들어갈 ArrayList
    static final int REQUEST_CORRECTION = 4; //수정하기의 리퀘스트코드

    private Object Context; //startActivityForResult 사용을 위해 ViewGroup을 저장할 변수

    static int currentDiaryPosition; // 수정시 아이템의 위치값을 알아야 하기 때문에 static 변수에 저장
    static String currentDate; //수정시 저장될 날짜

    Diary_Adapter(ArrayList<Diary_Item> arrayList) {
        this.arrayList = arrayList;
    }

    //RecyclerView가 새로운 아이템을 만들 때 마다 생성되는
    // viewHolder객체와 레이아웃을 연결해주는 구간
    @NonNull
    @Override
    public Diary_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Diary Adapter", "onCreateViewHolder");

        //레이아웃과 아이템 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        Context = parent.getContext();

        return holder;
    }

    //각 뷰가 보여질때 호출되는 구간
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("Diary Adapter", "onBindViewHolder [Position: " + position + "]");

        //아이템 데이터를 셋팅하는 부분
        holder.IV_thumbnail.setImageBitmap(arrayList.get(position).getIV_thumbnail());
        holder.TV_url.setText(arrayList.get(position).getTV_url());
        holder.TV_title.setText(arrayList.get(position).getTV_title());
        holder.TV_contents.setText(arrayList.get(position).getTV_contents());

        holder.itemView.setTag(position);

        //아이템을 클릭했을 시 해당 URL로 이동
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //각 TextView 안의 데이터를 String 변수 안에 저장
//                String url = String.valueOf(holder.TV_url.getText());
//                String title = String.valueOf(holder.TV_title.getText());
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); //해당 url로 이동하는 Intent
//
//                //입력한 URL로 이동 (잘못된URL 입력 시 & URL값이 없을 시 해당 Toast출력)
//                //Adapter는 Activity가 아니라서 startActivity, startActivityForResult를 사용할 수 없기 때문에
//                //Context라는 Activity 변수를 통하여 메소드를 받아와서 사용
//                try {
//                    v.getContext().startActivity(intent);
//                    Toast.makeText(v.getContext(), title + "로 이동", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Toast.makeText(v.getContext(), "잘못된 URL", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        //아이템을 롱클릭했을시 수정, 제거 Dialog 띄우는 부분
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //dialog (선언, 아이콘, 제목, 셋팅 메시지)
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setIcon(R.mipmap.ic_launcher); //Dialog icon
                dialog.setTitle(""); //Dialog title
                dialog.setMessage("작업을 선택하십시오."); //Dialog Message

                //Dialog의 수정버튼
                dialog.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(v.getContext(), Diary_write_Activity.class); //작성페이지로 연결된 Intent
                        currentDiaryPosition = holder.getAdapterPosition(); //수정시 아이템의 위치값을 알아야 하기 때문에 static 변수에 저장
                        currentDate = String.valueOf(holder.TV_title.getText()); //아이템의 현재 날짜
                        boolean isCorrection = true;

                        //Bitmap의 Uri를 얻어와서 변수에 저장(이미지 수정클릭시 가지고있던 데이터를 다시 보여주기위해)
                        if(arrayList.get(currentDiaryPosition).getIV_thumbnail() != null) {
                            Uri BitmapUri = SingleTon.getImageUri(v.getContext(), (arrayList.get(currentDiaryPosition).getIV_thumbnail()));
                            intent.putExtra("imageBitmap", String.valueOf(BitmapUri));
                        }

                        intent.putExtra("url", String.valueOf(arrayList.get(currentDiaryPosition).getTV_url()));
                        intent.putExtra("title", String.valueOf(arrayList.get(currentDiaryPosition).getTV_title()));
                        intent.putExtra("contents", String.valueOf(arrayList.get(currentDiaryPosition).getTV_contents()));
                        intent.putExtra("isCorrection", isCorrection);

                        //Adapter는 Activity가 아니라서 startActivity, startActivityForResult를 사용할 수 없기 때문에
                        //Context라는 Activity 변수를 통하여 메소드를 받아와서 사용
                        ((Activity) Context).startActivityForResult(intent, REQUEST_CORRECTION);

                        dialog.dismiss(); //dialog 닫기
                    }
                });

                //Dialog의 제거버튼
                dialog.setPositiveButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(holder.getAdapterPosition()); //해당 아이템의 위치를 받아서 그 아이템을 제거
                        dialog.dismiss(); //dialog 닫기
                    }
                });
                dialog.show(); //dialog창을 띄운다.

                return true;
            }
        });
    }

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
        } catch (Exception ignored){
        }
    }

    //아이템을 수정하는 메소드
    //들어가는 변수:
    //position: 해당 아이템의 위치,  image: 아이템의 사진, title: 아이템의 제목, url: 아이템의 url
    void change(int position, Bitmap image, String title, String contents, String url) {
        try{
            Diary_Item item = new Diary_Item(image, title, contents, url); //아이템의 구조 생성
            arrayList.set(position, item); //해당 아이템의 데이터를 다시 셋팅
            notifyDataSetChanged(); //바뀐 데이터의 값으로 아이템을 다시 셋팅
        } catch (Exception ignored){
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

}
