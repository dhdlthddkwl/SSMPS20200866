package com.example.ssmps_android.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssmps_android.R;
import com.example.ssmps_android.Recyclerview.CustomAdapter4;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuestItemListActivity extends AppCompatActivity {
    TextView itemName;
    List<Item> itemList = new ArrayList<>();

    ImageView searchBtn;
    EditText searchInput;

    Retrofit retrofit;
    RetrofitAPI service;

    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    String token;
    Store nowStore;

    boolean isSearched = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_guest_item_list);
        initData();
        setListData();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearched){
                    if(searchInput.getText().toString().equals("")){
                        Toast.makeText(GuestItemListActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchItemList();
                    searchBtn.setImageResource(R.drawable.close);
                    isSearched = true;
                }else{
                    setListData();
                    searchInput.setText(null);
                    searchBtn.setImageResource(R.drawable.search);
                    isSearched = false;
                }
            }
        });
    }

    private void setRecyclerview(){
        RecyclerView recyclerView = findViewById(R.id.guestItemList_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(linearLayoutManager);

        CustomAdapter4 customAdapter4 = new CustomAdapter4(itemList);
        recyclerView.setAdapter(customAdapter4);
    }

    private void initData(){

        searchInput = findViewById(R.id.guest_item_name_input);
        searchBtn = findViewById(R.id.guest_item_search_btn);
        
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

    private void setListData(){
        Call<List<Item>> findAllItem = service.findAllItem(nowStore.getId());
        findAllItem.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    Log.e("find all item error", response.errorBody().toString());
                    Toast.makeText(GuestItemListActivity.this, "매장 물건 가져오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                itemList = response.body();
                setRecyclerview();
            }
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("find all item fail", t.getMessage());
                Toast.makeText(GuestItemListActivity.this, "매장 물건 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void searchItemList(){
        String searchItemName = searchInput.getText().toString();
        Call<List<Item>> searchItemList = service.findItemByName(searchItemName, nowStore.getId());
        searchItemList.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.e("guest search item error", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(GuestItemListActivity.this, "물건 검색 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                itemList = response.body();
                setRecyclerview();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("guest search item fail", t.getMessage());
                Toast.makeText(GuestItemListActivity.this, "물건 검색 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}