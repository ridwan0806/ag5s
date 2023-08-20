package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Activity.CashierActivity;
import com.hanindya.ag5s.Activity.CostActivity;
import com.hanindya.ag5s.Activity.HistoryCashIn;
import com.hanindya.ag5s.Activity.HistoryCashOut;
import com.hanindya.ag5s.Activity.HistoryCashOutCosts;
import com.hanindya.ag5s.Activity.MenuActivity;
import com.hanindya.ag5s.Activity.SuppliesActivity;
import com.hanindya.ag5s.Activity.UserActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference root,dbUser;
    String userId;
    TextView userName,userBranch;

    ConstraintLayout food,supplies,cost,cashier,information,userPage,logout;

    TextView infoType,infoStart,infoEnd;
    String transactionType = "";
    String transactionStartDate = "";
    String transactionEndDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.txtUsername);
        userBranch = findViewById(R.id.txtBranch);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        root = FirebaseDatabase.getInstance().getReference();

        if (firebaseUser != null){
            userId = firebaseUser.getUid();
            userName.setText(firebaseUser.getDisplayName());

            dbUser = root.child("Users").child(userId);
            getUserInfo();
        } else {
            Intent login = new Intent(MainActivity.this,Login.class);
            startActivity(login);
            finish();
        }
        
        food = findViewById(R.id.ic_food);
        supplies = findViewById(R.id.ic_supplies);

        cost = findViewById(R.id.ic_cost);
        cashier = findViewById(R.id.ic_cashier);

        information = findViewById(R.id.ic_information);
        userPage = findViewById(R.id.ic_user);
        logout = findViewById(R.id.ic_logout);

        food.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
        });

        supplies.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SuppliesActivity.class);
            startActivity(i);
        });

        cost.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CostActivity.class);
            startActivity(i);
        });

        cashier.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CashierActivity.class);
            startActivity(i);
        });

        information.setOnClickListener(view -> {
            filterInfoType();
        });

        userPage.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, UserActivity.class);
            startActivity(i);
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(MainActivity.this);
                confirm.setCancelable(true);
                confirm.setMessage("Logout ?");

                confirm.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                confirm.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                });
                confirm.show();
            }
        });

    }

    private void filterInfoType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
//        builder.setMessage("Filter Pencarian");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_filter_history,null);
        builder.setView(layout);

        infoType = layout.findViewById(R.id.cmbHistoryFilterType);
        infoStart = layout.findViewById(R.id.cmbHistoryStartDate);
        infoEnd = layout.findViewById(R.id.cmbHistoryEndDate);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        infoType.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this,infoType);
            popupMenu.getMenuInflater().inflate(R.menu.menu_history_type,popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.history_pendapatan){
                        transactionType = "history_pendapatan";
                        infoType.setText("Penjualan Harian");
                    } else if (itemId == R.id.history_persediaan){
                        transactionType = "history_persediaan";
                        infoType.setText("Pengeluaran (Belanja Persediaan)");
                    } else if (itemId == R.id.history_non_persediaan){
                        transactionType = "history_non_persediaan";
                        infoType.setText("Pengeluaran Rutin (Non Belanja)");
                    } else if (itemId == R.id.history_trx_cancel){
                        transactionType = "history_trx_cancel";
                        infoType.setText("Transaksi Cancel / Gantung");
                    } else {
                        transactionType = "";
                    }
                    return true;
                }
            });
            popupMenu.show();
        });

        infoStart.setOnClickListener(view -> {
            DatePickerDialog dialogStart = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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

                    infoStart.setText(year+"-"+monthString+"-"+dayString);
                    transactionStartDate = infoStart.getText().toString();
                }
            }, year, month, day);
            dialogStart.show();
        });

        infoEnd.setOnClickListener(view -> {
            DatePickerDialog dialogEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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

                    infoEnd.setText(year+"-"+monthString+"-"+dayString);
                    transactionEndDate = infoEnd.getText().toString();
                }
            }, year, month, day);
            dialogEnd.show();
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Cari", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (transactionType == ""){
                    AlertDialog.Builder error = new AlertDialog.Builder(MainActivity.this);
                    error.setCancelable(false);
                    error.setTitle("Error");
                    error.setMessage("Tipe transaksi belum dipilih");
                    error.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            filterInfoType();
                        }
                    });
                    error.show();
                } else if (transactionStartDate == ""){
                    AlertDialog.Builder error = new AlertDialog.Builder(MainActivity.this);
                    error.setCancelable(false);
                    error.setTitle("Error");
                    error.setMessage("Tanggal mulai belum dipilih");
                    error.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            filterInfoType();
                            transactionType = "";
                        }
                    });
                    error.show();
                } else if (transactionEndDate == ""){
                    AlertDialog.Builder error = new AlertDialog.Builder(MainActivity.this);
                    error.setCancelable(false);
                    error.setTitle("Error");
                    error.setMessage("Tanggal akhir belum dipilih");
                    error.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            filterInfoType();
                            transactionType = "";
                        }
                    });
                    error.show();
                } else {
                    if (transactionType == "history_pendapatan"){
                        Intent pemasukan = new Intent(MainActivity.this, HistoryCashIn.class);
                        pemasukan.putExtra("startDate",transactionStartDate);
                        pemasukan.putExtra("endDate",transactionEndDate);
                        startActivity(pemasukan);
                    } else if (transactionType == "history_persediaan"){
                        Intent pengeluaran = new Intent(MainActivity.this, HistorySuppliesOrder.class);
                        pengeluaran.putExtra("startDate",transactionStartDate);
                        pengeluaran.putExtra("endDate",transactionEndDate);
                        startActivity(pengeluaran);
                    } else if (transactionType == "history_non_persediaan"){
                        Intent cashOutNonSupplies = new Intent(MainActivity.this, HistoryCashOut.class);
                        cashOutNonSupplies.putExtra("startDate",transactionStartDate);
                        cashOutNonSupplies.putExtra("endDate",transactionEndDate);
                        startActivity(cashOutNonSupplies);
                    } else if (transactionType == "history_trx_cancel"){
                        Toast.makeText(MainActivity.this, "fitur ini blm siap", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(MainActivity.this,Login.class);
        startActivity(login);
        finish();
    }

    private void getUserInfo() {
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String branch = snapshot.child("branch").getValue(String.class);
                userBranch.setText("CABANG : "+branch);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}