package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

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
import com.hanindya.ag5s.ViewHolder.VHOrderDetail;

public class CashierOrderDetail extends AppCompatActivity {
    String orderId = "";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<OrderItem, VHOrderDetail>adapter;
    Order currentOrder;

    DatabaseReference root,dbUser,dbOrder;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_order_detail);

        if (getIntent()!=null){
            orderId = getIntent().getStringExtra("orderId");
        }

        recyclerView = findViewById(R.id.rv_order_detail_item);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        root = FirebaseDatabase.getInstance().getReference();
        dbUser = root.child("Users").child(userId);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);

                dbOrder = root.child("Orders").child(branchName);
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
                // get parent order like : cust name, created datetime, subtotal all..


                FirebaseRecyclerOptions<OrderItem> listItem =
                        new FirebaseRecyclerOptions.Builder<OrderItem>()
                                .setQuery(dbOrder.child(orderId).child("orderItem"),OrderItem.class)
                                .build();
                adapter = new FirebaseRecyclerAdapter<OrderItem, VHOrderDetail>(listItem) {
                    @Override
                    protected void onBindViewHolder(@NonNull VHOrderDetail holder, int position, @NonNull OrderItem model) {
                        String orderItemId = adapter.getRef(position).getKey();
                        String orderItemQty = String.valueOf(adapter.getItem(position).getQty());
                        String orderItemPrice = String.valueOf(adapter.getItem(position).getPrice());
                        
                        int number = position + 1;
                        holder.numberCount.setText(String.valueOf(number));
                        holder.foodName.setText(model.getFoodName());
                        holder.qty.setText(String.valueOf(model.getQty()));
                        holder.price.setText(String.valueOf(model.getPrice()));
                        holder.subtotal.setText(String.valueOf(model.getSubtotal()));
                        
                        holder.menu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PopupMenu popupMenu = new PopupMenu(CashierOrderDetail.this,holder.menu);
                                popupMenu.getMenuInflater().inflate(R.menu.menu_cart_order,popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        int itemId = menuItem.getItemId();
                                        if (itemId == R.id.cart_order_edit_qty){
                                            Toast.makeText(CashierOrderDetail.this, ""+orderItemQty, Toast.LENGTH_SHORT).show();
                                        } else if (itemId == R.id.cart_order_edit_price){
                                            Toast.makeText(CashierOrderDetail.this, ""+orderItemPrice, Toast.LENGTH_SHORT).show();
                                        } else if (itemId == R.id.cart_order_delete_food){
                                            deleteOrderItem(orderItemId);
                                        }
                                        return true;
                                    }
                                });
                                popupMenu.show();
                            }

                            private void deleteOrderItem(String orderItemId) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public VHOrderDetail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_order_detail, parent, false);
                        return new VHOrderDetail(view);
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