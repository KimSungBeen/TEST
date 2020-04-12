package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

public class Naver_Map extends AppCompatActivity implements OnMapReadyCallback {
    Button BTN_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver__map);

        BTN_exit = findViewById(R.id.BTN_exit);

        //네이버 지도를 화면에 띄움
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //나가기버튼
        BTN_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        //마커의 좌표값 설정
        Bundle bundle = getIntent().getExtras();
        double YCODE = Double.parseDouble(bundle.getString("YCODE"));
        double XCODE = Double.parseDouble(bundle.getString("XCODE"));
        LatLng latLng = new LatLng(YCODE, XCODE);

        //마커생성
        Marker marker = new Marker();
        marker.setPosition(latLng);

        //마커를 클릭했을때 이벤트 설정
        marker.setOnClickListener(overlay -> {
            Toast.makeText(Naver_Map.this, "마커 클릭됨", Toast.LENGTH_SHORT).show();
            return true;
        });

        //마커와 네이버지도 매칭
        marker.setMap(naverMap);
        naverMap.setCameraPosition(new CameraPosition(latLng, 17));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
