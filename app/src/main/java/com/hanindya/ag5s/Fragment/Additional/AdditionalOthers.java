package com.hanindya.ag5s.Fragment.Additional;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.Model.Foods;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.Additional.VHAdditionalFoods;
import com.hanindya.ag5s.ViewHolder.Additional.VHAdditionalOthers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdditionalOthers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdditionalOthers extends Fragment {
    String orderId = "";
    String orderDate = "";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<Foods, VHAdditionalOthers> adapter;

    DatabaseReference root,dbFoods,dbUser,dbOrder;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    ImageView plusBtn,minusBtn;
    TextView qty;
    int numberOrder = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdditionalOthers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdditionalOthers.
     */
    // TODO: Rename and change types and number of parameters
    public static AdditionalOthers newInstance(String param1, String param2) {
        AdditionalOthers fragment = new AdditionalOthers();
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
        // Get OrderId from CashierOrderDetail
        orderId = getActivity().getIntent().getStringExtra("orderId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_additional_others,container,false);
        recyclerView = layout.findViewById(R.id.rv_additional_others);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = layout.findViewById(R.id.pb_additional_others);

        root = FirebaseDatabase.getInstance().getReference();
        dbFoods = root.child("Foods");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

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
                getMasterOthers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        return layout;
    }

    private void getMasterOthers() {
        FirebaseRecyclerOptions<Foods> list =
                new FirebaseRecyclerOptions.Builder<Foods>()
                        .setQuery(dbFoods.orderByChild("foodCategory").equalTo("Tambahan"),Foods.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Foods, VHAdditionalOthers>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHAdditionalOthers holder, int position, @NonNull Foods model) {
                String foodId = adapter.getRef(position).getKey();
                String foodName = adapter.getItem(position).getFoodName();
                String foodPrice = String.valueOf(adapter.getItem(position).getFoodPrice());

                holder.txtAdditionalFoodName.setText(model.getFoodName());
                holder.txtAdditionalFoodPrice.setText(String.valueOf(model.getFoodPrice()));
                holder.txtAdditionalFoodCategory.setText(model.getFoodCategory());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        addAdditionalOther(foodId,foodName,foodPrice);
                    }
                });
            }

            @NonNull
            @Override
            public VHAdditionalOthers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_additional_others,parent,false);
                return new VHAdditionalOthers(view);
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

    private void addAdditionalOther(String foodId, String foodName, String foodPrice) {
        AlertDialog.Builder setQty = new AlertDialog.Builder(getContext());
        setQty.setCancelable(false);
        setQty.setMessage("Masukan Qty Pesanan");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_edit_qty,null);
        setQty.setView(layout);

        minusBtn = layout.findViewById(R.id.btnMinus);
        plusBtn = layout.findViewById(R.id.btnPlus);
        qty = layout.findViewById(R.id.txtQty);

        minusBtn.setOnClickListener(view -> {
            if (numberOrder > 1){
                int newNumberOrder = Integer.parseInt(qty.getText().toString());
                numberOrder = newNumberOrder - 1;
                qty.setText(String.valueOf(numberOrder));
            }
        });

        plusBtn.setOnClickListener(view -> {
            numberOrder = numberOrder + 1;
            qty.setText(String.valueOf(numberOrder));
        });

        setQty.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                numberOrder = 1;
            }
        });

        setQty.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query checkItemExists = dbOrder.child(orderId).child("orderItem").orderByChild("foodId").equalTo(foodId);
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Toast.makeText(getContext(), "Ops. Menu ini sudah ada", Toast.LENGTH_SHORT).show();
                            numberOrder = 1;
                        } else {
                            double subtotal = numberOrder * Double.parseDouble(foodPrice);

                            OrderItem orderItem = new OrderItem();
                            orderItem.setFoodId(foodId);
                            orderItem.setFoodName(foodName);
                            orderItem.setPrice(Double.parseDouble(foodPrice));
                            orderItem.setQty(numberOrder);
                            orderItem.setSubtotal(subtotal);

                            String orderItemId = dbOrder.child(orderId).child("orderItem").push().getKey();
                            dbOrder.child(orderId).child("orderItem").child(orderItemId).setValue(orderItem);
                            numberOrder = 1;

                            // Re-Calculate Subtotal Item & Price (Order Parent)
                            DatabaseReference reference = dbOrder.child(orderId).child("orderItem");
                            ValueEventListener valueEventListener = new ValueEventListener() {
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
                                    Toast.makeText(getContext(), "Sukses. Item ditambahkan", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    throw error.toException();
                                }
                            };
                            reference.addListenerForSingleValueEvent(valueEventListener);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                };
                checkItemExists.addListenerForSingleValueEvent(listener);
            }
        });

        setQty.show();
    }
}