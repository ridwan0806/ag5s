package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Fragment.Supplies.SuppliesCore;
import com.hanindya.ag5s.Fragment.Supplies.SuppliesIngredients;
import com.hanindya.ag5s.Model.Supplies;
import com.hanindya.ag5s.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SuppliesActivity extends AppCompatActivity {
    String userId,branchName,userName;
    DatabaseReference dbSupplies;

    TextView suppliesCategory;
    EditText suppliesName;
    String categorySupplies = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplies);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbUser = root.child("Users").child(userId);
        dbSupplies = root.child("Supplies");

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabSupplies);
        ViewPager viewPager = findViewById(R.id.viewPagerSupplies);
        FloatingActionButton addSupplies = findViewById(R.id.fbAddSupplies);
        FloatingActionButton cartSupplies = findViewById(R.id.fbCartSupplies);

        addSupplies.setOnClickListener(view -> {
            createNewSupplies();
        });

        tabLayout.addTab(tabLayout.newTab().setText("Bahan Utama"));
        tabLayout.addTab(tabLayout.newTab().setText("Bumbu Dapur"));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        SuppliesCore suppliesCore = new SuppliesCore();
                        return suppliesCore;
                    case 1:
                        SuppliesIngredients suppliesIngredients = new SuppliesIngredients();
                        return suppliesIngredients;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createNewSupplies() {
        AlertDialog.Builder createSupplies = new AlertDialog.Builder(this);
        createSupplies.setCancelable(false);
        createSupplies.setMessage("Tambah Bahan Baku");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_add_supplies,null);
        createSupplies.setView(layout);

        suppliesName = layout.findViewById(R.id.etNewSuppliesName);
        suppliesCategory = layout.findViewById(R.id.cmbNewSuppliesCategory);

        suppliesCategory.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(SuppliesActivity.this,suppliesCategory);
            popupMenu.getMenuInflater().inflate(R.menu.menu_supplies_category,popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.supplies_core){
                        categorySupplies = "Core";
                        suppliesCategory.setText("Bahan Pokok");
                    } else if (itemId == R.id.supplies_ingredients){
                        categorySupplies = "Ingredients";
                        suppliesCategory.setText("Bumbu Dapur");
                    }
                    return true;
                }
            });
            popupMenu.show();
        });

        createSupplies.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        createSupplies.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (suppliesName.getText().toString().length() == 0){
                    Toast.makeText(SuppliesActivity.this, "GAGAL. nama invalid", Toast.LENGTH_SHORT).show();
                } else if (categorySupplies.equals("")){
                    Toast.makeText(SuppliesActivity.this, "GAGAL. kategori invalid", Toast.LENGTH_SHORT).show();
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
                            upperText = suppliesName.getText().toString();
                            upperText = upperText.toUpperCase();

                            Supplies supplies = new Supplies();
                            supplies.setName(upperText);
                            supplies.setCategory(categorySupplies);
                            supplies.setCreatedBy(userName);
                            supplies.setCreatedDateTime(createdDateTime.format(dateNow));
                            supplies.setEditedBy("");
                            supplies.setEditedDateTime("");

                            dbSupplies.child(branchName).child(categorySupplies).push().setValue(supplies);
                            Toast.makeText(SuppliesActivity.this, "Sukses. Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                }
            }
        });

        createSupplies.show();
    }

}