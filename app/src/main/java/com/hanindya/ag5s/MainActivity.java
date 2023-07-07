package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Activity.CartActivity;
import com.hanindya.ag5s.Activity.CashierActivity;
import com.hanindya.ag5s.Activity.FoodsActivity;
import com.hanindya.ag5s.Activity.UserActivity;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout food,supplies,cashier,userPage,logout;
    TextView userName,userBranch;
    FirebaseUser firebaseUser;
    DatabaseReference root,dbUser;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.txtUsername);
        userBranch = findViewById(R.id.txtBranch);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        if (firebaseUser != null){
            userName.setText(firebaseUser.getDisplayName());
        } else {
            Intent login = new Intent(MainActivity.this,Login.class);
            startActivity(login);
            finish();
        }

        root = FirebaseDatabase.getInstance().getReference();
        dbUser = root.child("Users").child(userId);
        getUserInfo();
        
        food = findViewById(R.id.ic_food);
        supplies = findViewById(R.id.ic_supplies);
        cashier = findViewById(R.id.ic_cashier);
        userPage = findViewById(R.id.ic_user);
        logout = findViewById(R.id.ic_logout);

        food.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
        });

        supplies.setOnClickListener(view -> {
            Toast.makeText(this, "supplies", Toast.LENGTH_SHORT).show();
        });

        cashier.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CashierActivity.class);
            startActivity(i);
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
                        FirebaseAuth.getInstance().signOut();
                        Intent login = new Intent(MainActivity.this,Login.class);
                        startActivity(login);
                        finish();
                    }
                });
                confirm.show();
            }
        });

    }

    private void getUserInfo() {
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String branch = snapshot.child("branch").getValue(String.class);
                userBranch.setText(branch);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}