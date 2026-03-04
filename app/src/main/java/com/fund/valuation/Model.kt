package com.fund.valuation

import com.google.gson.annotations.SerializedName

data class FundItem(
    val code: String = "",
    val name: String = "",
    val dwjz: String = "",   // 昨日净值
    val gsz: String = "",    // 估算值
    val zzl: Double = 0.0,   // 昨日涨跌幅
    val gszzl: Double = 0.0, // 估值涨跌幅
    val jzrq: String = "",   // 净值日期
    val gztime: String = ""  // 估值时间
)

data class UserConfig(
    val funds: List<FundItem> = emptyList()
)

data class UserConfigRecord(
    val id: Long,
    val data: UserConfig,
    @SerializedName("updated_at") val updatedAt: String
)
