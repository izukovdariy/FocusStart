package com.example.focusstart.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.focusstart.R;
import com.example.focusstart.api.NetworkService;
import com.example.focusstart.model.ExchangeRates;
import com.example.focusstart.model.ValuteModel;


import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConverterFragment extends Fragment {
    private EditText amountEditText;
    private TextView resultTextView;
    private Spinner firstCurrencySpinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_converter, container, false);
        resultTextView = view.findViewById(R.id.result_tv);
        amountEditText = view.findViewById(R.id.amount_tv);
        firstCurrencySpinner = view.findViewById(R.id.first_currency_sp);

        NetworkService.getInstance()
                        .getConverterApi()
                        .getRates()
                        .enqueue(new Callback<ExchangeRates>() {
                            @Override
                            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                                if(response.code()==404){
                                    resultTextView.setText("Ошибка");
                                    return;
                                }
                                    final ExchangeRates exchangeRates = response.body();
                                    ArrayList<String> keys = new ArrayList(exchangeRates.getValute().keySet());
                                    String[] currencies = new String[keys.size()];
                                    for(int i=0; i<exchangeRates.getValute().size(); i++){
                                        currencies[i] = keys.get(i);
                                    }
                                    final ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, currencies);
                                    firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    firstCurrencySpinner.setAdapter(firstAdapter);

                                    amountEditText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            String firstCurrency = (String) firstCurrencySpinner.getSelectedItem();
                                            ValuteModel selectedCurrency = exchangeRates.getValute().get(firstCurrency);
                                            double amount;
                                            if(amountEditText.getText().toString().equals("")) {
                                                amount = 0;
                                            } else amount = Double.parseDouble(amountEditText.getText().toString());
                                            double result = convertFromRouble(amount, selectedCurrency);

                                            resultTextView.setText(String.format("%.2f %n", result));
                                        }
                                    });

                                    firstCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String firstCurrency = (String) firstCurrencySpinner.getSelectedItem();
                                            ValuteModel selectedCurrency = exchangeRates.getValute().get(firstCurrency);
                                            double amount;
                                            if(amountEditText.getText().toString().equals("")) {
                                                amount = 0;
                                            } else amount = Double.parseDouble(amountEditText.getText().toString());
                                            double result = convertFromRouble(amount, selectedCurrency);

                                            resultTextView.setText(String.format("%.2f %n", result));
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                            }
                            @Override
                            public void onFailure(Call<ExchangeRates> call, Throwable t) {
                                resultTextView.setText("Ошибка");
                            }
                        });

        return view;
    }

    public double convertFromRouble(Double amount, ValuteModel currency){
        double nominal = currency.getNominal().doubleValue();
        double value = currency.getValue();
        double result = nominal/value * amount;
        return result;
    }
}
