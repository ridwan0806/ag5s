package com.hanindya.ag5s;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import com.hanindya.ag5s.Activity.FoodsActivity;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout food,supplies,cashier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        food = findViewById(R.id.ic_food);

        food.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, FoodsActivity.class);
            startActivity(i);
        });
    }
}