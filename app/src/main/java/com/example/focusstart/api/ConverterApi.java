package com.example.focusstart.api;

import com.example.focusstart.model.ExchangeRates;


import retrofit2.Call;
import retrofit2.http.GET;

public interface ConverterApi {
    @GET("/daily_json.js")
    Call<ExchangeRates> getRates();

}
