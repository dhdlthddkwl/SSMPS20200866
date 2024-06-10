package com.example.ssmps_android.guest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ssmps_android.R;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoreViewActivity extends AppCompatActivity {
    Button searchBtn;
    TextView searchResult, storeName;

    Retrofit retrofit;
    RetrofitAPI service;
    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    String token;
    Store nowStore;
    List<Location> locationList = new ArrayList<>();
    Canvas canvas;
    Paint paint;
    ImageView frame;

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_store_layout_view);

        initData();
        setStore();
        setLocationData();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem();
            }
        });

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK){
//                            Item searchItem = (Item) intent.getSerializableExtra("item");
                            Item searchItem = gson.fromJson(sharedPreferenceUtil.getData("item", "err"), Item.class);
                            List<Location> locaitonList = nowStore.getLocationList();
                            for(Location l : locaitonList){
                                for (Item i : l.getItemList()) {
                                    if (i.getName().equals(searchItem.getName())) {
                                        Toast.makeText(StoreViewActivity.this, "검색: " + searchItem.getName(), Toast.LENGTH_SHORT).show();
                                        showItemLocation(l);
                                        return;
                                    }
                                }
                            }
                            Toast.makeText(StoreViewActivity.this, searchItem.getName() + " 물건은\n진열 되어있지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initData(){
        searchBtn = findViewById(R.id.guest_item_search_btn);

        storeName = findViewById(R.id.guest_item_search_store_name);
        frame = findViewById(R.id.guest_canvas);

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

    private void setStore(){
        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
        storeName.setText(nowStore.getName());
    }

    private void setLocationData(){
        Call<List<Location>> findLocation = service.findStoreLocation(nowStore.getId());
        findLocation.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if(!response.isSuccessful()){
                    Log.e("find location err", response.errorBody().toString());
                    Toast.makeText(StoreViewActivity.this, "매장 뷰 불러오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                locationList = response.body();
                setLocation();

            }
            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.e("find location fail", t.getMessage());
                Toast.makeText(StoreViewActivity.this, "매장 뷰 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLocation(){
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.LTGRAY);
        frame.setImageBitmap(bitmap);
        paint = new Paint();
        paint.setColor(Color.WHITE);

        for(Location l : locationList){
            drawLocation(Color.WHITE, l);
            drawLocationType(l);
        }

    }

    private void drawLocation(int color, Location location){
        paint.setColor(color);
        canvas.drawRect(location.getStartX(), location.getStartY(), location.getEndX(), location.getEndY(), paint);
        frame.invalidate();
    }

    private void drawLocationType(Location location){
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        List<String> typeList = location.getItemList().stream().map(i -> i.getType() + " ").collect(Collectors.toList());
        String type = typeList.stream().distinct().collect(Collectors.joining());
        if(type.equals("")){
            type = "진열X";
        }
        canvas.drawText(type, location.getCenterX(), location.getCenterY(), paint);
    }

    private void searchItem(){
        Intent intent = new Intent(getApplicationContext(), GuestItemListActivity.class);
        resultLauncher.launch(intent);
    }

    private void showItemLocation(Location location){
        for(Location l : locationList){
            if(l.equals(location)){
                continue;
            }
            drawLocation(Color.LTGRAY, l);
            drawLocation(Color.WHITE, l);
            drawLocationType(l);
        }
        drawLocation(Color.LTGRAY, location);
        drawLocation(Color.GRAY, location);
        drawLocationType(location);
    }

    private void showSearchResult(Location location, String itemName){
    }
}
