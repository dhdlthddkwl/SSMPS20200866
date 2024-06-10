package com.example.ssmps_android.location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Location;
import com.example.ssmps_android.domain.Store;
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

// 테스트용 페이지
public class MainPage extends AppCompatActivity {
    Button registerBtn, removeBtn, saveBtn;
    TextView storeName, editAlarm;
    Retrofit retrofit;
    RetrofitAPI service;
    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    Canvas canvas;
    Paint paint;
    float endX, endY;
    float startX = 0.0f, startY = 0.0f;
    boolean flag = false; // 생성버튼이 눌렸나?
    boolean isIn = false; // 내부가 클릭 되었는가?
    boolean isMove = false; // 움직였나?
    boolean isRemove = false; // 삭제 버튼이 눌렸는가?
    boolean isRegister = false; // 물건 등록 버튼이 눌렸는가?
    int clickCnt = 0; // 시작점, 끝점 구분
    List<Location> locationList = new ArrayList<>();
    Location moveLocation;
    ImageView frame;
    float preX, preY; // 이동 전 좌표
    Store nowStore;
    String token;

//    boolean updatedLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_layout);
        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        setToken();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();
        removeLocation();
        registItem();
        addLocation();
        editLocation();
        setLocationList();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updatedLocation = false;
                setLocationList();
            }
        });
    }

    @Override
    protected void onRestart() {
        Toast.makeText(this, "등록됨", Toast.LENGTH_SHORT).show();
        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
        setLocationList();
//        for(Location l : nowStore.getLocationList()){
//            drawLocation(Color.LTGRAY, l);
//            drawLocation(Color.WHITE, l);
//            drawLocationType(l);
//        }

        super.onRestart();
    }
    private void initData(){
        storeName = findViewById(R.id.edit_store_layout_name);
        editAlarm = findViewById(R.id.edit_store_layout_explain);
        saveBtn = findViewById(R.id.mainPage_save_btn);
        registerBtn = findViewById(R.id.mainPage_regist_item_btn);
        removeBtn = findViewById(R.id.mainPage_remove_btn);

        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.LTGRAY);

        frame = findViewById(R.id.mainPage_draw_space);
        frame.setImageBitmap(bitmap);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        gson = new GsonBuilder().create();
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);

        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
        storeName.setText(nowStore.getName());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void editLocation(){
        frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.e("here", flag + " " + isRemove + " " + isRegister);
                        if(!flag && !isRemove && !isRegister){
                            // move
                            if(isClickLocation(event.getX(), event.getY())){
                                preX = event.getX();
                                preY = event.getY();
                            }
                            return true;
                        }
                        if(isRemove){
                            Toast.makeText(MainPage.this, "삭제", Toast.LENGTH_SHORT).show();
                            // 삭제
                            Log.e("삭제", "삭제됨");
                            if(isClickLocation(event.getX(), event.getY())){
                                drawLocation(Color.LTGRAY, moveLocation);
                                locationList.remove(moveLocation);
                            }
                            deleteLocation(moveLocation);
                            isRemove = false;
                            return true;
                        }
                        if(isRegister){
                            Toast.makeText(MainPage.this, "물건 등록", Toast.LENGTH_SHORT).show();
                                // 물건 등록
                            if(isClickLocation(event.getX(), event.getY())){
                                Log.e("now Location", moveLocation.getId() + "");
                                isRegister = false;
                                Intent intent = new Intent(getApplicationContext(), LocationDetailActivity.class);
                                sharedPreferenceUtil.putData("location", gson.toJson(moveLocation));
                                startActivity(intent);
                                return true;
                            }
                            Toast.makeText(MainPage.this, "저장을 한 후 물건을 추가하세요", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        Log.e("cnt", clickCnt + " ");
                        if(clickCnt == 0){
                            // 시작점 클릭
                            startX = event.getX();
                            startY = event.getY();
                            Log.e("start", startX + "  " + startY);
                            clickCnt = 1;
                        }else{
                            Toast.makeText(MainPage.this, "추가", Toast.LENGTH_SHORT).show();
                            // 끝점 클릭
                            // 추가
                            endX = event.getX();
                            endY = event.getY();
                            Location location = new Location(startX, startY, endX, endY);

                            locationList.add(location);
                            Log.e("nowLocation", locationList.size() + "");
                            nowStore.setLocation(locationList);
                            Log.e("nowS", nowStore.getLocationList().size() + "");

                            drawLocation(Color.WHITE, location);
                            drawLocationType(location);
                            clickCnt = 0;
                            registLocation(location);
                            setLocationList();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 무브
                        Log.e("flag", flag + "  " + isIn);
                        if((!flag) && isIn){
                            moveLocation(moveLocation, event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("clickCnt", clickCnt + "");
                        if(clickCnt == 0){
                            flag = false;
                        }
                        if(isMove){
                            drawLocation(Color.WHITE, moveLocation);
                            drawLocationType(moveLocation);
                            setLocationData(moveLocation);
                            updateLocation(moveLocation);
                            isMove = false;
                        }
                        isIn = false;
                        break;
                }
                return true;
            }
        });
    }

    private void setLocationList(){
        if(locationList.size() != 0){
            for(Location l : locationList){
                drawLocation(Color.LTGRAY, l);
            }
        }
        else{
            Log.e("ghe", "here");
            locationList = nowStore.getLocationList();
            Log.e("now", nowStore.getLocationList().size() + "");
        }

        Toast.makeText(this, "매장 불러오기", Toast.LENGTH_SHORT).show();
        Log.e("매장 불러오기", "성공");
        Call<List<Location>> findLocation = service.findStoreLocation(nowStore.getId());
        findLocation.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if(!response.isSuccessful()){
                    Log.e("find location error", response.errorBody().toString());
                    Toast.makeText(MainPage.this, "매장 불러오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                locationList = response.body();
                for(Location l : locationList){
                    drawLocation(Color.WHITE, l);
                    drawLocationType(l);
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.e("find location fail", t.getMessage());
                Toast.makeText(MainPage.this, "매장 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }

    private void addLocation(){
        findViewById(R.id.mainPage_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlarm.setText("추가할 매대의 시작점과 끝점을 선택해주세요");
                flag = true;
                isRegister = false;
                isMove = false;
                isRemove = false;
            }
        });
    }
    private void moveLocation(Location location, MotionEvent event){
        isMove = true;
        drawLocation(Color.LTGRAY, location);

        int dx = (int) (event.getX() - preX);
        int dy = (int) (event.getY() - preY);
        preX = event.getX();
        preY = event.getY();
        location.setStartX(location.getStartX() + dx);
        location.setStartY(location.getStartY() + dy);
        location.setEndX(location.getEndX() + dx);
        location.setEndY(location.getEndY() + dy);

        drawLocation(Color.DKGRAY, location);
        drawLocationType(location);
    }

    private void removeLocation(){
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRemove = true;
                isRegister = false;
                isMove = false;
                flag = false;
                editAlarm.setText("삭제할 매대를 선택해주세요");
            }
        });
    }

    private boolean isClickLocation(float x, float y){
        Log.e("location Size", locationList.size() + "");
        for(Location location : locationList){
            float locationSX = location.getStartX();
            float locationSY = location.getStartY();
            float locationEX = location.getEndX();
            float locationEY = location.getEndY();

            if((x >= locationSX && x <= locationEX)
                    && (y >= locationSY && y <= locationEY)){
                Log.e("test", "안에 체크 됨");
                moveLocation = location;
                isIn = true;
                return true;
            }
        }
        return false;
    }

    private void drawLocation(int color, Location location){
        paint.setColor(color);
        canvas.drawRect(location.getStartX(), location.getStartY(), location.getEndX(), location.getEndY(), paint);
        frame.invalidate();
    }

    private void registItem(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlarm.setText("진열할 매대를 선택해주세요");
                isRegister = true;
                isRemove = false;
                isMove = false;
                flag = false;
            }
        });
    }

    private void registLocation(Location location){
//        새로운 로케이션이 만들어짐
        checkModifyLocation();
        // location k을 보내는것으로 바꿔야 하는 거 아닌가??
        Call<String> registStore = service.registStoreLocation(nowStore.getId(), location);
        registStore.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.e("regist Location Error", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(MainPage.this, "매장 수정 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                response.body();
//                setNowStoreLocation(response.body());
                Toast.makeText(MainPage.this, "매장 수정 성공", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("regist location fail", t.getMessage());
                Toast.makeText(MainPage.this, "매장 수정 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLocation(Location location){
        Call<String> deleteLocation = service.deleteStoreLocation(location.getId());
        deleteLocation.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("delete Location error", response.errorBody().toString());
                    Toast.makeText(MainPage.this, "삭제 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainPage.this, "삭제 성공" + location.getId(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("delete location fail", t.getMessage());
                Toast.makeText(MainPage.this, "삭제 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocation(Location location){
        Call<String> updateLocation = service.updateStoreLocation(location);
        updateLocation.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("update location error", response.errorBody().toString());
                    Toast.makeText(MainPage.this, "수정 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainPage.this, "수정 성공", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("update location fail", t.getMessage());
                Toast.makeText(MainPage.this, "수정 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkModifyLocation(){
        List<Location> list = nowStore.getLocationList();
        List<Location> newList = new ArrayList<>();
        for(int i = 0;i < locationList.size();i++){
            if(!list.contains(locationList.get(i))){
                newList.add(locationList.get(i));
            }
        }
        nowStore.setLocation(newList);
    }

    private void setNowStoreLocation(List<Location> locations){
        nowStore.setLocation(locations);
        sharedPreferenceUtil.remove("store");
        sharedPreferenceUtil.putData("store", gson.toJson(nowStore, Store.class));
    }

    private void setLocationData(Location location){
        locationList.remove(location);
        locationList.add(location);
    }

    private void drawLocationType(Location location){
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        String type = "";
        if(location.getItemList() != null){
            List<String> typeList = location.getItemList().stream().map(i -> i.getType() + " ").collect(Collectors.toList());
            type = typeList.stream().distinct().collect(Collectors.joining());
        }
        if(type.equals("")){
            type = "진열X";
        }
        canvas.drawText(type, location.getCenterX(), location.getCenterY(), paint);
    }
}