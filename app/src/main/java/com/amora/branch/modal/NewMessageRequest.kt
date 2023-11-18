package com.amora.branch.modal

data class NewMessageRequest(
    val thread_id: Int,
    val body: String
)
