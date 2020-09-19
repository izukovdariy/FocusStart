package com.example.focusstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.focusstart.api.NetworkService;
import com.example.focusstart.model.ExchangeRates;
import com.example.focusstart.model.ValuteModel;
import com.example.focusstart.ui.ConverterFragment;
import com.example.focusstart.ui.ExchangeRatesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    final FragmentManager fm = getSupportFragmentManager();
    final Fragment converterFragment = new ConverterFragment();
    final Fragment exchangeRatesFragment = new ExchangeRatesFragment();
    BottomNavigationView bottomNavigationView;
    Fragment active = converterFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.converter);
        fm.beginTransaction().add(R.id.fragments, converterFragment, "converter").commit();
        fm.beginTransaction().add(R.id.fragments, exchangeRatesFragment, "exchange_rates").hide(exchangeRatesFragment).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.converter:
                    fm.beginTransaction().hide(active).show(converterFragment).commit();
                    active = converterFragment;
                    return true;

                case R.id.exchange_rates:
                    fm.beginTransaction().hide(active).show(exchangeRatesFragment).commit();
                    active = exchangeRatesFragment;
                    return true;

                default: return false;
            }
        }
    };
}