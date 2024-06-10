package com.example.ssmps_android.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.location.MainPage;
import com.example.ssmps_android.manager.ManagerSearchCenterItemActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FunctionSelectActivity extends AppCompatActivity {

    Button layoutSettingBtn, addBtn, deleteModifyBtn;

    TextView storeName;

    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    Store nowStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_select);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();
        settingLayout();
        addItem();
        deleteModifyItem();
    }
    private void initData(){
        layoutSettingBtn = findViewById(R.id.functionSelect_store_layout);
        addBtn = findViewById(R.id.functionSelect_item_add);
        deleteModifyBtn = findViewById(R.id.functionSelect_item_delete_modify);
        storeName = findViewById(R.id.functionSelect_store_name);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        gson = new GsonBuilder().create();
        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
        storeName.setText(nowStore.getName());
    }

    private void settingLayout(){
        layoutSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                startActivity(intent);
            }
        });
    }

    private void addItem(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManagerSearchCenterItemActivity.class);
                startActivity(intent);
            }
        });
    }

    private void deleteModifyItem(){
        deleteModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManagerSearchItemActivity.class);
                startActivity(intent);
            }
        });
    }
}
