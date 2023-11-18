package com.amora.branch.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.amora.branch.R
import com.amora.branch.api.ApiCalls
import com.amora.branch.modal.AuthTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {

    private val BASE_URL = "https://android-messaging.branch.co/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*Retrofit build*/
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiCalls::class.java)

        val username = "naklikhatu@gmail.com"
        val reversedPassword = "moc.liamg@utahtakilkan"

        val call = apiService.login(username, reversedPassword)
        call.enqueue(object : Callback<AuthTokenResponse> {
            override fun onResponse(call: Call<AuthTokenResponse>, response: Response<AuthTokenResponse>) {
                if (response.isSuccessful) {
                    // Login successful, get the auth token
                    val authTokenResponse = response.body()
                    val authToken = authTokenResponse?.authToken
                    Toast.makeText(this@Login, authToken, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Login, "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
                Log.d("THIS_IS_ERROR", response.toString())
            }

            override fun onFailure(call: Call<AuthTokenResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}