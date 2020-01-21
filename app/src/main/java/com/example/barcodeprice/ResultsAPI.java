package com.example.barcodeprice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ResultsAPI {
    //https://api.upcitemdb.com/prod/trial/lookup?upc=9781586170349
    @GET("prod/trial/lookup")
    Call<ResultsModel> getResultsList(
            @Query("upc") String upc
    );
}
