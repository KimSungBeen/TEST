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

    private void parse() {
        String key = "key";
        String url = "http://openapi.seoul.go.kr:8088/" + key + "/json/totalPhysicalTrainInfo/1/5/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("totalPhysicalTrainInfo");
                    JSONArray jsonArray = jsonObject.getJSONArray("row");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject gym = jsonArray.getJSONObject(i);
                        String name = gym.getString("NM");
                        String address = gym.getString("ADDR");
                        String state = gym.getString("STATE");
                        String tel = gym.getString("TEL");

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
        queue.add(jsonObjectRequest);
    }

}
