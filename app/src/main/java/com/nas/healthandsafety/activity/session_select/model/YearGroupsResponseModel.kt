package com.nas.healthandsafety.activity.session_select.model

data class YearGroupsResponseModel(
    val `data`: List<Any>,
    val exception: Exception,
    val message: String,
    val status: Int,
    val success: Boolean,
    val validation: String
) {
    class Exception
}