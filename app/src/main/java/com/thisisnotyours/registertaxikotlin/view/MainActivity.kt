package com.thisisnotyours.registertaxikotlin.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.data.CarInfoApiService
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.model.CarInfoVOS
import com.thisisnotyours.registertaxikotlin.model.UserInfoResponse
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private val retrofit = Retrofit.Builder()
    .baseUrl("http://49.50.174.192:8080/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object openApiObject {
    val retrofitService: CarInfoApiService by lazy {
        retrofit.create(CarInfoApiService::class.java)
    }
}

//@HiltAndroidApp - application 에서만 상용할 수 있음.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val log: String = "log_"
//    private val carInfoViewModel: CarInfoViewModel? = null
//    val carInfoViewModel: CarInfoViewModel by viewModels()
    private lateinit var carInfoViewModel: CarInfoViewModel
    private var resultData: CarInfoResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        carInfoViewModel = ViewModelProvider(this@MainActivity).get(CarInfoViewModel::class.java)

        getLoginData()

        getCarInfoListData()

        CarInfoListResultData_by_Coroutine()

    }//onCreate..


    //차량조회 리스트
    //비동기 방식말고 직접적으로 retrofit call 로 데이터 fetching
    private fun getLoginData() {
        Log.d(log+"onResponse_login", "call login api")

        val call = openApiObject.retrofitService.GetLoginInfo("test","test")

        call.enqueue(object : retrofit2.Callback<UserInfoResponse>{
            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                Log.d(log+"onResponse_login", response.body().toString())
                if (response.isSuccessful) {

                    Log.d(log+"onResponse_login", "isSuccessful")
                }else{
                    Log.d(log+"onResponse_login", "is not successful")
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {

            }
        })
    }

    //차량조회 리스트
    //MVVM 패턴 사용하여 비동기방식(코루틴 스레드)으로 데이터 fetching
    private fun getCarInfoListData() {
        Log.d(log+"onResponse_list", "getCarInfoListData")

        val call = openApiObject.retrofitService.GetCarInfoData("","","","20221026104924","20221027104924","0","5")

        call.enqueue(object : retrofit2.Callback<CarInfoResponse>{
            override fun onResponse(call: Call<CarInfoResponse>, response: Response<CarInfoResponse>) {
                Log.d(log+"onResponse_list", call.request().toString())
                Log.d(log+"onResponse_list", response.body().toString())
                if (response.isSuccessful) {
                    val item: CarInfoResponse? = response.body()
                    Log.d(log+"onResponse_list", item?.carInfoVO.toString())
                    Log.d(log+"onResponse_list", "successful")
                    Log.d(log+"onResponse_list", item.toString())
                }else{
                    Log.d(log+"onResponse_list", "not successful")
                }
            }

            override fun onFailure(call: Call<CarInfoResponse>, t: Throwable) {
                Log.d(log+"onFailure_list", call.request().toString())
                Log.d(log+"onFailure_list", t.message.toString())
            }

        })
    }

    //코루틴 & MVVM 사용하여 데이터 fetch
    private fun CarInfoListResultData_by_Coroutine() {
        lifecycleScope.launch {
            carInfoViewModel.getCarInfoList("","","","20221026104924","20221027104924","0","5")
                .let {
                    if (!it.isSuccessful) return@let

                    if (it.body() == null) return@let

                    Log.d(log+"coroutine", it.body().toString())
                    Log.d(log+"coroutine", it.body()?.carInfoVO?.get(0)?.company_name.toString())
                    Log.d(log+"coroutine", it.body()?.carInfoVO?.get(0)?.car_regnum.toString())
                    Log.d(log+"coroutine", it.body()?.carInfoVO?.get(0)?.type_name.toString())
                    Log.d(log+"coroutine", it.body()?.carInfoVO?.get(0)?.car_vin.toString())

                }
        }
    }

}


