package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hanindya.ag5s.Model.Order;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.VHOrderDetailComplete;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CashierOrderDetailCancel extends AppCompatActivity {
    String orderId = "";
    String orderDate = "";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<OrderItem, VHOrderDetailComplete> adapter;
    Order currentOrder;

    DatabaseReference root,dbUser,dbOrder;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    private TextView customerName,createdDateTime,cancelDateTime,cancelBy,status,orderType,customerType,totalBill,cancelReason;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_order_detail_cancel);

        if (getIntent()!=null){
            orderId = getIntent().getStringExtra("orderId");
        }

        recyclerView = findViewById(R.id.rvOrderDetailItemCancel);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        customerName = findViewById(R.id.txtOrderDetailCashierCompleteCustomerName);
        createdDateTime = findViewById(R.id.txtOrderDetailCashierCompleteCreatedDateTime);
        cancelDateTime = findViewById(R.id.txtOrderDetailCashierCompleteCompleteDateTime);
        cancelBy = findViewById(R.id.txtOrderDetailCashierCompleteCompleteBy);

        status = findViewById(R.id.txtOrderDetailCashierCompleteStatusOrder);
        orderType = findViewById(R.id.txtOrderDetailCashierCompleteOrderType);
        customerType = findViewById(R.id.txtOrderDetailCashierCompleteCustomerType);

        totalBill = findViewById(R.id.txtOrderDetailCashierCompleteTotalBill);

        cancelReason = findViewById(R.id.txtOrderDetailCashierCompleteCancelReason);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference serverTime = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        serverTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long serverTimeOffset = snapshot.getValue(Long.class);
                long estimateServerTime = System.currentTimeMillis()+serverTimeOffset;

                SimpleDateFormat date;
                date = new SimpleDateFormat("yyyy-MM-dd");

                Date resultDate = new Date(estimateServerTime);
                orderDate = date.format(resultDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        dbUser = root.child("Users").child(userId);
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbOrder = root.child("Orders").child(branchName).child(orderDate);
                getListOrderItems(orderId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void getListOrderItems(String orderId) {
        dbOrder.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentOrder = snapshot.getValue(Order.class);

                NumberFormat formatRp = new DecimalFormat("#,###");
                double totBill = currentOrder.getTotalBill();

                customerName.setText(currentOrder.getCustomerName());
                createdDateTime.setText(currentOrder.getCreatedDateTime());
                cancelDateTime.setText(currentOrder.getCancelDateTime());
                cancelBy.setText(currentOrder.getCancelBy());

                status.setText(currentOrder.getOrderStatus());
                orderType.setText(currentOrder.getOrderType());
                customerType.setText(currentOrder.getCustomerType());

                totalBill.setText(formatRp.format(totBill));
                cancelReason.setText(currentOrder.getCancelReason());

                FirebaseRecyclerOptions<OrderItem> listItem =
                        new FirebaseRecyclerOptions.Builder<OrderItem>()
                                .setQuery(dbOrder.child(orderId).child("orderItem"),OrderItem.class)
                                .build();
                adapter = new FirebaseRecyclerAdapter<OrderItem, VHOrderDetailComplete>(listItem) {
                    @Override
                    protected void onBindViewHolder(@NonNull VHOrderDetailComplete holder, int position, @NonNull OrderItem model) {
                        int number = position + 1;
                        holder.numberCount.setText(String.valueOf(number));

                        double price = model.getPrice();
                        double subtotal = model.getSubtotal();

                        holder.foodName.setText(model.getFoodName());
                        holder.qty.setText(String.valueOf(model.getQty()));
                        holder.price.setText(formatRp.format(price));
                        holder.subtotal.setText(formatRp.format(subtotal));
                    }

                    @NonNull
                    @Override
                    public VHOrderDetailComplete onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_order_detail_complete, parent, false);
                        return new VHOrderDetailComplete(view);
                    }
                };
                adapter.startListening();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}