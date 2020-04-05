package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 식품군 코드
 * A:곡류및그제품        B:감자류및전분류  C:당류          D:두류            E:견과류및종실류
 * F:채소류             G:버섯류         H:과일류         I:육류           J:난류
 * K:어패류및기타수산물  L:해조류         M:우유및유제품류  N:유지류          O:차류
 * P:음료류             Q:주류           R:조미료류       S:조리가공식품류  T:기타
 * */

public class Standard_Food_Elements_DB extends AppCompatActivity {

    //API URL의 요청변수
    String foodGrupp = "";
    String foodName = "";
    String key = "201908301119450BKQK9MA9D35NFBWCI";
    String url = "";

    Button BTN_exit;
    WheelView wheelView;
    EditText ET_search;
    LottieAnimationView LA_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard__food_elements__d_b);

        wheelView = findViewById(R.id.wheelView);
        BTN_exit = findViewById(R.id.BTN_exit);
        ET_search = findViewById(R.id.ET_search);
        LA_search = findViewById(R.id.LA_search);

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

//==================================================================================================

        wheelView.setCyclic(false);

        final List<String> mOptionsItems = new ArrayList<>();
        mOptionsItems.add("곡류및그제품");
        mOptionsItems.add("감자류및전분류");
        mOptionsItems.add("당류");
        mOptionsItems.add("두류");
        mOptionsItems.add("견과류및종실류");
        mOptionsItems.add("채소류");
        mOptionsItems.add("버섯류");
        mOptionsItems.add("과일류");
        mOptionsItems.add("육류");
        mOptionsItems.add("난류");
        mOptionsItems.add("어패류및기타수산물");
        mOptionsItems.add("해조류");
        mOptionsItems.add("우유및유제품류");
        mOptionsItems.add("유지류");
        mOptionsItems.add("차류");
        mOptionsItems.add("음료류");
        mOptionsItems.add("주류");
        mOptionsItems.add("조미료류");
        mOptionsItems.add("조리가공식품류");
        mOptionsItems.add("기타");

        wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(Standard_Food_Elements_DB.this, "" + mOptionsItems.get(index), Toast.LENGTH_SHORT).show();

                //wheelView에 따른 KEY 요청변수 foodGrupp을 초기화
                switch (mOptionsItems.get(index)) {
                    case "곡류및그제품":
                        foodGrupp = "A";
                        break;
                    case "감자류및전분류":
                        foodGrupp = "B";
                        break;
                    case "당류":
                        foodGrupp = "C";
                        break;
                    case "두류":
                        foodGrupp = "D";
                        break;
                    case "견과류및종실류":
                        foodGrupp = "E";
                        break;
                    case "채소류":
                        foodGrupp = "F";
                        break;
                    case "버섯류":
                        foodGrupp = "G";
                        break;
                    case "과일류":
                        foodGrupp = "H";
                        break;
                    case "육류":
                        foodGrupp = "I";
                        break;
                    case "난류":
                        foodGrupp = "J";
                        break;
                    case "어패류및기타수산물":
                        foodGrupp = "K";
                        break;
                    case "해조류":
                        foodGrupp = "L";
                        break;
                    case "우유및유제품류":
                        foodGrupp = "M";
                        break;
                    case "유지류":
                        foodGrupp = "N";
                        break;
                    case "차류":
                        foodGrupp = "O";
                        break;
                    case "음료류":
                        foodGrupp = "P";
                        break;
                    case "주류":
                        foodGrupp = "Q";
                        break;
                    case "조미료류":
                        foodGrupp = "R";
                        break;
                    case "조리가공식품류":
                        foodGrupp = "S";
                        break;
                    case "기타":
                        foodGrupp = "T";
                        break;
                }
            }
        });

        LA_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodName = ET_search.getText().toString();

                if(foodName.equals("")) {
                    Toast.makeText(Standard_Food_Elements_DB.this, "검색어를 입력하십시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //OPEN API URL
        url = "http://koreanfood.rda.go.kr/kfi/openapi/service?apiKey=" + key + "&serviceType=AA002&" +
                "nowPage=1&pageSize=500&fdGrupp=" + foodGrupp + "&fdNm=" + foodName;
    }

//==================================================================================================
}
