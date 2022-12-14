package com.thisisnotyours.registertaxikotlin.Repository

import com.thisisnotyours.registertaxikotlin.data.CarInfoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarInfoRepository @Inject constructor(private val carInfoApiService: CarInfoApiService) {

    suspend fun carInfoListData(car_num: String
                                , mdn: String
                                , company_name: String
                                , offset: String
                                , limit: String
                                , reg_id: String) =
        withContext(Dispatchers.Default) {
            carInfoApiService.GetCarInfoSuspend(
                car_num,
                mdn,
                company_name,
                offset,
                limit,
                reg_id
            )
        }


    suspend fun carInfoFareListData() =
        withContext(Dispatchers.Main) {
            carInfoApiService.GetFareIdSuspend()
        }


    suspend fun carInfoCityListData() =
        withContext(Dispatchers.Main) {
            carInfoApiService.GetCityIdSuspend()
        }

    suspend fun carInfoFirmwareListData() =
        withContext(Dispatchers.Main) {
            carInfoApiService.GetFirmwareIdSuspend()
        }

//    suspend fun updateCarInfoData(map: HashMap<String, String>) =
//        withContext(Dispatchers.Main) {
//            carInfoApiService.UpdateCarInfo(map)
//        }

    suspend fun updateCarData(mapDatas: HashMap<String, String>) =
        withContext(Dispatchers.Main) {
            carInfoApiService.UpdateCarInfo(mapDatas)
        }


}