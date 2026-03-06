package com.fund.valuation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FundViewModel : ViewModel() {

    private val apiKey = "sb_publishable_c5f58knbVz8UgOh6L88MUQ_p9j8c1Q-"
    private val userId = "68245e5e-b494-45ef-93dd-f8df31b930da"
    private val token = "eyJhbGciOiJFUzI1NiIsImtpZCI6ImQ2NDQxMzZiLWU1ZGItNDM4MC05Y2QxLTQyZDYzNjhlYmE5YiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL21vdXZzcWxtZ3ltc2F4aWt2cXNoLnN1cGFiYXNlLmNvL2F1dGgvdjEiLCJzdWIiOiI2ODI0NWU1ZS1iNDk0LTQ1ZWYtOTNkZC1mOGRmMzFiOTMwZGEiLCJhdWQiOiJhdXRoZW50aWNhdGVkIiwiZXhwIjoxNzczMzY1ODUxLCJpYXQiOjE3NzI3NjEwNTEsImVtYWlsIjoidGFvZmFxaUBnbWFpbC5jb20iLCJwaG9uZSI6IiIsImFwcF9tZXRhZGF0YSI6eyJwcm92aWRlciI6ImVtYWlsIiwicHJvdmlkZXJzIjpbImVtYWlsIl19LCJ1c2VyX21ldGFkYXRhIjp7ImVtYWlsIjoidGFvZmFxaUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicGhvbmVfdmVyaWZpZWQiOmZhbHNlLCJzdWIiOiI2ODI0NWU1ZS1iNDk0LTQ1ZWYtOTNkZC1mOGRmMzFiOTMwZGEifSwicm9sZSI6ImF1dGhlbnRpY2F0ZWQiLCJhYWwiOiJhYWwxIiwiYW1yIjpbeyJtZXRob2QiOiJvdHAiLCJ0aW1lc3RhbXAiOjE3NzIwODkxNDh9XSwic2Vzc2lvbl9pZCI6IjM0MTM2NTIzLWU4MjEtNDQ4Ni1iNTU1LWRkZDBjZWE0YzJiMiIsImlzX2Fub255bW91cyI6ZmFsc2V9.Lhrhm8E73788_aSmgvFs7ZdSqIt7c1mBmcIrYXY0sjUwpXB1lqqaeP1Cg1X4ueJiFVQqQMjT9uMagw9ircfo-g"

    private val _funds = MutableStateFlow<List<FundItem>>(emptyList())
    val funds: StateFlow<List<FundItem>> = _funds

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadFunds()
        startAutoRefresh()
    }

    fun loadFunds() {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                ApiClient.api.getUserConfigs(
                    userId = "eq.$userId",
                    apiKey = apiKey,
                    authorization = "Bearer $token"
                )
            }.onSuccess { records ->
                _funds.value = records.firstOrNull()?.data?.funds ?: emptyList()
            }
            _isLoading.value = false
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(30_000)
                loadFunds()
            }
        }
    }
}
