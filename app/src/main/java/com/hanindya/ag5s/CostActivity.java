package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.Model.CostReference;
import com.hanindya.ag5s.ViewHolder.Menu.VHMenuFoods;
import com.hanindya.ag5s.ViewHolder.VHCostRef;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CostActivity extends AppCompatActivity {
    DatabaseReference dbCostRef;
    String userId,branchName,userName;
    TextView costRefName,costRefDate;
    FirebaseRecyclerAdapter<CostReference, VHCostRef>adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbUser = root.child("Users").child(userId);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbCostRef = root.child("CostReference").child(branchName);
                getListCostRef();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        FloatingActionButton addCostRef = findViewById(R.id.fbAddCostRef);
        addCostRef.setOnClickListener(view -> {
            createCostRef();
        });

        recyclerView = findViewById(R.id.rv_cost_reference);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.pb_cost_reference);
    }

    private void getListCostRef() {
        FirebaseRecyclerOptions<CostReference> list =
                new FirebaseRecyclerOptions.Builder<CostReference>()
                        .setQuery(dbCostRef.orderByChild("costRefName"),CostReference.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<CostReference, VHCostRef>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHCostRef holder, int position, @NonNull CostReference model) {
                String costRefId = adapter.getRef(position).getKey();
                String name = adapter.getItem(position).getCostRefName();
                int number = position + 1;

                holder.costRefNumber.setText(number+".");
                holder.costRefName.setText(model.getCostRefName());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (name.equals("BELANJA BAHAN BAKU")){
                            Toast.makeText(CostActivity.this, "Belanja Bahan Baku tidak ditambahkan dihalaman ini.", Toast.LENGTH_SHORT).show();
                        } else {
                            addCostDaily(costRefId,name);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public VHCostRef onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_cost_ref,parent,false);
                return new VHCostRef(view);
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

    private void addCostDaily(String costRefId, String name) {
        AlertDialog.Builder createCostDaily = new AlertDialog.Builder(this);
        createCostDaily.setCancelable(false);
        createCostDaily.setMessage(name);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_add_cost,null);
        createCostDaily.setView(layout);
        
        costRefDate = layout.findViewById(R.id.txt_cost_add_date);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        costRefDate.setOnClickListener(view -> {
            DatePickerDialog dateCost = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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

                    costRefDate.setText(year+"-"+monthString+"-"+dayString);

                }
            }, year, month, day);
            dateCost.show();
        });

        createCostDaily.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        createCostDaily.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CostActivity.this, "simpan", Toast.LENGTH_SHORT).show();
            }
        });

        createCostDaily.show();
    }

    private void createCostRef() {
        AlertDialog.Builder createCostRef = new AlertDialog.Builder(this);
        createCostRef.setCancelable(false);
        createCostRef.setMessage("Buat Jenis Pengeluaran Baru");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_add_new_cost_ref,null);
        createCostRef.setView(layout);

        costRefName = layout.findViewById(R.id.etCostAddCostRef);

        createCostRef.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        createCostRef.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (costRefName.getText().length() == 0){
                    Toast.makeText(CostActivity.this, "Error. Invalid input", Toast.LENGTH_SHORT).show();
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

                            String upperText;
                            upperText = costRefName.getText().toString();
                            upperText = upperText.toUpperCase();

                            CostReference costReference = new CostReference();
                            costReference.setCostRefName(upperText);
                            costReference.setCostCreatedBy(userName);
                            costReference.setCostCreatedDateTime(createdDateTime.format(dateNow));
                            
                            dbCostRef.push().setValue(costReference);
                            Toast.makeText(CostActivity.this, "Sukses. Pengeluaran berhasil ditambah", Toast.LENGTH_SHORT).show();
                            getListCostRef();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                }
            }
        });

        createCostRef.show();
    }
}