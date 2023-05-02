package com.nas.healthandsafety.activity.home.model

data class StudentsResponseModel(
    var `data`: ArrayList<Data?>?,
    var exception: Exception?,
    var message: String?, // Success
    var status: Int?, // 200
    var success: Boolean?, // true
    var validation: String?
) {
    data class Data(
        var full_name: String?, // Fazaa Maktoom Salem Otman Alzaabi
        var id: Int?, // 13
        var profile_photo_path: String?, // http://gama.mobatia.in:8080/nais-dubai-fire-evacuation/public/storage/default-avatars/blank-avatar.png
        var profile_photo_url: String? // null
    )

    class Exception
}