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
import android.widget.Toast;

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
import com.hanindya.ag5s.Model.HistoryRekapCashIn;
import com.hanindya.ag5s.ViewHolder.History.VHHistoryCashIn;

public class HistoryCashIn extends AppCompatActivity {
    String startDate = "";
    String endDate = "";

    DatabaseReference root,dbUser,dbOrder,tmpSummary;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    String dateTrx = "";
    String test = "";
    int transactionPerDate = 0;
    double billPerDate = 0;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<HistoryRekapCashIn, VHHistoryCashIn> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cash_in);

        recyclerView = findViewById(R.id.rvHistoryCashIn);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.pbHistoryCashIn);

        root = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        dbUser = root.child("Users").child(userId);

        if (getIntent() != null){
            startDate = getIntent().getStringExtra("startDate");
            endDate = getIntent().getStringExtra("endDate");
        }

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbOrder = root.child("Orders").child(branchName);
                tmpSummary = root.child("Summary").child(branchName).child(userId);
                getSummaryCashIn(startDate,endDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void getSummaryCashIn(String startDate, String endDate) {
        tmpSummary.removeValue(); //reset last temporary history
        Query query = dbOrder.orderByKey().startAt(startDate).endAt(endDate);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    AlertDialog.Builder info = new AlertDialog.Builder(HistoryCashIn.this);
                    info.setCancelable(false);
                    info.setTitle("Info");
                    info.setMessage("Data tidak ditemukan");
                    info.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            Intent home = new Intent(HistoryCashIn.this, MainActivity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(home);
                            finish();
                        }
                    });
                    info.show();
                } else {
                    for (DataSnapshot ordersByDate:snapshot.getChildren()){
                        String orderDate = ordersByDate.getKey();
                        Query db = dbOrder.child(orderDate).orderByChild("orderStatus").equalTo("Paid");
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dateTrx = snapshot.getKey();
                                    transactionPerDate = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));

                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        double bill = Double.valueOf(ds.child("totalBill").getValue(long.class)); //bill tiap transaksi
                                        billPerDate = billPerDate + bill;
                                    }

                                    int newValue = (int) Math.round(billPerDate); // Convert into Integer (Pembulatan)
                                    HistoryRekapCashIn item = new HistoryRekapCashIn(dateTrx,transactionPerDate,newValue);
                                    String tempId = tmpSummary.push().getKey();
                                    tmpSummary.child(tempId).setValue(item);

                                    billPerDate = 0; // Reset Total bill per tiap hari
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
        getTempDataHistoryCashIn();
    }

    private void getTempDataHistoryCashIn() {
        FirebaseRecyclerOptions<HistoryRekapCashIn> list =
                new FirebaseRecyclerOptions.Builder<HistoryRekapCashIn>()
                        .setQuery(tmpSummary.orderByChild("tanggal"),HistoryRekapCashIn.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<HistoryRekapCashIn, VHHistoryCashIn>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHHistoryCashIn holder, int position, @NonNull HistoryRekapCashIn model) {
                holder.historyCashInOrderDate.setText(model.getTanggal());
                holder.historyCashInTotalTransaction.setText(String.valueOf(model.getTotalTransaksi()));
                holder.historyCashInTotalBill.setText(String.valueOf(model.getTotalSetoran()));
                holder.menuHistoryCashIn.setOnClickListener(view -> {
                    Toast.makeText(HistoryCashIn.this, "test", Toast.LENGTH_SHORT).show();
                });
            }

            @NonNull
            @Override
            public VHHistoryCashIn onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_history_cash_in,parent,false);
                return new VHHistoryCashIn(view);
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

    public void refreshActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        refreshActivity();
        super.onBackPressed();
    }
}