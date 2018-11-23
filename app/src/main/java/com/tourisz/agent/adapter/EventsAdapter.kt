package com.tourisz.agent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tourisz.R
import com.tourisz.agent.view.fragment.child_fragment.EventsFragment
import com.tourisz.api.URLS
import com.tourisz.api.response.EventData
import com.tourisz.util.helper.AndroidHelper

class EventsAdapter(private val hotelBookingList: List<EventData>, private val eventsFragment: EventsFragment) : RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtHotelDesc: TextView
        var txtTitle: TextView
        var txtCountry: TextView
        var txtStatus: TextView
        var txtAvailability: TextView
        var imgHotel: ImageView

        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtHotelDesc = view.findViewById<View>(R.id.txtHotelDesc) as TextView
            txtCountry = view.findViewById<View>(R.id.txtCountry) as TextView
            txtStatus = view.findViewById<View>(R.id.txtStatus) as TextView
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView
            txtAvailability = view.findViewById<View>(R.id.txtAvailability) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.agent_event_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelBooking = hotelBookingList[position]
        holder.txtTitle.setText(hotelBooking.eventTitle)
        holder.txtHotelDesc.setText(hotelBooking.shortDescription)
        holder.txtCountry.setText(hotelBooking.country)
        holder.txtStatus.setText(AndroidHelper.getStatus(hotelBooking.status))
        holder.txtAvailability.setText(AndroidHelper.getAvailability(hotelBooking.isAvailable))

        var names = AndroidHelper.getImageArray(hotelBooking.poster)
        if(names.isNotEmpty()){

            Glide.with(eventsFragment.activity).load(URLS.newInstance().IMG_URL + names[0])
                    .placeholder(R.drawable.ic_eventplaceholder)
                    .error(R.drawable.ic_eventplaceholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgHotel)
        }else{
            holder.imgHotel.setImageResource(R.drawable.ic_eventplaceholder)
        }

        holder.linRoot.setOnClickListener({ eventsFragment.onEventListItemClick(hotelBooking) })
    }

    override fun getItemCount(): Int {
        return hotelBookingList.size
    }
}