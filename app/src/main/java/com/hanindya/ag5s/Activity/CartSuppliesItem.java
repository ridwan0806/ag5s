package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
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
import com.hanindya.ag5s.Adapter.CartSuppliesItemAdapter;
import com.hanindya.ag5s.Helper.DatabaseSuppliesOrderItem;
import com.hanindya.ag5s.MainActivity;
import com.hanindya.ag5s.Model.Cost;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.Model.SuppliesOrder;
import com.hanindya.ag5s.Model.SuppliesOrderItem;
import com.hanindya.ag5s.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartSuppliesItem extends AppCompatActivity {
    String userId,branchName,userName;
    DatabaseReference dbSuppliesOrder,dbCost;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<SuppliesOrderItem> suppliesItem = new ArrayList<>();
    CartSuppliesItemAdapter adapter;

    TextView btnConfirmOrder,subtotalPrice,suppliesOrderDate,suppliesOrderNotes,suppliesOrderAttachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_supplies_item);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbUser = root.child("Users").child(userId);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbSuppliesOrder = root.child("SuppliesOrder").child(branchName);
                dbCost = root.child("Cost").child(branchName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        recyclerView = findViewById(R.id.rvCartSuppliesItem);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        subtotalPrice = findViewById(R.id.txtCartSuppliesOrderSubtotal);

        // Get RecyclerView OrderItem
        loadListSuppliesItem();

        btnConfirmOrder = findViewById(R.id.btnCartSuppliesItemConfirm);
        btnConfirmOrder.setOnClickListener(view -> {
            confirmSuppliesOrder();
        });
    }

    private void confirmSuppliesOrder() {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setCancelable(false);
        confirm.setMessage("Konfirmasi Belanjaan");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_confirm_supplies_order,null);
        confirm.setView(layout);

        suppliesOrderDate = layout.findViewById(R.id.tvSuppliesOrderAddDate);
        suppliesOrderNotes = layout.findViewById(R.id.tvSuppliesOrderAddNotes);
        suppliesOrderAttachment = layout.findViewById(R.id.tvSuppliesOrderAddAttachment);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        suppliesOrderDate.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;

                    String monthString = "";
                    if (month < 10){
                        monthString = "0"+month;
                    } else {
                        monthString = String.valueOf(month);
                    }

                    String dayString = "";
                    if (day < 10){
                        dayString = "0"+day;
                    } else {
                        dayString = String.valueOf(day);
                    }

                    suppliesOrderDate.setText(year+"-"+monthString+"-"+dayString);
                }
            }, year, month, day);
            dialog.show();
        });

        confirm.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        confirm.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (suppliesOrderDate.getText().equals("--Pilih Tanggal--")){
                    Toast.makeText(CartSuppliesItem.this, "Gagal. Invalid Date", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long serverTimeOffset = snapshot.getValue(Long.class);
                            long estimateServerTime = System.currentTimeMillis()+serverTimeOffset;

                            SimpleDateFormat createdDateTime;
                            createdDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateNow = new Date(estimateServerTime);

                            SuppliesOrder suppliesOrder = new SuppliesOrder();
                            suppliesOrder.setDate(suppliesOrderDate.getText().toString());
                            suppliesOrder.setUrlAttachment("");
                            suppliesOrder.setNotes(suppliesOrderNotes.getText().toString());
                            suppliesOrder.setCreatedBy(userName);
                            suppliesOrder.setCreatedDateTime(createdDateTime.format(dateNow));
                            suppliesOrder.setSubtotal(Double.parseDouble(subtotalPrice.getText().toString()));

                            String orderId = dbSuppliesOrder.child(suppliesOrderDate.getText().toString()).push().getKey();
                            dbSuppliesOrder.child(suppliesOrderDate.getText().toString()).child(orderId).setValue(suppliesOrder);

                            //Insert Items
                            HashMap<String,SuppliesOrderItem> items = new HashMap<>();
                            for (int i = 0; i < suppliesItem.size(); i++){
                                String id = suppliesItem.get(i).getId();
                                String name = suppliesItem.get(i).getName();
                                String category = suppliesItem.get(i).getCategory();
                                String notes = suppliesItem.get(i).getNotes();
                                String qty = suppliesItem.get(i).getQty();
                                String units = suppliesItem.get(i).getUnits();
                                String price = String.valueOf(suppliesItem.get(i).getPrice());
                                String subtotal = String.valueOf(suppliesItem.get(i).getSubtotal());

                                SuppliesOrderItem list = new SuppliesOrderItem(id,name,category,notes,qty,units,Double.parseDouble(price),Double.parseDouble(subtotal));
                                items.put(dbSuppliesOrder.child(suppliesOrderDate.getText().toString()).child(orderId).child("suppliesItem").push().getKey(),list);
                                suppliesOrder.setItems(items);
                                dbSuppliesOrder.child(suppliesOrderDate.getText().toString()).child(orderId).child("suppliesItem").setValue(items);
                            }

                            // Auto Insert Cost Reference = "Belanja Bahan Baku"
                            Cost cost = new Cost();
                            cost.setName("BELANJA BAHAN BAKU");
                            cost.setDate(suppliesOrderDate.getText().toString());
                            cost.setUrlAttachment("");
                            cost.setCreatedBy(userName);
                            cost.setCreatedDateTime(createdDateTime.format(dateNow));
                            cost.setNotes(suppliesOrderNotes.getText().toString());
                            cost.setSubtotal(Double.parseDouble(subtotalPrice.getText().toString()));

                            dbCost.child(suppliesOrderDate.getText().toString()).push().setValue(cost);

                            // Clean Cart Supplies Items
                            new DatabaseSuppliesOrderItem(getBaseContext()).cleanAll();
                            Toast.makeText(CartSuppliesItem.this, "Sukses. Belanjaan berhasil diinput", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(CartSuppliesItem.this, MainActivity.class);
                            startActivity(home);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                }
            }
        });

        confirm.show();
    }

    private void loadListSuppliesItem() {
        suppliesItem = new DatabaseSuppliesOrderItem(this).getAllSuppliesOrderItems();
        adapter = new CartSuppliesItemAdapter(this,suppliesItem);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        int total = 0;
        for (SuppliesOrderItem list:suppliesItem){
            total += list.getSubtotal();
        }
        subtotalPrice.setText(String.valueOf(total));
    }
}