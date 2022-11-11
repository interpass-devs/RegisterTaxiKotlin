package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thisisnotyours.registertaxikotlin.databinding.ActivityLoginBinding
import com.thisisnotyours.registertaxikotlin.storage.PreferenceManager
import com.thisisnotyours.registertaxikotlin.viewModel.LoginInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val log = "log_"
    private lateinit var mContext: Context
    private var use_yn: String = ""
    private var id: String = ""
    private var pw: String = ""
    private var name: String  = ""
    private var roles: String = ""
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginInfoViewModel: LoginInfoViewModel
    private var autoClicked = false
    private var savedAuto = "false"
    private var savedId = ""
    private var savedPw = ""
    private var savedName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //레이아웃 바인딩
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@LoginActivity

        //저장된 로그인정보 있는지 확인
        if (!PreferenceManager.getString(mContext, "auto_login").equals("")) {
            savedAuto = PreferenceManager.getString(mContext, "auto_login").toString()
            savedId = PreferenceManager.getString(mContext, "reg_id").toString()
            savedPw = PreferenceManager.getString(mContext, "reg_pw").toString()
            savedName = PreferenceManager.getString(mContext, "reg_name").toString()

            //로그인 자동저장 값이 있는지 확인
            when(savedAuto) {
                "false" -> {
                    binding.etLoginId.setText("")
                    binding.etLoginPw.setText("")
                    binding.checkbox.isChecked = false
                }
                "true" -> {
                    binding.etLoginId.setText(savedId)
                    binding.etLoginPw.setText(savedId)
                    binding.checkbox.isChecked = true
                    binding.btnLogin.performClick()
                }
            }
        }

        loginInfoViewModel = ViewModelProvider(this@LoginActivity).get(LoginInfoViewModel::class.java)

        //로그인버튼 클릭리스너
        binding.btnLogin.setOnClickListener {
            LoginResultData_by_Coroutine()
        }

        //자동로그인 체크박스
        binding.checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b == true) {
                autoClicked = true
            }else{
                autoClicked = false
            }
        })
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
                    id = it.body()?.loginInfoVO?.get(0)?.id.toString()
                    pw = it.body()?.loginInfoVO?.get(0)?.pw.toString()
                    name = it.body()?.loginInfoVO?.get(0)?.name.toString()
                    use_yn = it.body()?.loginInfoVO?.get(0)?.use_yn.toString()
                    roles = it.body()?.loginInfoVO?.get(0)?.roles.toString()
                    use_yn = it.body()?.loginInfoVO?.get(0)?.use_yn.toString()
                }
        }
        when(use_yn) {
            "Y" -> {
                //메인화면으로 이동  //두번 클릭해야 이동하는 경향이 있음..
                if (autoClicked == true) {
                    PreferenceManager.setString(mContext, "auto_login","true")
                }else{
                    PreferenceManager.setString(mContext, "auto_login", "false")
                }
                PreferenceManager.setString(mContext, "reg_id", id)
                PreferenceManager.setString(mContext, "reg_pw", pw)
                PreferenceManager.setString(mContext, "reg_name", name)

                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
            "N" -> {
                showTimber("N")   //timber - README 파일참조
                Toast.makeText(mContext, "로그인 정보가 일치하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }//when..
    }



    private fun showTimber(msg: String) {
        Timber.d(msg)
    }



}