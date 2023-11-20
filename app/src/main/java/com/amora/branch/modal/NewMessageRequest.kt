package com.amora.branch.modal

import com.google.gson.annotations.SerializedName

data class NewMessageRequest(
    @SerializedName("thread_id") val threadId: String?,
    @SerializedName("body") val body: String
)
