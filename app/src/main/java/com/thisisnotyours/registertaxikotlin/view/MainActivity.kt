package com.thisisnotyours.registertaxikotlin.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.data.CarInfoApiService
import com.thisisnotyours.registertaxikotlin.databinding.ActivityMainBinding
import com.thisisnotyours.registertaxikotlin.model.CarInfoResponse
import com.thisisnotyours.registertaxikotlin.viewModel.CarInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


public val retrofit = Retrofit.Builder()
    .baseUrl("http://49.50.174.192:8080/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

public object openApiObject {
    val retrofitService: CarInfoApiService by lazy {
        retrofit.create(CarInfoApiService::class.java)
    }
}

//@HiltAndroidApp - application 에서만 상용할 수 있음.
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {
    val log: String = "log_"
    private lateinit var mContext: Context
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.searchCar.setOnClickListener(this)
        binding.registerCar.setOnClickListener(this)

        binding.searchCar.performClick()

    }//onCreate..


    //툴바에 레이아웃삽입
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.logout -> {
                showToast("클릭 로그아웃")
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //클릭리스너
    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.search_car -> changeFragment(0)
            R.id.register_car -> changeFragment(1)
        }
    }

    //해당 프래그먼트로 이동
    private fun changeFragment(itemId: Int) {
        showTimber(itemId.toString())
        when(itemId) {
            0 -> {  //차량조회 화면
                val searchFrag = CarSearchFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_change, searchFrag)
                    .commit()
            }
            1 -> { //차량등록/수정 화면
                val registerFrag = CarRegistrationFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_change, registerFrag)
                    .commit()
            }
        }
    }

    //로그대신 timber 라이브러리 사용
    fun showTimber(msg: String) {
        Timber.d(msg)
    }

    //토스트 메소드
    fun showToast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }




}


