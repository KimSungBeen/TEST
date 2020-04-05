package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

        //아이템 데이터를 셋팅하는 부분
        holder.TV_gymInfoList.setText(gymArrayList.get(position).getTV_gymInfoList());
        holder.TV_gymCallNumber.setText(gymArrayList.get(position).getTV_gymCallNumber());

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("작업을 선택 하십시오.");

                dialog.setPositiveButton("전화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String callNumber = "tel:" + gymArrayList.get(position).getTV_gymCallNumber();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(callNumber));
                        v.getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    //item의 총 갯수 리턴 (null이라면 리턴값: 0)
    @Override
    public int getItemCount() {
        return (null != gymArrayList ? gymArrayList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TV_gymInfoList;
        TextView TV_gymCallNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.TV_gymInfoList = itemView.findViewById(R.id.TV_gymInfoList);
            this.TV_gymCallNumber = itemView.findViewById(R.id.TV_gymCallNumber);
        }
    }
}
