package com.netfix.network

import com.netfix.di.NFModule
import com.netfix.util.AppConst
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  NFServiceClient {

    fun getClient():NFAPIServices{

        var logIntercepter = HttpLoggingInterceptor()
        logIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY)

        var client = OkHttpClient.Builder()
            .addInterceptor(logIntercepter)
            .build()

        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppConst.BASE_URL)
            .build()
            .create(NFAPIServices::class.java)
    }

}