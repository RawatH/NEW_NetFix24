package com.netfix.di

import com.netfix.network.NFAPIServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object  NFModule {

    val BASE_URL = "https://netfix24.webgalaxy.in/"

    @Provides
    fun provideNFAPIServices():NFAPIServices{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
            .create(NFAPIServices::class.java)
    }
}