package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReturnResultApiInterface {
    @GET("get_photo")
    Call<List<List<ReturnResult>>> getResultList();
}
