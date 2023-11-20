package com.amora.branch.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amora.branch.Adapters.ThreadAdapter
import com.amora.branch.R
import com.amora.branch.api.ApiCalls
import com.amora.branch.modal.ThreadModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MessageThreads : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var messageThreads: ArrayList<ThreadModal>

    private val BASE_URL = "https://android-messaging.branch.co/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_threads)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.darkthemebg)

        /* Get data from previous screen */
        val authToken = intent.getStringExtra("BRANCH_INTERNATIONAL_AUTH_TOKEN")

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiCalls::class.java)

        // Make the API call
        val call = apiService.getAllMessages()

        call.enqueue(object : Callback<List<ThreadModal>> {
            override fun onResponse(call: Call<List<ThreadModal>>, response: Response<List<ThreadModal>>) {
                if (response.isSuccessful) {
                    // Update your list with the received messages
                    messageThreads.addAll(response.body() ?: emptyList())
                    recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    return
                }
                Log.d("THIS_IS_ERROR", response.toString())
            }

            override fun onFailure(call: Call<List<ThreadModal>>, t: Throwable) {
                println("Request failed: ${t.message}")
                val errorBody = (t as? HttpException)?.response()?.errorBody()?.string()
                println("Error Body: $errorBody")
            }
        })


        recyclerView = findViewById(R.id.threadsRecyclerView)
        messageThreads = arrayListOf<ThreadModal>()

        recyclerView.layoutManager = LinearLayoutManager(this)

        val threadAdapter = ThreadAdapter(messageThreads, this)
        recyclerView.adapter = threadAdapter

        threadAdapter.setOnItemClickListener(object : ThreadAdapter.onItemClickListener{
            override fun onItemClicking(position: Int) {

                val intent = Intent(this@MessageThreads, IndividualMessage::class.java)
                intent.putExtra("USER_ID", messageThreads[position].user_id.toString())
                intent.putExtra("USER_MESSAGE", messageThreads[position].body)
                intent.putExtra("BRANCH_INTERNATIONAL_AUTH_TOKEN", authToken)
                startActivity(intent)

            }
        })
    }
}