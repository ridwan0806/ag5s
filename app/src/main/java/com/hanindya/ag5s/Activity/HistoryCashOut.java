package com.hanindya.ag5s.Activity;

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
import com.hanindya.ag5s.MainActivity;
import com.hanindya.ag5s.Model.HistoryRekapCashIn;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.History.VHHistoryCashOut;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class HistoryCashOut extends AppCompatActivity {
    String startDate = "";
    String endDate = "";

    DatabaseReference root,dbUser,dbCost,tmpSummary;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    String dateTrx = "";
    int transactionPerDate = 0;
    double subtotalPerDate = 0;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<HistoryRekapCashIn, VHHistoryCashOut> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cash_out_non_supplies);

        recyclerView = findViewById(R.id.rvHistoryCashOutNonSupplies);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.pbHistoryCashOutNonSupplies);

        TextView dateFrom = findViewById(R.id.txtHistoryCashOutNonSuppliesStartDate);
        TextView dateTo = findViewById(R.id.txtHistoryCashOutNonSuppliesEndDate);

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
                dbCost = root.child("Cost").child(branchName);
                tmpSummary = root.child("Summary").child(branchName).child(userId).child("CashOut");
                getSummaryCashOut(startDate,endDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void getSummaryCashOut(String startDate, String endDate) {
        tmpSummary.removeValue(); //reset last temporary history
        Query query = dbCost.orderByKey().startAt(startDate).endAt(endDate);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    AlertDialog.Builder info = new AlertDialog.Builder(HistoryCashOut.this);
                    info.setCancelable(false);
                    info.setTitle("Info");
                    info.setMessage("Data tidak ditemukan");
                    info.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            Intent home = new Intent(HistoryCashOut.this, MainActivity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(home);
                            finish();
                        }
                    });
                    info.show();
                } else {
                    for (DataSnapshot ordersByDate:snapshot.getChildren()){
                        String orderDate = ordersByDate.getKey();
                        Query db = dbCost.child(orderDate);
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
        getTempDataHistoryCashOut();
    }

    private void getTempDataHistoryCashOut() {
        FirebaseRecyclerOptions<HistoryRekapCashIn> list =
                new FirebaseRecyclerOptions.Builder<HistoryRekapCashIn>()
                        .setQuery(tmpSummary.orderByChild("tanggal"),HistoryRekapCashIn.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<HistoryRekapCashIn, VHHistoryCashOut>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHHistoryCashOut holder, int position, @NonNull HistoryRekapCashIn model) {
                String dateCost = adapter.getItem(position).getTanggal();

                NumberFormat formatRp = new DecimalFormat("#,###");
                double total = model.getTotalSetoran();

                int number = position + 1;

                holder.historyCashOutNonSuppliesNumber.setText(number+".");
                holder.historyCashOutNonSuppliesOrderDate.setText(model.getTanggal());
                holder.historyCashOutNonSuppliesTransaction.setText(String.valueOf(model.getTotalTransaksi()));
                holder.historyCashOutNonSuppliesSubtotal.setText(formatRp.format(total));
                holder.menuHistoryCashOutNonSuppliesOrderDate.setOnClickListener(view -> {
                    Intent detailCost = new Intent(HistoryCashOut.this, HistoryCashOutCosts.class);
                    detailCost.putExtra("costDate",dateCost);
                    startActivity(detailCost);
                });
            }

            @NonNull
            @Override
            public VHHistoryCashOut onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_history_cash_in,parent,false);
                return new VHHistoryCashOut(view);
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