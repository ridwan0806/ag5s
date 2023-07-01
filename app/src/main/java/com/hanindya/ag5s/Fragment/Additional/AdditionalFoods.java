package com.hanindya.ag5s.Fragment.Additional;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.Model.Foods;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.Additional.VHAdditionalFoods;
import com.hanindya.ag5s.ViewHolder.Foods.VHMasterFoods;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdditionalFoods#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdditionalFoods extends Fragment {
    String orderId = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<Foods, VHAdditionalFoods> adapter;
    DatabaseReference root,dbFoods;

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

    public AdditionalFoods() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdditionalFoods.
     */
    // TODO: Rename and change types and number of parameters
    public static AdditionalFoods newInstance(String param1, String param2) {
        AdditionalFoods fragment = new AdditionalFoods();
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
        View layout = inflater.inflate(R.layout.fragment_additional_foods,container,false);
        recyclerView = layout.findViewById(R.id.rv_additional_foods);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = layout.findViewById(R.id.pb_additional_foods);

        root = FirebaseDatabase.getInstance().getReference();
        dbFoods = root.child("Foods");

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMasterFoods();
    }

    private void getMasterFoods() {
        FirebaseRecyclerOptions<Foods> list =
                new FirebaseRecyclerOptions.Builder<Foods>()
                        .setQuery(dbFoods.orderByChild("foodCategory").equalTo("Makanan"),Foods.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Foods, VHAdditionalFoods>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHAdditionalFoods holder, int position, @NonNull Foods model) {
                String foodId = adapter.getRef(position).getKey();
                String foodName = adapter.getItem(position).getFoodName();
                String foodPrice = String.valueOf(adapter.getItem(position).getFoodPrice());

                holder.txtAdditionalFoodName.setText(model.getFoodName());
                holder.txtAdditionalFoodPrice.setText(String.valueOf(model.getFoodPrice()));
                holder.txtAdditionalFoodCategory.setText(model.getFoodCategory());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        addAdditionalFood(foodId,foodName,foodPrice);
                    }
                });
            }

            @NonNull
            @Override
            public VHAdditionalFoods onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_additional_foods,parent,false);
                return new VHAdditionalFoods(view);
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

    private void addAdditionalFood(String foodId, String foodName, String foodPrice) {

    }
}