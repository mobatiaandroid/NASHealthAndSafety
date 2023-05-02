package com.nas.healthandsafety.activity.fire_marshall.model

data class EvacuationStartResponseModel(
    var `data`: Data?,
    var exception: Exception?,
    var message: String?, // Fire Evacuation success
    var status: Int?, // 200
    var success: Boolean?, // true
    var validation: String?
) {
    data class Data(
        var get_key: String? // -NUOZBbmDwWV92NGicuF
    )

    class Exception
}