package com.example.ssmps_android.manager;

import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssmps_android.R;
import com.example.ssmps_android.Recyclerview.CustomAdapter;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.LoginType;
import com.example.ssmps_android.domain.Manager;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.dto.StoreResponse;
import com.example.ssmps_android.guest.GuestItemListActivity;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManagerStoreSelectActivity extends AppCompatActivity {

    EditText searchInput;
    ImageView searchBtn;

    SharedPreferenceUtil sharedPreferenceUtil;
    TokenInterceptor tokenInterceptor;

    Retrofit retrofit;
    RetrofitAPI service;
    Gson gson;

    Manager nowManager;

    List<Store> storeList = new ArrayList<>();
    CustomAdapter customAdapter;
    String token;

    boolean isSearched = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_select);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initData();
        getStorelistData();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearched){
                    if(searchInput.getText().toString().equals("")){
                        Toast.makeText(ManagerStoreSelectActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchStore();
                    searchBtn.setImageResource(R.drawable.close);
                    isSearched = true;
                }else{
                    getStorelistData();
                    searchInput.setText(null);
                    searchBtn.setImageResource(R.drawable.search);
                    isSearched = false;
                }
            }
        });

//        ArrayList<Store> testDataSet = new ArrayList<>();
//        for (int i =0; i<20; i++) {
//            testDataSet.add(new Store(Long.valueOf(i), i + "번 매장", "천안", null));
//        }
    }

    private void initData(){
        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        searchInput = findViewById(R.id.storeSelect_search_input);
        searchBtn = findViewById(R.id.storeSelect_search_btn);

        setToken();
        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);
        gson = new GsonBuilder().create();

        nowManager = gson.fromJson(sharedPreferenceUtil.getData("manager", "err"), Manager.class);
    }

    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }
    private void getStorelistData(){
        Call<List<StoreResponse>> findStoreList = service.findStoreList(nowManager.getId());
        findStoreList.enqueue(new Callback<List<StoreResponse>>() {
            @Override
            public void onResponse(Call<List<StoreResponse>> call, Response<List<StoreResponse>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ManagerStoreSelectActivity.this, "매장 불러오기 에러", Toast.LENGTH_SHORT).show();
                    Log.e("store finding error", response.errorBody().toString());
                    return;
                }
                Log.e("store finding success", "매장 불러오기 성공");
                storeList = response.body().stream()
                        .map(s -> new Store(s.getId(), s.getName(), s.getAddress(), s.getLocationList()))
                        .collect(Collectors.toList());

                Log.e("store size", storeList.size() + "");
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<List<StoreResponse>> call, Throwable t) {
                Toast.makeText(ManagerStoreSelectActivity.this, "매장 불러오기 실패", Toast.LENGTH_SHORT).show();
                Log.e("store finding", "error");
            }
        });
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.storeSelect_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(linearLayoutManager);

        customAdapter = new CustomAdapter(storeList, getApplicationContext(), LoginType.MANAGER);

        Log.e("store list", storeList.size() + "");
        recyclerView.setAdapter(customAdapter);
    }

    private void searchStore(){
        String findStore = searchInput.getText().toString();
        Call<List<Store>> searchStore = service.findStoreByName(findStore);
        searchStore.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if(!response.isSuccessful()) {
                    Log.e("search store error", response.errorBody().toString());
                    Toast.makeText(ManagerStoreSelectActivity.this, "매장 검색 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                storeList = response.body();
                Log.e("store size", storeList.size() + "");
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.e("search store fail", t.getMessage());
                Toast.makeText(ManagerStoreSelectActivity.this, "매장 검색 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
}