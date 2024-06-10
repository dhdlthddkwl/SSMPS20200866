package com.example.ssmps_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.example.ssmps_android.domain.Manager;
import com.example.ssmps_android.dto.LoginResponse;
import com.example.ssmps_android.guest.GuestStoreSelectActivity;
import com.example.ssmps_android.login.SignActivity;
import com.example.ssmps_android.manager.ManagerStoreSelectActivity;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    EditText idInput, passInput;
    TextView guestBtn, joinBtn;
    Button loginBtn;

    Retrofit retrofit;
    RetrofitAPI service;

    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        initComponent();
        join();
        guest_login();

//        findViewById(R.id.updateImageTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageView image = findViewById(R.id.img_test);
//                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//                String imgPath = bitmapToEncodingString(drawable.getBitmap());
//                String imgSubstring = imgPath.substring(2, imgPath.length() - 2);
//                Log.e("sub", imgSubstring);
//                Call<String> insertImage = service.imageUpdate(imgPath);
//                insertImage.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if(!response.isSuccessful()){
//                            Toast.makeText(MainActivity.this, "아이템 저장 에러", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        Toast.makeText(MainActivity.this, "아이템 저장 성공", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Toast.makeText(MainActivity.this, "아이템 저장 실패", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferenceUtil.remove("token");
                login();
            }
        });

        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuestStoreSelectActivity.class);
                startActivity(intent);
            }
        });

        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);
    }
    private String bitmapToEncodingString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);    //bitmap compress
        byte [] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);
        return image;
    }


    private void initComponent(){
        idInput = findViewById(R.id.login_id);
        passInput = findViewById(R.id.login_password);
        guestBtn = findViewById(R.id.mainActivity_login_guest);
        joinBtn = findViewById(R.id.login_join);
        loginBtn = findViewById(R.id.login_login_btn);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        gson = new GsonBuilder().create();
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);
    }

    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }

    private void login(){
        String id = idInput.getText().toString();
        String password = passInput.getText().toString();
        Call<LoginResponse> login;
        Log.e("token", token);
        if(token.equals("err")){
            login = service.loginFirst(id, password);
        }else{
            login = service.login(id, password);
        }
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse managerResponse = response.body();
//                    List<Store> storeList = managerResponse.getStoreResponseList().stream()
//                            .map(s -> new Store(s.getId(), s.getName(), s.getAddress(), null))
//                            .collect(Collectors.toList());
                    Manager manager = new Manager(managerResponse.getId(), managerResponse.getAccountId(), " ", null);
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    sharedPreferenceUtil.putData("manager", gson.toJson(manager, Manager.class)); // manager 저장
                    sharedPreferenceUtil.putData("token", managerResponse.getToken());
                    Log.e("token", token);
                    sharedPreferenceUtil.remove("token");
                    Intent intent = new Intent(getApplicationContext(), ManagerStoreSelectActivity.class);
                    startActivity(intent);
                    return;
                }
                Toast.makeText(MainActivity.this, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show();
                try {
                    assert response.errorBody() != null;
                    Log.e("login fail", response.errorBody().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "로그인 실패!", Toast.LENGTH_SHORT).show();
                Log.e("login error", t.getMessage());
            }
        });
    }

    private void join(){
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
            }
        });

    }
    private void guest_login(){
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManagerStoreSelectActivity.class);
                startActivity(intent);
            }
        });
    }
}