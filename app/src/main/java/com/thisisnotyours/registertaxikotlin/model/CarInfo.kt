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
    @SerializedName("type_name") var type_name: String? = null,
    @SerializedName("mdn") var mdn: String? = null,
    @SerializedName("car_regnum") var car_regnum: String? = null,
    @SerializedName("car_vin") var car_vin: String? = null,
    @SerializedName("car_type") var car_type: String? = null,
    @SerializedName("car_num") var car_num: String? = null,
    @SerializedName("driver_id1") var driver_id1: String? = null,
    @SerializedName("driver_id2") var driver_id2: String? = null,
    @SerializedName("driver_id3") var driver_id3: String? = null,
    @SerializedName("fare_id") var fare_id: String? = null,
    @SerializedName("fare_name") var fare_name: String? = null,
    @SerializedName("city_id") var city_id: String? = null,
    @SerializedName("city_name") var city_name: String? = null,
    @SerializedName("firmware_id") var firmware_id: String? = null,
    @SerializedName("firmware_name") var firmware_name: String? = null,
    @SerializedName("speed_factor") var speed_factor: String? = null,
    @SerializedName("store_id") var store_id: String? = null,
    @SerializedName("unit_num") var unit_num: String? = null,
    @SerializedName("unit_sn") var unit_sn: String? = null,
        )


data class CarInfoSpinnerResponse (
    @SerializedName("result") var spinnerVOS: List<SpinnerVOS>? = mutableListOf()
)

data class SpinnerVOS (
    @SerializedName("fare_id") var fare_id: String? = null,
    @SerializedName("fare_name") var fare_name: String? = null,
    @SerializedName("city_id") var city_id: String? = null,
    @SerializedName("city_name") var city_name: String? = null,
    @SerializedName("firmware_id") var firmware_id: String? = null,
    @SerializedName("firmware_name") var firmware_name: String? = null
)


