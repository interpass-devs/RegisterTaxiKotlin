package com.thisisnotyours.registertaxikotlin.model

import com.google.gson.annotations.SerializedName

data class CarInfoResponse (
    @SerializedName("connInfoList") var carInfoVO: List<CarInfoVOS>? = mutableListOf()

)

data class CarInfoVOS (
    @SerializedName("company_name") var company_name: String? = null,
    @SerializedName("car_regnum") var car_regnum: String? = null,
    @SerializedName("type_name") var type_name: String? = null,
    @SerializedName("car_vin") var car_vin: String? = null
)