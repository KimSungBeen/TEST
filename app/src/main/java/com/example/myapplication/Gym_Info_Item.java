package com.example.myapplication;

import com.airbnb.lottie.LottieAnimationView;

public class Gym_Info_Item {

    private String TV_gymInfoList;
    private String TV_gymCallNumber;

    public Gym_Info_Item(String TV_gymInfoList, String TV_gymCallNumber) {
        this.TV_gymInfoList = TV_gymInfoList;
        this.TV_gymCallNumber = TV_gymCallNumber;
    }

    public String getTV_gymInfoList() {
        return TV_gymInfoList;
    }

    public void setTV_gymInfoList(String TV_gymInfoList) {
        this.TV_gymInfoList = TV_gymInfoList;
    }

    public String getTV_gymCallNumber() {
        return TV_gymCallNumber;
    }

    public void setTV_gymCallNumber(String TV_gymCallNumber) {
        this.TV_gymCallNumber = TV_gymCallNumber;
    }

}
