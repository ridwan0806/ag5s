package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Adapter.CartOrderAdapter;
import com.hanindya.ag5s.Helper.DatabaseOrderItem;
import com.hanindya.ag5s.MainActivity;
import com.hanindya.ag5s.Model.Order;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<OrderItem> orderItem = new ArrayList<>();
    CartOrderAdapter adapter;

    FirebaseUser firebaseUser;
    String userId,branchName,userName;
    DatabaseReference root,dbUser,dbOrder;

    TextView btnConfirmOrder,subtotalItem,subtotalPrice;
    EditText customerName;

    int rdOrderTypeId = 0;
    int rdCustomerTypeId = 0;
    String rdOrderType = "";
    String rdCustomerType = "";
    int totalItem = 0;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.rvCartOder);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnConfirmOrder = findViewById(R.id.btnCartOrderConfirm);
        subtotalItem = findViewById(R.id.txtCartSubtotalQty);
        subtotalPrice = findViewById(R.id.txtCartSubtotalPrice);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        root = FirebaseDatabase.getInstance().getReference();
        dbUser = root.child("Users").child(userId);
        dbOrder = root.child("Orders");

        //Get User Branch..
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        // Get RecyclerView OrderItem
        loadListOrderItem();

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrder();
            }
        });

    }

    private void confirmOrder() {
        AlertDialog.Builder confirm = new AlertDialog.Builder(CartActivity.this);
        confirm.setCancelable(false);
        confirm.setMessage("Halaman Konfirmasi Pemesanan");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_confirm_order,null);
        confirm.setView(view);

        customerName = view.findViewById(R.id.txtConfirmOrderCustName);

        confirm.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        confirm.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (rdOrderTypeId == 0){
                    AlertDialog.Builder failed = new AlertDialog.Builder(CartActivity.this);
                    failed.setCancelable(false);
                    failed.setMessage("Gagal Tersimpan. Tipe Pesanan belum dipilih");
                    failed.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            // reset radio customer..
                            rdCustomerTypeId = 0;
                        }
                    });
                    failed.show();
                } else if (rdCustomerTypeId == 0){
                    AlertDialog.Builder failed = new AlertDialog.Builder(CartActivity.this);
                    failed.setCancelable(false);
                    failed.setMessage("Gagal Tersimpan. Tipe Konsumen belum dipilih");
                    failed.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            // reset radio orderType..
                            rdOrderTypeId = 0;
                        }
                    });
                    failed.show();
                } else {
                    submitOrder();
                }
            }
        });

        confirm.show();
    }

    private void submitOrder() {
        final DatabaseReference serverTime = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        serverTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long serverTimeOffset = snapshot.getValue(Long.class);
                long estimateServerTime = System.currentTimeMillis()+serverTimeOffset;

                SimpleDateFormat createdDateTime,date;
                createdDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = new SimpleDateFormat("yyyy-MM-dd");
                Date resultDate = new Date(estimateServerTime);

                String orderDate = date.format(resultDate);

                Order order = new Order();
                order.setCustomerName(customerName.getText().toString());
                order.setCustomerType(rdCustomerType);
                order.setOrderType(rdOrderType);
                order.setCreatedByUser(userName);
                order.setCreatedDateTime(createdDateTime.format(resultDate));
                order.setCreatedDate(date.format(resultDate));
                order.setOrderStatus("Process");
                order.setCancelReason("");
                order.setTotalItem(totalItem);
                order.setTotalBill(totalPrice);
                order.setPaymentNominal(0);
                order.setChange(0);
                order.setPaymentMethod("");
                order.setCompleteBy("");
                order.setCompleteDateTime("");
                order.setCancelBy("");
                order.setCancelDateTime("");

                String orderId = dbOrder.child(branchName).child(orderDate).push().getKey();
                dbOrder.child(branchName).child(orderDate).child(orderId).setValue(order);

                //Insert Items
                HashMap<String,OrderItem> orderItems = new HashMap<>();
                for (int i = 0; i < orderItem.size(); i++){
                    String id = orderItem.get(i).getId();
                    String foodId = orderItem.get(i).getFoodId();
                    String foodName = orderItem.get(i).getFoodName();
                    int foodQty = orderItem.get(i).getQty();
                    double foodPrice = orderItem.get(i).getPrice();
                    double foodSubtotal = orderItem.get(i).getSubtotal();

                    OrderItem listItem = new OrderItem(id,foodId,foodName,foodQty,foodPrice,foodSubtotal);

                    orderItems.put(dbOrder.child(branchName).child(orderDate).child(orderId).child("orderItem").push().getKey(),listItem);
                    order.setOrderItem(orderItems);
                    dbOrder.child(branchName).child(orderDate).child(orderId).child("orderItem").setValue(orderItems);

                    // Clean Cart
                    new DatabaseOrderItem(getBaseContext()).cleanAll();
                    Toast.makeText(CartActivity.this, "Pesanan berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(CartActivity.this, MainActivity.class);
                    startActivity(home);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void loadListOrderItem() {
        orderItem = new DatabaseOrderItem(this).getAllOrderItems();
        adapter = new CartOrderAdapter(this,orderItem);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        NumberFormat formatRp = new DecimalFormat("#,###");

        for (OrderItem list:orderItem){
            totalItem += list.getQty();
            totalPrice += (list.getQty())*(list.getPrice());
        }
        subtotalItem.setText(String.valueOf(totalItem));
        subtotalPrice.setText(formatRp.format(totalPrice));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.rdDineIn:
                if (checked){
                    rdOrderTypeId = 1;
                    rdOrderType = "Dine In";
                }
                break;
            case R.id.rdTakeAway:
                if (checked){
                    rdOrderTypeId = 2;
                    rdOrderType = "Take Away";
                }
                break;
            case R.id.rdUmum:
                if (checked){
                    rdCustomerTypeId = 1;
                    rdCustomerType = "Umum";
                }
                break;
            case R.id.rdOnline:
                if (checked){
                    rdCustomerTypeId = 2;
                    rdCustomerType = "Online";
                }
                break;
            case R.id.rdKaryawan:
                if (checked){
                    rdCustomerTypeId = 3;
                    rdCustomerType = "Karyawan";
                }
                break;
            case R.id.rdKerabat:
                if (checked){
                    rdCustomerTypeId = 4;
                    rdCustomerType = "Kerabat";
                }
                break;
        }
    }

}