package com.amora.branch.api

import com.amora.branch.modal.AuthTokenResponse
import com.amora.branch.modal.NewMessageRequest
import com.amora.branch.modal.ThreadModal
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiCalls {

    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<AuthTokenResponse>


    @GET("api/messages")
    @Headers("X-Branch-Auth-Token: oAmjWG_OWKtw_aslX16pRQ")
    fun getAllMessages(): Call<List<ThreadModal>>

    @POST("api/messages")
    @Headers("Content-Type: application/json")
    fun createNewMessage(
        @Header("X-Branch-Auth-Token") authToken: String,
        @Body newMessage: NewMessageRequest
    ): Call<ThreadModal>

}