package com.nas.healthandsafety.activity.register.model

data class RegisterAccountModel(
    val `data`: List<Any?>?,
    val exception: Exception?,
    val message: String?,
    val status: Int?,
    val success: Boolean?,
    val validation: String?
) {
    class Exception
}