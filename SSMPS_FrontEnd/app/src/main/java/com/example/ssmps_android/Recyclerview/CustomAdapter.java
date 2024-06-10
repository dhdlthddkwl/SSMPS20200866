package com.example.ssmps_android.Recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssmps_android.domain.LoginType;
import com.example.ssmps_android.guest.StoreViewActivity;
import com.example.ssmps_android.manager.FunctionSelectActivity;
import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Store> localDataSet = new ArrayList<>();
    Context context;
    LoginType type;

    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView, textView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
        }

        public TextView getTextView() {
            return textView;
        }
        public TextView getTextView2(){
            return textView2;
        }
    }

    public CustomAdapter(List<Store> dataSet, Context context, LoginType type) {
        localDataSet = dataSet;
        this.context = context;
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        gson = new GsonBuilder().create();
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_storeselect, parent, false);
        context = parent.getContext();
        CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Store store = localDataSet.get(position);

        holder.textView.setText(store.getName());
        holder.textView2.setText(store.getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(type == LoginType.MANAGER){
                    // 매니저
                    intent = new Intent(context, FunctionSelectActivity.class);
                }else{
                    // 고객
                    intent = new Intent(context, StoreViewActivity.class);
                }
                sharedPreferenceUtil.putData("store", gson.toJson(store, Store.class));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
