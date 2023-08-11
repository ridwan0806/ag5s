package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HistoryCashIn extends AppCompatActivity {
    String startDate = "";
    String endDate = "";

    DatabaseReference root,dbUser,dbOrder;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    String dateTrx = "";
    String test = "";
    int transactionPerDate = 0;
    double billPerDate = 0;

    HashMap<String,String> summaryByDate = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cash_in);

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
                getSummaryCashIn(startDate,endDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void getSummaryCashIn(String startDate, String endDate) {
        Query query = dbOrder.orderByKey().startAt(startDate).endAt(endDate);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Toast.makeText(HistoryCashIn.this, "data not found", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot ordersByDate:snapshot.getChildren()){
                        String orderDate = ordersByDate.getKey();
//                        Log.d("TAG", String.valueOf(dateTrx));
                        Query db = dbOrder.child(orderDate).orderByChild("orderStatus").equalTo("Paid");
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dateTrx = snapshot.getKey();
                                    transactionPerDate = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
//                                    Log.d("TAG", String.valueOf(dateTrx));
//                                    Log.d("TAG", String.valueOf(transactionPerDate));
                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        double bill = Double.valueOf(ds.child("totalBill").getValue(long.class));
                                        billPerDate = billPerDate + bill;
                                    }
//                                    Log.d("TAG", String.valueOf(billPerDate));

                                    summaryByDate.put("tanggal",dateTrx);
                                    summaryByDate.put("jumlahTrx", String.valueOf(transactionPerDate));
                                    summaryByDate.put("totalSetoran", String.valueOf(billPerDate));

                                    Log.d("TAG", String.valueOf(summaryByDate));

                                    billPerDate = 0; //reset
                                }
//                                Log.d("TAG", String.valueOf(summaryByDate));
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
    }
}