package com.example.ssmps_android.Recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssmps_android.ItemRegisterActivity;
import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.CenterItem;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.dto.CenterItemResponse;
import com.example.ssmps_android.manager.ItemModifyDeleteActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ManagerModifyDeleteAdapter extends RecyclerView.Adapter<ManagerModifyDeleteAdapter.ViewHolder> {
    private List<Item> itemList = new ArrayList<>();
    Context context;

    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemSearchManager_name);
            image = itemView.findViewById(R.id.itemSearchManager_image);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ManagerModifyDeleteAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

//    public CustomAdapter2(List<Item> dataSet){
//
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_itemsearchmanager, parent, false);
        ManagerModifyDeleteAdapter.ViewHolder viewHolder = new ManagerModifyDeleteAdapter.ViewHolder(view);
        context = parent.getContext();


        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        gson = new GsonBuilder().create();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.textView.setText(item.getName());
        holder.image.setImageBitmap(byteToImage(item.getImage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemModifyDeleteActivity.class);
                sharedPreferenceUtil.putData("item", gson.toJson(item));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private Bitmap byteToImage(String b){
        try {
            byte[] encodeByte = Base64.decode(b, Base64.DEFAULT);
            // Base64 코드를 디코딩하여 바이트 형태로 저장
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            // 바이트 형태를 디코딩하여 비트맵 형태로 저장
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
