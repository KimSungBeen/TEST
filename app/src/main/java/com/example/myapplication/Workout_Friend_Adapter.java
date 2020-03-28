package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Workout_Friend_Adapter extends BaseAdapter {

    static ArrayList<Workout_Friend_Item> contacts = new ArrayList<>();

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext(); // 뷰그룹에 고유한 값 저장
        if(convertView == null){ // 뷰에 아무것도 없으면
            LayoutInflater list = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //리스트뷰에 아이템을 합치는 명령
            convertView = list.inflate(R.layout.item_workout_friend, parent, false); // 리스트에서 보이는 뷰에 생성한 아이템 합친것 적용
        }

        //뷰 선언, 초기화
        TextView TV_name = convertView.findViewById(R.id.TV_name);
        TextView TV_number = convertView.findViewById(R.id.TV_number);

        //ArrayList에 있는 아이템의 정보들 저장
        Workout_Friend_Item contactItem = contacts.get(position);

        //아이템의 텍스트 셋팅
        TV_name.setText(contactItem.getTV_name());
        TV_number.setText(contactItem.getTV_number());


        return convertView;
    }

    //아이템을 생성하는 메소드
    public void addItem(String name, String number) {
        Workout_Friend_Item workout_friend_item = new Workout_Friend_Item();

        //item에서 선언된 변수에 내용을 저장시킴
        workout_friend_item.setTV_name(name);
        workout_friend_item.setTV_number(number);

        contacts.add(workout_friend_item); //아이템 ArrayList에 값을 넣어줌
        notifyDataSetChanged();
    }

}
