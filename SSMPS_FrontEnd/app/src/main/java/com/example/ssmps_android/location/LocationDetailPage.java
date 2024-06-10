package com.example.ssmps_android.location;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssmps_android.R;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationDetailPage extends AppCompatActivity {
    TableLayout tableLayout;
    List<TableRow> tableRowList = new ArrayList<>();
    List<Item> itemList = new ArrayList<>();
    String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tableRowList.add(findViewById(R.id.location_detail_row1));
        tableRowList.add(findViewById(R.id.location_detail_row2));
        tableLayout = findViewById(R.id.location_detail_table);

        initData();
        setType();

        Intent intent = getIntent();
        Location location = (Location) intent.getSerializableExtra("location");
        Toast.makeText(this, location.getStartX() + "", Toast.LENGTH_SHORT).show();

        findViewById(R.id.location_detail_add_col).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 열 추가
                TableRow tableRow = tableRowList.get(0);
                TextView textView = new TextView(getApplicationContext());
                textView.setText("+");
                textView.setTextSize(50);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(LocationDetailPage.this, ((TextView)v).getText().toString(), Toast.LENGTH_SHORT).show();
                        addItem(v);
                    }
                });
                tableRow.addView(textView);
            }
        });

        findViewById(R.id.location_detail_add_row).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 행 추가
                TableRow tableRow = new TableRow(getApplicationContext());
                TextView textView = new TextView(getApplicationContext());

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;
                textView.setText("+");
                textView.setTextSize(50);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(LocationDetailPage.this, ((TextView)v).getText().toString() , Toast.LENGTH_SHORT).show();
                        String text = ((TextView)v).getText().toString();
                        if(text.equals("+")){
                            Intent intent1 = new Intent();
                        }
                        addItem(v);
                    }
                });

                tableRow.setLayoutMode(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(textView, layoutParams);
                tableLayout.addView(tableRow);
            }
        });
    }

    private void initData(){
        Item item = new Item(1L, null, "삼양라면이벤트입니다", "라면", 20000, 10, "12345", null, "01010101");
        itemList.add(item);
    }
    public void addItem(View v){
        Toast.makeText(this, "click test", Toast.LENGTH_SHORT).show();
        TextView textView = (TextView)v;
        textView.setText(itemList.get(0).getName());
        textView.setTextSize(30);
    }

    private void setType(){
        for(Item item : itemList){
            type += item.getType() + "/";
        }
    }
}