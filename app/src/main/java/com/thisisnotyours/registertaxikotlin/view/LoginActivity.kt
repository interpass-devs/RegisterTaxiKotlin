package com.thisisnotyours.registertaxikotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.model.UserInfoResponse
import com.thisisnotyours.registertaxikotlin.viewModel.LoginInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val log = "log_"
    private lateinit var loginInfoViewModel: LoginInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginInfoViewModel = ViewModelProvider(this@LoginActivity).get(LoginInfoViewModel::class.java)

        LoginResultData_by_Coroutine()

    }




    //로그인 정보) -방법2
    //MVVM, 비동기방식, 코루틴 이용한 데이터 fetching
    private fun LoginResultData_by_Coroutine() {
        lifecycleScope.launch {
            loginInfoViewModel.getLoginInfo("test","test")
                .let {
                    if (!it.isSuccessful) return@let

                    if (it.body() == null) return@let

                    Log.d(log+"login_body", it.body().toString())
                    Log.d(log+"login_id", it.body()?.loginInfoVO?.get(0)?.id.toString())
                    Log.d(log+"login_pw", it.body()?.loginInfoVO?.get(0)?.pw.toString())
                }
        }
    }



    //로그인 정보) -방법1
    //retrofit call - data fetching
    //비동기방식 아님
    private fun getLoginData() {
        Log.d(log+"onResponse_login", "call login api")

        val call = openApiObject.retrofitService.GetLoginInfo("test","test")

        call.enqueue(object : retrofit2.Callback<UserInfoResponse>{
            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                Log.d(log+"onResponse_login", response.body().toString())
                if (response.isSuccessful) {

                    Log.d(log+"onResponse_login", "isSuccessful")
                }else{
                    Log.d(log+"onResponse_login", "is not successful")
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {

            }
        })
    }


}