package com.amora.branch.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import com.amora.branch.R
import com.amora.branch.api.ApiCalls
import com.amora.branch.modal.NewMessageRequest
import com.amora.branch.modal.ThreadModal
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IndividualMessage : AppCompatActivity() {

    private val BASE_URL = "https://android-messaging.branch.co/"

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_message)


        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.black)

        val userIdView = findViewById<TextView>(R.id.userid)
        val userMessageView = findViewById<TextView>(R.id.messageView)
        val submitBtn = findViewById<CardView>(R.id.submitButton)
        val spinner = findViewById<ProgressBar>(R.id.spinnerView)

        /* Initially Null */
        spinner.visibility = View.INVISIBLE


        /* Data  from previous screen */
        val userId = intent.getStringExtra("USER_ID")
        val userMessage = intent.getStringExtra("USER_MESSAGE")
        val authToken = intent.getStringExtra("BRANCH_INTERNATIONAL_AUTH_TOKEN")

        /* Set data */
        userIdView.text = userId
        userMessageView.text = userMessage

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiCalls::class.java)

        val threadId = userId
        val newMessageRequest = NewMessageRequest(threadId, "The weekly text reminders are a nuisance NICE")



        val createMessageCall = authToken?.let { apiService.createNewMessage(it, newMessageRequest) }
        createMessageCall?.enqueue(object : Callback<ThreadModal> {
            override fun onResponse(call: Call<ThreadModal>, response: Response<ThreadModal>) {
                if (response.isSuccessful) {
                    // Handle the newly created message
                    val newMessage = response.body()
                } else {
                    println("Create message request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ThreadModal>, t: Throwable) {
                // Handle failure
                println("Create message request failed: ${t.message}")
                val errorBody = (t as? HttpException)?.response()?.errorBody()?.string()
                println("Error Body: $errorBody")
            }
        })


        /* Submit On Click */
        submitBtn.setOnClickListener {
            submitBtn.visibility = View.INVISIBLE
            spinner.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                delay(1500)
                withContext(Dispatchers.Main){
                    Toast.makeText(this@IndividualMessage, "Message sent", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@IndividualMessage, MessageThreads::class.java)
                    intent.putExtra("BRANCH_INTERNATIONAL_AUTH_TOKEN", authToken)
                    startActivity(intent)
                }
            }
        }

    }
}
