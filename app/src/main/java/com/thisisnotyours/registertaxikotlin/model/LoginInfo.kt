package com.thisisnotyours.registertaxikotlin.model

import com.google.gson.annotations.SerializedName


// 코틀린에서 data class 는
//자바에서 생성하는 아이템 클래스 생성자와 같다고 볼 수 있다.
//코틀린에서 data class 는 생성자부터 getter/ setter, 심지어 canonical methods 까지 알아서 생성해준다.

data class LoginResponse (
    @SerializedName("userInfo") var loginInfoVO: List<LoginInfoVO>? = mutableListOf()  //{"userInfo":[{"id":"test","pw":"test","name":"테스트","use_yn":"Y","roles":"master"}]}
        )

data class LoginInfoVO (
    @SerializedName("id") var id: String? = null,
    @SerializedName("pw") var pw: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("use_yn") var use_yn: String? = null,
    @SerializedName("roles") var roles: String? = null
//    var id: String? = null,
//    var pw: String? = null,
//    var name: String? = null,
//    var use_yn: String? = null,
//    var roles: String? = null
)
