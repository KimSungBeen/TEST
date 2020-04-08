package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.PublicSptCenter_Info_Adapter.publicSptCenterArrayList;

public class PublicSptCenter_Info_Activity extends AppCompatActivity {

    /**
     *  Volley: 네트워크 요청을 관리하는 Android 용 네트워킹 라이브러리
     *          RequestQueue를 통해 자동으로 비동기 처리를 해줌 = 쓰레드, AsyncTask 사용할 필요가 X
     *
     * 네트워크 요청의 자동 예약.
     * 여러 개의 동시 네트워크 연결.
     * 캐싱.
     * 우선 순위 요청.
     * 진행중인 API 요청 취소
     * */

    TextView TV_publicSptCenterInfoNotice;
    Button BTN_exit;
    RequestQueue queue;
    RecyclerView RV_publicSptCenterInfo;
    PublicSptCenter_Info_Adapter publicSptCenter_info_adapter;
    LinearLayoutManager linearLayoutManager;
    LottieAnimationView LA_publicSptCenterGuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicsptcenter_info_);

        TV_publicSptCenterInfoNotice = findViewById(R.id.TV_publicSptCenterInfoNotice); //알림용 텍스트뷰
        LA_publicSptCenterGuy = findViewById(R.id.LA_publicSptCenterGuy);
        BTN_exit     = findViewById(R.id.BTN_exit);

    //리싸이클러뷰
        RV_publicSptCenterInfo   = findViewById(R.id.RV_publicSptCenterInfo);

        //리싸이클러뷰내의 레이아웃을 컨트롤할 수 있게 해줌
        linearLayoutManager = new LinearLayoutManager(this);
        RV_publicSptCenterInfo.setLayoutManager(linearLayoutManager);

        //아이템의 데이터가 저장될 ArrayList 생성
        publicSptCenterArrayList = new ArrayList<>();

        // publicSptCenterArrayList에 있는 값을 publicSptCenter_info_adapter에 저장
        //리사이클러뷰에 publicSptCenter_info_adapter 안의 값을 셋팅
        publicSptCenter_info_adapter = new PublicSptCenter_Info_Adapter(publicSptCenterArrayList, this);
        RV_publicSptCenterInfo.setAdapter(publicSptCenter_info_adapter);

        //볼리 Request 큐 생성
        queue = Volley.newRequestQueue(this);
        parse(); //데이터 parsing 메소드 호출
    }

    @Override
    protected void onResume() {
        super.onResume();

        //나가기 버튼
        BTN_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Home_Activity.class);
                startActivity(intent);
            }
        });

    }

//==================================================================================================

    //공공데이터 JSON 파일에서 데이터를 Parsing 하는 메소드
    //공공체육시설
    private void parse() {
        String key = Key_API.PublicSptCenterKey;
        String url = "http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicSptCenter2019/1/751/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("TbPublicSptCenter2019");
                    JSONArray jsonArray = jsonObject.getJSONArray("row");

                    //배열의 길이만큼 반복해서 데이터 출력
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject center = jsonArray.getJSONObject(i);
                        String name = center.getString("NM");
                        String address = center.getString("ADDR");
                        String in_out = center.getString("IN_OUT");
                        String tel = "02-" + center.getString("TEL");
                        String YCODE = center.getString("YCODE");
                        String XCODE = center.getString("XCODE");

                        //출력할 데이터 조합
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("시설명: ").append(name).append("\n").
                                append("주소: ").append(address).append("\n").
                                append("실내/실외: ").append(in_out);

                        //가져온 데이터 리스트에 저장
                        PublicSptCenter_Info_Item item = new PublicSptCenter_Info_Item(String.valueOf(stringBuilder),
                                "전화번호: " + tel, YCODE, XCODE);
                        publicSptCenterArrayList.add(item);
                    }
                    publicSptCenter_info_adapter.notifyDataSetChanged(); //리스트에 저장한 데이터 띄우기(새로고침)

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TV_publicSptCenterInfoNotice.setText("인터넷 연결상태를 확인해 주십시오.");
            }
        });

        //대기열에 데이터를 Parsing 하는 요청을 넣음
        queue.add(jsonObjectRequest);
    }

//==================================================================================================

}
