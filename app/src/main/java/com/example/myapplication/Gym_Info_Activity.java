package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import static com.example.myapplication.Gym_Info_Adapter.gymArrayList;

public class Gym_Info_Activity extends AppCompatActivity {

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

    TextView TV_gymInfoNotice;
    Button BTN_exit;
    RequestQueue queue;
    RecyclerView RV_gymInfo;
    Gym_Info_Adapter gym_info_adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_info_);

        TV_gymInfoNotice = findViewById(R.id.TV_gymInfoNotice); //알림용 텍스트뷰
        BTN_exit     = findViewById(R.id.BTN_exit);

    //리싸이클러뷰
        RV_gymInfo   = findViewById(R.id.RV_gymInfo);

        //리싸이클러뷰내의 레이아웃을 컨트롤할 수 있게 해줌
        linearLayoutManager = new LinearLayoutManager(this);
        RV_gymInfo.setLayoutManager(linearLayoutManager);

        //아이템의 데이터가 저장될 ArrayList 생성
        gymArrayList = new ArrayList<>();

        // gymArrayList에 있는 값을 gym_info_adapter에 저장
        //리사이클러뷰에 gym_info_adapter 안의 값을 셋팅
        gym_info_adapter = new Gym_Info_Adapter(gymArrayList);
        RV_gymInfo.setAdapter(gym_info_adapter);

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
    private void parse() {
        String key = "616e4e545970737636334349416466";
        String url = "http://openapi.seoul.go.kr:8088/" + key + "/json/totalPhysicalTrainInfo/1/99/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("totalPhysicalTrainInfo");
                    JSONArray jsonArray = jsonObject.getJSONArray("row");

                    //배열의 길이만큼 반복해서 데이터 출력
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject gym = jsonArray.getJSONObject(i);
                        String name = gym.getString("NM");
                        String address = gym.getString("ADDR");
                        String state = gym.getString("STATE");
                        String tel = gym.getString("TEL");

                        //출력할 데이터 조합
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("업체명: ").append(name).append("\n").
                                append("주소: ").append(address).append("\n").
                                append("운영상태: ").append(state);

                        //가져온 데이터 리스트에 저장
                        Gym_Info_Item item = new Gym_Info_Item(String.valueOf(stringBuilder),
                                "전화번호: " + tel);
                        gymArrayList.add(item);
                    }
                    gym_info_adapter.notifyDataSetChanged(); //리스트에 저장한 데이터 띄우기(새로고침)

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TV_gymInfoNotice.setText("인터넷 연결상태를 확인해 주십시오.");
            }
        });

        //대기열에 데이터를 Parsing 하는 요청을 넣음
        queue.add(jsonObjectRequest);
    }

//==================================================================================================

}
