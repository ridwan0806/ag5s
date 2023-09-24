package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Activity.HistoryCashOut;
import com.hanindya.ag5s.Model.HistoryRekapCashIn;
import com.hanindya.ag5s.Model.SuppliesOrder;
import com.hanindya.ag5s.ViewHolder.History.VHHistoryCashOut;
import com.hanindya.ag5s.ViewHolder.History.VHHistorySuppliesOrder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class HistorySuppliesOrder extends AppCompatActivity {
    String startDate = "";
    String endDate = "";

    DatabaseReference root,dbUser,dbSuppliesOrder,tmpSummary;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    String dateTrx = "";
    int transactionPerDate = 0;
    double subtotalPerDate = 0;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<SuppliesOrder, VHHistorySuppliesOrder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_supplies_order);

        recyclerView = findViewById(R.id.rvHistorySuppliesOrder);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.pbHistorySuppliesOrder);

        TextView dateFrom = findViewById(R.id.txtHistorySuppliesOrderDateStart);
        TextView dateTo = findViewById(R.id.txtHistorySuppliesOrderDateEnd);

        root = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        dbUser = root.child("Users").child(userId);

        if (getIntent() != null){
            startDate = getIntent().getStringExtra("startDate");
            endDate = getIntent().getStringExtra("endDate");
            dateFrom.setText(startDate);
            dateTo.setText(endDate);
        }

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbSuppliesOrder = root.child("SuppliesOrder").child(branchName);
                tmpSummary = root.child("Summary").child(branchName).child(userId).child("SuppliesOrder");
                getSummarySuppliesOrder(startDate,endDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void getSummarySuppliesOrder(String startDate, String endDate) {
        tmpSummary.removeValue(); //reset last temporary history
        Query query = dbSuppliesOrder.orderByKey().startAt(startDate).endAt(endDate);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    AlertDialog.Builder info = new AlertDialog.Builder(HistorySuppliesOrder.this);
                    info.setCancelable(false);
                    info.setTitle("Info");
                    info.setMessage("Data tidak ditemukan");
                    info.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            Intent home = new Intent(HistorySuppliesOrder.this, MainActivity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(home);
                            finish();
                        }
                    });
                    info.show();
                } else {
                    for (DataSnapshot ordersByDate:snapshot.getChildren()){
                        String orderDate = ordersByDate.getKey();
                        Query db = dbSuppliesOrder.child(orderDate);
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dateTrx = snapshot.getKey();
                                    transactionPerDate = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));

                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        double subtotal = Double.valueOf(ds.child("subtotal").getValue(long.class)); //bill tiap transaksi
                                        subtotalPerDate = subtotalPerDate + subtotal;
                                    }
//
                                    int newValue = (int) Math.round(subtotalPerDate); // Convert into Integer (Pembulatan)
                                    HistoryRekapCashIn item = new HistoryRekapCashIn(dateTrx,transactionPerDate,newValue);
                                    String tempId = tmpSummary.push().getKey();
                                    tmpSummary.child(tempId).setValue(item);

                                    subtotalPerDate = 0; // Reset subtotal perhari
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                throw error.toException();
                            }
                        };
                        db.addListenerForSingleValueEvent(valueEventListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        };
        query.addListenerForSingleValueEvent(listener);
        getTempDataHistorySuppliesOrder();
    }

    private void getTempDataHistorySuppliesOrder() {
        FirebaseRecyclerOptions<SuppliesOrder> list =
                new FirebaseRecyclerOptions.Builder<SuppliesOrder>()
                        .setQuery(tmpSummary.orderByChild("tanggal"),SuppliesOrder.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<SuppliesOrder, VHHistorySuppliesOrder>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHHistorySuppliesOrder holder, int position, @NonNull SuppliesOrder model) {
                String dateOrder = adapter.getItem(position).getDate();

                NumberFormat formatRp = new DecimalFormat("#,###");
                double total = model.getSubtotal();

                int number = position + 1;

                holder.VHHistorySuppliesOrderNumber.setText(number+".");
                holder.VHHistorySuppliesOrderDate.setText(model.getDate());
                holder.VHHistorySuppliesOrderSubtotal.setText(formatRp.format(total));
            }

            @NonNull
            @Override
            public VHHistorySuppliesOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_history_supplies_order,parent,false);
                return new VHHistorySuppliesOrder(view);
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
}