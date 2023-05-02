package com.nas.healthandsafety.activity.session_select.model

data class SubjectsResponseModel(
    var `data`: ArrayList<Data?>?,
    var exception: Exception?,
    var message: String?, // Success
    var status: Int?, // 200
    var success: Boolean?, // true
    var validation: String?
) {
    data class Data(
        var class_id: Int?, // 2
        var name: String? // Primary Music
    )

    class Exception
}