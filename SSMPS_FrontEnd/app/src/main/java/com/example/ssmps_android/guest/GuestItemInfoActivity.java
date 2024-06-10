package com.example.ssmps_android.guest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuestItemInfoActivity extends AppCompatActivity {
    TextView itemName, itemPrice, itemType, itemQuantity;
    ImageView itemImg;
    Gson gson;
    Item nowItem;

    SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_item_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();
        setItemData();
    }

    private void initData(){
        itemName = findViewById(R.id.guestItemInfo_item_name);
        itemPrice = findViewById(R.id.guestItemInfo_item_price);
        itemType = findViewById(R.id.guestItemInfo_item_type);
        itemQuantity = findViewById(R.id.guestItemInfo_item_quantity);
        itemImg = findViewById(R.id.guestItemInfo_item_img);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        gson = new GsonBuilder().create();

        nowItem = gson.fromJson(sharedPreferenceUtil.getData("item", "err"), Item.class);
    }

    private void setItemData(){
        itemName.setText(nowItem.getName());
        itemPrice.setText(Integer.toString(nowItem.getPrice()));
        itemType.setText(nowItem.getType());
        itemQuantity.setText(Integer.toString(nowItem.getQuantity()));
        itemImg.setImageBitmap(byteToImage(nowItem.getImage()));
    }

    private Bitmap byteToImage(String b){
        try {
            byte[] encodeByte = Base64.decode(b, Base64.DEFAULT);
            // Base64 코드를 디코딩하여 바이트 형태로 저장
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            // 바이트 형태를 디코딩하여 비트맵 형태로 저장
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
