package com.nas.healthandsafety.activity.home.model

data class EvacuationStatusResponseModel(
    var `data`: ArrayList<Data?>?,
    var exception: Exception?,
    var message: String?, // Success
    var status: Int?, // 200
    var success: Boolean?, // true
    var validation: String?
) {
    data class Data(
        var created_by: String?, // Sanju Sabu
        var evacuate_end: String?, // 0000-00-00 00:00:00
        var evacuate_start: String?, // 2023-04-29 14:15:37
        var evacuate_id: String?, // -NUCNUc58bYPCUF1B_ke
        var id: Int?, // 6
        var status: Int?, // 1
        var status_value: String? // evacuation start
    )

    class Exception
}