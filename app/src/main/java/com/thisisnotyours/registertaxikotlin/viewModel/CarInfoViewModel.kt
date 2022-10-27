package com.thisisnotyours.registertaxikotlin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thisisnotyours.registertaxikotlin.Repository.CarInfoRepository
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
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



}