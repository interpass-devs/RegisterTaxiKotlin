package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.databinding.ActivityLoginBinding
import com.thisisnotyours.registertaxikotlin.viewModel.LoginInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val log = "log_"
    private lateinit var mContext: Context
    private var use_yn: String = ""
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginInfoViewModel: LoginInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //레이아웃 바인딩
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@LoginActivity

        loginInfoViewModel = ViewModelProvider(this@LoginActivity).get(LoginInfoViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            LoginResultData_by_Coroutine()
        }
    }//onCreate..




    //로그인 정보)
    //MVVM, 비동기방식, 코루틴 이용한 데이터 fetching
    private fun LoginResultData_by_Coroutine() {
        lifecycleScope.launch {
            loginInfoViewModel.getLoginInfo(binding.etLoginId.text.toString(), binding.etLoginPw.text.toString())
                .let {
                    if (!it.isSuccessful) return@let

                    if (it.body() == null) return@let

                    Log.d(log+"login_body", it.body().toString())
                    Log.d(log+"login_id", it.body()?.loginInfoVO?.get(0)?.id.toString())
                    Log.d(log+"login_pw", it.body()?.loginInfoVO?.get(0)?.pw.toString())
                    Log.d(log+"login_name", it.body()?.loginInfoVO?.get(0)?.name.toString())
                    Log.d(log+"login_use_yn", it.body()?.loginInfoVO?.get(0)?.use_yn.toString())
                    Log.d(log+"login_roles", it.body()?.loginInfoVO?.get(0)?.roles.toString())
                    use_yn = it.body()?.loginInfoVO?.get(0)?.use_yn.toString()
                }
        }
        when(use_yn) {
            "Y" -> {
                //메인화면으로 이동
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
            "N" -> {
                Timber.d(log+"n", "error")
                Toast.makeText(mContext, "로그인 정보가 일치하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }//when..
    }



}