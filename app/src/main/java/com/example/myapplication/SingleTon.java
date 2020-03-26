package com.example.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class SingleTon {

    public static boolean isBackPress = false;

    //Bitmap에서 Uri를 얻어오는 메소드
    public static Uri getImageUri(Context context, @Nullable Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);

            //path에 null이 들어올시 NullPointerException이 발생하므로 예외처리
            try {
                return Uri.parse(path);
            }catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    //브로드캐스트 리시버기능 생성 메소드
    public static void broadcastReceiver (Context context, Broadcast_Receiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        context.registerReceiver(receiver, intentFilter);
    }

}
