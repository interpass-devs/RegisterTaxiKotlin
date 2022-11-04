package com.thisisnotyours.registertaxikotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thisisnotyours.registertaxikotlin.R
import com.thisisnotyours.registertaxikotlin.adapter.CarInfoAdapter.*
import com.thisisnotyours.registertaxikotlin.databinding.RecyclerCarInfoItemBinding
import com.thisisnotyours.registertaxikotlin.model.CarInfoVOS

class CarInfoAdapter(private val context: Context
                        , private val items: List<CarInfoVOS>
                        , private val clickCallBack: (carinfoID: String)->Any) :
    RecyclerView.Adapter<Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerCarInfoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.setData(item, clickCallBack)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class Holder(val binding: RecyclerCarInfoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(carInfo: CarInfoVOS, callback: (carinfoID: String) -> Any) {
            binding.tvCompanyName.text = carInfo.company_name
            binding.tvMdn.text = carInfo.mdn
            binding.tvCarRegnum.text = carInfo.car_regnum
            binding.tvCarType.text = carInfo.car_type
            binding.tvCarNum.text = carInfo.car_num
            binding.tvDriverId.text = carInfo.driver_id1
            binding.tvFareId.text = carInfo.fare_name
            binding.tvCityId.text = carInfo.city_name
            binding.tvFirmwareId.text = carInfo.firmware_id
            binding.tvSpeedFactor.text = carInfo.speed_factor
        }
    }

}













