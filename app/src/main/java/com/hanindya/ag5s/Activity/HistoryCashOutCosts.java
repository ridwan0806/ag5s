package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.hanindya.ag5s.Model.Cost;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.History.VHHistoryCashOutCost;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class HistoryCashOutCosts extends AppCompatActivity {
    String orderDate = "";

    DatabaseReference root,dbUser,dbCost;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<Cost, VHHistoryCashOutCost> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cash_out_costs);

        recyclerView = findViewById(R.id.rvHistoryCashOutCost);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.pbHistoryCashOutCost);

        TextView dateInfo = findViewById(R.id.txtHistoryCashOutCostDate);

        root = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        dbUser = root.child("Users").child(userId);

        if (getIntent() != null){
            orderDate = getIntent().getStringExtra("costDate");
            dateInfo.setText(orderDate);
        }

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbCost = root.child("Cost").child(branchName);
                getListCost(orderDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void getListCost(String orderDate) {
        FirebaseRecyclerOptions<Cost> listCost =
                new FirebaseRecyclerOptions.Builder<Cost>()
                        .setQuery(dbCost.child(orderDate),Cost.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Cost, VHHistoryCashOutCost>(listCost) {
            @Override
            protected void onBindViewHolder(@NonNull VHHistoryCashOutCost holder, int position, @NonNull Cost model) {
                String costId = adapter.getRef(position).getKey();
                String urlAttachment = adapter.getItem(position).getUrlAttachment();

                NumberFormat formatRp = new DecimalFormat("#,###");
                double total = model.getSubtotal();
                int number = position + 1;

                holder.historyCashOutCostNumber.setText(number+".");
                holder.historyCashOutCostName.setText(model.getName());
                holder.historyCashOutCostSubtotal.setText(formatRp.format(total));
                holder.historyCashOutCostNotes.setText(model.getNotes());
                holder.historyCashOutCostMenu.setOnClickListener(view -> {
                    if (Objects.equals(urlAttachment, "")){
                        Toast.makeText(HistoryCashOutCosts.this, "Lampiran tidak tersedia", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HistoryCashOutCosts.this, "link attach :", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public VHHistoryCashOutCost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_history_cash_out_cost,parent,false);
                return new VHHistoryCashOutCost(view);
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