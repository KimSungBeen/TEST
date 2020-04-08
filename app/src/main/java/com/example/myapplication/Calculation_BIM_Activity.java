package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import kotlin.jvm.Throws;

public class Calculation_BIM_Activity extends AppCompatActivity {

    public static final int MINIMUM_HEIGHT = 0;
    public static final int MAXIMUM_HEIGHT = 300;
    public static final int MUNIMUM_WEIGHT = 0;
    public static final int MAXIMUM_WEIGHT = 500;
    RadioGroup radioGroup;
    RadioButton RB_man, RB_woman;
    EditText ET_height, ET_weight;
    Button BTN_apply, BTN_exit;
    LottieAnimationView LA_kakaoLink;
    TextView TV_bmiInfo;
    String sex = "";

    double height = 0; //키
    double weight = 0; //몸무게
    String BMI; //비만도(BMI)
    String result; //측정결과

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
        LA_kakaoLink= findViewById(R.id.LA_kakaoLink);
    }

//==================================================================================================

    @Override
    protected void onResume() {
        super.onResume();

        String templateID = "23040";

        //카카오 링크 테스트 버튼
        LA_kakaoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TV_bmiInfo.getText().toString().equals("")) {
                    Toast.makeText(Calculation_BIM_Activity.this, "측정을 완료해 주십시오.", Toast.LENGTH_SHORT).show();
                } else {

                    TemplateParams params = FeedTemplate
                            .newBuilder(ContentObject.newBuilder(
                                    "BMI측정",
                                    "https://cdn2.iconfinder.com/data/icons/fitness-8/512/fitness-color-16-512.png",
                                    LinkObject.newBuilder()
                                            .setWebUrl("https://developers.kakao.com")
                                            .setMobileWebUrl("https://developers.kakao.com")
                                            .build())
                                    .setDescrption(String.format("비만도(BMI): %s%%\n판정결과: %s", BMI, result))
                                    .build())
                            .addButton(new ButtonObject(
                                    "앱에서 보기",
                                    LinkObject.newBuilder()
                                            .setAndroidExecutionParams("key1=value1")
                                            .setIosExecutionParams("key1=value1")
                                            .build()))
                            .build();

                    // 기본 템플릿으로 카카오링크 보내기
                    KakaoLinkService.getInstance()
                            .sendDefault(v.getContext(), params, new ResponseCallback<KakaoLinkResponse>() {
                                @Override
                                public void onFailure(ErrorResult errorResult) {
                                    Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
                                }

                                @Override
                                public void onSuccess(KakaoLinkResponse result) {
                                    Log.i("KAKAO_API", "카카오링크 공유 성공");

                                    // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                                    Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                                    Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
                                }
                            });
                }
            }
        });

        //나가기 버튼
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
                    if (sex.equals("")) {
                        Toast.makeText(Calculation_BIM_Activity.this, "성별을 체크하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (ET_height.getText().toString().equals("")) {
                            Toast.makeText(Calculation_BIM_Activity.this, "키를 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else if (!((MINIMUM_HEIGHT < parsingInteger(ET_height)) && (parsingInteger(ET_height) <= MAXIMUM_HEIGHT))) {
                            Toast.makeText(Calculation_BIM_Activity.this, "키를 확인해 주세요 (범위: 1 ~ 300)", Toast.LENGTH_SHORT).show();
                        } else {
                            height = Integer.parseInt(String.valueOf(ET_height.getText()));

                            if (ET_weight.getText().toString().equals("")) {
                                Toast.makeText(Calculation_BIM_Activity.this, "몸무게를 입력하세요.", Toast.LENGTH_SHORT).show();
                            } else if (!((MUNIMUM_WEIGHT < parsingInteger(ET_weight)) && (parsingInteger(ET_weight) <= MAXIMUM_WEIGHT))) {
                                Toast.makeText(Calculation_BIM_Activity.this, "몸무게를 확인해 주세요 (범위: 1 ~ 500)", Toast.LENGTH_SHORT).show();
                            } else {
                                weight = Integer.parseInt(String.valueOf(ET_weight.getText()));

                                // 신장(m) * 신장(m) * 22 : 남자
                                // 신장(m) * 신장(m) * 21 : 여자
                                /*표준체중*/
                                double standardWeight = sex.equals("남성") ? ((height / 100) * (height / 100) * 22) : ((height / 100) * (height / 100) * 21);
                                /*비만도*/
                                double percentWeight = weight / ((height / 100) * (height / 100));

                                //비만도 범위 측정
                                if ((percentWeight >= 18.5) && (percentWeight <= 22.9)) {
                                    result = "정상";
                                } else if ((percentWeight >= 23) && (percentWeight <= 24.9)) {
                                    result = "과체중";
                                } else if (percentWeight >= 25) {
                                    result = "비만";
                                } else {
                                    result = "너무 마른 상태";
                                }

                                BMI = String.format("%.2f", percentWeight);

                                TV_bmiInfo.setText(String.format("성별: %s\n키: %scm\n몸무게: %skg\n표준몸무게: %skg\n비만도(BMI): %s%%\n판정결과: %s",
                                        sex, height, weight, String.format("%.2f", standardWeight), BMI, result));
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(Calculation_BIM_Activity.this, "잘못된 입력값 입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

//==================================================================================================

    //EditText에서의 문자열을 정수형으로 변환
    private int parsingInteger(EditText editText) {
        return Integer.parseInt(String.valueOf(editText.getText()));
    }

//==================================================================================================

}