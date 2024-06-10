package com.example.ssmps_android.Recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.guest.GuestItemInfoActivity;
import com.example.ssmps_android.guest.GuestItemListActivity;
import com.example.ssmps_android.guest.StoreViewActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter4 extends RecyclerView.Adapter<CustomAdapter4.ViewHolder> {

    private List<Item> localDataSet = new ArrayList<>();
    Context context;

    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private Button locationBtn, infoBtn;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            locationBtn = itemView.findViewById(R.id.guest_item_location_btn);
            infoBtn = itemView.findViewById(R.id.guest_item_information_btn);
            image = itemView.findViewById(R.id.guest_item_image);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public CustomAdapter4(List<Item> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_itemlistguest, parent, false);
        CustomAdapter4.ViewHolder viewHolder = new CustomAdapter4.ViewHolder(view);
        context = parent.getContext();

        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        gson = new GsonBuilder().create();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = localDataSet.get(position);

        holder.textView.setText(item.getName());
        holder.image.setImageBitmap(byteToImage(item.getImage()));

        holder.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoreViewActivity.class);
//                Intent intent = ((Activity) context).getIntent();
                sharedPreferenceUtil.putData("item", gson.toJson(item));
//                intent.putExtra("item", item);
                Log.e("item", item.getName());
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                ((Activity) context).finish();
            }
        });

        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GuestItemInfoActivity.class);
                sharedPreferenceUtil.putData("item", gson.toJson(item));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
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