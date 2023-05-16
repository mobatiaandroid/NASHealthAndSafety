package com.nas.healthandsafety.constants

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil

object ResourceUriHelper {
    fun getResourceUri(context: Context, resourceId: Int): Uri? {
        val packageName = context.packageName
        val resName = context.resources.getResourceEntryName(resourceId)
        val resType = context.resources.getResourceTypeName(resourceId)

        val uriString = when (resType) {
            "raw" -> "android.resource://$packageName/raw/$resName"
            "drawable" -> "android.resource://$packageName/drawable/$resName"
            else -> null
        }

        return if (uriString != null && URLUtil.isValidUrl(uriString)) {
            Uri.parse(uriString)
        } else {
            null
        }
    }
}
