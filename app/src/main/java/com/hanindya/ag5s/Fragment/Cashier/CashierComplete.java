package com.hanindya.ag5s.Fragment.Cashier;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Activity.CashierOrderDetailComplete;
import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.Model.Order;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.Cashier.VHCashierComplete;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CashierComplete#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CashierComplete extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<Order, VHCashierComplete> adapter;
    DatabaseReference root,dbOrder,dbUser;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CashierComplete() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CashierComplete.
     */
    // TODO: Rename and change types and number of parameters
    public static CashierComplete newInstance(String param1, String param2) {
        CashierComplete fragment = new CashierComplete();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_cashier_complete,container,false);
        recyclerView = layout.findViewById(R.id.rv_cashier_complete);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = layout.findViewById(R.id.pb_cashier_complete);

        root = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        dbUser = root.child("Users").child(userId);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);

                DatabaseReference serverTime = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
                serverTime.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long serverTimeOffset = snapshot.getValue(Long.class);
                        long estimateServerTime = System.currentTimeMillis()+serverTimeOffset;

                        SimpleDateFormat date;
                        date = new SimpleDateFormat("yyyy-MM-dd");

                        Date resultDate = new Date(estimateServerTime);
                        String orderDate = date.format(resultDate);
                        dbOrder = root.child("Orders").child(branchName).child(orderDate);

                        getOrderList();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        return layout;
    }

    private void getOrderList() {
        FirebaseRecyclerOptions<Order> list =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(dbOrder.orderByChild("orderStatus").equalTo("Paid"),Order.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Order, VHCashierComplete>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHCashierComplete holder, int position, @NonNull Order model) {
                NumberFormat formatRp = new DecimalFormat("#,###");
                double total = model.getTotalBill();

                holder.customerName.setText(model.getCustomerName());
                holder.customerType.setText(model.getCustomerType());
                holder.orderType.setText(model.getOrderType());
                holder.totalBill.setText(formatRp.format(total));
                holder.status.setText(model.getOrderStatus());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent detailOrderComplete = new Intent(getContext(), CashierOrderDetailComplete.class);
                        detailOrderComplete.putExtra("orderId",adapter.getRef(position).getKey());
                        startActivity(detailOrderComplete);
                    }
                });
            }

            @NonNull
            @Override
            public VHCashierComplete onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_cashier_complete,parent,false);
                return new VHCashierComplete(view);
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