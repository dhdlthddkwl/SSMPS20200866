package com.example.ssmps_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.CenterItem;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.dto.RegistItemRequest;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ItemRegisterActivity extends AppCompatActivity{
    TextView itemName, itemPrice, itemType;
    ImageView itemImage;
    Button registerBtn;
    CenterItem nowItem;
    Store nowStore;

    Retrofit retrofit;
    RetrofitAPI service;

    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    String token;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();
        setItemData();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerItem();
            }
        });
    }

    private void initData(){
        itemName = findViewById(R.id.itemRegister_item_name);
        itemPrice = findViewById(R.id.itemRegister_price);
        itemType = findViewById(R.id.itemRegister_item_type);

        itemImage = findViewById(R.id.itemRegister_item_img);
        registerBtn = findViewById(R.id.itemRegister_register_btn);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);
        gson = new GsonBuilder().create();


        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
        nowItem = gson.fromJson(sharedPreferenceUtil.getData("item", "err"), CenterItem.class);
    }

    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }

    private void setItemData(){
        Intent intent = getIntent();
        itemName.setText(nowItem.getName());
        itemType.setText(nowItem.getType());
        itemPrice.setText(Integer.toString(nowItem.getPrice()));
        setItemImage();
    }

    private void registerItem(){
        Call<Item> registItem = service.registItem(nowStore.getId(), nowItem.getId());
        registItem.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(!response.isSuccessful()){
                    Log.e("regist item error", response.errorBody().toString());
                    Toast.makeText(ItemRegisterActivity.this, "아이템 등록 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Item registedItem = response.body();
                Toast.makeText(ItemRegisterActivity.this, registedItem.getName() + "이 등록되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("regist item fail", t.getMessage());
                Toast.makeText(ItemRegisterActivity.this, "아이템 등록 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setItemImage(){
        Bitmap bitmap = byteToImage(nowItem.getImage());
        itemImage.setImageBitmap(bitmap);
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