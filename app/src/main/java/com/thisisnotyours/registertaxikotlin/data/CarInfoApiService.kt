package com.thisisnotyours.registertaxikotlin.data

import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import retrofit2.Response
import retrofit2.http.*

interface CarInfoApiService {

    //차량조회 api 1)
    @GET("get-connection-info")
    fun GetCarInfoData(@Query("car_num") car_num: String
                       , @Query("mdn") mdn: String
                       , @Query("company_name") company_name: String
                       , @Query("st_dtti") st_dtti: String
                       , @Query("et_dtti") et_dtti: String
                       , @Query("offset") offset: String
                       , @Query("limit") limit: String
    ): retrofit2.Call<CarInfoResponse>


    //차량조회 api 2) suspend function
    @GET("get-connection-info")
    suspend fun GetCarInfoSuspend(@Query("car_num") car_num: String
                       , @Query("mdn") mdn: String
                       , @Query("company_name") company_name: String
                       , @Query("st_dtti") st_dtti: String
                       , @Query("et_dtti") et_dtti: String
                       , @Query("offset") offset: String
                       , @Query("limit") limit: String
    ): Response<CarInfoResponse>


    //차량조회 건수 api)
    @GET("get-connection-info-length")
    fun GetCarInfoCnt(@Query("car_num") car_num: String
                                     , @Query("mdn") mdn: String
                                     , @Query("company_name") company_name: String
    ): retrofit2.Call<String>
}