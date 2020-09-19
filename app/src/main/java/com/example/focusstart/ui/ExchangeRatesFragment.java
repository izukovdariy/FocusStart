package com.example.focusstart.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.focusstart.MainActivity;
import com.example.focusstart.R;
import com.example.focusstart.api.NetworkService;
import com.example.focusstart.model.ExchangeRates;
import com.example.focusstart.model.ValuteModel;

import java.io.Console;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesFragment extends Fragment {
    private TableLayout tableLayout;
    private TextView textView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_valute, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);
        textView = view.findViewById((R.id.textView));
        NetworkService.getInstance()
                .getConverterApi()
                .getRates()
                .enqueue(new Callback<ExchangeRates>() {
                    @Override
                    public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                        if(response.code()==404){
                            textView.setText("Ошибка");
                            return;
                        }
                        final ExchangeRates exchangeRates = response.body();
                        ArrayList<ValuteModel> arrayList = new ArrayList<>(exchangeRates.getValute().values());
                        for (int i = 0; i<arrayList.size(); i++ ){
                            TableRow tableRow = new TableRow(view.getContext());
                            tableRow.setLayoutParams( new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                            TextView textCharCode = new TextView(view.getContext());
                            TextView textNominal = new TextView(view.getContext());
                            TextView textValute = new TextView(view.getContext());
                            TextView textCourse = new TextView(view.getContext());
                            textCharCode.setText(arrayList.get(i).getCharCode());
                            tableRow.addView(textCharCode, 0);
                            textNominal.setText(arrayList.get(i).getNominal().toString());
                            tableRow.addView(textNominal, 1);
                            textValute.setText(arrayList.get(i).getName());
                            tableRow.addView(textValute, 2);
                            textCourse.setText(arrayList.get(i).getValue().toString());
                            tableRow.addView(textCourse, 3);
                            tableLayout.addView(tableRow, i);

                        }

                    }
                    @Override
                    public void onFailure(Call<ExchangeRates> call, Throwable t) {
                        textView.setText("Ошибка");
                    }
                });
        return view;
    }

}
