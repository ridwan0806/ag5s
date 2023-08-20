package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.Model.Order;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.History.VHHistoryCashInOrder;
import com.hanindya.ag5s.ViewHolder.History.VHHistoryCashInOrderItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class HistoryCashInOrders extends AppCompatActivity {
    String orderDate = "";

    DatabaseReference root,dbUser,dbOrder;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    RecyclerView recyclerView,recyclerViewItem;
    RecyclerView.LayoutManager layoutManager,layoutManagerItem;
    ProgressBar progressBar,progressBarItem;
    FirebaseRecyclerAdapter<Order, VHHistoryCashInOrder> adapter;
    FirebaseRecyclerAdapter<OrderItem, VHHistoryCashInOrderItem> adapterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cash_in_orders);

        recyclerView = findViewById(R.id.rvHistoryCashInOrders);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.pbHistoryCashInOrders);

        TextView dateInfo = findViewById(R.id.txtHistoryCashInOrdersDate);

        root = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        dbUser = root.child("Users").child(userId);

        if (getIntent() != null){
            orderDate = getIntent().getStringExtra("orderDate");
            dateInfo.setText(orderDate);
        }

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbOrder = root.child("Orders").child(branchName);
                getListOrder(orderDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void getListOrder(String orderDate) {
        FirebaseRecyclerOptions<Order> listOrder =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(dbOrder.child(orderDate).orderByChild("orderStatus").equalTo("Paid"),Order.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Order, VHHistoryCashInOrder>(listOrder) {
            @Override
            protected void onBindViewHolder(@NonNull VHHistoryCashInOrder holder, int position, @NonNull Order model) {
                String orderId = adapter.getRef(position).getKey();

                NumberFormat formatRp = new DecimalFormat("#,###");
                double total = model.getTotalBill();
                int number = position + 1;

                holder.historyCashInOrderNo.setText(number+".");
                holder.historyCashInOrderName.setText(model.getCustomerName());
                holder.historyCashInOrderBill.setText(formatRp.format(total));
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        viewOrderDetail(orderId);
                    }
                });
            }

            @NonNull
            @Override
            public VHHistoryCashInOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_history_cash_in_orders,parent,false);
                return new VHHistoryCashInOrder(view);
            }

            @Override
            public void onDataChanged(){
                if (progressBar!=null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void viewOrderDetail(String orderId) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(HistoryCashInOrders.this);
        dialog.setCancelable(false);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_history_cash_in_order_items,null);
        dialog.setView(dialogView);

        recyclerViewItem = dialogView.findViewById(R.id.RVHistoryCashInOrderItems);
        layoutManagerItem = new LinearLayoutManager(this);
        recyclerViewItem.setLayoutManager(layoutManagerItem);
        progressBarItem = dialogView.findViewById(R.id.pbHistoryCashInOrderItems);

        FirebaseRecyclerOptions<OrderItem> listOrderItem =
                new FirebaseRecyclerOptions.Builder<OrderItem>()
                        .setQuery(dbOrder.child(orderDate).child(orderId).child("orderItem"),OrderItem.class)
                        .build();
        adapterItem = new FirebaseRecyclerAdapter<OrderItem, VHHistoryCashInOrderItem>(listOrderItem) {
            @Override
            protected void onBindViewHolder(@NonNull VHHistoryCashInOrderItem holder, int position, @NonNull OrderItem model) {
                int number = position + 1;
                NumberFormat formatRp = new DecimalFormat("#,###");
                double total = model.getSubtotal();

                holder.historyCashInOrderItemNumberOfItem.setText(number+".");
                holder.historyCashInOrderItemNumberNameFood.setText(model.getFoodName());
                holder.historyCashInOrderItemNumberQty.setText(String.valueOf(model.getQty()));
                holder.historyCashInOrderItemNumberTotalBill.setText(formatRp.format(total));
            }

            @NonNull
            @Override
            public VHHistoryCashInOrderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_history_cash_in_order_items,parent,false);
                return new VHHistoryCashInOrderItem(view);
            }

            @Override
            public void onDataChanged(){
                if (progressBarItem!=null){
                    progressBarItem.setVisibility(View.GONE);
                }
            }
        };
        adapterItem.startListening();
        recyclerViewItem.setAdapter(adapterItem);

        dialog.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
}