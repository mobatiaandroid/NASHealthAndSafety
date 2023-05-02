package com.nas.healthandsafety.activity.home.model

data class AssemblyPointsResponseModel(
    var `data`: List<Data?>?,
    var exception: Exception?,
    var message: String?,
    var status: Int?,
    var success: Boolean?,
    var validation: String?
) {
    data class Data(
        var assemblyPoint: String?,
        var classes: List<String?>?,
        var id: Int?
    )

    class Exception
}