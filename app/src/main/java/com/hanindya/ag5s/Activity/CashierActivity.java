package com.hanindya.ag5s.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hanindya.ag5s.Fragment.Cashier.CashierCancel;
import com.hanindya.ag5s.Fragment.Cashier.CashierComplete;
import com.hanindya.ag5s.Fragment.Cashier.CashierProcess;
import com.hanindya.ag5s.R;

public class CashierActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        tabLayout = findViewById(R.id.tabCashier);
        viewPager = findViewById(R.id.viewPagerCashier);

        tabLayout.addTab(tabLayout.newTab().setText("Proses"));
        tabLayout.addTab(tabLayout.newTab().setText("Selesai"));
        tabLayout.addTab(tabLayout.newTab().setText("Batal"));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        CashierProcess cashierProcess = new CashierProcess();
                        return cashierProcess;
                    case 1:
                        CashierComplete cashierComplete = new CashierComplete();
                        return cashierComplete;
                    case 2:
                        CashierCancel cashierCancel = new CashierCancel();
                        return cashierCancel;
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