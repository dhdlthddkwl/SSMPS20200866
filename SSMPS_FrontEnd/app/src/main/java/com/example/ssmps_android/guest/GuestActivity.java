package com.example.ssmps_android.guest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ssmps_android.R;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Location;
import com.example.ssmps_android.domain.Store;

import java.util.ArrayList;
import java.util.List;

public class GuestActivity extends AppCompatActivity{
    Canvas canvas;
    Paint paint;
    ImageView frame;

    EditText itemNameInput;
    Button searchBtn, informationBtn;

    List<Location> locationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_store_layout_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();
        getLocationData();
        searchItem();
    }

    private void initData(){
        Bitmap bitmap = Bitmap.createBitmap(410, 500, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);

        frame = findViewById(R.id.guest_canvas);
        frame.setImageBitmap(bitmap);

        paint = new Paint();
        paint.setColor(Color.WHITE);

        searchBtn = findViewById(R.id.guest_item_search_btn);
        // 임시 데이터
        locationList.add(new Location(150, 10, 230, 100));
    }

    private void getLocationData(){
        Intent intent = getIntent();
        Store store = (Store) intent.getSerializableExtra("store");
        Long storeId = store.getId();
        // DB에서 storeId값인것 가져오기

    }

    private void searchItem(){
        String itemName = itemNameInput.getText().toString();
        // DB에서 itemName인 것 가져오기
        Location location = locationList.get(0);

    }
}
