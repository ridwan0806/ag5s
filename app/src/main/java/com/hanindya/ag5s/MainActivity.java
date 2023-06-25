package com.hanindya.ag5s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Activity.FoodsActivity;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout food,supplies,cashier,cartOrder,userPage,logout;
    TextView userName;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.txtUsername);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            userName.setText(firebaseUser.getDisplayName());
        } else {
            Intent login = new Intent(MainActivity.this,Login.class);
            startActivity(login);
            finish();
        }
        
        food = findViewById(R.id.ic_food);
        supplies = findViewById(R.id.ic_supplies);
        cartOrder = findViewById(R.id.ic_cartOrder);
        userPage = findViewById(R.id.ic_user);
        logout = findViewById(R.id.ic_logout);

        food.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, FoodsActivity.class);
            startActivity(i);
        });

        supplies.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
        });

        cartOrder.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CartActivity.class);
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
}