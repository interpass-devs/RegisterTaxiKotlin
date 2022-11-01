package com.thisisnotyours.registertaxikotlin.viewModel

import androidx.lifecycle.ViewModel
import com.thisisnotyours.registertaxikotlin.Repository.LoginInfoRepository
import com.thisisnotyours.registertaxikotlin.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginInfoViewModel @Inject constructor(
    private val loginInfoRepository: LoginInfoRepository
): ViewModel() {


    suspend fun getLoginInfo(
        id: String,
        pw: String
    ): Response<LoginResponse> {
        return loginInfoRepository.loginInfoData(id, pw)
    }
}