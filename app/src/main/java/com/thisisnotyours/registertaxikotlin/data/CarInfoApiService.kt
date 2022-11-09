package com.thisisnotyours.registertaxikotlin.data

import com.google.gson.GsonBuilder
import com.thisisnotyours.registertaxikotlin.BuildConfig
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.model.CarInfoSpinnerResponse
import com.thisisnotyours.registertaxikotlin.view.MainActivity
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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


    //요금조회 api
    @POST("get-fare-type")
    suspend fun GetFareIdSuspend(): Response<CarInfoSpinnerResponse>

    //시경계조회 api
    @POST("get-city-type")
    suspend fun GetCityIdSuspend(): Response<CarInfoSpinnerResponse>

    //벤사조회 api
    @POST("get-van-type")
    suspend fun GetFirmwareIdSuspend(): Response<CarInfoSpinnerResponse>

    //차량등록 insert api
    @GET("put-car-info")
    fun InsertCarData(@QueryMap map: HashMap<String, String>): retrofit2.Call<String>

    //차량등록 업데이트 api)  --> 현재 사용안함
    @Headers("Content-Type: application/json")
    @GET("update-car-info")
    suspend fun UpdateCarInfo(@QueryMap map: HashMap<String, String>): Response<String>

    //차량등록 업데이트 api) --> 현재 이걸로 사용함
    @GET("update-car-info")
    fun UpdateCarData(@QueryMap map: Map<String, String>): retrofit2.Call<String>

}