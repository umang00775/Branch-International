package com.amora.branch.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amora.branch.R
import com.amora.branch.modal.ThreadModal

class ThreadAdapter(val threadList: ArrayList<ThreadModal>, val context: Activity):
RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder>() {

    lateinit var clickListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClicking(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        clickListener = listener
    }

    class ThreadViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val userID = itemView.findViewById<TextView>(R.id.userid)
        val timeStamp = itemView.findViewById<TextView>(R.id.timestamp)
        val messageBody = itemView.findViewById<TextView>(R.id.message)

        init {
            itemView.setOnClickListener {
                listener.onItemClicking(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThreadAdapter.ThreadViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.message_thread, parent, false)

        return ThreadViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: ThreadAdapter.ThreadViewHolder, position: Int) {
        var currentItem = threadList[position]

        holder.userID.text = currentItem.user_id
        holder.timeStamp.text = currentItem.timestamp
        holder.messageBody.text = currentItem.body
    }

    override fun getItemCount(): Int {
        return threadList.size
    }

}