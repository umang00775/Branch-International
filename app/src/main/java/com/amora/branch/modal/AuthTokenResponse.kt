package com.amora.branch.modal

import com.google.gson.annotations.SerializedName

data class AuthTokenResponse(
    @SerializedName("auth_token") val authToken: String
)
