package com.thisisnotyours.registertaxikotlin.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.adapter.CarInfoAdapter
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarSearchBinding
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.model.CarInfoVOS
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CarSearchFragment : Fragment(), View.OnClickListener {
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

        //조회버튼 클릭리스너
        binding.btnSearch.setOnClickListener(this)
        binding.btnSearch.performClick()


//        setCarInfoRecyclerView(mContext)

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

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btn_search -> {carInfoListCnt()}  //[조회버튼]: 조회차량 건수출력
        }
    }

    //차량조회 리스트 건수 - retrofit call
    //retrofit 비동기?
    //without using coroutine
    private fun carInfoListCnt() {
        val call = openApiObject.retrofitService.GetCarInfoCnt(binding.etCarNum.text.toString()
                                                                ,binding.etMdn.text.toString()
                                                                ,binding.etCompanyName.text.toString())
        call.enqueue(object : retrofit2.Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (!response.body().toString().equals("") || response.body().toString() != null) {
                    showLog("onResponse_str: "+response.body().toString())
                    binding.tvResultCnt.text = response.body().toString()

                    if (response.body().toString().equals("0")) {
                        //검색건수가 없을 때 리사이클러뷰 초기화
                        carinfoList.clear()
                        binding.carinfoRecyclerview.removeAllViews()
                        carInfoAdapter.notifyDataSetChanged()
                        showLog("건수_0")
                    }else{
                        showLog("건수_있음")
                        //조회 차량리스트
                        setCarInfoRecyclerView(mContext)
                    }
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

        //조회차량 리스트 데이터 call
        carInfoList_by_MVVM()

        //initialize CarInfoAdapter
        carInfoAdapter = CarInfoAdapter(context, carinfoList) {
            //do nothing?
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
            getYesterdayString()?.let {
                getCurrentDayString()?.let { it1 ->
                    carInfoViewModel.getCarInfoList(binding.etCarNum.text.toString()
                        ,binding.etMdn.text.toString()
                        ,binding.etCompanyName.text.toString()
                        ,it   //yesterday
                        ,it1  //today
                        ,"0","5")
                        .let {
                            if (!it.isSuccessful) return@let
                            if (it.body() == null) return@let

                            showLog("DATA-> "+binding.etCarNum.text.toString())
                            showLog("DATA-> "+binding.etMdn.text.toString())
                            showLog("DATA-> "+binding.etCompanyName.text.toString())
                            showLog("DATA-> "+it)
                            showLog("DATA-> "+it1)

//                            showLog(it.body()?.carInfoVO?.get(0)?.company_name.toString())

                            //me: 리스트 사이즈만큼 세팅해야함
                            //데이터 리스트 -> 리사이클러뷰에 표출
                            carinfoList.addAll(it.body()?.carInfoVO as MutableList<CarInfoVOS>)  //carInfoItem 해도됨
                            showLog("DATA_SIZE-> "+carinfoList.size)  //건수
                            showLog("DATA_RESULT-> "+carinfoList.toString())

                            binding.carinfoRecyclerview.adapter = carInfoAdapter   //lateinit property carInfoAdapter has not been initialized
                            binding.carinfoRecyclerview.layoutManager =
                                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

                            binding.carinfoRecyclerview.post{
                                carInfoAdapter.notifyDataSetChanged()
                            }
                        }
                }
            }
        }
    }

    private fun getYesterdayString(): String? {
        val day = Calendar.getInstance()
        day.add(Calendar.DATE, -1)
        return SimpleDateFormat("yyyyMMddHHmmss").format(day.time)
    }

    private fun getCurrentDayString(): String? {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val time = Calendar.getInstance()
        return sdf.format(time.time)
    }

    private fun showAlertDialog(title: String, type: String) {
        when(type) {
            "cancel" -> {  //등록 또는 수정 취소
                val dialog: AlertDialog.Builder = AlertDialog.Builder(mContext)
                dialog.setTitle(title+"을 취소하시겠습니까?")
                    .setNegativeButton("아니오", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
                    .setPositiveButton("예", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->

                    })
                dialog.show()
            }
            "transfer" -> {  //수정화면으로 이동
                val dialog: AlertDialog.Builder = AlertDialog.Builder(mContext)
                dialog.setTitle(title+"을 하려면 확인버튼을 눌러주세요")
                    .setNegativeButton("취소", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
                    .setPositiveButton("확인", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->

                    })
                dialog.show()
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