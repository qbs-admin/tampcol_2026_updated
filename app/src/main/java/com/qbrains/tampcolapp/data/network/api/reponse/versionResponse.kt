package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class versionResponse {

    @SerializedName("app_version")
    @Expose
    var version: String? = null
}



