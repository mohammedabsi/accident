package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReturnResultApiInterface {
    @GET("get_all_results")
    Call<List<List<ReturnResult>>> getResultList();
}
