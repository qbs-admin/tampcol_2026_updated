package com.qbrains.tampcolapp.ui.fragments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePasswordRequest {

    @SerializedName("user_id")
    @Expose
    var userId: Int? = 0

    @SerializedName("old_password")
    @Expose
    var oldPassword: String? = null

    @SerializedName("new_password")
    @Expose
    var newPassword: String? = null

}
