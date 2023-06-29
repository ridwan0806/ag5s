package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanindya.ag5s.Model.User;
import com.hanindya.ag5s.R;

public class UserActivity extends AppCompatActivity {
    EditText name,email,password;
    TextView branch,submit;
    String branchName;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        name = findViewById(R.id.txtLoginEmail);
        email = findViewById(R.id.txtAddUserEmail);
        password = findViewById(R.id.txtLoginPassword);
        branch = findViewById(R.id.txtAddUserBranch);
        submit = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        branchName = "unset";

        progressDialog = new ProgressDialog(UserActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("tunggu sebentar..");
        progressDialog.setCancelable(false);

        branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(UserActivity.this,branch);
                popupMenu.getMenuInflater().inflate(R.menu.menu_user_branch,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.menu_branch_cibatok){
                            branchName = "CIBATOK";
                            branch.setText("CIBATOK");
                        } else if (itemId == R.id.menu_branch_cikampak){
                            branchName = "CIKAMPAK";
                            branch.setText("CIKAMPAK");
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                registerBranch(name.getText().toString(),email.getText().toString(),password.getText().toString());
                registerUser(name.getText().toString(),email.getText().toString(),password.getText().toString());
            }
        });

    }

    private void registerUser(String userName, String userEmail, String userPassword) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser != null){
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String userKey = firebaseUser.getUid();
                                DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference();
                                User user = new User();
                                user.setUserId(userKey);
                                user.setEmail(userEmail);
                                user.setPassword(userPassword);
                                user.setUsername(userName);
                                user.setBranch(branchName);
                                dbUser.child("Users").child(userKey).setValue(user);
                            }
                        });
                    } else {
                        Toast.makeText(UserActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(UserActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void registerBranch(String userName, String userEmail, String userPassword) {
//        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    FirebaseUser firebaseUser = task.getResult().getUser();
//                    if (firebaseUser != null){
//                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
//                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(UserActivity.this, "Branch Ditambah", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } else {
//                        Toast.makeText(UserActivity.this, "failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else {
//                    Toast.makeText(UserActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}