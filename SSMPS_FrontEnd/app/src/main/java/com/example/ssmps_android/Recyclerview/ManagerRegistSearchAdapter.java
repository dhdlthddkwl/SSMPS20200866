package com.example.ssmps_android.Recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ManagerRegistSearchAdapter extends RecyclerView.Adapter<ManagerRegistSearchAdapter.ViewHolder> {

    private List<CenterItem> itemList = new ArrayList<>();
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

    public ManagerRegistSearchAdapter(List<CenterItem> itemList) {
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ManagerRegistSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_itemsearchmanager, parent, false);
        ManagerRegistSearchAdapter.ViewHolder viewHolder = new ManagerRegistSearchAdapter.ViewHolder(view);
        context = parent.getContext();

        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        gson = new GsonBuilder().create();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerRegistSearchAdapter.ViewHolder holder, int position) {
        CenterItem item = itemList.get(position);

        holder.textView.setText(item.getName());
        setItemImage(holder.image, item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemRegisterActivity.class);
                sharedPreferenceUtil.putData("item", gson.toJson(item));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void setItemImage(ImageView image, CenterItem item){
        Bitmap bitmap = byteToImage(item.getImage());
        image.setImageBitmap(bitmap);
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

    public static String decompress(String compressedStr) {
        StringBuilder decompressed = new StringBuilder();

        for (int i = 0; i < compressedStr.length(); i += 2) {
            char c = compressedStr.charAt(i);
            int count = Character.getNumericValue(compressedStr.charAt(i + 1));

            for (int j = 0; j < count; j++) {
                decompressed.append(c);
            }
        }

        return decompressed.toString();
    }
}
