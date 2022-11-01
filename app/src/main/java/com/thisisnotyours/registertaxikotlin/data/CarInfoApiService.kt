package com.thisisnotyours.registertaxikotlin.data

import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.model.UserInfoResponse
import retrofit2.Response
import retrofit2.http.*

interface CarInfoApiService {
    //login api
    @POST("mobile_login")
    fun GetLoginInfo(@Query("id") id: String, @Query("pw") pw: String)
    : retrofit2.Call<UserInfoResponse>


    @GET("get-connection-info")
    fun GetCarInfoData(@Query("car_num") car_num: String
                       , @Query("mdn") mdn: String
                       , @Query("company_name") company_name: String
                       , @Query("st_dtti") st_dtti: String
                       , @Query("et_dtti") et_dtti: String
                       , @Query("offset") offset: String
                       , @Query("limit") limit: String
    ): retrofit2.Call<CarInfoResponse>


//    @Headers("Content-Type: application/json")
    @GET("get-connection-info")
    suspend fun GetCarInfoSuspend(@Query("car_num") car_num: String
                       , @Query("mdn") mdn: String
                       , @Query("company_name") company_name: String
                       , @Query("st_dtti") st_dtti: String
                       , @Query("et_dtti") et_dtti: String
                       , @Query("offset") offset: String
                       , @Query("limit") limit: String
    ): Response<CarInfoResponse>
}