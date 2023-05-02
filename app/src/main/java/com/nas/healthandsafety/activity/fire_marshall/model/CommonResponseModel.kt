package com.nas.healthandsafety.activity.fire_marshall.model

data class CommonResponseModel(
    var `data`: List<Any?>?,
    var exception: Exception?,
    var message: String?,
    var status: Int?,
    var success: Boolean?,
    var validation: String?
) {
    class Exception
}