package com.example.ssmps_android.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ssmps_android.R;
import com.example.ssmps_android.Recyclerview.CustomAdapter;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.LoginType;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.manager.ManagerStoreSelectActivity;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuestStoreSelectActivity extends AppCompatActivity {

    EditText searchInput;
    ImageView searchBtn;
    SharedPreferenceUtil sharedPreferenceUtil;
    TokenInterceptor tokenInterceptor;

    Retrofit retrofit;
    RetrofitAPI service;
    Gson gson;
    List<Store> storeList = new ArrayList<>();
    boolean isSearched;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_select);
        initData();
        setStoreList();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        isSearched = false;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearched){
                    if(searchInput.getText().toString().equals("")){
                        Toast.makeText(GuestStoreSelectActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchStore();
                    searchBtn.setImageResource(R.drawable.close);
                    isSearched = true;
                }else{
                    setStoreList();
                    searchInput.setText(null);
                    searchBtn.setImageResource(R.drawable.search);
                    isSearched = false;
                }

            }
        });
    }

    private void initData(){
        searchBtn = findViewById(R.id.storeSelect_search_btn);
        searchInput = findViewById(R.id.storeSelect_search_input);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);
        gson = new GsonBuilder().create();
    }

    private void setRecyclerviewData(){
        RecyclerView recyclerView = findViewById(R.id.storeSelect_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(linearLayoutManager);

        CustomAdapter customAdapter = new CustomAdapter(storeList, getApplicationContext(), LoginType.GUEST);

        Log.e("store list", storeList.size() + "");
        recyclerView.setAdapter(customAdapter);
    }

    private void setStoreList(){
        Call<List<Store>> findAllStore = service.findAllStore();
        findAllStore.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if(!response.isSuccessful()){
                    Log.e("find store list error", response.errorBody().toString());
                    Toast.makeText(GuestStoreSelectActivity.this, "매장 불러오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                storeList = response.body();
                Log.e("store size", storeList.size() + "");
                setRecyclerviewData();
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.e("find store list fail", t.getMessage());
                Toast.makeText(GuestStoreSelectActivity.this, "매장 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void searchStore(){
        String findStore = searchInput.getText().toString();
        Call<List<Store>> searchStore = service.findStoreByName(findStore);
        searchStore.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if(!response.isSuccessful()) {
                    Log.e("search store error", response.errorBody().toString());
                    Toast.makeText(GuestStoreSelectActivity.this, "매장 검색 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                storeList = response.body();
                setRecyclerviewData();
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.e("search store fail", t.getMessage());
                Toast.makeText(GuestStoreSelectActivity.this, "매장 검색 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
}