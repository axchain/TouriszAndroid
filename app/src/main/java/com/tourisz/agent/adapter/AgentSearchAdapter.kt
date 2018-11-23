package com.tourisz.agent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.tourisz.R
import com.tourisz.entity.HotelBooking
import com.tourisz.util.BookingTypes
import com.tourisz.util.listener.SearchClickListener

class AgentSearchAdapter(private val hotelBookingList: List<HotelBooking>, private val searchClickListener: SearchClickListener) : RecyclerView.Adapter<AgentSearchAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtTitle: TextView
        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.agent_hotel_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelBooking = hotelBookingList[position]
        if (hotelBooking.type == BookingTypes.HOTEL){
            holder.txtTitle.text = "Hotel $position"
        }else{
            holder.txtTitle.text = "Event $position"

        }
        holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }
    }

    override fun getItemCount(): Int {
        return hotelBookingList.size
    }
}