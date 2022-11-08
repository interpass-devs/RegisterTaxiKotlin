package com.thisisnotyours.registertaxikotlin.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thisisnotyours.registertaxikotlin.Repository.CarInfoRepository
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.model.CarInfoSpinnerResponse
import com.thisisnotyours.registertaxikotlin.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class CarInfoViewModel @Inject constructor(
    private val carInfoRepository: CarInfoRepository
    ): ViewModel() {
    private val _liveData = MutableLiveData<Result<CarInfoResponse>>()
    private val liveData: LiveData<Result<CarInfoResponse>>
        get() = _liveData

    var mLiveData = MutableLiveData<String>("응답없음")
    fun getData() = mLiveData
    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val data = carInfoRepository.carInfoListData("","","","20221026104924","20221027104924","0","5")
            when(data.isSuccessful) {
                true -> {
                    mLiveData.postValue(data.body().toString())
                    Log.d("log_true", data.body().toString())}
                }

        }
    }


    suspend fun getCarInfoList(
        car_num: String,
        mdn: String,
        company_name: String,
        st_dtti: String,
        et_dtti: String,
        offset: String,
        limit: String
    ): Response<CarInfoResponse> {
        return carInfoRepository.carInfoListData(car_num, mdn, company_name, st_dtti, et_dtti, offset, limit)
    }


    suspend fun getCarInfoFareList(): Response<CarInfoSpinnerResponse> {
        return carInfoRepository.carInfoFareListData()
    }

    suspend fun getCarInfoCityList(): Response<CarInfoSpinnerResponse> {
        return carInfoRepository.carInfoCityListData()
    }

    suspend fun getCarInfoFirmwareList(): Response<CarInfoSpinnerResponse> {
        return carInfoRepository.carInfoFirmwareListData()
    }




}