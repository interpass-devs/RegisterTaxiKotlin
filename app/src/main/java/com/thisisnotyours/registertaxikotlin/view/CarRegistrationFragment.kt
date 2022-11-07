package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thisisnotyours.registertaxikotlin.databinding.FragmentCarRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarRegistrationFragment : Fragment() {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarRegistrationBinding.inflate(inflater)

        mContext = requireActivity()

        if (arguments != null) {
            Log.d(log+"frag_type", requireArguments().getString("frag_type").toString())
            companyName = requireArguments().getString("company_name").toString()
            carRegnum = requireArguments().getString("car_regnum").toString()
            mdn = requireArguments().getString("mdn").toString()
            carType = requireArguments().getString("car_type").toString()
            carVin = requireArguments().getString("car_vin").toString()
            carNum = requireArguments().getString("car_num").toString()
            driverId1 = requireArguments().getString("driver_id1").toString()
            driverId2 = requireArguments().getString("driver_id2").toString()
            driverId3 = requireArguments().getString("driver_id3").toString()
            Log.d(log+"companyName", companyName)
            Log.d(log+"carRegnum", carRegnum)
            Log.d(log+"mdn", mdn)
            Log.d(log+"car_type", carType)
            Log.d(log+"car_vin", carVin)
            Log.d(log+"car_num", carNum)
            Log.d(log+"driver_id1", driverId1)
            Log.d(log+"driver_id2", driverId2)
            Log.d(log+"driver_id3", driverId3)
        }else{

        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}