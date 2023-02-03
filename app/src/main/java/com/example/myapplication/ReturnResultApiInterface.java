package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReturnResultApiInterface {
    @POST("get_photo")
    Call<List<List<ReturnResult>>> getResultList();
}
