package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.hanindya.ag5s.Activity.CashierActivity;
import com.hanindya.ag5s.Helper.MoneyTextWatcher;
import com.hanindya.ag5s.Model.Order;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.ViewHolder.VHOrderDetail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CashierOrderDetail extends AppCompatActivity {
    String orderId = "";
    String orderDate = "";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<OrderItem, VHOrderDetail>adapter;
    Order currentOrder;

    DatabaseReference root,dbUser,dbOrder;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    EditText etEditPrice,etCancelReason;
    ImageView plusBtn,minBtn;
    int numberOrder = 1;
    TextView lastQty,btnCancelOrder;

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

        btnCancelOrder = findViewById(R.id.btnOrderDetailCancelOrder);

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
                Log.d("TAG",""+orderDate);
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

        btnCancelOrder.setOnClickListener(view -> {
            AlertDialog.Builder confirm = new AlertDialog.Builder(CashierOrderDetail.this);
            confirm.setCancelable(false);
            confirm.setTitle("WARNING !");
            confirm.setMessage("Batalkan pesanan ini ?");
            confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cancelCurrentOrder(orderId);
                }
            });
            confirm.show();
        });
    }

    private void cancelCurrentOrder(String orderId) {
        AlertDialog.Builder cancelReason = new AlertDialog.Builder(CashierOrderDetail.this);
        cancelReason.setCancelable(false);
        cancelReason.setMessage("Transaksi akan dibatalkan");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_cancel_order_reason,null);
        cancelReason.setView(layout);

        etCancelReason = layout.findViewById(R.id.etOrderDetailCancelReason);

        cancelReason.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        cancelReason.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cancelNote = etCancelReason.getText().toString();

                if (etCancelReason.getText().toString().length() == 0){
                    cancelNote = "not set";
                    dbOrder.child(orderId).child("orderStatus").setValue("Cancel");
                    dbOrder.child(orderId).child("cancelReason").setValue(cancelNote);

                    Toast.makeText(CashierOrderDetail.this, "Sukses. Pesanan dibatalkan", Toast.LENGTH_SHORT).show();
                    Intent listOrder = new Intent(CashierOrderDetail.this, CashierActivity.class);
                    startActivity(listOrder);
                    finish();
                } else {
                    dbOrder.child(orderId).child("orderStatus").setValue("Cancel");
                    dbOrder.child(orderId).child("cancelReason").setValue(cancelNote);

                    Toast.makeText(CashierOrderDetail.this, "Sukses. Pesanan dibatalkan", Toast.LENGTH_SHORT).show();
                    Intent listOrder = new Intent(CashierOrderDetail.this, CashierActivity.class);
                    startActivity(listOrder);
                    finish();
                }
            }
        });

        cancelReason.show();
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
                    protected void onBindViewHolder(@NonNull VHOrderDetail holder, @SuppressLint("RecyclerView") int position, @NonNull OrderItem model) {
                        String orderItemId = adapter.getRef(position).getKey();
                        String orderItemName = adapter.getItem(position).getFoodName();
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
                                            editQtyItem(orderItemId,orderItemPrice,orderItemQty);
                                        } else if (itemId == R.id.cart_order_edit_price){
                                            editPriceItem(orderItemId,orderItemPrice,orderItemQty);
                                        } else if (itemId == R.id.cart_order_delete_food){
                                            AlertDialog.Builder confirm = new AlertDialog.Builder(CashierOrderDetail.this);
                                            confirm.setCancelable(false);
                                            confirm.setMessage("Hapus "+orderItemName+" dari pesanan ?");

                                            confirm.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });

                                            confirm.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    deleteOrderItem(orderItemId);
                                                }
                                            });
                                            confirm.show();
                                        }
                                        return true;
                                    }
                                });
                                popupMenu.show();
                            }

                            private void deleteOrderItem(String orderItemId) {
                                dbOrder.child(orderId).child("orderItem").child(orderItemId).removeValue();

                                //Re-calculate subtotal item & price (order parent)
                                DatabaseReference reference = dbOrder.child(orderId).child("orderItem");
                                ValueEventListener listener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int newSubtotalItem = 0;
                                        double newSubtotalPrice = 0;

                                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            int totalItem = dataSnapshot.child("qty").getValue(int.class);
                                            double totalPrice = Double.valueOf(dataSnapshot.child("subtotal").getValue(long.class));

                                            newSubtotalItem = newSubtotalItem + totalItem;
                                            newSubtotalPrice = newSubtotalPrice + totalPrice;
                                        }
                                        dbOrder.child(orderId).child("subtotalItem").setValue(newSubtotalItem);
                                        dbOrder.child(orderId).child("subtotalPrice").setValue(newSubtotalPrice);

                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(CashierOrderDetail.this, "Sukses. Item dihapus", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        throw error.toException();
                                    }
                                };
                                reference.addListenerForSingleValueEvent(listener);
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

    private void editQtyItem(String orderItemId, String orderItemPrice, String orderItemQty) {
        AlertDialog.Builder editQty = new AlertDialog.Builder(CashierOrderDetail.this);
        editQty.setCancelable(false);
        editQty.setMessage("Ubah Qty Pesanan");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_edit_qty,null);
        editQty.setView(layout);

        plusBtn = layout.findViewById(R.id.btnPlus);
        minBtn = layout.findViewById(R.id.btnMinus);
        lastQty = layout.findViewById(R.id.txtQty);
        lastQty.setText(orderItemQty);

        numberOrder = Integer.parseInt(orderItemQty);

        minBtn.setOnClickListener(view -> {
            if (numberOrder > 1){
                int newNumberOrder = Integer.parseInt(lastQty.getText().toString());
                numberOrder = newNumberOrder - 1;
                lastQty.setText(String.valueOf(numberOrder));
            }
        });

        plusBtn.setOnClickListener(view -> {
            int newNumberOrder = Integer.parseInt(lastQty.getText().toString());
            numberOrder = newNumberOrder + 1;
            lastQty.setText(String.valueOf(numberOrder));
        });

        editQty.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        editQty.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (numberOrder == Integer.parseInt(orderItemQty)){
                    Toast.makeText(CashierOrderDetail.this, "Gagal. Qty masih sama", Toast.LENGTH_SHORT).show();
                } else {
                    // update qty, subtotal (orderItem)
                    double newSubtotal = 0;
                    dbOrder.child(orderId).child("orderItem").child(orderItemId).child("qty").setValue(numberOrder);
                    newSubtotal = numberOrder * Double.parseDouble(orderItemPrice);
                    dbOrder.child(orderId).child("orderItem").child(orderItemId).child("subtotal").setValue(newSubtotal);

                    // update subtotalPrice & subtotalItem (order parent)
                    DatabaseReference reference = dbOrder.child(orderId).child("orderItem");
                    ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double newSubtotalAll = 0;
                            int newQtyAll = 0;
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                double subtotal = Double.valueOf(dataSnapshot.child("subtotal").getValue(long.class));
                                int qty = dataSnapshot.child("qty").getValue(int.class);

                                newQtyAll = newQtyAll + qty;
                                newSubtotalAll = newSubtotalAll + subtotal;
                            }
                            dbOrder.child(orderId).child("subtotalItem").setValue(newQtyAll);
                            dbOrder.child(orderId).child("subtotalPrice").setValue(newSubtotalAll);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    };
                    reference.addListenerForSingleValueEvent(listener);
                    Toast.makeText(CashierOrderDetail.this, "Sukses. Qty berhasil diubah.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editQty.show();
    }

    private void editPriceItem(String orderItemId, String orderItemPrice, String orderItemQty) {
        AlertDialog.Builder editPrice = new AlertDialog.Builder(CashierOrderDetail.this);
        editPrice.setCancelable(false);
        editPrice.setMessage("Ubah Harga Jual Satuan");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_edit_price,null);
        editPrice.setView(layout);

        etEditPrice = layout.findViewById(R.id.etFragmentFoodEditPrice);
        etEditPrice.addTextChangedListener(new MoneyTextWatcher(etEditPrice));

        editPrice.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        editPrice.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etEditPrice.getText().toString().length() == 0 | etEditPrice.getText().toString().equals("Rp 0")){
                    Toast.makeText(CashierOrderDetail.this, "Gagal. Harga Invalid.", Toast.LENGTH_SHORT).show();
                } else {
                    BigDecimal bigDecimal = MoneyTextWatcher.parseCurrencyValue(etEditPrice.getText().toString());
                    String cleanEditTextPrice = String.valueOf(bigDecimal);
                    int newPrice = Integer.parseInt(cleanEditTextPrice);
                    
                    double oldPrice = Double.parseDouble(orderItemPrice);
                    
                    if (newPrice == oldPrice){
                        Toast.makeText(CashierOrderDetail.this, "Gagal. Harga masih sama.", Toast.LENGTH_SHORT).show();
                    } else {
                        // update price, subtotal (orderItem)
                        dbOrder.child(orderId).child("orderItem").child(orderItemId).child("price").setValue(newPrice);
                        double newSubtotalPrice = newPrice * Integer.parseInt(orderItemQty);
                        dbOrder.child(orderId).child("orderItem").child(orderItemId).child("subtotal").setValue(newSubtotalPrice);

                        // update subtotal (order parent)
                        DatabaseReference reference = dbOrder.child(orderId).child("orderItem");
                        ValueEventListener listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double newSubtotalAll = 0;
                                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    double subtotal = Double.valueOf(dataSnapshot.child("subtotal").getValue(long.class));

                                    newSubtotalAll = newSubtotalAll + subtotal;
                                }
                                dbOrder.child(orderId).child("subtotalPrice").setValue(newSubtotalAll);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                throw error.toException();
                            }
                        };
                        reference.addListenerForSingleValueEvent(listener);
                        Toast.makeText(CashierOrderDetail.this, "Sukses. Harga berhasil diubah.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        editPrice.show();
    }

}