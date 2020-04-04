package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Gym_List_Activity extends AppCompatActivity {

    /**
     *  Volley: 네트워크 요청을 관리하는 Android 용 네트워킹 라이브러리
     *          RequestQueue를 통해 자동으로 비동기 처리를 해줌
     *
     * 네트워크 요청의 자동 예약.
     * 여러 개의 동시 네트워크 연결.
     * 캐싱.
     * 우선 순위 요청.
     * 진행중인 API 요청 취소
     * */

    TextView TV_gymList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym__list_);

        TV_gymList = findViewById(R.id.TV_gymList);

        queue = Volley.newRequestQueue(this);
        parse();
    }

    //공공데이터 JSON 파일에서 데이터를 Parsing 하는 메소드
    private void parse() {
        String key = "616e4e545970737636334349416466";
        String url = "http://openapi.seoul.go.kr:8088/" + key + "/json/totalPhysicalTrainInfo/1/5/";

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
                                append("운영상태: ").append(state).append("\n").
                                append("전화번호: ").append(tel);

                        TV_gymList.setText(stringBuilder);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, Throwable::printStackTrace);

        //대기열에 데이터를 Parsing 하는 요청을 넣음
        queue.add(jsonObjectRequest);
    }

}
