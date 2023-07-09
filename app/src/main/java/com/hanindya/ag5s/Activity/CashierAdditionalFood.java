package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.hanindya.ag5s.Fragment.Additional.AdditionalDrinks;
import com.hanindya.ag5s.Fragment.Additional.AdditionalFoods;
import com.hanindya.ag5s.R;

public class CashierAdditionalFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_additional_food);

        TabLayout tabLayout = findViewById(R.id.tabAdditionalMenu);
        ViewPager viewPager = findViewById(R.id.viewPagerAdditionalMenu);

        tabLayout.addTab(tabLayout.newTab().setText("Makanan"));
        tabLayout.addTab(tabLayout.newTab().setText("Minuman"));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        AdditionalFoods additionalFoods = new AdditionalFoods();
                        return additionalFoods;
                    case 1:
                        AdditionalDrinks additionalDrinks = new AdditionalDrinks();
                        return additionalDrinks;
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
}