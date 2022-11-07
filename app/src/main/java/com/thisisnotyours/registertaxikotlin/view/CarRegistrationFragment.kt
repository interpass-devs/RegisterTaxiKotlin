package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

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
    private lateinit var keyMap: HashMap<String, String>
    private var viewMoreClicked: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarRegistrationBinding.inflate(inflater)

        mContext = requireActivity()

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

    private fun setMapData() {
        keyMap.put("company_name", binding.etCompanyName.text.toString())
        keyMap.put("car_regnum", binding.etCarRegnum.text.toString())
        keyMap.put("mdn", binding.etMdn.text.toString())
//        keyMap.put("car_type", binding.carty)
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
    }



}