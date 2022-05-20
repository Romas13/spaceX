package com.example.spacex.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpaceXService {
    companion object {
        private var service: SpaceXApi? = null

        fun getInstance() : SpaceXApi {
            if (service == null) {
                service = initService()
            }
            return service!!
        }

        private fun initService(): SpaceXApi {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(SpaceXApi.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(SpaceXApi::class.java)
        }
    }
}