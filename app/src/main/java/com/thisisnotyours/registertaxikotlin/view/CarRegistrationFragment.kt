package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.data.CarInfoApiService
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarRegistrationBinding
import com.thisisnotyours.registertaxikotlin.model.CarInfoSpinnerResponse
import com.thisisnotyours.registertaxikotlin.model.SpinnerVOS
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

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
    private var firmwareUpdate: String = ""
    private var daemonUpdate: String = ""
    private var fareIdIndex: Int = 0
    private var cityIdIndex: Int = 0
    private var firmwareIdIndex: Int = 0
    private var daemonUpdate_idx = 0
    private var firmwareUpdate_idx = 0
    private var fareIdList = arrayListOf<String>()
    private var fareIdAdapter: ArrayAdapter<*>? = null  // <*> : star-projections 타입 (README 파일 참조)
    private var fareIdResult: CarInfoSpinnerResponse? = null
    private var cityIdList = arrayListOf<String>()
    private var cityIdAdapter: ArrayAdapter<*>? = null
    private var cityIdResult: CarInfoSpinnerResponse? = null
    private var firmwareIdList = arrayListOf<String>()
    private var firmwareIdAdapter: ArrayAdapter<*>? = null
    private var firmwareIdResult: CarInfoSpinnerResponse? = null
    private var firmwareUpdateList = arrayListOf<String>()
    private var firmwareUpdateAdapter: ArrayAdapter<String>? = null
    private var daemonUpdateList = arrayListOf<String>()
    private var daemonUpdateAdapter: ArrayAdapter<String>? = null
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
        binding.btnRegister.setOnClickListener(this)
        binding.btnRegisterCancel.setOnClickListener(this)

        fragmentType = "register"

        //preference 에 저장되어있는 로그인정보 가져오기
        regId = "test"
        updateId = "test"

        if (arguments != null) {
            binding.updateLayout.visibility = View.VISIBLE
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
            firmwareUpdate = requireArguments().getString("firmware_update").toString()
            daemonUpdate = requireArguments().getString("daemon_update").toString()

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
            if (firmwareUpdate.equals("Y")) { firmwareUpdate_idx = 0 }else if (firmwareUpdate.equals("N")) { firmwareUpdate_idx = 1 }
            if (daemonUpdate.equals("Y")) { daemonUpdate_idx = 0 }else if (daemonUpdate.equals("N")) { daemonUpdate_idx = 1 }

        }else{
            binding.updateLayout.visibility = View.GONE
            carPageType = "등록"
            binding.btnRegister.text = carPageType+" 완료"
        }

        //스피너
        getFareIdSpinnerList(mContext)  //요금
        getCityIdSpinnerList(mContext)  //시경계
        getFirmwareIdSpinnerList(mContext) //벤사
        UpdateSetting(mContext)  //펌웨어 & 대몬 업데이트기능.

        return binding.root

    }//onCreateView..


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
            R.id.tv_view_more_driver_id -> { //[더보기]버튼
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
            R.id.btn_register -> {getParams()}  //[등록]/[수정]완료버튼
            R.id.btn_register_cancel -> { //등록취소버튼

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

                    for (i in 0 until fareIdResult!!.spinnerVOS?.size!!) {
                        //수정값인지 확인
                        if (!fareId.equals("") || fareId != null) {
                            if (fareId.equals(fareIdResult!!.spinnerVOS!!.get(i).fare_id)) {
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

                        if (fareIdResult!!.spinnerVOS?.get(i)?.fare_name == selectedFareName) {
                            fareId = fareIdResult!!.spinnerVOS?.get(i)?.fare_id.toString()   //선택한 아이템 fare_id 저장
                            fareIdIndex = pos   //선택한 아이템 pos 저장
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
    }

    //시경계 스피너
    private fun getCityIdSpinnerList(context: Context) {
        lifecycleScope.launch {
            carInfoViewModel.getCarInfoCityList()
                .let {
                    if (!it.isSuccessful) return@let
                    if (it.body() == null) return@let
                    cityIdResult = it.body()!!   //결과값

                    for (i in 0 until cityIdResult!!.spinnerVOS?.size!!) {
                        //수정값있는지 확인 및 적용
                        if (!cityId.equals("")) {
                            if (cityId.equals(cityIdResult!!.spinnerVOS?.get(i)?.city_id)) {
                                cityIdIndex = i
                            }
                        }
                        //arrayList 에 스피너값 더하기
                        cityIdResult!!.spinnerVOS?.get(i)?.city_name?.let { it1 -> cityIdList.add(it1) }
                    }
                } //let

            //스피너 어뎁터 & 세팅
            cityIdAdapter = ArrayAdapter(
                context,
                androidx.appcompat.R.layout.select_dialog_item_material,
                cityIdList as List<String?>
            )

            binding.spinnerCityId.adapter = cityIdAdapter
            binding.spinnerCityId.setSelection(cityIdIndex)
            binding.spinnerCityId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    var selectedCityName = cityIdList.get(pos)  //선택한 아이템

                    for (i in 0 until cityIdResult?.spinnerVOS?.size!!) {
                        if (cityIdResult!!.spinnerVOS?.get(i)?.city_name.equals(selectedCityName)) {
                            cityId = cityIdResult!!.spinnerVOS?.get(i)?.city_id.toString()  //선택한 아이템 fare_id 저장
                            cityIdIndex = pos  //선택한 아이템 pos 저장
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
    }

    //펌웨어(벤사)스피너
    private fun getFirmwareIdSpinnerList(context: Context) {
        lifecycleScope.launch {
            carInfoViewModel.getCarInfoFirmwareList()
                .let {
                    if (!it.isSuccessful) return@let
                    if (it.body() == null) return@let
                    firmwareIdResult = it.body()!!   //결과값

                    for (i in 0 until firmwareIdResult!!.spinnerVOS?.size!!) {
                        //수정값있는지 확인 및 적용
                        if (!firmwareId.equals("")) {
                            if (firmwareId.equals(firmwareIdResult!!.spinnerVOS?.get(i)?.firmware_id)) {
                                firmwareIdIndex = i
                            }
                        }
                        //arrayList 에 스피너값 더하기
                        firmwareIdResult!!.spinnerVOS?.get(i)?.firmware_name?.let { it1 -> firmwareIdList.add(it1) }
                    }
                } //let

            //스피너 어뎁터 & 세팅
            firmwareIdAdapter = ArrayAdapter(
                context,
                androidx.appcompat.R.layout.select_dialog_item_material,
                firmwareIdList as List<String?>
            )

            binding.spinnerFirmwareId.adapter = firmwareIdAdapter
            binding.spinnerFirmwareId.setSelection(firmwareIdIndex)
            binding.spinnerFirmwareId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, v: View?, pos: Int, p3: Long) {
                    var selectedFirmwareName = firmwareIdList.get(pos)  //선택한 아이템

                    for (i in 0 until firmwareIdResult?.spinnerVOS?.size!!) {
                        if (firmwareIdResult!!.spinnerVOS?.get(i)?.firmware_name.equals(selectedFirmwareName)) {
                            firmwareId = firmwareIdResult!!.spinnerVOS?.get(i)?.firmware_id.toString()  //선택한 아이템 fare_id 저장
                            firmwareIdIndex = pos  //선택한 아이템 pos 저장
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
    }

    private fun UpdateSetting(context: Context) {
        //펌웨어 업데이트 여부
        firmwareUpdateList.add("Y")
        firmwareUpdateList.add("N")
        firmwareUpdateAdapter = ArrayAdapter(
            context,
            androidx.appcompat.R.layout.select_dialog_item_material,
            firmwareUpdateList as List<String>
        )
        binding.spinnerFirmwareUpdate.adapter = firmwareUpdateAdapter
        binding.spinnerFirmwareUpdate.setSelection(firmwareUpdate_idx)
        binding.spinnerFirmwareUpdate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var selectedItem = firmwareUpdateList.get(pos)
                Log.d("selected_firm_update", selectedItem)
                for (i in 0 until firmwareUpdateList.size) {
                    if (selectedItem.equals(firmwareUpdateList.get(i))) {
                        Log.d("selected_firm_update==", i.toString())
                        Log.d("selected_firm_update==", selectedItem+" == "+firmwareUpdateList.get(i))
                        Log.d("selected_firm_update==","-------------------------------")
                        firmwareUpdate_idx = i
                        firmwareUpdate = firmwareUpdateList.get(i)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        //대몬 업데이트 여부
        daemonUpdateList.add("Y")
        daemonUpdateList.add("N")
        daemonUpdateAdapter = ArrayAdapter(
            context,
            androidx.appcompat.R.layout.select_dialog_item_material,
            daemonUpdateList as List<String>
        )
        binding.spinnerDaemonUpdate.adapter = daemonUpdateAdapter
        binding.spinnerDaemonUpdate.setSelection(daemonUpdate_idx)
        binding.spinnerDaemonUpdate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var selectedItem = daemonUpdateList.get(pos)
                for (t in 0 until daemonUpdateList.size) {
                    if (selectedItem.equals(daemonUpdateList.get(t))) {
                        Log.d("selected_daemon_update==", t.toString())
                        Log.d("selected_daemon_update==", selectedItem+" == "+firmwareUpdateList.get(t))
                        daemonUpdate_idx = t
                        daemonUpdate = daemonUpdateList.get(t)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }



    //map 데이터 -> send to DB
    private fun getParams() {
        when(carPageType) {
            "등록" -> {keyMap.put("reg_id","test")}
            "수정" -> {keyMap.put("update_id","test")}
        }
        keyMap.put("company_name", binding.etCompanyName.text.toString())
        keyMap.put("car_regnum", binding.etCarRegnum.text.toString())
        keyMap.put("mdn", binding.etMdn.text.toString())
        keyMap.put("car_type", carType)
        keyMap.put("car_vin", binding.etCarVin.text.toString())
        keyMap.put("car_num", binding.etCarNum.text.toString())
        keyMap.put("driver_id1", binding.etDriverId1.text.toString())

        if (binding.etDriverId2.text.toString().equals("")) {
            keyMap.put("driver_id2", "222222222")
        } else {
            keyMap.put("driver_id2", binding.etDriverId2.text.toString())
        }

        if (binding.etDriverId3.text.toString().equals("")) {
            keyMap.put("driver_id3", "333333333")
        } else {
            keyMap.put("driver_id3", binding.etDriverId3.text.toString())
        }

        keyMap.put("fare_id", fareId)
        keyMap.put("city_id", cityId)
        keyMap.put("daemon_id", "1")
        keyMap.put("firmware_id", firmwareId)
        keyMap.put("speed_factor", binding.etSpeedFactor.text.toString())
        keyMap.put("firmware_update", firmwareUpdate)
        keyMap.put("daemon_update", daemonUpdate)

        Log.d("key_maps", keyMap.toString())
        Log.d("key_maps_size", keyMap.size.toString()+"개")

        when(carPageType) {
            "등록" -> {insertData(keyMap, carPageType)}
            "수정" -> {updateData(keyMap, carPageType)}
        }
    }


    //하나의 데이터가 있어도 true 로 나옴
    private fun mapNullCheck(map: HashMap<String, String>): Boolean {
        return (map == null || map.isEmpty())
    }

    private fun mapValueCheck(map: HashMap<String, String>, keyValue: String) {
        for (i in 0 until map.size) {
            if (map.get(keyValue).equals("")) {
                Log.d("key_maps_size", map.size.toString())
                Log.d("key_maps_empty", keyValue)
            }
        }
    }

    //차량등록 insert
    private fun insertData(datas: HashMap<String, String>, registerType: String) {
        val call = openApiObject.retrofitService.InsertCarData(datas)
        call.enqueue(object : retrofit2.Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d("insert_response", response.body().toString())
                    //받아온 결과값 확인
                    responseResultCheck(response.body().toString(), registerType)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("update_response_fail", t.message.toString())
            }
        })
    }

    //차량등록 update
    private fun updateData(datas: HashMap<String, String>, registerType: String) {
        val call = openApiObject.retrofitService.UpdateCarData(datas)
        call.enqueue(object : retrofit2.Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    responseResultCheck(response.body().toString(), registerType)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("update_response_fail", t.message.toString())
            }

        })
    }

    //try mvvm method
    fun updateCarInfoData(datas: HashMap<String, String>) {
        lifecycleScope.launch {
            carInfoViewModel.updateCarData(datas)
                .let {
                    if (!it.isSuccessful) return@let
                    if (it.body() == null) return@let

                    if (it.isSuccessful) {
                        Log.d(log+"updateData", it.body().toString())
                    }
                }
        }
    }

    //받아온 데이터(response.body) 중 없는게 있는지 확인
    private fun responseResultCheck(response: String, what: String) {
        if (response.equals("Y")) {
            showToast(what+"이 완료되었습니다.")  //수정 또는 등록완료
            //조회화면으로 이동
            val frag = CarSearchFragment()
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.add(R.id.frame_change, frag)
                ?.commit()
        }else if (response.equals("N")) {
            showToast("서버오류")
        }else{
            var what = ""
            var msg = " 입력해주세요"
            when(response) {
                "reg_id,00" -> {
                    what = "로그인 정보가"
                    msg = " 없습니다"
                }
                "update_id,00" -> {
                    what = "로그인 정보가"
                    msg = " 없습니다"
                }
                "company_name,00" -> {
                    what = "운수사이름을"
                }
                "mdn:01" -> {
                    what = "입력된 모뎀번호가 이미 존재합니다.\n다른 모뎀번호를"
                }
                "mdn,00" -> {
                    what = "모뎀번호를"
                }
                "car_num,00" -> {
                    what = "차량번호를"
                }
                "car_type,00" -> {
                    what = "차량유형을"
                }
                "car_vin,00" -> {
                    what = "차대번호를"
                }
                "driver_id1,00" -> {
                    what = "운전자 자격번호1를"
                }
                "driver_id2,00" -> {
                    what = "운전자 자격번호2를"
                }
                "driver_id3,00" -> {
                    what = "운전자 자격번호3를"
                }
                "car_regnum,00" -> {
                    what = "사업자번호를"
                }
                "fare_id,00" -> {
                    what = "요금을"
                    msg = " 선택해주세요"
                }
                "city_id,00" -> {
                    what = "시경계를"
                    msg = " 선택해주세요"
                }
                "daemon_id,00" -> {
                    what = "daemon_id"
                    msg = "is empty"
                }
                "firmware_id,00" -> {
                    what = "벤사를"
                    msg = " 선택해주세요"
                }
                "firmware_update,00" -> {

                }
                "daemon_udpate,00" -> {
                }
            }
            showToast(what+msg)
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

}