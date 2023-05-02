package com.nas.healthandsafety.activity.fire_marshall.model

data class EvacuationEndResponseModel(
    var `data`: List<Any?>?,
    var exception: Exception?,
    var message: String?, // Data successfully updated
    var status: Int?, // 200
    var success: Boolean?, // true
    var validation: String?
) {
    class Exception
}