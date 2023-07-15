package com.hanindya.ag5s.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hanindya.ag5s.Adapter.CartOrderAdapter;
import com.hanindya.ag5s.Adapter.CartSuppliesItemAdapter;
import com.hanindya.ag5s.Helper.DatabaseSuppliesOrderItem;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.Model.SuppliesOrderItem;
import com.hanindya.ag5s.R;

import java.util.ArrayList;
import java.util.List;

public class CartSuppliesItem extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<SuppliesOrderItem> suppliesItem = new ArrayList<>();
    CartSuppliesItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_supplies_item);

        recyclerView = findViewById(R.id.rvCartSuppliesItem);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Get RecyclerView OrderItem
        loadListSuppliesItem();
    }

    private void loadListSuppliesItem() {
        suppliesItem = new DatabaseSuppliesOrderItem(this).getAllSuppliesOrderItems();
        adapter = new CartSuppliesItemAdapter(this,suppliesItem);
        recyclerView.setAdapter(adapter);
    }
}