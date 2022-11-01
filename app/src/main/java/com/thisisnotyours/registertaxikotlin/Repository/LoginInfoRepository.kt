package com.thisisnotyours.registertaxikotlin.Repository

import com.thisisnotyours.registertaxikotlin.data.LoginInfoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginInfoRepository @Inject constructor(private val loginInfoApiService: LoginInfoApiService){

    suspend fun loginInfoData(id: String, pw: String) =
        withContext(Dispatchers.Default) {
            loginInfoApiService.GetLoginInfoSuspned(id, pw)
        }

}