package com.nas.healthandsafety.activity.home.model

data class DeviceRegistrationResponseModel(
    var `data`: Data?,
    var exception: Exception?,
    var message: String?,
    var status: Int?,
    var success: Boolean?,
    var validation: String?
) {
    data class Data(
        var appVersion: String?,
        var createdAt: String?,
        var deviceId: String?,
        var deviceName: String?,
        var deviceType: Int?,
        var fcmId: String?,
        var id: Int?,
        var staffId: Int?,
        var updatedAt: String?
    )

    class Exception
}