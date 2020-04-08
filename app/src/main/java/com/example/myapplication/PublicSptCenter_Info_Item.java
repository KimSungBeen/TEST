package com.example.myapplication;

public class PublicSptCenter_Info_Item {

    private String TV_publicSptCenterInfoList;
    private String TV_publicSptCenterCallNumber;
    private String  YCODE;
    private String  XCODE;

    public PublicSptCenter_Info_Item(String TV_publicSptCenterInfoList, String TV_publicSptCenterCallNumber, String YCODE, String XCODE) {
        this.TV_publicSptCenterInfoList = TV_publicSptCenterInfoList;
        this.TV_publicSptCenterCallNumber = TV_publicSptCenterCallNumber;
        this.YCODE = YCODE;
        this.XCODE = XCODE;
    }

    public String getTV_publicSptCenterInfoList() {
        return TV_publicSptCenterInfoList;
    }

    public void setTV_publicSptCenterInfoList(String TV_publicSptCenterInfoList) {
        this.TV_publicSptCenterInfoList = TV_publicSptCenterInfoList;
    }

    public String getTV_publicSptCenterCallNumber() {
        return TV_publicSptCenterCallNumber;
    }

    public void setTV_publicSptCenterCallNumber(String TV_publicSptCenterCallNumber) {
        this.TV_publicSptCenterCallNumber = TV_publicSptCenterCallNumber;
    }

    public String getYCODE() {
        return YCODE;
    }

    public void setYCODE(String YCODE) {
        this.YCODE = YCODE;
    }

    public String getXCODE() {
        return XCODE;
    }

    public void setXCODE(String XCODE) {
        this.XCODE = XCODE;
    }
}
