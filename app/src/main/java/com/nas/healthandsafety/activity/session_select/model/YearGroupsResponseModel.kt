package com.nas.healthandsafety.activity.session_select.model

data class YearGroupsResponseModel(
    var `data`: ArrayList<Data?>?,
    var exception: Exception?,
    var message: String?, // Success
    var status: Int?, // 200
    var success: Boolean?, // true
    var validation: String?
) {
    data class Data(
        var id: Int?, // 101
        var year_group: String? // 13-Y13E
    )

    class Exception
}