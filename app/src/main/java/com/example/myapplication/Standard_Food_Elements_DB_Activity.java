package com.example.myapplication;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.Food_Elements_Adapter.foodArrayList;

/**
 * 식품군 코드
 * A:곡류및그제품        B:감자류및전분류  C:당류          D:두류            E:견과류및종실류
 * F:채소류             G:버섯류         H:과일류         I:육류           J:난류
 * K:어패류및기타수산물  L:해조류         M:우유및유제품류  N:유지류          O:차류
 * P:음료류             Q:주류           R:조미료류       S:조리가공식품류  T:기타
 * */

public class Standard_Food_Elements_DB_Activity extends AppCompatActivity {

    //API URL의 요청변수
    String foodGrupp = "";
    String foodName = "";
    String key = Key_API.FoodElementsKey;
    String url = "";
    boolean isSearch;

    Button BTN_exit, BTN_resultZoomIn,BTN_resultZoomOut;
    WheelView wheelView;
    TextView TV_foodElementsNotice, TV_manufacturedGoodsSearch, TV_manufacturedGoodsChoice;
    EditText ET_search;
    LottieAnimationView LA_search, LA_loading;
    RecyclerView RV_foodElements;
    Food_Elements_Adapter food_elements_adapter;
    LinearLayoutManager linearLayoutManager;
    View view, view2, view3, view4;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard__food_elements__d_b);

        //뷰 선언
        wheelView = findViewById(R.id.wheelView);
        BTN_exit = findViewById(R.id.BTN_exit);
        BTN_resultZoomIn = findViewById(R.id.BTN_resultZoomIn);
        BTN_resultZoomOut = findViewById(R.id.BTN_resultZoomOut);
        ET_search = findViewById(R.id.ET_search);
        LA_search = findViewById(R.id.LA_search);
        LA_loading = findViewById(R.id.LA_loading);
        TV_foodElementsNotice = findViewById(R.id.TV_foodElementsNotice);
        TV_manufacturedGoodsSearch = findViewById(R.id.TV_manufacturedGoodsSearch);
        TV_manufacturedGoodsChoice = findViewById(R.id.TV_manufacturedGoodsChoice);
        RV_foodElements = findViewById(R.id.RV_foodElements);
        view = findViewById(R.id.view);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);

    //리싸이클러뷰
        RV_foodElements   = findViewById(R.id.RV_foodElements);

        //리싸이클러뷰내의 레이아웃을 컨트롤할 수 있게 해줌
        linearLayoutManager = new LinearLayoutManager(this);
        RV_foodElements.setLayoutManager(linearLayoutManager);

        //아이템의 데이터가 저장될 foodArrayList 생성
        foodArrayList = new ArrayList<>();

        // foodArrayList에 있는 값을 food_elements_adapter에 저장
        //리사이클러뷰에 food_elements_adapter 안의 값을 셋팅
        food_elements_adapter = new Food_Elements_Adapter(foodArrayList);
        RV_foodElements.setAdapter(food_elements_adapter);

        //볼리 Request 큐 생성
        queue = Volley.newRequestQueue(this);
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

        wheelView.setCyclic(true);

        //제품군 리스트
        final List<String> optionsItems = new ArrayList<>();
        optionsItems.add("난류");
        optionsItems.add("어패류및기타수산물");
        optionsItems.add("해조류");
        optionsItems.add("우유및유제품류");
        optionsItems.add("유지류");
        optionsItems.add("차류");
        optionsItems.add("음료류");
        optionsItems.add("주류");
        optionsItems.add("조미료류");
        optionsItems.add("조리가공식품류");
        optionsItems.add("기타");
        optionsItems.add("전체검색");
        optionsItems.add("곡류및그제품");
        optionsItems.add("감자류및전분류");
        optionsItems.add("당류");
        optionsItems.add("두류");
        optionsItems.add("견과류및종실류");
        optionsItems.add("채소류");
        optionsItems.add("버섯류");
        optionsItems.add("과일류");
        optionsItems.add("육류");

        wheelView.setAdapter(new ArrayWheelAdapter<>(optionsItems));

        //제품군을 선택하는 휠뷰 선택리스너
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(Standard_Food_Elements_DB_Activity.this, "" + optionsItems.get(index), Toast.LENGTH_SHORT).show();

                //wheelView에 따른 KEY 요청변수 foodGrupp을 초기화
                switch (optionsItems.get(index)) {
                    case "전체검색":
                        foodGrupp = "";
                        break;
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

        //제품명 검색
        LA_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodName = ET_search.getText().toString();

                //OPEN API URL
                url = "http://koreanfood.rda.go.kr/kfi/openapi/service?apiKey=" + key + "&serviceType=AA002&" +
                        "nowPage=1&pageSize=500&fdGrupp=" + foodGrupp + "&fdNm=" + foodName;

//                Log.i("ooo", url);

                if(foodName.equals("")) {
                    Toast.makeText(Standard_Food_Elements_DB_Activity.this, "검색어를 입력하십시오.", Toast.LENGTH_SHORT).show();
                }else {
                    parse();

                    //로딩 애니메이션
                    Handler handler = new Handler();
                    handler.post(new Thread() {
                        @Override
                        public void run() {
                            loadingCustomAnimators(0.3f, 0.57f, 900);
                            if (isSearch) {
                                handler.postDelayed(this, 950);
                            } else {
                                loadingCustomAnimators(0.57f, 1f, 1000);
                            }
                        }
                    });

                }
            }
        });

        //결과 크게보기
        BTN_resultZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_manufacturedGoodsSearch.setVisibility(View.GONE);
                ET_search.setVisibility(View.GONE);
                LA_search.setVisibility(View.GONE);
                TV_manufacturedGoodsChoice.setVisibility(View.GONE);
                wheelView.setVisibility(View.GONE);
                BTN_resultZoomIn.setVisibility(View.GONE);
                TV_foodElementsNotice.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                BTN_resultZoomOut.setVisibility(View.VISIBLE);
            }
        });

        //결과 작게보기
        BTN_resultZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_manufacturedGoodsSearch.setVisibility(View.VISIBLE);
                ET_search.setVisibility(View.VISIBLE);
                LA_search.setVisibility(View.VISIBLE);
                TV_manufacturedGoodsChoice.setVisibility(View.VISIBLE);
                wheelView.setVisibility(View.VISIBLE);
                BTN_resultZoomIn.setVisibility(View.VISIBLE);
                TV_foodElementsNotice.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                BTN_resultZoomOut.setVisibility(View.GONE);
            }
        });

    }

