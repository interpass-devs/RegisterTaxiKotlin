package com.thisisnotyours.registertaxikotlin.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.data.CarInfoApiService
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.model.UserInfoResponse
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//////
private val retrofit = Retrofit.Builder()
    .baseUrl("http://49.50.174.192:8080/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object openApiObject {
    val retrofitService: CarInfoApiService by lazy {
        retrofit.create(CarInfoApiService::class.java)
    }
}

//@HiltAndroidApp
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
//    private lateinit var job: Job
//    override val coroutineContext: CoroutineContext
//    get() = Dispatchers.Main + job
    val log: String = "log_"
    private val carInfoViewModel: CarInfoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLoginData()

        getCarInfoListData()

        getCarInfoListCoroutine()

    }//onCreate..


    private fun getLoginData() {
        Log.d(log+"onResponse", "call login api")

        val call = openApiObject.retrofitService.GetLoginInfo("test","test")

        call.enqueue(object : retrofit2.Callback<UserInfoResponse>{
            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
//                Log.d(log+"onResponse", call.request().url.toString())
//                Log.d(log+"onResponse", response.body().toString())
                if (response.isSuccessful) {

                    Log.d(log+"onResponse", "isSuccessful")
                }else{
                    Log.d(log+"onResponse", "is not successful")
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {

            }
        })
    }


    private fun getCarInfoListData() {
        Log.d(log+"onResponse", "getCarInfoListData")

        val call = openApiObject.retrofitService.GetCarInfoData("","","","20221026104924","20221027104924","0","5")

        call.enqueue(object : retrofit2.Callback<CarInfoResponse>{
            override fun onResponse(call: Call<CarInfoResponse>, response: Response<CarInfoResponse>) {
                Log.d(log+"onResponse", call.request().toString())
                Log.d(log+"onResponse", response.body().toString())
                if (response.isSuccessful) {
                    val item: CarInfoResponse? = response.body()
                    Log.d(log+"onResponse_item", item?.carInfoVO.toString())
                    Log.d(log+"onResponse", "successful")
                    Log.d(log+"onResponse", item.toString())
                }else{
                    Log.d(log+"onResponse", "not successful")
                }
            }

            override fun onFailure(call: Call<CarInfoResponse>, t: Throwable) {
                Log.d(log+"onFailure", call.request().toString())
                Log.d(log+"onFailure", t.message.toString())
            }

        })
    }


    private fun getCarInfoListCoroutine() {
        lifecycleScope.launch {
            Log.d(log+"coroutine", "coroutine")

            carInfoViewModel?.getCarInfoList("","","","20221026104924","20221027104924","0","5")
                .let {
                    if (it != null) {
                        if (!it.isSuccessful){
                            Log.d(log+"coroutine", "faile")
                        }else{
                            Log.d(log+"coroutine", "is successful")
                        }
                    }
                }
        }
    }

}