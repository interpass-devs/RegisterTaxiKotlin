package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarRegistrationBinding
import com.thisisnotyours.registertaxikotlin.model.CarInfoSpinnerResponse
import com.thisisnotyours.registertaxikotlin.model.SpinnerVOS
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CarRegistrationFragment : Fragment(), View.OnClickListener {
    val log = "log_"
    private lateinit var mContext: Context
    private lateinit var binding: FragmentCarRegistrationBinding
    private var regId: String = ""
    private var updateId: String = ""
    private var companyName: String = ""
    private var carRegnum: String = ""
    private var mdn: String = ""
    private var carType: String = ""
    private var carVin: String = ""
    private var carNum: String = ""
    private var driverId1: String = ""
    private var driverId2: String = ""
    private var driverId3: String = ""
    private var fareId: String = ""
    private var cityId: String = ""
    private var firmwareId: String = ""
    private var speedFactor: String = ""
    private var fareIdIndex: Int = 0
    private var cityIdIndex: Int = 0
    private var firmwareIdIndex: Int = 0
    private var fareIdList = arrayListOf<String>()
    private var fareIdAdapter: ArrayAdapter<*>? = null  // <*> : star-projections 타입 (README 파일 참조)
    private var fareIdResult: CarInfoSpinnerResponse? = null
    private var cityIdList = arrayListOf<String>()
    private var cityIdAdapter: ArrayAdapter<*>? = null
    private var vityIdResult: CarInfoSpinnerResponse? = null
    private var firmwareIdList = arrayListOf<String>()
    private var firmwareIdAdapter: ArrayAdapter<*>? = null
    private var firmwareIdResult: CarInfoSpinnerResponse? = null
    private var viewMoreClicked: Boolean = true
    private val keyMap = HashMap<String, String>()
    private lateinit var carInfoViewModel: CarInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarRegistrationBinding.inflate(inflater)

        mContext = requireActivity()

        carInfoViewModel = ViewModelProvider(this).get(CarInfoViewModel::class.java)

        binding.btnCarTypePersonal.setOnClickListener(this)
        binding.btnCarTypeCompany.setOnClickListener(this)
        binding.tvViewMoreDriverId.setOnClickListener(this)

        fragmentType = "register"

        //preference 에 저장되어있는 로그인정보 가져오기
        regId = "test"
        updateId = "test"

        if (arguments != null) {
            carPageType = "수정"
            binding.btnRegister.text = carPageType+" 완료"
            companyName = requireArguments().getString("company_name").toString()
            carRegnum = requireArguments().getString("car_regnum").toString()
            mdn = requireArguments().getString("mdn").toString()

            if (requireArguments().getString("car_type").toString().equals("개인")) {
                carType = "22"
                //view 에 값 세팅
                binding.btnCarTypePersonal.setBackgroundResource(R.drawable.btn_search_box)
                binding.btnCarTypePersonal.setTextColor(resources.getColor(R.color.white))
                binding.btnCarTypeCompany.setBackgroundResource(R.drawable.btn_reset_box)
                binding.btnCarTypeCompany.setTextColor(resources.getColor(R.color.black))
            }else if (requireArguments().getString("car_type").toString().equals("법인")) {
                carType = "21"
                //view 에 값 세팅
                binding.btnCarTypeCompany.setBackgroundResource(R.drawable.btn_search_box)
                binding.btnCarTypeCompany.setTextColor(resources.getColor(R.color.white))
                binding.btnCarTypePersonal.setBackgroundResource(R.drawable.btn_reset_box)
                binding.btnCarTypePersonal.setTextColor(resources.getColor(R.color.black))
            }
            carVin = requireArguments().getString("car_vin").toString()
            carNum = requireArguments().getString("car_num").toString()
            checkDriverId(requireArguments().getString("driver_id1").toString(), 1)
            checkDriverId(requireArguments().getString("driver_id2").toString(), 2)
            checkDriverId(requireArguments().getString("driver_id3").toString(), 3)
            fareId = requireArguments().getString("fare_id").toString()
            cityId = requireArguments().getString("city_id").toString()
            firmwareId = requireArguments().getString("firmware_id").toString()
            speedFactor = requireArguments().getString("speed_factor").toString()
            Log.d(log+"companyName", companyName)
            Log.d(log+"carRegnum", carRegnum)
            Log.d(log+"mdn", mdn)
            Log.d(log+"car_type", carType)
            Log.d(log+"car_vin", carVin)
            Log.d(log+"car_num", carNum)
            Log.d(log+"driver_id1", driverId1)
            Log.d(log+"driver_id2", driverId2)
            Log.d(log+"driver_id3", driverId3)
            Log.d(log+"fare_id", fareId)
            Log.d(log+"city_id", cityId)
            Log.d(log+"firmware_id", firmwareId)
            Log.d(log+"speed_factor", speedFactor)

            //view 에 값 세팅
            binding.etCompanyName.setText(companyName)
            binding.etMdn.setText(mdn)
            binding.etCarNum.setText(carNum)
            binding.etCarVin.setText(carVin)
            binding.etDriverId1.setText(driverId1)
            binding.etDriverId2.setText(driverId2)
            binding.etDriverId3.setText(driverId3)
            binding.etCarRegnum.setText(carRegnum)
            binding.etSpeedFactor.setText(speedFactor)

        }else{
            carPageType = "등록"
            binding.btnRegister.text = carPageType+" 완료"
        }

//        getParams()
        //요금스피너
        getFareIdSpinnerList(mContext)

        return binding.root
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btn_car_type_personal -> {  //[개인]버튼
                binding.btnCarTypePersonal.setBackgroundResource(R.drawable.btn_search_box)
                binding.btnCarTypePersonal.setTextColor(resources.getColor(R.color.white))
                binding.btnCarTypeCompany.setBackgroundResource(R.drawable.btn_reset_box)
                binding.btnCarTypeCompany.setTextColor(resources.getColor(R.color.black))
                carType = "22"
            }
            R.id.btn_car_type_company -> {   //[법인]버튼
                binding.btnCarTypeCompany.setBackgroundResource(R.drawable.btn_search_box)
                binding.btnCarTypeCompany.setTextColor(resources.getColor(R.color.white))
                binding.btnCarTypePersonal.setBackgroundResource(R.drawable.btn_reset_box)
                binding.btnCarTypePersonal.setTextColor(resources.getColor(R.color.black))
                carType = "21"
            }
            R.id.tv_view_more_driver_id -> {
                when(viewMoreClicked) {
                    true -> {
                        binding.tvViewMoreDriverId.text = "닫기"
                        binding.tvViewMoreDriverId.setTextColor(resources.getColor(R.color.blue))
                        binding.layoutDriverId2.visibility = View.VISIBLE
                        binding.layoutDriverId3.visibility = View.VISIBLE
                        viewMoreClicked = false
                    }
                    false -> {
                        binding.tvViewMoreDriverId.text = "더보기"
                        binding.tvViewMoreDriverId.setTextColor(resources.getColor(R.color.red))
                        binding.layoutDriverId2.visibility = View.GONE
                        binding.layoutDriverId3.visibility = View.GONE
                        viewMoreClicked = true
                    }
                }
            }
        }
    }


    fun checkDriverId(driverId: String?, num: Int?) {
        if (driverId != null) {
            if (driverId.contains("#")) {
                val str = driverId.split("#").toTypedArray()
                for (i in str.indices) {
                    Log.d(log + "driverID", str[i])
                    when (num) {
                        1 -> driverId1 = str[i]
                        2 -> driverId2 = str[i]
                        3 -> driverId3 = str[i]
                    }
                }
            }
        }
    }

    //요금스피너 리스트 fetching & setting
    private fun getFareIdSpinnerList(context: Context) {
        lifecycleScope.launch {
            carInfoViewModel.getCarInfoFareList()
                .let {
                    if (!it.isSuccessful) return@let
                    if (it.body() == null) return@let

//                    val item: CarInfoSpinnerResponse = it.body()!!
                    fareIdResult = it.body()!!
//                    Log.d(log+"item_spinner", item.spinnerVOS?.size.toString()+"개:  "+item.toString())

                    for (i in 0 until fareIdResult!!.spinnerVOS?.size!!) {
                        //수정값인지 확인
                        if (!fareId.equals("") || fareId != null) {
                            Log.d(log+"spinner_fareID", fareId)
                            if (fareId.equals(fareIdResult!!.spinnerVOS!!.get(i).fare_id)) {
                                fareIdResult!!.spinnerVOS!!.get(i).fare_id?.let { it1 ->
                                    Log.d(log+"spinner_value_id",
                                        it1
                                    )

                                    fareIdResult!!.spinnerVOS!!.get(i).fare_name?.let { it2 ->
                                        Log.d(log+"spinner_value_name",
                                            it2
                                        )
                                    }
                                }
                                //get the idx of data
                                fareIdIndex = i
                            }
                        }
//                        fareIdList.add(item.spinnerVOS.get(i).fare_name)
                        fareIdResult!!.spinnerVOS!!.get(i).fare_name?.let { it1 -> fareIdList.add(it1) }
                    }
                }

            //요금 스피너에 데이터 표출
            fareIdAdapter = ArrayAdapter<Any?>(
                context,
                androidx.appcompat.R.layout.select_dialog_item_material,
                fareIdList as List<String?>
            )
            //스피너에 adapter 붙이기
            binding.spinnerFareId.adapter = fareIdAdapter
            binding.spinnerFareId.setSelection(fareIdIndex)  //전달받은 수정값(spinner position)이 있으면 세팅/ 기본값은 0 임
            //스피너아이템 클릭리스너
            binding.spinnerFareId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, v: View?, pos: Int, l: Long) {
                    var selectedFareName = fareIdList.get(pos)

                    for (i in 0 until fareIdResult?.spinnerVOS?.size!!) {
//                        Log.d(log+"fareIdResult_size", fareIdResult?.spinnerVOS?.size!!.toString())
                        if (fareIdResult!!.spinnerVOS?.get(i)?.fare_name == selectedFareName) {
                            fareId = fareIdResult!!.spinnerVOS?.get(i)?.fare_id.toString()   //선택한 아이템 fare_id 저장
                            fareIdIndex = pos   //선택한 아이템 pos 저장
//                            Log.d(log+"fareIdResult_name", fareIdResult!!.spinnerVOS?.get(i)?.fare_name+" == "+selectedFareName+"  ,id: "+fareIdResult!!.spinnerVOS?.get(i)?.fare_id)
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        }
    }


    private fun getCityIdSpinnerList() {

    }



    private fun getParams() {
        keyMap.put("reg_id", "test")
        keyMap.put("update_id", "test")
        keyMap.put("company_name", binding.etCompanyName.text.toString())
        keyMap.put("car_regnum", binding.etCarRegnum.text.toString())
        keyMap.put("mdn", binding.etMdn.text.toString())
        keyMap.put("car_type", carType)
        keyMap.put("car_vin", binding.etCarVin.text.toString())
        keyMap.put("car_num", binding.etCarNum.text.toString())
        keyMap.put("driver_id1", binding.etDriverId1.text.toString())

        if (binding.etDriverId2.text.toString().equals("")) {
            keyMap.put("driver_id2", "222222222")
        }else{
            keyMap.put("driver_id2", binding.etDriverId2.text.toString())
        }

        if (binding.etDriverId3.text.toString().equals("")) {
            keyMap.put("driver_id3", "333333333")
        }else{
            keyMap.put("driver_id3", binding.etDriverId3.text.toString())
        }

        keyMap.put("fare_id", fareId)
        keyMap.put("city_id", cityId)
        keyMap.put("daemon_id", "1")
        keyMap.put("firmware_id", firmwareId)
        keyMap.put("speed_factor", binding.etSpeedFactor.text.toString())

        Log.d("final_key_maps", keyMap.toString())
    }



}