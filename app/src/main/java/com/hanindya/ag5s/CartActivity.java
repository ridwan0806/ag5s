package com.hanindya.ag5s;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hanindya.ag5s.Adapter.CartOrderAdapter;
import com.hanindya.ag5s.Helper.DatabaseOrderItem;
import com.hanindya.ag5s.Model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<OrderItem> orderItem = new ArrayList<>();
    CartOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.rvCartOder);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        loadListOrderItem();
        orderItem = new DatabaseOrderItem(this).getAllOrderItems();
        adapter = new CartOrderAdapter(this,orderItem);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadListOrderItem() {
        orderItem = new DatabaseOrderItem(this).getAllOrderItems();
        adapter = new CartOrderAdapter(this,orderItem);
        recyclerView.setAdapter(adapter);
    }
}