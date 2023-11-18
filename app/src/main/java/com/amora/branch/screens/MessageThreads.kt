package com.amora.branch.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        /* Random data */
//        val id1 = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)
//        val threadid1 = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)
//        val userid1 = arrayListOf<String>("One", "Two", "Three", "Four", "Five",
//            "Six", "Seven", "Eight", "Nine", "Ten")
//        val agentid1 = arrayListOf<String>("One1", "Two1", "Three1", "Four1", "Five1",
//            "Six1", "Seven1", "Eight1", "Nine1", "Ten1")
//        val body1 = arrayListOf<String>("This is body", "This is body",
//            "This is body",  "This is body", "This is body", "This is body", "This is body",
//            "This is body", "This is body", "This is body")


        /******************************/
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
                    // Handle error
                    // For example, you can check response.code() to see the HTTP status code
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

//        for(index in id1.indices){
//            messageThreads.add(ThreadModal(id1[index], threadid1[index], userid1[index], agentid1[index], body1[index], "10:00 AM"))
//        }


        val threadAdapter = ThreadAdapter(messageThreads, this)
        recyclerView.adapter = threadAdapter

        threadAdapter.setOnItemClickListener(object : ThreadAdapter.onItemClickListener{
            override fun onItemClicking(position: Int) {

                Toast
                    .makeText(this@MessageThreads, messageThreads[position].user_id, Toast.LENGTH_SHORT)
                    .show()
            }

        })




        /*
        * message body, timestamp, and agent OR user id
        * */
    }
}