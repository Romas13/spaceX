package com.example.spacex.data.api

import com.example.spacex.data.model.launch.entities.Launch
import com.example.spacex.data.model.payload.Payload
import com.example.spacex.data.model.rocket.Rocket
import retrofit2.http.GET
import retrofit2.http.Path

interface SpaceXApi {

    companion object {
        const val BASE_URL = "https://api.spacexdata.com"
    }

    @GET("v4/launches")
    suspend fun getLaunches() : Array<Launch>

    @GET("/v4/rockets/{id}")
    suspend fun getRocketInfo(@Path("id") rocketId:String) : Rocket

    @GET("/v4/payloads/{id}")
    suspend fun getPayload(@Path("id") payloadId:String) : Payload

}