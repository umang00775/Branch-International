package com.amora.branch.modal

data class ThreadModal(
    val id: Int,
    val thread_id: Int,
    val user_id: String,
    val agent_id: String,
    val body: String,
    val timestamp: String
)
