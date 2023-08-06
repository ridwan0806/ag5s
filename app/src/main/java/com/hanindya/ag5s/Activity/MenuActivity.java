package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.hanindya.ag5s.Fragment.Menu.MenuDrinks;
import com.hanindya.ag5s.Fragment.Menu.MenuFoods;
import com.hanindya.ag5s.Helper.MoneyTextWatcher;
import com.hanindya.ag5s.Model.Menu;
import com.hanindya.ag5s.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    TextView menuCategory;
    EditText menuName,menuPrice;
    String category = "";

    DatabaseReference root,dbMenu,dbUser;
    FirebaseUser firebaseUser;
    String userId,branchName,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tabLayout = findViewById(R.id.tabMenu);
        ViewPager viewPager = findViewById(R.id.viewPagerMenu);
        TextView btnAddMenu = findViewById(R.id.btnMenuAddNew);
        TextView cart = findViewById(R.id.btnMenuViewCart);

        cart.setOnClickListener(view -> {
            Intent cartActivity = new Intent(MenuActivity.this, CartActivity.class);
            startActivity(cartActivity);
        });

        tabLayout.addTab(tabLayout.newTab().setText("Makanan"));
        tabLayout.addTab(tabLayout.newTab().setText("Minuman"));

        btnAddMenu.setOnClickListener(view -> {
            addNewMenu();
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        root = FirebaseDatabase.getInstance().getReference();
        dbMenu = root.child("Menu");
        dbUser = root.child("Users").child(userId);

        //Get User Branch..
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

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        MenuFoods foods = new MenuFoods();
                        return foods;
                    case 1:
                        MenuDrinks drinks = new MenuDrinks();
                        return drinks;
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

    private void addNewMenu() {
        AlertDialog.Builder createMenu = new AlertDialog.Builder(this);
        createMenu.setCancelable(false);
        createMenu.setMessage("Tambah Menu Baru ?");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_add_food,null);
        createMenu.setView(layout);

        menuName = layout.findViewById(R.id.etNewFoodName);
        menuPrice = layout.findViewById(R.id.etNewFoodPrice);
        menuPrice.addTextChangedListener(new MoneyTextWatcher(menuPrice));
        menuCategory =layout.findViewById(R.id.etNewFoodCategory);

        menuCategory.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MenuActivity.this,menuCategory);
            popupMenu.getMenuInflater().inflate(R.menu.menu_foods_category,popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.food_category_food){
                        category = "Foods";
                        menuCategory.setText("MAKANAN");
                    } else if (itemId == R.id.food_category_drink){
                        category = "Drinks";
                        menuCategory.setText("MINUMAN");
                    } else {
                        category = "";
                    }
                    return true;
                }
            });
            popupMenu.show();
        });

        createMenu.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        createMenu.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (category == ""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Error");
                    builder.setMessage("Kategori belum dipilih");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            addNewMenu();
                        }
                    });
                    builder.show();
                } else if (menuName.getText().toString().length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Error");
                    builder.setMessage("Nama menu belum diisi");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            category = "";
                            addNewMenu();
                        }
                    });
                    builder.show();
                } else if (menuPrice.getText().toString().equals("Rp 0") || menuPrice.getText().toString().length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Error");
                    builder.setMessage("Harga jual invalid");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            category = "";
                            addNewMenu();
                        }
                    });
                    builder.show();
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

                            BigDecimal valuePrice = MoneyTextWatcher.parseCurrencyValue(menuPrice.getText().toString());
                            String price = String.valueOf(valuePrice);

                            Menu menu = new Menu();
                            menu.setName(menuName.getText().toString());
                            menu.setPrice(Double.parseDouble(price));
                            menu.setCategory(category);
                            menu.setCreatedDateTime(createdDateTime.format(dateNow));
                            menu.setCreatedBy(userName);
                            menu.setEditedBy("");
                            menu.setEditedDateTime("");

                            dbMenu.child(branchName).child(category).push().setValue(menu);
                            Toast.makeText(MenuActivity.this, menuName.getText().toString().toUpperCase(Locale.ROOT)+" berhasil ditambah", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                }
            }
        });

        createMenu.show();
    }
}