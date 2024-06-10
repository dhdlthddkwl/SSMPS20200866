package com.example.ssmps_paymentscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;

public class start extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
