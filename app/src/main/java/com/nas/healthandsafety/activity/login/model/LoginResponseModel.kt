package com.nas.healthandsafety.activity.login.model

data class LoginResponseModel(
    val `data`: Data,
    val exception: Exception,
    val message: String,
    val status: Int,
    val success: Boolean,
    val validation: String
) {
    data class Data(
        val staff_id: Int,
        val token: String,
        val is_martial: String
    )

    class Exception
}