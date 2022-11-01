package com.thisisnotyours.registertaxikotlin.data

import com.thisisnotyours.registertaxikotlin.model.LoginResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginInfoApiService {
    //login api
    @POST("mobile_login")
    suspend fun GetLoginInfo(@Query("id") id: String, @Query("pw") pw: String): Response<LoginResponse>
}