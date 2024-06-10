package com.example.ssmps_android.manager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssmps_android.R;
import com.example.ssmps_android.Recyclerview.ManagerModifyDeleteAdapter;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.CenterItem;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.dto.CenterItemResponse;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManagerSearchItemActivity extends AppCompatActivity {
    EditText itemNameInput;
    ImageView searchBtn;

    Retrofit retrofit;
    RetrofitAPI service;
    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    String token;
    List<Item> itemList = new ArrayList<>();
    Store nowStore;

    boolean isSearched = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_item_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initData();
        getAllCenterItem();

        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearched){
                    if(itemNameInput.getText().toString().equals("")){
                        Toast.makeText(ManagerSearchItemActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchItem();
                    searchBtn.setImageResource(R.drawable.close);
                    isSearched = true;
                }else{
                    getAllCenterItem();
                    itemNameInput.setText(null);
                    searchBtn.setImageResource(R.drawable.search);
                    isSearched = false;
                }
            }
        });
    }
    
    private void initData(){
        itemNameInput = findViewById(R.id.managerSearchItem_item_name_input);
        searchBtn = findViewById(R.id.managerSearchItem_search_btn);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        gson = new GsonBuilder().create();
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);

        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
    }

    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }

    private void getAllCenterItem(){
        Call<List<Item>> getAllItem = service.findAllItem(nowStore.getId());
        getAllItem.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    Log.e("getAllItem Error", response.errorBody().toString());
                    Toast.makeText(ManagerSearchItemActivity.this, "아이템 리스트 가져오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ManagerSearchItemActivity.this, "리스트업 성공!", Toast.LENGTH_SHORT).show();
                itemList = response.body();
                setItemRecyclerview();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("getAllItem fail", t.getMessage());
                Toast.makeText(ManagerSearchItemActivity.this, "아이템 리스트 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void searchItem(){
        String itemName = itemNameInput.getText().toString();
        Log.e("itemName", itemName);
        Call<List<Item>> findItemByName = service.findItemByName(itemName, nowStore.getId());
        findItemByName.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (!response.isSuccessful()) {
                    try {
                        Log.e("get item error", response.errorBody().toString());
                        Log.e("get search item error", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(ManagerSearchItemActivity.this, "아이템 검색 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ManagerSearchItemActivity.this, "아이템 검색 성공", Toast.LENGTH_SHORT).show();
                itemList = response.body();
                Log.e("size", "item list size" + itemList.size());
                setItemRecyclerview();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("get search Item fail", t.getMessage());
                Toast.makeText(ManagerSearchItemActivity.this, "아이템 검색 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setItemRecyclerview(){
        RecyclerView recyclerView = findViewById(R.id.itemSearchManager_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ManagerModifyDeleteAdapter adapter = new ManagerModifyDeleteAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
//        Toast.makeText(this, "다시시작됨", Toast.LENGTH_SHORT).show();
        // 처음 시작했을 때도 여기로 오는 문제 해결해야 함
        super.onResume();
    }

    @Override
    protected void onRestart() {
        getAllCenterItem();
        super.onRestart();
    }
}