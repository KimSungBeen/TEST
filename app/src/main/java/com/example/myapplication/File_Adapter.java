package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class File_Adapter extends BaseAdapter {

    static ArrayList<File_Item> fileItems = new ArrayList<>();

    //item의 갯수
    @Override
    public int getCount() {
        return fileItems.size();
    }

    //item을 전송
    @Override
    public Object getItem(int position) {
        return fileItems.get(position);
    }

    //전송할 item의 위치
    @Override
    public long getItemId(int position) {
        return position;
    }

    //뷰를
    //convertView: 전환되는뷰, parent: 뷰그룹 자체
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("File Adapter", "Create [Position: " + position + "]");
        Context context = parent.getContext(); // 뷰그룹에 고유한 값 저장
        if(convertView == null){ // 뷰에 아무것도 없으면
            LayoutInflater list = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //리스트뷰에 아이템을 합치는 명령
            convertView = list.inflate(R.layout.item_layout, parent, false); // 리스트에서 보이는 뷰에 생성한 아이템 합친것 적용
        }
        //뷰 선언, 초기화
        ImageView IV_thumbnail = convertView.findViewById(R.id.IV_thumbnail);
        TextView TV_title = convertView.findViewById(R.id.TV_title);
        TextView TV_contents = convertView.findViewById(R.id.TV_contents);
        TextView TV_url = convertView.findViewById(R.id.TV_url);

        //ArrayList에 있는 아이템의 정보들 저장
        File_Item fileItem = fileItems.get(position);

        //아이템의 이미지, 텍스트 셋팅
        IV_thumbnail.setImageBitmap(fileItem.getIV_thumbnail());
        TV_title.setText(fileItem.getTV_title());
        TV_contents.setText(fileItem.getTV_contents());
        TV_url.setText(fileItem.getTV_url());

        return convertView;
    }

    //아이템을 생성하는 메소드
    public void addItem(Bitmap thumbnail, String title, String contents, String url) {
        File_Item fileItem = new File_Item();

        //item에서 선언된 변수에 이미지, 제목, 내용을 저장시킴
        fileItem.setIV_thumbnail(thumbnail);
        fileItem.setTV_title(title);
        fileItem.setTV_contents(contents);
        fileItem.setTV_url(url);

        fileItems.add(fileItem); //아이템 ArrayList에 값을 넣어줌
    }

    //아이템을 수정하는 메소드
    public void changeItem(int position, Bitmap thumbnail, String title, String contents, String url) {
        File_Item fileItem = new File_Item();

        fileItem.setIV_thumbnail(thumbnail);
        fileItem.setTV_title(title);
        fileItem.setTV_contents(contents);
        fileItem.setTV_url(url);

        fileItems.set(position, fileItem);
        notifyDataSetChanged();
    }
}
