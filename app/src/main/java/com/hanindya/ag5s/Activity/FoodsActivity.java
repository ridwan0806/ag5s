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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanindya.ag5s.Fragment.Foods.MasterDrinks;
import com.hanindya.ag5s.Fragment.Foods.MasterFoodAdditional;
import com.hanindya.ag5s.Fragment.Foods.MasterFoods;
import com.hanindya.ag5s.Model.Foods;
import com.hanindya.ag5s.R;

public class FoodsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FloatingActionButton addFood;
    TextView foodName,foodPrice,foodCategory;
    String categoryFood = "";

    Foods newFood;
    DatabaseReference root,dbFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);

        tabLayout = findViewById(R.id.tabFoodMaster);
        viewPager = findViewById(R.id.viewPagerFoodMaster);
        addFood = findViewById(R.id.fbAddFood);

        root = FirebaseDatabase.getInstance().getReference();
        dbFood = root.child("Foods");
        
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFood();
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Makanan"));
        tabLayout.addTab(tabLayout.newTab().setText("Minuman"));
        tabLayout.addTab(tabLayout.newTab().setText("Tambahan"));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        MasterFoods masterFoods = new MasterFoods();
                        return masterFoods;
                    case 1:
                        MasterDrinks masterDrinks = new MasterDrinks();
                        return masterDrinks;
                    case 2:
                        MasterFoodAdditional masterFoodAdditional = new MasterFoodAdditional();
                        return masterFoodAdditional;
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

    private void addNewFood() {
        AlertDialog.Builder createFood = new AlertDialog.Builder(this);
        createFood.setCancelable(false);
        createFood.setMessage("Buat Data Menu Baru");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogAddNewFood = layoutInflater.inflate(R.layout.dialog_add_food,null);
        createFood.setView(dialogAddNewFood);

        foodName = dialogAddNewFood.findViewById(R.id.etNewFoodName);
        foodPrice = dialogAddNewFood.findViewById(R.id.etNewFoodPrice);
        foodCategory = dialogAddNewFood.findViewById(R.id.etNewFoodCategory);

        foodCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(FoodsActivity.this,foodCategory);
                popupMenu.getMenuInflater().inflate(R.menu.menu_foods_category,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.food_category_food){
                            categoryFood = "Makanan";
                            foodCategory.setText("Makanan");
                        } else if (itemId == R.id.food_category_drink){
                            categoryFood = "Minuman";
                            foodCategory.setText("Minuman");
                        }  else {
                            categoryFood = "unSelect";
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        createFood.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        createFood.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (foodName.getText().toString().length() == 0){
                    Toast.makeText(FoodsActivity.this, "Gagal. Nama Menu Invalid", Toast.LENGTH_SHORT).show();
                } else if (foodPrice.getText().toString() == "Rp 0" || foodPrice.getText().toString().length() == 0){
                    Toast.makeText(FoodsActivity.this, "Gagal. Harga Invalid", Toast.LENGTH_SHORT).show();
                } else if (categoryFood.equals("unSelect") || categoryFood.equals("")){
                    Toast.makeText(FoodsActivity.this, "Gagal. Kategori Invalid", Toast.LENGTH_SHORT).show();
                } else {
                    newFood = new Foods(foodName.getText().toString(),categoryFood,Double.parseDouble(foodPrice.getText().toString()),1);
                    if (newFood!=null){
                        dbFood.push().setValue(newFood);
                        Toast.makeText(FoodsActivity.this, "Sukses. Menu "+foodName.getText().toString()+" Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        createFood.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}