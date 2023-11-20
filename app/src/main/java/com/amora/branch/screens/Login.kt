package com.amora.branch.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.amora.branch.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.darkthemebg)


        val usernameView = findViewById<TextInputEditText>(R.id.usernameText)
        val passwordView = findViewById<TextInputEditText>(R.id.userpasswordText)

        val submitBtn = findViewById<CardView>(R.id.submitButton)

        submitBtn.setOnClickListener {
            val username = usernameView.text.toString()
            val password = passwordView.text.toString()

            if (password == username.reversed()){
                GlobalScope.launch(Dispatchers.IO) {
                    // Step 1: Log in as a customer service agent
                    val authToken = login(username, password)
                    Log.d("AUTH_TOKEN", authToken)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@Login, "Token: $authToken", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Login, MessageThreads::class.java)
                        intent.putExtra("BRANCH_INTERNATIONAL_AUTH_TOKEN", authToken)
                        startActivity(intent)
                    }
                }
            }
            else{
                Toast.makeText(this@Login, "Password is reversed email!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun login(username: String, password: String): String {
        val client = OkHttpClient()
        val url = "https://android-messaging.branch.co/api/login"
        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, "{\"username\": \"$username\", \"password\": \"$password\"}")

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        return if (response.isSuccessful && responseBody != null) {
            // Parse the auth token from the response
            // Note: You may want to use a JSON parsing library for production code
            val authToken = responseBody.split("\"auth_token\":")[1].split("}")[0].trim(' ', '"', ':')
            authToken
        } else {
            // Handle login failure
            ""
        }
    }

    fun makeAuthenticatedRequest(authToken: String, endpoint: String, method: RequestMethod): String {
        val client = OkHttpClient()
        val url = "https://android-messaging.branch.co/$endpoint"

        val requestBuilder = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Branch-Auth-Token", authToken)

        val request = when (method) {
            RequestMethod.GET -> requestBuilder.get().build()
            RequestMethod.POST -> TODO()  // Add support for other HTTP methods as needed
        }

        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

}



enum class RequestMethod {
    GET,
    POST,
    // Add other HTTP methods as needed
}