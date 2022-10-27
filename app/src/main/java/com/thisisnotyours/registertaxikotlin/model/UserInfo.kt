package com.thisisnotyours.registertaxikotlin.model

import com.google.gson.annotations.SerializedName

class UserInfoResponse {
    @SerializedName("userInfo") var userInfo = ArrayList<User>()
}

class User {
    @SerializedName("id") var id: String? = null
    @SerializedName("pw") var pw: String? = null
    @SerializedName("name") var name: String? = null
    @SerializedName("use_yn") var use_yn: String? = null
    @SerializedName("roles") var roles: String? = null
}


