package com.example.ssmps_android.location;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ssmps_android.R;
import com.example.ssmps_android.Recyclerview.CustomAdapter3;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Location;
import com.example.ssmps_android.domain.Store;
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

public class LocationDetailActivity extends AppCompatActivity {
    EditText searchInput;
    ImageView searchBtn;
    RecyclerView recyclerView;

    Store nowStore;

    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    Retrofit retrofit;
    RetrofitAPI service;
    TokenInterceptor tokenInterceptor;

    List<Item> itemList = new ArrayList<>();
    List<Location> locationList = new ArrayList<>();
    Location nowLocation;
    String token;

    CustomAdapter3 customAdapter3;

    boolean isSearched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_item_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();
        setItemList();
        // 검색 버튼 클릭
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearched){
                    if(searchInput.getText().toString().equals("")){
                        Toast.makeText(LocationDetailActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchItem();
                    searchBtn.setImageResource(R.drawable.close);
                    isSearched = true;
                }else{
                    setItemList();
                    searchInput.setText(null);
                    searchBtn.setImageResource(R.drawable.search);
                    isSearched = false;
                }
            }
        });

        // 등록버튼 클릭
        findViewById(R.id.location_item_register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> locationItemList = customAdapter3.getLocationItemList(); // 변경 된 List
                nowLocation.setItemList(locationItemList); // 현재 location 아이템 리스트 변경
                for(int i = 0;i < nowStore.getLocationList().size();i++){
                    Location nowStoreLocation = nowStore.getLocationList().get(i);
                    if(nowLocation.getId() == nowStoreLocation.getId()){
                        Location location = nowStoreLocation; // 현재 로케이션 찾음
                        nowStoreLocation.setItemList(locationItemList);
                        location = nowLocation;
                    }
                }
                sharedPreferenceUtil.putData("store", gson.toJson(nowStore));
                updateItemLocation(locationItemList);
                finish();
            }
        });
    }

    private void initData(){
        searchInput = findViewById(R.id.locationDetail_search_input);
        searchBtn = findViewById(R.id.locationDetail_search_btn);
        recyclerView = findViewById(R.id.locationDetail_recyclerView);
        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        gson = new GsonBuilder().create();
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);

        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
        locationList = nowStore.getLocationList();
        nowLocation = gson.fromJson(sharedPreferenceUtil.getData("location", "err"), Location.class);
//        nowLocation = (Location) (getIntent().getSerializableExtra("location"));
    }

    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }

    private void setRecyclerViewData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter3 = new CustomAdapter3(itemList, nowLocation);
        recyclerView.setAdapter(customAdapter3);
    }
    private void setItemList(){
        Call<List<Item>> findAllItem = service.findAllItem(nowStore.getId());
        findAllItem.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    Log.e("find all item error", response.errorBody().toString());
                    Toast.makeText(LocationDetailActivity.this, "매장 물건 불러오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                itemList = response.body();
                setRecyclerViewData();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("find all item fail", t.getMessage());
                Toast.makeText(LocationDetailActivity.this, "매장 물건 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchItem(){
        String itemName = searchInput.getText().toString();
        Call<List<Item>> searchItem = service.findItemByName(itemName, nowStore.getId());
        searchItem.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    Log.e("search item error", response.errorBody().toString());
                    Toast.makeText(LocationDetailActivity.this, "물건 검색 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                itemList = response.body();
                setRecyclerViewData();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("search item fail", t.getMessage());
                Toast.makeText(LocationDetailActivity.this, "물건 검색 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateItemLocation(List<Item> locationItemList){
        Call<Location> updateLocation = service.modifyItemLocation(locationItemList, nowLocation.getId());
        updateLocation.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if(!response.isSuccessful()){
                    Log.e("update location error", response.errorBody().toString());
                    Toast.makeText(LocationDetailActivity.this, "매대 수정 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LocationDetailActivity.this, "매대 정보가 수정되었습니다", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Log.e("update location fail", t.getMessage());
                Toast.makeText(LocationDetailActivity.this, "매대 수정 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}