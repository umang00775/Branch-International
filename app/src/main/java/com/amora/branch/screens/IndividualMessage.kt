package com.amora.branch.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amora.branch.R
import com.amora.branch.api.ApiCalls
import com.amora.branch.modal.NewMessageRequest
import com.amora.branch.modal.ThreadModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IndividualMessage : AppCompatActivity() {

    private val BASE_URL = "https://android-messaging.branch.co/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_message)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiCalls::class.java)


        // ...

        val newMessageRequest = NewMessageRequest(1, "Your message body")

// Call the createNewMessage API
        val createMessageCall = apiService.createNewMessage(newMessageRequest)
        createMessageCall.enqueue(object : Callback<ThreadModal> {
            override fun onResponse(call: Call<ThreadModal>, response: Response<ThreadModal>) {
                if (response.isSuccessful) {
                    // Handle the newly created message
                    val newMessage = response.body()
                    // Update your UI or perform any other actions
                } else {
                    // Handle error
                    // For example, you can check response.code() to see the HTTP status code
                }
            }

            override fun onFailure(call: Call<ThreadModal>, t: Throwable) {
                // Handle failure
                println("Create message request failed: ${t.message}")
                val errorBody = (t as? HttpException)?.response()?.errorBody()?.string()
                println("Error Body: $errorBody")
            }
        })

    }
}