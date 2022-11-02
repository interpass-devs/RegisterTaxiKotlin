package com.thisisnotyours.registertaxikotlin.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.data.CarInfoApiService
import com.thisisnotyours.registertaxikotlin.databinding.ActivityMainBinding
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


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
class MainActivity : AppCompatActivity(), View.OnClickListener {
    val log: String = "log_"
//    private val carInfoViewModel: CarInfoViewModel? = null  //이거 쓰니까 데이터가 안왔음
//    val carInfoViewModel: CarInfoViewModel by viewModels()
    private lateinit var carInfoViewModel: CarInfoViewModel
    private var resultData: CarInfoResponse? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.searchCar.setOnClickListener(this)
        binding.registerCar.setOnClickListener(this)

        //these needs to be move to fragment
        carInfoViewModel = ViewModelProvider(this@MainActivity).get(CarInfoViewModel::class.java)

        carInfoListResultData_by_Coroutine()

        carInfoListCnt()

    }//onCreate..


    //클릭리스너
    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.search_car -> changeFragment(0)
            R.id.register_car -> changeFragment(1)
        }
    }

    //해당 프래그먼트로 이동
    private fun changeFragment(itemId: Int) {
        showTimber(itemId.toString())
        when(itemId) {
            0 -> {  //차량조회 화면
                val searchFrag = CarSearchFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_change, searchFrag)
                    .commit()
            }
            1 -> { //차량등록/수정 화면
                val registerFrag = CarRegistrationFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_change, registerFrag)
                    .commit()
            }
        }
    }

    private fun showTimber(msg: String) {
        Timber.d(msg)
    }


    //차량조회 리스트) -방법2
    //MVVM 패턴 사용하여 비동기방식(코루틴 스레드)으로 데이터 fetching
    //코루틴 & MVVM 사용하여 데이터 fetch
    private fun carInfoListResultData_by_Coroutine() {
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

                    //데이터 리스트 -> 리사이클러뷰에 표출해야함..

                }
        }
    }


    //차량조회 리스트 개수)
    //retrofit call
    private fun carInfoListCnt() {
        val call = openApiObject.retrofitService.GetCarInfoCnt("","","")
        call.enqueue(object : retrofit2.Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(log+"onResponse_str", response.body().toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(log+"onResponse_str", t.message.toString())
            }

        })
    }


    //차량조회 리스트) -방법1
    //MVVM 패턴을 쓰지않고, 비동기 방식말고 직접적으로 retrofit call 로 데이터 fetching
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
                Log.e(log+"onFailure_list", call.request().toString())
                Log.e(log+"onFailure_list", t.message.toString())
            }

        })
    }



}


