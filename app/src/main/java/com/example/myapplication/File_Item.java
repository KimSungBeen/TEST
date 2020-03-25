package com.example.myapplication;

import android.graphics.Bitmap;

public class File_Item {

    private Bitmap IV_thumbnail;
    private String TV_title;
    private String TV_contents;
    private String TV_url;


    public String getTV_url() {
        return TV_url;
    }

    public void setTV_url(String TV_url) {
        this.TV_url = TV_url;
    }

    public Bitmap getIV_thumbnail() {
        return IV_thumbnail;
    }

    public void setIV_thumbnail(Bitmap IV_thumbnail) {
        this.IV_thumbnail = IV_thumbnail;
    }

    public String getTV_title() {
        return TV_title;
    }

    public void setTV_title(String TV_title) {
        this.TV_title = TV_title;
    }

    public String getTV_contents() {
        return TV_contents;
    }

    public void setTV_contents(String TV_contents) {
        this.TV_contents = TV_contents;
    }

}
