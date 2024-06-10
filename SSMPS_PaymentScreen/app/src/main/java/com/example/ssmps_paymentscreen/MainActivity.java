package com.example.ssmps_paymentscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ListViewAdapter.OnDataChangeListener {

    List<ListViewItem> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button startButton = findViewById(R.id.payment_button);

        startButton.setOnClickListener(view -> new Thread(() -> {

            Intent intent = new Intent(getApplicationContext(), start.class);
            startActivity(intent);
            try (Socket client = new Socket()) {
                InetSocketAddress ipep = new InetSocketAddress("1.234.5.119", 12345);

                client.connect(ipep);
                try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream()) {
                    JSONObject jsonObj = new JSONObject();
                    for (int i = 0; i < items.size(); i++) {
                        jsonObj.put(items.get(i).getText(), items.get(i).getPrice()+":"+items.get(i).getCnt());  // Modify with your JSON data
                    }
                    String msg = jsonObj.toString();
                    byte[] data = msg.getBytes();

                    ByteBuffer b = ByteBuffer.allocate(4);
                    b.order(ByteOrder.LITTLE_ENDIAN);
                    b.putInt(data.length);
                    sender.write(b.array(), 0, 4);
                    sender.write(data);

                    data = new byte[4];
                    int bytesRead = receiver.read(data, 0, 4);
                    if (bytesRead == -1) {
                        System.out.println("END");
                    } else if (bytesRead < 4) {
                        ByteBuffer bs = ByteBuffer.wrap(data);
                        bs.order(ByteOrder.LITTLE_ENDIAN);
                        int length = bs.getInt();
                        data = new byte[length];
                        bytesRead = receiver.read(data, 0, length);

                        if (bytesRead == -1) {
                            System.out.println("END");
                        } else if (bytesRead < length) {
                            msg = new String(data, StandardCharsets.UTF_8);
                            System.out.println(msg);
                        }
                    }

                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }).start());
        items = new ArrayList<>();

        items.add(new ListViewItem("포카리", 2000, 2));
        items.add(new ListViewItem("코카콜라", 3000, 3));
        items.add(new ListViewItem("빼빼로", 4000, 3));
        items.add(new ListViewItem("포카리", 2000, 2));
        items.add(new ListViewItem("빼빼로", 4000, 3));
        items.add(new ListViewItem("포카리", 2000, 2));
        items.add(new ListViewItem("코카콜라", 3000, 3));
        items.add(new ListViewItem("빼빼로", 4000, 3));
        items.add(new ListViewItem("포카리", 2000, 2));
        items.add(new ListViewItem("코카콜라", 3000, 3));
        items.add(new ListViewItem("빼빼로", 4000, 3));

        ListViewAdapter adapter = new ListViewAdapter(this, items);
        ListView listView = findViewById(R.id.product_list);
        adapter.setOnDataChangeListener(this);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        updateAllPrice();
    }
    @Override
    public void onDataChanged(){
        updateAllPrice();
    }
    @SuppressLint("SetTextI18n")
    public void updateAllPrice(){
        TextView sum_price = findViewById(R.id.sum_price);
        int s_price = 0;
        for(int i = 0; i < items.size(); i++){
            s_price = s_price + Integer.parseInt(items.get(i).getAll_price());
        }
        sum_price.setText("총 금액: " + s_price);
        Toast.makeText(getApplicationContext(), "됨" + s_price, Toast.LENGTH_SHORT).show();
    }
}