package com.thisisnotyours.registertaxikotlin.item

data class CarInfoItem (
        val company_name: String,  //운수사명
        val mdn: String,      //모뎀번호
        val car_regnum: String, //사업자번호
        val car_vin: String,  //차대번호
        val car_type: String, //차량유형
        val car_num: String,  //차량번호
        val driver_id1: String, //운전자 자격번호-1
        val driver_id2: String,
        val driver_id3: String,
        val fare_name: String,  //요금
        val city_name: String,  //시경계
        val firmware_name: String, //벤사
        val speed_factor: String,  //감속률
        val store_id: String,  //
        val unit_num: String,  //단말기번호
        val unit_sn: String
        )