//==================================================================================================

    //공공데이터 JSON 파일에서 데이터를 Parsing 하는 메소드
    private void parse() {
        isSearch = true; //검색중
        foodArrayList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                StringBuilder stringBuilder;

                try {
                    JSONObject jsonObject = response.getJSONObject("service");
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    //배열의 길이만큼 반복해서 데이터 출력
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject food = jsonArray.getJSONObject(i);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("명칭: ").append(food.getString("fdNm")).append("\n");

                        //elements:전체구성성분
                        JSONArray elements = food.getJSONArray("irdnt");
                        for (int j = 0; j < elements.length(); j++) {

                            //elementsKind: 구성성분 종류
                            JSONObject elementsKind = elements.getJSONObject(j);

                            //irdntSeNm: 성분구분명
                            stringBuilder.append("[").append(elementsKind.getString("irdntSeNm")).append("]\n");

                            //irdnttcket: 상세성분
                            JSONArray irdnttcket = elementsKind.getJSONArray("irdnttcket");
                            for (int k = 0; k < irdnttcket.length(); k++) {

                                //irdnttcketKind:상세성분 종류
                                JSONObject irdnttcketKind = irdnttcket.getJSONObject(k);

                                //irdntNm: 상세성분 이름
                                stringBuilder.append(irdnttcketKind.getString("irdntNm")).append(": ").
                                            append(irdnttcketKind.getString("contInfo")).append(irdnttcketKind.getString("irdntUnitNm")).append("\n");

                            }
                        }
                        //가져온 데이터 리스트에 저장
                        Food_Elements_Item item = new Food_Elements_Item(String.valueOf(stringBuilder));
                        foodArrayList.add(item);
                    }
                    isSearch = false; //검색완료
                    food_elements_adapter.notifyDataSetChanged(); //리스트에 저장한 데이터 띄우기(새로고침)

                } catch (JSONException e) {
                    isSearch = false; //검색완료
                    e.printStackTrace();
                    Toast.makeText(Standard_Food_Elements_DB_Activity.this, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TV_foodElementsNotice.setText("인터넷 연결상태를 확인해 주십시오.");
            }
        });

        //대기열에 데이터를 Parsing 하는 요청을 넣음
        queue.add(jsonObjectRequest);
    }

//==================================================================================================

    //loading 로티애니메이션 커스텀메소드
    private void loadingCustomAnimators(float startPosition, float finishPosition, int duration) {

        //애니메이션의 진행위치, 종료위치, 진행속도를 설정
        ValueAnimator animator = ValueAnimator.ofFloat(startPosition, finishPosition).setDuration(duration);
        animator.addUpdateListener(animation -> {
            LA_loading.setProgress((Float) animation.getAnimatedValue());
        });
        animator.start();
    }

//==================================================================================================
}
