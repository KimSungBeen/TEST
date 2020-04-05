package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Food_Elements_Adapter extends RecyclerView.Adapter<Food_Elements_Adapter.MyViewHolder> {

    static ArrayList<Food_Elements_Item> foodArrayList;

    Food_Elements_Adapter(ArrayList<Food_Elements_Item> foodArrayList) {
        Food_Elements_Adapter.foodArrayList = foodArrayList;
    }

    //viewHolder객체를 생성하는 구간
    @NonNull
    @Override
    public Food_Elements_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //레이아웃과 아이템 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_elements, parent, false);
        Food_Elements_Adapter.MyViewHolder holder = new Food_Elements_Adapter.MyViewHolder(view);

        return holder;
    }

    //ViewHolder에 데이터를 넣는 작업을 수행함
    @Override
    public void onBindViewHolder(@NonNull Food_Elements_Adapter.MyViewHolder holder, int position) {
        //아이템 데이터를 셋팅하는 부분
        holder.TV_foodElements.setText(foodArrayList.get(position).getTV_foodElements());

        holder.itemView.setTag(position);
    }

    //item의 총 갯수 리턴 (null이라면 리턴값: 0)
    @Override
    public int getItemCount() {
        return (null != foodArrayList ? foodArrayList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TV_foodElements;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.TV_foodElements = itemView.findViewById(R.id.TV_foodElements);
        }
    }
}
