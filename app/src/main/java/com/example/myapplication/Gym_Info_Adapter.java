package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Gym_Info_Adapter extends RecyclerView.Adapter<Gym_Info_Adapter.MyViewHolder> {

    static ArrayList<Gym_Info_Item> gymArrayList;

    Gym_Info_Adapter(ArrayList<Gym_Info_Item> gymArrayList) {
        Gym_Info_Adapter.gymArrayList = gymArrayList;
    }

    //viewHolder객체를 생성하는 구간
    @NonNull
    @Override
    public Gym_Info_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //레이아웃과 아이템 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym_info, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    //ViewHolder에 데이터를 넣는 작업을 수행함
    @Override
    public void onBindViewHolder(@NonNull Gym_Info_Adapter.MyViewHolder holder, int position) {
        holder.TV_gymInfoList.setText(gymArrayList.get(position).getTV_gymInfoList());

        holder.itemView.setTag(position);

    }

    //item의 총 갯수 리턴 (null이라면 리턴값: 0)
    @Override
    public int getItemCount() {
        return (null != gymArrayList ? gymArrayList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TV_gymInfoList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.TV_gymInfoList = itemView.findViewById(R.id.TV_gymInfoList);
        }
    }
}
