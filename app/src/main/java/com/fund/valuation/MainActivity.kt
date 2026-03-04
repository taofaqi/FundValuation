package com.fund.valuation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

val BgDark = Color(0xFF0F1923)
val BgCard = Color(0xFF162030)
val BgHeader = Color(0xFF0D1520)
val TextPrimary = Color(0xFFE0E6F0)
val TextSecondary = Color(0xFF6B7A8D)
val ColorRed = Color(0xFF00C853)
val ColorGreen = Color(0xFFFF4D4F)
val ColorCyan = Color(0xFF00E5FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { FundApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundApp(vm: FundViewModel = viewModel()) {
    val funds by vm.funds.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = BgDark) {
        Column(modifier = Modifier.systemBarsPadding()) {
            TableHeader()
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = vm::loadFunds
            ) {
                if (isLoading && funds.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ColorCyan)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(funds) { fund -> FundRow(fund) }
                    }
                }
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgHeader)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("基金名称", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(2.2f))
        Text("净值", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1.2f))
        Text("估值", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1.2f))
        Text("昨日涨跌幅", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1.4f))
        Text("估值涨跌幅", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1.4f))
        Spacer(Modifier.weight(0.5f))
    }
}

@Composable
fun FundRow(fund: FundItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgCard)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 基金名称 + 代码
        Column(modifier = Modifier.weight(2.2f)) {
            Text(
                fund.name,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("#${fund.code}", color = TextSecondary, fontSize = 11.sp)
        }

        // 净值
        Text(fund.dwjz, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))

        // 估值
        Text(fund.gsz.ifEmpty { "-" }, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))

        // 昨日涨跌幅
        Column(modifier = Modifier.weight(1.4f)) {
            val zzlColor = if (fund.zzl >= 0) ColorGreen else ColorRed
            Text(
                "${if (fund.zzl >= 0) "+" else ""}${"%.2f".format(fund.zzl)}%",
                color = zzlColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(fund.jzrq, color = TextSecondary, fontSize = 10.sp)
        }

        // 估值涨跌幅
        Column(modifier = Modifier.weight(1.4f)) {
            val gszzlColor = if (fund.gszzl >= 0) ColorGreen else ColorRed
            Text(
                "${if (fund.gszzl >= 0) "+" else ""}${"%.2f".format(fund.gszzl)}%",
                color = gszzlColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            val time = fund.gztime.substringAfter(" ")
            val date = fund.gztime.substringBefore(" ")
            Text("$date $time", color = TextSecondary, fontSize = 10.sp)
        }


    }
    HorizontalDivider(color = Color(0xFF1E2D3D), thickness = 0.5.dp)
}
