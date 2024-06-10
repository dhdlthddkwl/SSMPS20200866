package com.example.ssmps_android.login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Manager;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignActivity extends AppCompatActivity {
    EditText idInput, passInput, passCheckInput, storenamesetting, storeaddresssetting;
    Button joinBtn, plusBtn, preBtn;
    Retrofit retrofit;
    RetrofitAPI service;

    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    List<EditText> storeNameList = new ArrayList<>();
    List<EditText> storeAddressList = new ArrayList<>();
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_signup);
        initComponent();
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join();
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { addStoreName();}
        });
    }

    private void initComponent(){
        idInput = findViewById(R.id.join_id);
        passInput = findViewById(R.id.join_password);
        passCheckInput = findViewById(R.id.join_password_check);
        storenamesetting = findViewById(R.id.join_store_name);
        storeaddresssetting = findViewById(R.id.join_store_address);
        joinBtn = findViewById(R.id.join_join_btn);
        plusBtn = findViewById(R.id.join_store_btn);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);



        storeNameList.add(storenamesetting);
        storeAddressList.add(storeaddresssetting);
    }
    private void addStoreName(){


        LinearLayout con = findViewById(R.id.joinStoreList);
        con.removeView(plusBtn);

        LinearLayout parent = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams conParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        parent.setOrientation(LinearLayout.VERTICAL);
        LinearLayout nameParent = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams linParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        linParams2.leftMargin = 10;
        linParams2.rightMargin = 10;
        linParams2.topMargin = 30;
        //TextView textStoreName = new TextView(getApplicationContext());
        //textStoreName.setText("가게 이름 설정 :");

        EditText editStoreName = new EditText(getApplicationContext());
        LinearLayout.LayoutParams editParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editParam.width = 350;
        editParam.height = 150;
        editStoreName.setSingleLine(true);
        editStoreName.setLayoutParams(editParam);
        editStoreName.setHint("가게 이름을 입력해 주세요");
        editStoreName.setBackground(null);

        nameParent.setBackgroundColor(Color.parseColor("#eeeeee"));
        //nameParent.addView(textStoreName);
        nameParent.addView(editStoreName);
        storeNameList.add(editStoreName);
        nameParent.setLayoutParams(linParams2);


        linParams2.leftMargin = 10;
        linParams2.rightMargin = 10;
        linParams2.bottomMargin = 30;
        LinearLayout addressParent = new LinearLayout(getApplicationContext());
        //TextView textStoreAddress = new TextView(getApplicationContext());
        //textStoreAddress.setText("           가게 주소 :");

        parent.setOrientation(LinearLayout.VERTICAL);
        EditText editStoreAddress = new EditText(getApplicationContext());
        LinearLayout.LayoutParams editParam2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editParam2.width = 350;
        editParam2.height = 100;
        editParam2.topMargin = 20;
        editStoreAddress.setHint("가게 주소를 입력해 주세요");
        editStoreAddress.setBackground(null);

        editStoreAddress.setSingleLine(true);
        addressParent.setBackgroundColor(Color.parseColor("#eeeeee"));

        editStoreName.setLayoutParams(editParam);
        editStoreAddress.setLayoutParams(editParam);
        //addressParent.addView(textStoreAddress);
        addressParent.addView(editStoreAddress);

        storeAddressList.add(editStoreAddress);

        parent.addView(nameParent);
        parent.addView(addressParent);

        Button button = new Button(getApplicationContext());
        button.setText("➕");
        button.setId(cnt++);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStoreName();
            }
        });
        con.removeView(findViewById(cnt - 2));
        con.addView(parent);
        con.addView(button);
    }
    private void setToken(){
        String token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }

    private void join(){
        String id = idInput.getText().toString();
        String password = passInput.getText().toString();
        String passwordCheck = passCheckInput.getText().toString();

        Log.e("id check", id);
        if(!password.equals(passwordCheck)){
            Toast.makeText(SignActivity.this, "비밀번호가 같지 않습니다", Toast.LENGTH_SHORT).show();
            return;
        }
        if(id.isEmpty() || password.isEmpty()){
            Toast.makeText(SignActivity.this, "아이디 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("size: ", storeNameList.size() + "");
        Log.e("size: ", storeAddressList.size() + "");
        if (storeNameList.size() == 0 || storeAddressList.size() == 0 || (storeNameList.size() != storeAddressList.size())){
            Toast.makeText(this, "매장 정보를 바르게 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Store> storeList = new ArrayList<>();
        for(int i = 0; i < storeNameList.size();i++){
            storeList.add(new Store(null, storeNameList.get(i).getText().toString(), storeAddressList.get(i).getText().toString()));
        }
        Manager newManager = new Manager(null, id, password, storeList);
        Call<Manager> join = service.join(newManager);
        join.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SignActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                Toast.makeText(SignActivity.this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                Log.e("join error", t.getMessage());
            }
        });
    }
}