package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Calculation_BIM_Activity extends AppCompatActivity {

    public static final int MINIMUM_HEIGHT = 0;
    public static final int MAXIMUM_HEIGHT = 300;
    public static final int MUNIMUM_WEIGHT = 0;
    public static final int MAXIMUM_WEIGHT = 500;
    RadioGroup radioGroup;
    RadioButton RB_man, RB_woman;
    EditText ET_height, ET_weight;
    Button BTN_apply, BTN_exit;
    TextView TV_bmiInfo;
    String sex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation__b_i_m_);

        //뷰 선언
        radioGroup  = findViewById(R.id.radioGroup);
        RB_man      = findViewById(R.id.RB_man);
        RB_woman    = findViewById(R.id.RB_woman);
        ET_height   = findViewById(R.id.ET_height);
        ET_weight   = findViewById(R.id.ET_weight);
        BTN_apply   = findViewById(R.id.BTN_apply);
        TV_bmiInfo  = findViewById(R.id.TV_bmiInfo);
        BTN_exit    = findViewById(R.id.BTN_exit);
    }

//==================================================================================================

    @Override
    protected void onResume() {
        super.onResume();

        BTN_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //라디오 그룹
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RB_man) {
                    sex = String.valueOf(RB_man.getText());
                }else if (checkedId == R.id.RB_woman) {
                    sex = String.valueOf(RB_woman.getText());
                }
            }
        });

        //확인 버튼
        BTN_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double height = Integer.parseInt(String.valueOf(ET_height.getText()));
                    double weight = Integer.parseInt(String.valueOf(ET_weight.getText()));

                if(sex.equals("")) {
                    Toast.makeText(Calculation_BIM_Activity.this, "성별을 체크하세요.", Toast.LENGTH_SHORT).show();

                    // 범위: (0 < 키 < 300)
                }else if(!((height > MINIMUM_HEIGHT) && (height < MAXIMUM_HEIGHT))) {
                    Toast.makeText(Calculation_BIM_Activity.this, "키를 입력하세요.", Toast.LENGTH_SHORT).show();

                    // 범위: (0 < 몸무게 < 300)
                }else if(!((weight > MUNIMUM_WEIGHT) && (weight < MAXIMUM_WEIGHT))) {
                    Toast.makeText(Calculation_BIM_Activity.this, "몸무게를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {

                    // 신장(m) * 신장(m) * 22 : 남자
                    // 신장(m) * 신장(m) * 21 : 여자
        /*표준체중*/ double standardWeight = sex.equals("남성") ? (( height / 100) * (height / 100) * 22) : ((height / 100) * (height / 100) * 21);
          /*비만도*/ double percentWeight = weight / ((height / 100) * (height / 100));
                    String result;

                    //비만도 범위 측정
                    if((percentWeight >= 18.5) && (percentWeight <= 22.9)) {
                        result = "정상";
                    }else if((percentWeight >= 23) && (percentWeight <= 24.9)) {
                        result = "과체중";
                    }else if(percentWeight >= 25) {
                        result = "비만";
                    }else {
                        result = "너무 마른 상태";
                    }

                    TV_bmiInfo.setText(String.format("성별: %s\n키: %scm\n몸무게: %skg\n표준몸무게: %skg\n비만도(BMI): %s%%\n판정결과: %s",
                            sex, height, weight, String.format("%.2f", standardWeight), String.format("%.2f", percentWeight), result));
                }
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Calculation_BIM_Activity.this, "잘못된 입력값 입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

//==================================================================================================

}