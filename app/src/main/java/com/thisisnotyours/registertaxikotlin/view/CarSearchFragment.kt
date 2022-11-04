package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thisisnotyours.registertaxikotlin.adapter.CarInfoAdapter
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarSearchBinding
import com.thisisnotyours.registertaxikotlin.item.CarInfoItem
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.model.CarInfoVOS
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

@AndroidEntryPoint
class CarSearchFragment : Fragment() {
    var log = "log_"
    private lateinit var binding: FragmentCarSearchBinding
    private lateinit var mContext: Context
//    private val carInfoViewModel: CarInfoViewModel? = null  //이거 쓰니까 데이터가 안왔음
//    val carInfoViewModel: CarInfoViewModel by viewModels()
    private lateinit var carInfoViewModel: CarInfoViewModel
    private var resultData: CarInfoResponse? = null
    private lateinit var carInfoAdapter: CarInfoAdapter
    private var carinfoList = mutableListOf<CarInfoVOS>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLog("LifeCycle_Search_frag: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarSearchBinding.inflate(inflater)

        showLog("LifeCycle_Search_frag: onCreateView")

        mContext = requireActivity()

        //initialize carInfoViewModel
        carInfoViewModel = ViewModelProvider(this).get(CarInfoViewModel::class.java)

        //차량조회 건수
        carInfoListCnt()

        //initialize CarInfoAdapter
        setCarInfoRecyclerView(mContext)

        //차량조회 정보
//        carInfoList_by_MVVM()

//        carInfoList_by_Coroutine()

        return binding.root
    }

    //로그대신 timber 라이브러리 사용
    fun showTimber(msg: String) {
        Timber.d("timber: "+msg)
    }

    fun showLog(msg: String) {
        Log.d(log, msg)
    }

    //토스트 메소드
    fun showToast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLog("LifeCycle_Search_frag: onViewCreated")
    }

    //차량조회 리스트 건수 - retrofit call
    //retrofit 비동기?
    //without using coroutine
    private fun carInfoListCnt() {
        val call = openApiObject.retrofitService.GetCarInfoCnt("","","")
        call.enqueue(object : retrofit2.Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (!response.body().toString().equals("") || response.body().toString() != null) {
                    showLog("onResponse_str: "+response.body().toString())
                    binding.tvResultCnt.text = response.body().toString()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                showLog("onResponse_str: "+t.message.toString())
            }

        })
    }

    //차량정보 리사이클러뷰 세팅
    private fun setCarInfoRecyclerView(context: Context) {

        showLog("add_carinfoList_0:  "+carinfoList.toString())

        carInfoList_by_MVVM()

        carInfoAdapter = CarInfoAdapter(context, carinfoList) {
            showLog("add_carinfoList_1:  "+carinfoList.toString())
            carInfoList_by_MVVM()
        }
        binding.carinfoRecyclerview.adapter = carInfoAdapter   //lateinit property carInfoAdapter has not been initialized
        binding.carinfoRecyclerview.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)


    }


    //차량조회 리스트
    //MVVM 패턴 사용하여 비동기방식(코루틴 스레드)으로 데이터 fetching
    //코루틴 & MVVM 사용하여 데이터 fetch
    private fun carInfoList_by_MVVM() {
        lifecycleScope.launch {
            carInfoViewModel.getCarInfoList("","","","20221026104924","20221027104924","0","5")
                .let {
                    if (!it.isSuccessful) return@let
                    if (it.body() == null) return@let

                    showLog(it.body().toString())
                    showLog(it.body()?.carInfoVO?.get(0)?.company_name.toString())
                    showLog(it.body()?.carInfoVO?.get(0)?.car_regnum.toString())
                    showLog(it.body()?.carInfoVO?.get(0)?.type_name.toString())
                    showLog(it.body()?.carInfoVO?.get(0)?.car_vin.toString())

                    showLog("add_carinfoList_2:  "+carinfoList.toString())

                    //데이터 리스트 -> 리사이클러뷰에 표출해야함..

//                    for (i in 0 until item.getCarInfoVOS().size()) {
//                    for (i in 0 until it.body().carInfoVO.si) {
//                        setCarInfoRecyclerViewItems(mContext)
//                    }
                    carinfoList.addAll(it.body()?.carInfoVO as MutableList<CarInfoVOS>)  //carInfoItem 해도됨
                    showLog("add_carinfoList_3_0:  "+carinfoList.size)
                    showLog("add_carinfoList_3_1:  "+carinfoList.toString())
                    binding.carinfoRecyclerview.post{
                        carInfoAdapter.notifyDataSetChanged()
                    }
                }
        }
    }



    private fun carInfoList_by_Coroutine() {
//        val service: CarInfoApiService = retrofit.create(CarInfoApiService::class.java)
//        val carInfoList = service.GetCarInfoData("","","","20221026104924","20221027104924","0","5")
//        val response = carInfoList.execute()
//        CoroutineScope(Dispatchers.Main).launch {
//            val body = response.body()
//
//            if (response.isSuccessful) {
//                if (body != null) {
//                    Log.d(log+"response", body.toString())
//                }
//            }
//        }
    }




}