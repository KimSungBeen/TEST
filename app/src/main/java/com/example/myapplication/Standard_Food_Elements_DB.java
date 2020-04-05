package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.List;

public class Standard_Food_Elements_DB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard__food_elements__d_b);

        String key = "key";

        /**
         * 식품군 코드
         * A:곡류및그제품        B:감자류및전분류  C:당류          D:두류            E:견과류및종실류
         * F:채소류             G:버섯류         H:과일류         I:육류           J:난류
         * K:어패류및기타수산물  L:해조류         M:우유및유제품류  N:유지류          O:차류
         * P:음료류             Q:주류           R:조미료류       S:조리가공식품류  T:기타
         * */
        String foodGrupp = "";

        String foodName = "";

        String url = "http://koreanfood.rda.go.kr/kfi/openapi/service?apiKey=" + key + "&serviceType=AA002&" +
                "nowPage=1&pageSize=500&fdGrupp=" + foodGrupp + "&fdNm=" + foodName;

        WheelView wheelView = findViewById(R.id.wheelView);

        wheelView.setCyclic(false);

        final List<String> mOptionsItems = new ArrayList<>();
        mOptionsItems.add("곡류및그제품");
        mOptionsItems.add("감자류및전분류");
        mOptionsItems.add("당류");

        wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(Standard_Food_Elements_DB.this, "" + mOptionsItems.get(index), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
