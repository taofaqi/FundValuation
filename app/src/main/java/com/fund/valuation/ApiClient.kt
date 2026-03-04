package com.fund.valuation

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SupabaseApi {
    @GET("rest/v1/user_configs")
    suspend fun getUserConfigs(
        @Query("select") select: String = "id,data,updated_at",
        @Query("user_id") userId: String,
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String
    ): List<UserConfigRecord>
}

object ApiClient {
    val api: SupabaseApi = Retrofit.Builder()
        .baseUrl("https://mouvsqlmgymsaxikvqsh.supabase.co/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SupabaseApi::class.java)
}
