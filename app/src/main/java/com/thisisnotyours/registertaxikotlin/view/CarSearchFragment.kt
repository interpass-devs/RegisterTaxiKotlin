package com.thisisnotyours.registertaxikotlin.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.adapter.CarInfoAdapter
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarSearchBinding
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
    private lateinit var carInfoAdapter: CarInfoAdapter
    private var carinfoList = mutableListOf<CarInfoVOS>()
    private var offset: Int = 0
    private var limit: Int = 10
    private var totalItemCnt: Int = 0

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

        fragmentType = "search"

        //initialize carInfoViewModel
        carInfoViewModel = ViewModelProvider(this).get(CarInfoViewModel::class.java)

        //initialize CarInfoAdapter to set RecyclerView items
        carInfoAdapter = CarInfoAdapter(mContext, carinfoList) {
            //do nothing?
        }

        binding.btnSearch.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.btnSearch.performClick()

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
            R.id.btn_search -> {  //[조회]버튼
                offset = 0;  //초기화
                carInfoListCnt()  //조회차량 건수출력
            }
            R.id.btn_reset -> { //[초기화]버튼
                carinfoList.clear()
                binding.carinfoRecyclerview.removeAllViews()
                carInfoAdapter.notifyDataSetChanged()
                resetEditTextValue(binding.etCarNum)
                resetEditTextValue(binding.etCompanyName)
                resetEditTextValue(binding.etMdn)
                binding.tvResultCnt.text = "0"
                binding.btnBack.visibility = View.GONE
                binding.btnNext.visibility = View.GONE
                binding.btnEmpty.visibility = View.GONE
            }
            R.id.btn_next -> {  //[다음]버튼
                offset += 10
                setCarInfoRecyclerView(mContext, offset, limit, totalItemCnt)
            }
            R.id.btn_back -> { //[이전]버튼
                if (offset == 0) {
                    offset = -10
                }else if (offset > 0) {
                    offset -= 10
                }
                setCarInfoRecyclerView(mContext, offset, limit, totalItemCnt)
            }

        }
    }

    //editText 값 초기화
    private fun resetEditTextValue(et : EditText) {
        et.setText(null)
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
                    totalItemCnt = (response.body().toString()).toInt()

                    if (response.body().toString().equals("0")) {
                        //검색건수가 없을 때 리사이클러뷰 초기화
                        carinfoList.clear()
                        binding.carinfoRecyclerview.removeAllViews()
                        carInfoAdapter.notifyDataSetChanged()
                    }else{
                        //검색건수 있을 때 리사이클러뷰 데이터 표출
                        //조회 차량리스트
                        carinfoList.clear()
                        carInfoAdapter.notifyDataSetChanged()
                        setCarInfoRecyclerView(mContext, offset, limit, totalItemCnt)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                showLog("onResponse_str: "+t.message.toString())
            }

        })
    }

    //차량정보 리사이클러뷰 세팅
    private fun setCarInfoRecyclerView(context: Context, offset: Int, limit: Int, itemCnt: Int) {
        //조회차량 리스트 데이터 call
        carInfoList_by_MVVM(offset, limit, itemCnt)

        binding.carinfoRecyclerview.adapter = carInfoAdapter   //lateinit property carInfoAdapter has been initialized at onCreate
        binding.carinfoRecyclerview.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }


    //차량조회 리스트
    //MVVM 패턴 사용하여 비동기방식(코루틴 스레드)으로 데이터 fetching
    //코루틴 & MVVM 사용하여 데이터 fetch
    private fun carInfoList_by_MVVM(offset: Int, limit: Int, totalItemCnt: Int) {
        showLog("itemCnt==> "+totalItemCnt+"개,   offset:"+offset+", limit:"+limit)

        if (offset == -10) {
            showToast("처음 페이지입니다")
        }else if (offset > totalItemCnt) {
            showToast("마지막 페이지입니다")
        }else{
            lifecycleScope.launch {
                getYesterdayString()?.let {
                    getCurrentDayString()?.let { it1 ->
                        carInfoViewModel.getCarInfoList(binding.etCarNum.text.toString()
                            ,binding.etMdn.text.toString()
                            ,binding.etCompanyName.text.toString()
                            ,it   //yesterday
                            ,it1  //today
                            ,offset.toString()
                            ,limit.toString())
                            .let {
                                if (!it.isSuccessful) return@let
                                if (it.body() == null) return@let

                                showLog("DATA-> "+binding.etCarNum.text.toString())
                                showLog("DATA-> "+binding.etMdn.text.toString())
                                showLog("DATA-> "+binding.etCompanyName.text.toString())
                                showLog("DATA-> "+it)
                                showLog("DATA-> "+it1)
//                            showLog(it.body()?.carInfoVO?.get(0)?.company_name.toString())

                                //me: 리스트 사이즈만큼 세팅해야함?
                                //리사이클러뷰 대이터 표출하기 전에 한번 초기화
                                carinfoList.clear()

                                binding.btnEmpty.visibility = View.VISIBLE
                                binding.btnBack.visibility = View.VISIBLE
                                binding.btnNext.visibility = View.VISIBLE

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

                                carInfoAdapter.setMyLongClickListener(object :
                                    CarInfoAdapter.mItemLongClickListener {
                                    override fun onItemLongClick(v: View?, pos: Int) {
//                                        showAlertDialog("수정","transfer")
                                        val dialog: AlertDialog.Builder = AlertDialog.Builder(mContext)
                                        dialog.setTitle("수정을 하려면 확인버튼을 눌러주세요")
                                            .setNegativeButton("취소", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
                                            .setPositiveButton("확인", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                                                //프래그먼트 이동 및 값전달
                                                val b = Bundle()
                                                b.putString("company_name", it.body()?.carInfoVO?.get(pos)?.company_name.toString())
                                                b.putString("car_regnum", it.body()?.carInfoVO?.get(pos)?.car_regnum.toString())
                                                b.putString("mdn", it.body()?.carInfoVO?.get(pos)?.mdn.toString())
                                                b.putString("car_type", it.body()?.carInfoVO?.get(pos)?.type_name.toString())
                                                b.putString("car_vin", it.body()?.carInfoVO?.get(pos)?.car_vin.toString())
                                                b.putString("car_num", it.body()?.carInfoVO?.get(pos)?.car_num.toString())
                                                b.putString("driver_id1", it.body()?.carInfoVO?.get(pos)?.driver_id1.toString())
                                                b.putString("driver_id2", it.body()?.carInfoVO?.get(pos)?.driver_id2.toString())
                                                b.putString("driver_id3", it.body()?.carInfoVO?.get(pos)?.driver_id3.toString())
                                                b.putString("fare_id", it.body()?.carInfoVO?.get(pos)?.fare_id.toString())
                                                b.putString("city_id", it.body()?.carInfoVO?.get(pos)?.city_id.toString())
                                                b.putString("firmware_id", it.body()?.carInfoVO?.get(pos)?.firmware_id.toString())
                                                b.putString("speed_factor", it.body()?.carInfoVO?.get(pos)?.speed_factor.toString())
                                                val frag = CarRegistrationFragment()
                                                frag.arguments = b
                                                activity?.supportFragmentManager
                                                    ?.beginTransaction()
                                                    ?.add(R.id.frame_change, frag)
                                                    ?.commit()
                                            })
                                        dialog.show()
                                    }
                                })

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

            }
        }
    }

    fun changeFragment(itemId: Int) {
        when(itemId) {
            0 -> {
                val searchFrag = CarSearchFragment()
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.add(R.id.frame_change, searchFrag)
                    ?.commit()
            }
            1 -> {
                val registerFrag = CarRegistrationFragment()
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.add(R.id.frame_change, registerFrag)
                    ?.commit()
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