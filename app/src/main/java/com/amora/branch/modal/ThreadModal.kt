package com.amora.branch.modal

data class ThreadModal(
    val id: Int,
    val thread_id: Int,
    val user_id: Int,
    val agent_id: Int,
    val body: String,
    val timestamp: String
)
