package com.example.letschat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,val messageList:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var ITEM_RECEIVE=1
    var ITEM_SENT=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            val view:View= LayoutInflater.from(context).inflate(R.layout.recieve,parent,false)
            return receiveViewHoldre(view)
        }else{
            val view:View= LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return sentViewHoldre(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage=messageList[position]
        if(holder.javaClass==sentViewHoldre::class.java){

            val viewHoldre=holder as sentViewHoldre
            holder.sentMassage.text=currentMessage.message

        }else{

            val viewHoldre=holder as receiveViewHoldre
            holder.recieveMassage.text=currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class sentViewHoldre(itemView: View) :RecyclerView.ViewHolder(itemView){

        val sentMassage=itemView.findViewById<TextView>(R.id.txt_sentMessage)
    }
    class receiveViewHoldre(itemView: View) :RecyclerView.ViewHolder(itemView){

        val recieveMassage=itemView.findViewById<TextView>(R.id.txt_receiveMessage)
    }
}