package com.thisisnotyours.registertaxikotlin.model

import com.google.gson.annotations.SerializedName


// 코틀린에서 data class 는
//자바에서 생성하는 아이템 클래스 생성자와 같다고 볼 수 있다.
//코틀린에서 data class 는 생성자부터 getter/ setter, 심지어 canonical methods 까지 알아서 생성해준다.

data class CarInfoResponse (
    @SerializedName("connInfoList") var carInfoVO: List<CarInfoVOS>? = mutableListOf()

)

data class CarInfoVOS (
    @SerializedName("company_name") var company_name: String? = null,
    @SerializedName("car_regnum") var car_regnum: String? = null,
    @SerializedName("type_name") var type_name: String? = null,
    @SerializedName("car_vin") var car_vin: String? = null
)