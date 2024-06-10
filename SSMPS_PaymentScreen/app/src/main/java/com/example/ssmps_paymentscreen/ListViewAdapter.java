package com.example.ssmps_paymentscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<ListViewItem> items;

    public interface OnDataChangeListener{
        void onDataChanged();
    }
    OnDataChangeListener mOnDataChangeListener;
    public ListViewAdapter(Context context, List<ListViewItem> items) {
        this.context = context;
        this.items = items;
    }
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ListViewItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        final ListViewItem item = getItem(position);

        TextView text1 = convertView.findViewById(R.id.product_name);
        TextView text2 = convertView.findViewById(R.id.price);
        TextView text3 = convertView.findViewById(R.id.cnt);
        TextView text4 = convertView.findViewById(R.id.all_price);

        if (item != null) {
            text1.setText(item.getText());
            text2.setText(item.getPrice());
            text3.setText(item.getCnt());
            text4.setText(item.getAll_price());
        }

        Button deleteButton = convertView.findViewById(R.id.delButton);
        deleteButton.setOnClickListener(v -> {
            Toast.makeText(context, "삭제됨", Toast.LENGTH_SHORT).show();
            items.remove(position);
            notifyDataSetChanged();
            if(mOnDataChangeListener != null){
                mOnDataChangeListener.onDataChanged();
            }
        });

        return convertView;
    }
}